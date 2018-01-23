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
            .setProperty(TAXONOMY_NAME_PROPERTY, TAXONOMY_NAME);

        Node taxonomyTreeNode = rootNode.addNode("/content/taxonomies/" + TAXONOMY_NAME + "/" + TAXONOMY_NAME);
        createTaxonomyTree(taxonomyTreeNode, "taxonomy", 1);

        fullTaxonomyModule.initialize(session);
    }

    @Test
    @UseDataProvider("taxonomyKeyTests")
    public void getFullTaxonomyTest(List<String> input, List<String> expected) throws RepositoryException {
        HippoWorkflowEvent event = mockEvent();

        String docName = "fullTaxonomyDocument" + System.nanoTime();
        given(event.subjectPath()).willReturn("/" + docName);
        Node docNode = rootNode.addNode(docName);
        Node document = addSubDocument(docNode, "draft", "");

        Value[] taxonomyValues = input.stream()
            .map(StringValue::new)
            .toArray(Value[]::new);
        document.setProperty("hippotaxonomy:keys", taxonomyValues);

        fullTaxonomyModule.handleEvent(event);

        Value[] fullTaxonomyValues = docNode.getNode(docName).getProperty(FULL_TAXONOMY_PROPERTY).getValues();
        List<String> fullTaxonomy = stream(fullTaxonomyValues)
            .map(value -> {
                try {
                    return value.getString();
                } catch (RepositoryException e) {
                    throw new RuntimeException(e);
                }
            }).collect(toList());

            assertThat("Full taxonomy is as expected.", fullTaxonomy, containsInAnyOrder(expected.toArray()));
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
        document.setProperty("hippotaxonomy:keys", new Value[]{new StringValue("taxonomy_1_1_1")});
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
            Node child = parent.addNode(childKey, "hippotaxonomy:category");
            child.setProperty("hippotaxonomy:key", childKey);
            child.addNode("unrelated", "unrelated:node");
            createTaxonomyTree(child, childKey, depth+1);
        }
    }

    @DataProvider
    public static Object[][] taxonomyKeyTests() {
        // These pairs are the input taxonomy keys and the output full taxonomy expected

        return new List[][]{
            new List[]{
                asList("taxonomy_1_1_1", "taxonomy_1_2", "taxonomy_2_1", "taxonomy_2_1_2"),
                asList("taxonomy_1", "taxonomy_1_1", "taxonomy_1_1_1", "taxonomy_1_2", "taxonomy_2_1", "taxonomy_2", "taxonomy_2_1_2")
            },
            new List[]{
                singletonList("taxonomy_3_3_3"),
                asList("taxonomy_3", "taxonomy_3_3", "taxonomy_3_3_3")
            },
            new List[]{
                singletonList("taxonomy_2"),
                singletonList("taxonomy_2")
            },
            new List[]{
                emptyList(),
                emptyList()
            }};
    }
}
