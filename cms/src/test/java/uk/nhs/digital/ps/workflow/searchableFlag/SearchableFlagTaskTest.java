package uk.nhs.digital.ps.workflow.searchableFlag;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.apache.sling.testing.mock.jcr.MockJcr;
import org.hippoecm.repository.HippoStdNodeType;
import org.hippoecm.repository.api.WorkflowContext;
import org.hippoecm.repository.api.WorkflowException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onehippo.repository.documentworkflow.DocumentHandle;
import org.onehippo.repository.documentworkflow.DocumentVariant;
import uk.nhs.digital.ps.JcrProvider;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

@RunWith(DataProviderRunner.class)
public class SearchableFlagTaskTest {

    private SearchableFlagTask searchableFlagTask = new SearchableFlagTask();
    private Session session;
    private Node rootNode = null;

    private final static String RPS_ROOT_FOLDER = "/content/documents/corporate-website/publication-system";

    @Before
    public void setUp() throws Exception {
        session = new JcrProvider()
            .getJcrFromFixture(new FileInputStream("src/test/resources/searchableFlagUnitTestJcrFixture.yml"));
        rootNode = session.getRootNode();

        WorkflowContext workflowContext = mock(WorkflowContext.class);
        given(workflowContext.getInternalWorkflowSession()).willReturn(session);
        searchableFlagTask.setWorkflowContext(workflowContext);
    }

    private String getResult(String state) {
        switch (state) {
            case HippoStdNodeType.PUBLISHED:
                return RPS_ROOT_FOLDER + "/publication-with-datasets/dataset/dataset";
            case HippoStdNodeType.DRAFT:
                return RPS_ROOT_FOLDER + "/publication-with-datasets/dataset/dataset[2]";
            case HippoStdNodeType.UNPUBLISHED:
                return RPS_ROOT_FOLDER + "/publication-with-datasets/dataset/dataset[3]";
            default:
                throw new RuntimeException("Unknown state: " + state);
        }
    }

    @Test
    @UseDataProvider("unpublishDocuments")
    public void shouldUpdateDatasetOnDepublishTest(ArrayList<String> changed, ArrayList<String> unchanged) throws Exception {
        // mock query results
        MockJcr.setQueryResult(session, getQueryResults(singletonList(
            RPS_ROOT_FOLDER + "/accessible-publication-with-datasets/dataset/dataset"
        )));

        // process publication and datasets
        execute("/accessible-publication-with-datasets/content", HippoStdNodeType.PUBLISHED, true);

        for (int i = 0; i < changed.size(); i++) {
            assertFalse(
                "common:searchable flag is set to false for " + changed.get(i),
                getNodeBooleanProperty(changed.get(i), "common:searchable")
            );
        }

        for (int i = 0; i < unchanged.size(); i++) {
            assertTrue(
                "common:searchable flag is set to true for " + unchanged.get(i),
                getNodeBooleanProperty(unchanged.get(i), "common:searchable")
            );
        }
    }

    @Test
    @UseDataProvider("datasetDocumentState")
    public void shouldUpdateDocumentWithGivenStatusTest(final String state, ArrayList<String> changed, ArrayList<String> unchanged) throws Exception {

        // process publication and datasets
        execute("/publication-with-datasets/dataset", state, false);

        for (int i = 0; i < changed.size(); i++) {
            assertFalse(
                "common:searchable flag is set to false for " + changed.get(i),
                getNodeBooleanProperty(changed.get(i), "common:searchable")
            );
        }

        for (int i = 0; i < unchanged.size(); i++) {
            assertTrue(
                "common:searchable flag is set to true for " + unchanged.get(i),
                getNodeBooleanProperty(unchanged.get(i), "common:searchable")
            );
        }
    }

    @Test
    @UseDataProvider("datasetInSubfolder")
    public void shouldUpdateDatasetFromSubfolder(final String state, ArrayList<String> changed, ArrayList<String> unchanged) throws Exception {

        // process datasets from within subfolder
        execute("/publication-with-datasets/subfolder/dataset", state, false);

        for (int i = 0; i < changed.size(); i++) {
            assertFalse(
                "common:searchable flag is set to false for " + changed.get(i),
                getNodeBooleanProperty(changed.get(i), "common:searchable")
            );
        }

        for (int i = 0; i < unchanged.size(); i++) {
            assertTrue(
                "common:searchable flag is set to true for " + unchanged.get(i),
                getNodeBooleanProperty(unchanged.get(i), "common:searchable")
            );
        }
    }

    private void execute(String path, String state, boolean depublishing) throws RepositoryException, WorkflowException {
        Node node = getNode(RPS_ROOT_FOLDER + path);
        DocumentHandle handle = new DocumentHandle(node);
        handle.initialize();
        DocumentVariant variant = handle.getDocuments().get(state);
        searchableFlagTask.setVariant(variant);
        searchableFlagTask.setDepublishing(depublishing);
        searchableFlagTask.setDocumentHandle(handle);
        searchableFlagTask.execute();
    }

    @DataProvider
    public static Object[][] datasetDocumentState() {
        return new Object[][] {
            {
                "draft",
                new ArrayList() {{
                    add(RPS_ROOT_FOLDER + "/publication-with-datasets/dataset/dataset[2]");
                }},
                new ArrayList() {{
                    add(RPS_ROOT_FOLDER + "/publication-with-datasets/content/content");
                    add(RPS_ROOT_FOLDER + "/publication-with-datasets/content/content[2]");
                    add(RPS_ROOT_FOLDER + "/publication-with-datasets/content/content[3]");
                    add(RPS_ROOT_FOLDER + "/publication-with-datasets/dataset/dataset");
                    add(RPS_ROOT_FOLDER + "/publication-with-datasets/dataset/dataset[3]");
                    // unrelated dataset should not be changed
                    add(RPS_ROOT_FOLDER + "/random-folder/dataset/dataset");
                    add(RPS_ROOT_FOLDER + "/random-folder/dataset/dataset[2]");
                    add(RPS_ROOT_FOLDER + "/random-folder/dataset/dataset[3]");
                }}
            },
            {
                "published",
                new ArrayList() {{
                    add(RPS_ROOT_FOLDER + "/publication-with-datasets/dataset/dataset");
                }},
                new ArrayList() {{
                    add(RPS_ROOT_FOLDER + "/publication-with-datasets/content/content");
                    add(RPS_ROOT_FOLDER + "/publication-with-datasets/content/content[2]");
                    add(RPS_ROOT_FOLDER + "/publication-with-datasets/content/content[3]");
                    add(RPS_ROOT_FOLDER + "/publication-with-datasets/dataset/dataset[2]");
                    add(RPS_ROOT_FOLDER + "/publication-with-datasets/dataset/dataset[3]");
                    // unrelated dataset should not be changed
                    add(RPS_ROOT_FOLDER + "/random-folder/dataset/dataset");
                    add(RPS_ROOT_FOLDER + "/random-folder/dataset/dataset[2]");
                    add(RPS_ROOT_FOLDER + "/random-folder/dataset/dataset[3]");
                }}
            },
        };
    }

    @DataProvider
    public static Object[][] unpublishDocuments() {
        return new Object[][] {
            {
                new ArrayList() {{
                    add(RPS_ROOT_FOLDER + "/accessible-publication-with-datasets/dataset/dataset");
                }},
                new ArrayList() {{
                    add(RPS_ROOT_FOLDER + "/accessible-publication-with-datasets/content/content[2]");
                    add(RPS_ROOT_FOLDER + "/accessible-publication-with-datasets/content/content[3]");
                    add(RPS_ROOT_FOLDER + "/accessible-publication-with-datasets/dataset/dataset[2]");
                    add(RPS_ROOT_FOLDER + "/accessible-publication-with-datasets/dataset/dataset[3]");
                    // unrelated dataset should not be changed
                    add(RPS_ROOT_FOLDER + "/random-folder/dataset/dataset");
                    add(RPS_ROOT_FOLDER + "/random-folder/dataset/dataset[2]");
                    add(RPS_ROOT_FOLDER + "/random-folder/dataset/dataset[3]");
                }}
            }
        };
    }

    @DataProvider
    public static Object[][] datasetInSubfolder() {
        return new Object[][] {
            {
                "draft",
                new ArrayList<String>() {{
                    add(RPS_ROOT_FOLDER + "/publication-with-datasets/subfolder/dataset/dataset[2]");
                }},
                new ArrayList<String>() {{
                    // publication unchanged
                    add(RPS_ROOT_FOLDER + "/publication-with-datasets/content/content");
                    add(RPS_ROOT_FOLDER + "/publication-with-datasets/content/content[2]");
                    add(RPS_ROOT_FOLDER + "/publication-with-datasets/content/content[3]");
                    // the second dataset attached to parent publication unchanged
                    add(RPS_ROOT_FOLDER + "/publication-with-datasets/dataset/dataset");
                    add(RPS_ROOT_FOLDER + "/publication-with-datasets/dataset/dataset[2]");
                    add(RPS_ROOT_FOLDER + "/publication-with-datasets/dataset/dataset[3]");
                    // draft and live version unchanged
                    add(RPS_ROOT_FOLDER + "/publication-with-datasets/subfolder/dataset/dataset");
                    add(RPS_ROOT_FOLDER + "/publication-with-datasets/subfolder/dataset/dataset[3]");
                    // unrelated dataset should not be changed
                    add(RPS_ROOT_FOLDER + "/random-folder/dataset/dataset");
                    add(RPS_ROOT_FOLDER + "/random-folder/dataset/dataset[2]");
                    add(RPS_ROOT_FOLDER + "/random-folder/dataset/dataset[3]");
                }}
            }
        };
    }

    public List<Node> getQueryResults(List<String> paths) throws RepositoryException {
        List<Node> nodes = new ArrayList<>();

        for (int i=0; i < paths.size(); i++) {
            nodes.add(getNode(paths.get(i)));
        }

        return nodes;
    }

    protected Node getNode(String path) throws RepositoryException {
        return rootNode.getNode(path);
    }

    protected Boolean getNodeBooleanProperty(String path, String propertyName) throws RepositoryException {
        return rootNode.getNode(path).getProperty(propertyName).getBoolean();
    }
}
