package uk.nhs.digital.ps.modules.searchableFlagModule;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
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
public class DatasetProcessorTest {

    private Session session;
    private DatasetProcessor datasetProcessor = new DatasetProcessor();
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
    public void shouldUpdateDatasetFromSubfolder(final String state, ArrayList<String> changed, ArrayList<String> unchanged) throws Exception {

        // process publication and datasets
        datasetProcessor.processNode(getNode(RPS_ROOT_FOLDER + "/publication-with-datasets/dataset"), Arrays.asList(state));

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
    public void shouldUpdateDocumentWithGivenStatusTest(final String state, ArrayList<String> changed, ArrayList<String> unchanged) throws Exception {

        // process datasets from within subfolder
        datasetProcessor.processNode(getNode(RPS_ROOT_FOLDER + "/publication-with-datasets/subfolder/dataset"), Arrays.asList(state));

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

    @DataProvider
    public static Object[][] documentState() {
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
    public static Object[][] datasetInSubfolder() {
        return new Object[][] {
            {
                "draft",
                new ArrayList() {{
                    add(RPS_ROOT_FOLDER + "/publication-with-datasets/subfolder/dataset/dataset[2]");
                }},
                new ArrayList() {{
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

    protected Node getNode(String path) throws RepositoryException {
        return rootNode.getNode(path);
    }

    protected Boolean getNodeBooleanProperty(String path, String propertyName) throws RepositoryException {
        return rootNode.getNode(path).getProperty(propertyName).getBoolean();
    }
}
