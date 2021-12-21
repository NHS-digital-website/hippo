package uk.nhs.digital.arc.transformer.impl.pagelevel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.onehippo.forge.content.pojo.model.ContentNode;
import uk.nhs.digital.arc.json.Dataset;
import uk.nhs.digital.arc.json.publicationsystem.PublicationsystemResourceOrExternalLink;
import uk.nhs.digital.arc.storage.ArcStorageManager;
import uk.nhs.digital.arc.transformer.abs.AbstractTransformer;
import uk.nhs.digital.externalstorage.s3.S3ObjectMetadata;

import java.util.ArrayList;
import java.util.List;

public class DatasetTransformerTest {

    public static final String TITLE = "Title";
    public static final String SUMMARY = "Summary";
    public static final String SHORT_DATE = "2022-02-02";
    public static final String LONG_DATE = "2022-02-02T00:00:00.000Z";
    public static final String DATASET = "PublicationSystem:Dataset";

    @Mock
    ArcStorageManager mockStorageManager;

    @Mock
    S3ObjectMetadata mockS3ObjectMetaData;

    @Before
    public void setUp() {
        openMocks(this);
    }

    @Test
    public void createsContentNodeForABareBonesDataset() {
        //* given
        DatasetTransformer transformer = buildASimpleDatasetTransformer(null, null);

        //* when
        ContentNode node = transformer.process();

        //* then
        assertEquals(TITLE, node.getProperty(AbstractTransformer.PUBLICATIONSYSTEM_TITLE_UC).getValue());
        assertEquals(SUMMARY, node.getProperty(AbstractTransformer.PUBLICATIONSYSTEM_SUMMARY).getValue());
        assertEquals(LONG_DATE, node.getProperty(AbstractTransformer.PUBLICATIONSYSTEM_NOMINALDATE).getValue());

        assertNull(node.getNode(AbstractTransformer.PUBLICATION_SYSTEM + "Files-v3"));
    }

    @Test
    public void createsContentNodeForABareBonesDatasetWithLinks() {
        //* given
        DatasetTransformer transformer = buildASimpleDatasetTransformer(listOfThings("resource"), null);

        //* when
        ContentNode node = transformer.process();

        //* then
        assertEquals(TITLE, node.getProperty(AbstractTransformer.PUBLICATIONSYSTEM_TITLE_UC).getValue());
        assertEquals(SUMMARY, node.getProperty(AbstractTransformer.PUBLICATIONSYSTEM_SUMMARY).getValue());
        assertEquals(LONG_DATE, node.getProperty(AbstractTransformer.PUBLICATIONSYSTEM_NOMINALDATE).getValue());

        List<ContentNode> nodes = node.getNodes();
        assertCountOfNodesIs(nodes, AbstractTransformer.PUBLICATIONSYSTEM_RELATEDLINK, 2);
        assertCountOfNodesIs(nodes, AbstractTransformer.PUBLICATION_SYSTEM + "Files-v3", 0);
    }

    @Test
    public void createsContentNodeForADatasetWithAttachmentsAndLinks() {
        //* given
        DatasetTransformer transformer = buildASimpleDatasetTransformer(listOfThings("resource"), listOfThings("file"));
        given(mockStorageManager.uploadFileToS3(any(), any())).willReturn(mockS3ObjectMetaData);

        //* when
        ContentNode node = transformer.process();

        //* then
        assertEquals(TITLE, node.getProperty(AbstractTransformer.PUBLICATIONSYSTEM_TITLE_UC).getValue());
        assertEquals(SUMMARY, node.getProperty(AbstractTransformer.PUBLICATIONSYSTEM_SUMMARY).getValue());
        assertEquals(LONG_DATE, node.getProperty(AbstractTransformer.PUBLICATIONSYSTEM_NOMINALDATE).getValue());

        List<ContentNode> nodes = node.getNodes();
        assertCountOfNodesIs(nodes, AbstractTransformer.PUBLICATIONSYSTEM_RELATEDLINK, 2);
        assertCountOfNodesIs(nodes, AbstractTransformer.PUBLICATIONSYSTEM_EXTATTACHMENT, 2);
    }

    private DatasetTransformer buildASimpleDatasetTransformer(List<PublicationsystemResourceOrExternalLink> resources,
                                                              List<PublicationsystemResourceOrExternalLink> files) {
        Dataset dataset = new Dataset(DATASET, TITLE, SUMMARY, SHORT_DATE);
        dataset.setResourceLinks(resources);
        dataset.setFiles(files);

        DatasetTransformer transformer = new DatasetTransformer();
        transformer.setStorageManager(mockStorageManager);
        transformer.setDoctype(dataset);

        return transformer;
    }

    private void assertCountOfNodesIs(List<ContentNode> nodes, String nodeType, int counter) {
        if (nodes != null) {
            assertEquals(counter, nodes.stream().filter(node -> nodeType.equals(node.getPrimaryType())).count());
        }
    }

    private List<PublicationsystemResourceOrExternalLink> listOfThings(String what) {
        PublicationsystemResourceOrExternalLink link1 = new PublicationsystemResourceOrExternalLink("TEXT 1" + what, "URL_1");
        PublicationsystemResourceOrExternalLink link2 = new PublicationsystemResourceOrExternalLink("TEXT 2" + what, "URL_2");

        List<PublicationsystemResourceOrExternalLink> list = new ArrayList<>();
        list.add(link1);
        list.add(link2);

        return list;
    }

}