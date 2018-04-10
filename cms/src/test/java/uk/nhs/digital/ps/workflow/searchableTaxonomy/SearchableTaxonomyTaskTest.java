package uk.nhs.digital.ps.workflow.searchableTaxonomy;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hippoecm.repository.HippoStdNodeType.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static uk.nhs.digital.ps.workflow.searchableTaxonomy.SearchableTaxonomyTask.*;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.value.StringValue;
import org.apache.sling.testing.mock.jcr.MockJcr;
import org.hippoecm.repository.api.WorkflowContext;
import org.hippoecm.repository.api.WorkflowException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onehippo.repository.documentworkflow.DocumentHandle;
import org.onehippo.repository.documentworkflow.DocumentVariant;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.util.List;
import javax.jcr.*;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PowerMockRunnerDelegate(DataProviderRunner.class)
public class SearchableTaxonomyTaskTest {

    private static final String TAXONOMY_NAME = "test_taxonomy";

    private SearchableTaxonomyTask searchableTaxonomyTask;
    private Node rootNode;

    @Before
    public void setUp() throws RepositoryException {
        Repository repository = MockJcr.newRepository();
        Session session = repository.login();
        rootNode = session.getRootNode();
        searchableTaxonomyTask = new SearchableTaxonomyTask();

        WorkflowContext workflowContext = mock(WorkflowContext.class);
        given(workflowContext.getInternalWorkflowSession()).willReturn(session);
        searchableTaxonomyTask.setWorkflowContext(workflowContext);

        rootNode.addNode(HIPPO_CLASSIFIABLE_PATH)
            .setProperty(TAXONOMY_NODE_NAME_PROPERTY, TAXONOMY_NAME);

        Node taxonomyTreeNode = rootNode.addNode("/content/taxonomies/" + TAXONOMY_NAME + "/" + TAXONOMY_NAME);
        createTaxonomyTree(taxonomyTreeNode, "taxonomy", 1);
    }

    private void performTask(Node docNode) throws WorkflowException {
        DocumentHandle documentHandle = new DocumentHandle(docNode);
        documentHandle.initialize();
        DocumentVariant draft = documentHandle.getDocuments().get(DRAFT);
        searchableTaxonomyTask.setVariant(draft);
        searchableTaxonomyTask.execute();
    }

    @Test
    @UseDataProvider("taxonomyKeyTests")
    public void getFullTaxonomyTest(List<String> input, List<String> expectedFullTaxonomy, List<String> expectedSearchableTags) throws RepositoryException, WorkflowException {
        String docName = "fullTaxonomyDocument" + System.nanoTime();
        Node docNode = rootNode.addNode(docName);
        Node document = addSubDocument(docNode, "draft", "");

        Value[] taxonomyValues = input.stream()
            .map(StringValue::new)
            .toArray(Value[]::new);
        document.setProperty(TAXONOMY_KEYS_PROPERTY, taxonomyValues);

        performTask(docNode);

        Node newNode = docNode.getNode(docName);

        if (expectedFullTaxonomy == null || expectedSearchableTags == null) {
            if (expectedFullTaxonomy == null) {
                assertFalse("Doesn't have taxonomy property", newNode.hasProperty(FULL_TAXONOMY_PROPERTY));
            }

            if (expectedSearchableTags == null) {
                assertFalse("Doesn't have searchable tags property", newNode.hasProperty(SEARCHABLE_TAGS_PROPERTY));
            }

            return;
        }

        List<String> fullTaxonomy = getStringValuesFromProperty(newNode, FULL_TAXONOMY_PROPERTY);
        assertThat("Full taxonomy is as expected.", fullTaxonomy, containsInAnyOrder(expectedFullTaxonomy.toArray()));

        List<String> searchableTags = getStringValuesFromProperty(newNode, SEARCHABLE_TAGS_PROPERTY);
        assertThat("Searchable tags are as expected", searchableTags, containsInAnyOrder(expectedSearchableTags.toArray()));
    }

    private List<String> getStringValuesFromProperty(Node node, String property) throws RepositoryException {
        Value[] values = node.getProperty(property).getValues();
        return stream(values)
            .map(value -> {
                try {
                    return value.getString();
                } catch (RepositoryException repositoryException) {
                    throw new RuntimeException(repositoryException);
                }
            }).collect(toList());
    }

    @Test
    public void noTaxonomyToProcess() throws RepositoryException, WorkflowException {
        String docName = "documentWithoutTaxonomy";
        Node docNode = rootNode.addNode(docName);
        Node document = docNode.addNode(docName);
        document.setProperty("hippostd:state", "draft");

        performTask(docNode);

        assertFalse("Full taxonomy is not set", document.hasProperty(FULL_TAXONOMY_PROPERTY));
        assertFalse("Searchable tags are not set", document.hasProperty(SEARCHABLE_TAGS_PROPERTY));
    }

    @Test
    public void settingNewPropertyOnCorrectNodes() throws RepositoryException, WorkflowException {
        final String docName = "publishedDocument";
        final Node documentNode = rootNode.addNode(docName);
        final Node published = addSubDocument(documentNode, PUBLISHED, "");
        final Node unpublished = addSubDocument(documentNode, UNPUBLISHED, "[2]");
        final Node draft = addSubDocument(documentNode, DRAFT, "[3]");

        performTask(documentNode);

        assertFalse("Full taxonomy is not set for published", published.hasProperty(FULL_TAXONOMY_PROPERTY));
        assertFalse("Full taxonomy is not set for unpublished", unpublished.hasProperty(FULL_TAXONOMY_PROPERTY));
        assertTrue("Full taxonomy is set for draft", draft.hasProperty(FULL_TAXONOMY_PROPERTY));
    }

    @PrepareForTest(SearchableTaxonomyTask.class)
    @Test(expected = WorkflowException.class)
    public void repositoryExceptionIsPropogated() throws Exception {
        PowerMockito.spy(SearchableTaxonomyTask.class);
        PowerMockito.when(SearchableTaxonomyTask.getWrapExceptions(any()))
            .thenThrow(new RepositoryExceptionWrapper(new RepositoryException()));

        Node documentNode = rootNode.addNode("document");
        addSubDocument(documentNode, DRAFT, "");

        performTask(documentNode);
    }

    private Node addSubDocument(Node documentNode, String state, String suffix) throws RepositoryException {
        String name = documentNode.getName() + suffix;
        Node document = documentNode.addNode(name);
        document.setProperty("hippostd:state", state);
        document.setProperty(TAXONOMY_KEYS_PROPERTY, new Value[]{new StringValue("taxonomy_1_1_1")});
        return document;
    }

    private void createTaxonomyTree(Node parent, String parentKey, int depth) throws RepositoryException {
        if (depth > 3) {
            return;
        }

        for (int i = 1; i <= 3; i++) {
            String childKey = parentKey + "_" + i;
            Node child = parent.addNode(childKey, TAXONOMY_CATEGORY_NODE_TYPE);
            child.setProperty(TAXONOMY_KEY_PROPERTY, childKey);
            child.addNode("unrelated", "unrelated:node");
            createTaxonomyTree(child, childKey, depth + 1);

            Node info = child.addNode(TAXONOMY_CATEGORY_INFOS_PROPERTY).addNode("en");
            info.setProperty(TAXONOMY_NAME_PROPERTY, convertKeyToName(childKey));

            addTaxonomySynonyms(info, childKey);
        }
    }

    private void addTaxonomySynonyms(Node info, String childKey) throws RepositoryException {
        String[] synonyms;
        switch (childKey) {
            case "taxonomy_1_1_1":
                synonyms = new String[]{"Synonym One One One"};
                break;
            case "taxonomy_1_2":
                synonyms = new String[]{"Synonym One Two"};
                break;
            case "taxonomy_1_2_3":
                synonyms = new String[]{"Synonym One", "Synonym Two", "Synonym Three"};
                break;
            default:
                return;
        }

        Value[] values = stream(synonyms)
            .map(StringValue::new)
            .toArray(Value[]::new);

        info.setProperty(TAXONOMY_SYNONYMS_PROPERTY, values);
    }

    private String convertKeyToName(String childKey) {
        // "taxonomy_1_2_3" -> "Taxonomy One Two Three"
        return StringUtils.capitalize(childKey.replaceAll("_", " ")
            .replaceAll("1", "One")
            .replaceAll("2", "Two")
            .replaceAll("3", "Three"));
    }

    @DataProvider
    public static Object[][] taxonomyKeyTests() {
        // These triplets are:
        // input taxonomy keys
        // full taxonomy expected
        // searchable tags expected

        return new List[][]{
            new List[]{
                asList("taxonomy_1_1_1", "taxonomy_1_2", "taxonomy_2_1", "taxonomy_2_1_2"),
                asList("taxonomy_1", "taxonomy_1_1", "taxonomy_1_1_1", "taxonomy_1_2", "taxonomy_2_1", "taxonomy_2", "taxonomy_2_1_2"),
                asList("Taxonomy One One One", "Synonym One One One", "Taxonomy One Two", "Synonym One Two", "Taxonomy Two One", "Taxonomy Two One Two")
            },
            new List[]{
                singletonList("taxonomy_3_3_3"),
                asList("taxonomy_3", "taxonomy_3_3", "taxonomy_3_3_3"),
                singletonList("Taxonomy Three Three Three")
            },
            new List[]{
                singletonList("taxonomy_2"),
                singletonList("taxonomy_2"),
                singletonList("Taxonomy Two")
            },
            new List[]{
                singletonList("taxonomy_1_2_3"),
                asList("taxonomy_1", "taxonomy_1_2", "taxonomy_1_2_3"),
                asList("Taxonomy One Two Three", "Synonym One", "Synonym Two", "Synonym Three")
            },
            new List[]{
                singletonList("not-a-taxonomy-key"),
                null,
                null
            },
            new List[]{
                asList("not-a-taxonomy-key", "taxonomy_1_1_1"),
                asList("taxonomy_1", "taxonomy_1_1", "taxonomy_1_1_1"),
                asList("Taxonomy One One One", "Synonym One One One")
            },
            new List[]{
                emptyList(),
                null,
                null
            }};
    }
}
