package uk.nhs.digital.ps.modules.searchableFlagModule;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.apache.sling.testing.mock.jcr.MockJcr;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(DataProviderRunner.class)
public class PublicationProcessorTest {

    private Session session;
    private PublicationProcessor publicationProcessor = new PublicationProcessor();
    private Node rootNode = null;
    private final static String RPS_ROOT_FOLDER = "/content/documents/corporate-website/publication-system";

    @Before
    public void setUp() throws Exception {
        session = new JcrProvider()
            .getJcrFromFixture(new FileInputStream("src/test/resources/searchableFlagUnitTestJcrFixture.yml"));
        rootNode = session.getRootNode();
    }

    @Test
    @UseDataProvider("documentState")
    public void shouldUpdateDocumentWithGivenStatusTest(final String state, ArrayList<String> changed, ArrayList<String> unchanged) throws Exception {
        // mock query results
        MockJcr.setQueryResult(session, getQueryResults(Arrays.asList(
            RPS_ROOT_FOLDER + "/publication-with-datasets/dataset/dataset",
            RPS_ROOT_FOLDER + "/publication-with-datasets/dataset/dataset[2]",
            RPS_ROOT_FOLDER + "/publication-with-datasets/dataset/dataset[3]"
        )));
        // process publication and datasets
        publicationProcessor.processNode(getNode(RPS_ROOT_FOLDER + "/publication-with-datasets/content"), Arrays.asList(state));

        for (int i = 0; i < changed.size(); i++) {
            assertFalse(
                "common:searchable flag is set to false for " + changed.get(i),
                getNodeBooleanProperty(changed.get(i), "common:searchable")
            );
        }

        for (int i = 0; i < changed.size(); i++) {
            assertFalse(
                "common:searchable flag is set to true for " + changed.get(i),
                getNodeBooleanProperty(changed.get(i), "common:searchable")
            );
        }
    }

    public List<Node> getQueryResults(List<String> paths) throws RepositoryException {
        List<Node> nodes = new ArrayList<>();

        for (int i=0; i < paths.size(); i++) {
            nodes.add(getNode(paths.get(i)));
        }

        return nodes;
    }

    @DataProvider
    public static Object[][] documentState() {
        return new Object[][] {
            {
                "draft",
                new ArrayList() {{
                    add(RPS_ROOT_FOLDER + "/publication-with-datasets/content/content[2]");
                    add(RPS_ROOT_FOLDER + "/publication-with-datasets/dataset/dataset[2]");
                }},
                new ArrayList() {{
                    add(RPS_ROOT_FOLDER + "/publication-with-datasets/content/content");
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
                    add(RPS_ROOT_FOLDER + "/publication-with-datasets/content/content");
                    add(RPS_ROOT_FOLDER + "/publication-with-datasets/dataset/dataset");
                }},
                new ArrayList() {{
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

    protected Node getNode(String path) throws RepositoryException {
        return rootNode.getNode(path);
    }

    protected Boolean getNodeBooleanProperty(String path, String propertyName) throws RepositoryException {
        return rootNode.getNode(path).getProperty(propertyName).getBoolean();
    }
}
