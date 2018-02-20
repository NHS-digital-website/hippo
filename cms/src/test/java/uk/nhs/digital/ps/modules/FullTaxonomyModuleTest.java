package uk.nhs.digital.ps.modules;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static uk.nhs.digital.ps.modules.AbstractDaemonModule.ACTION_COMMIT;
import static uk.nhs.digital.ps.modules.AbstractDaemonModule.DOCUMENT_WORKFLOW_NAME;
import static uk.nhs.digital.ps.modules.FullTaxonomyModule.*;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.value.StringValue;
import org.apache.sling.testing.mock.jcr.MockJcr;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onehippo.repository.events.HippoWorkflowEvent;

import java.util.List;
import javax.jcr.*;

@RunWith(DataProviderRunner.class)
public class FullTaxonomyModuleTest {

    private static final String TAXONOMY_NAME = "test_taxonomy";

    private FullTaxonomyModule fullTaxonomyModule;
    private Node rootNode;

    @Before
    public void setUp() throws RepositoryException {
        fullTaxonomyModule = new FullTaxonomyModule();
        Repository repository = MockJcr.newRepository();
        Session session = repository.login();
        rootNode = session.getRootNode();

        rootNode.addNode(HIPPO_CLASSIFIABLE_PATH)
            .setProperty(TAXONOMY_NODE_NAME_PROPERTY, TAXONOMY_NAME);

        Node taxonomyTreeNode = rootNode.addNode("/content/taxonomies/" + TAXONOMY_NAME + "/" + TAXONOMY_NAME);
        createTaxonomyTree(taxonomyTreeNode, "taxonomy", 1);

        fullTaxonomyModule.initialize(session);
    }

    @Test
    @UseDataProvider("taxonomyKeyTests")
    public void getFullTaxonomyTest(List<String> input, List<String> expectedFullTaxonomy, List<String> expectedSearchableTags) throws RepositoryException {
        HippoWorkflowEvent event = mockEvent();

        String docName = "fullTaxonomyDocument" + System.nanoTime();
        given(event.subjectPath()).willReturn("/" + docName);
        Node docNode = rootNode.addNode(docName);
        Node document = addSubDocument(docNode, "draft", "");

        Value[] taxonomyValues = input.stream()
            .map(StringValue::new)
            .toArray(Value[]::new);
        document.setProperty(TAXONOMY_KEYS_PROPERTY, taxonomyValues);

        fullTaxonomyModule.handleEvent(event);

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
                } catch (RepositoryException e) {
                    throw new RuntimeException(e);
                }
            }).collect(toList());
    }

    @Test
    public void unsuccessfulEvent() {
        HippoWorkflowEvent event = mockEvent();
        given(event.success()).willReturn(false);
        fullTaxonomyModule.handleEvent(event);
        verify(event).success();
        verifyNoMoreInteractions(event);
    }

    @Test
    public void notCommitEvent() {
        HippoWorkflowEvent event = mockEvent();
        given(event.action()).willReturn(ACTION_PUBLISH);
        fullTaxonomyModule.handleEvent(event);
        verify(event).success();
        verify(event).workflowName();
        verify(event).action();
        verifyNoMoreInteractions(event);
    }

    @Test
    public void noTaxonomyToProcess() throws RepositoryException {
        HippoWorkflowEvent event = mockEvent();
        String docName = "documentWithoutTaxonomy";
        given(event.subjectPath()).willReturn("/" + docName);
        Node docNode = rootNode.addNode(docName);
        Node document = docNode.addNode(docName);
        document.setProperty("hippostd:state", "draft");

        fullTaxonomyModule.handleEvent(event);

        assertFalse("Full taxonomy is not set", document.hasProperty(FULL_TAXONOMY_PROPERTY));
        assertFalse("Searchable tags are not set", document.hasProperty(SEARCHABLE_TAGS_PROPERTY));
    }

    @Test
    public void settingNewPropertyOnCorrectNodes() throws RepositoryException {
        HippoWorkflowEvent event = mockEvent();

        String docName = "publishedDocument";
        given(event.subjectPath()).willReturn("/" + docName);
        Node documentNode = rootNode.addNode(docName);
        Node published = addSubDocument(documentNode, STATE_PUBLISHED, "");
        Node unpublished = addSubDocument(documentNode, "unpublished", "[1]");
        Node draft = addSubDocument(documentNode, "draft", "[2]");

        fullTaxonomyModule.handleEvent(event);

        assertFalse("Full taxonomy is not set for published", published.hasProperty(FULL_TAXONOMY_PROPERTY));
        assertTrue("Full taxonomy is set for draft", unpublished.hasProperty(FULL_TAXONOMY_PROPERTY));
        assertTrue("Full taxonomy is set for published", draft.hasProperty(FULL_TAXONOMY_PROPERTY));
    }

    private Node addSubDocument(Node documentNode, String state, String suffix) throws RepositoryException {
        String name = documentNode.getName() + suffix;
        Node document = documentNode.addNode(name);
        document.setProperty("hippostd:state", state);
        document.setProperty(TAXONOMY_KEYS_PROPERTY, new Value[]{new StringValue("taxonomy_1_1_1")});
        return document;
    }

    private HippoWorkflowEvent mockEvent() {
        HippoWorkflowEvent event = mock(HippoWorkflowEvent.class);
        given(event.success()).willReturn(true);
        given(event.workflowName()).willReturn(DOCUMENT_WORKFLOW_NAME);
        given(event.action()).willReturn(ACTION_COMMIT);
        return event;
    }

    private void createTaxonomyTree(Node parent, String parentKey, int depth) throws RepositoryException {
        if (depth > 3) {
            return;
        }

        for (int i=1; i<=3; i++) {
            String childKey = parentKey + "_" + i;
            Node child = parent.addNode(childKey, TAXONOMY_CATEGORY_NODE_TYPE);
            child.setProperty(TAXONOMY_KEY_PROPERTY, childKey);
            child.addNode("unrelated", "unrelated:node");
            createTaxonomyTree(child, childKey, depth+1);

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
