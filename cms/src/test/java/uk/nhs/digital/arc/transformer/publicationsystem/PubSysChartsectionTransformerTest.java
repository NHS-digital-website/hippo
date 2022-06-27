package uk.nhs.digital.arc.transformer.publicationsystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.onehippo.forge.content.pojo.model.ContentNode;
import uk.nhs.digital.arc.json.publicationsystem.PublicationsystemChartSection;
import uk.nhs.digital.arc.storage.ArcFileData;
import uk.nhs.digital.arc.storage.S3StorageManager;
import uk.nhs.digital.arc.util.FilePathData;

import java.io.InputStream;

import javax.jcr.Session;

public class PubSysChartsectionTransformerTest {

    private static final String CONTENT_TYPE = "application/json";
    public static final String HTTP_FILELOCATION = "http://filelocation";
    public static final String DOCBASE = "docbase";

    @Mock
    Session session;

    @Mock
    S3StorageManager mockStorageManager;

    @Mock
    S3ObjectInputStream mockS3ObjectInputStream;

    @Captor
    ArgumentCaptor<FilePathData> capturedFileData;

    @Before
    public void before() {
        openMocks(this);
    }

    @Test
    public void processNodeWithNoS3DataFileProvided() {
        PublicationsystemChartSection section = getPublicationSystemChartSection(HTTP_FILELOCATION);
        PubSysChartsectionTransformer transformer = new PubSysChartsectionTransformer(session, section, DOCBASE, mockStorageManager);

        // given
        ArcFileData arcFileData = new ArcFileData(mockS3ObjectInputStream, CONTENT_TYPE);
        when(mockStorageManager.getFileMetaData(capturedFileData.capture())).thenReturn(arcFileData);

        // when
        ContentNode node = transformer.process();

        // then
        assertNull(node.getNode(PubSysChartsectionTransformer.PUBLICATIONSYSTEM_DATAFILE));
        assertEquals(HTTP_FILELOCATION, capturedFileData.getValue().getFilePath());
    }

    @Test
    public void processNodeWithAS3DataFileProvided() {
        PublicationsystemChartSection section = getPublicationSystemChartSection("s3://file/location");
        PubSysChartsectionTransformer transformer = new PubSysChartsectionTransformer(session, section, DOCBASE, mockStorageManager);

        // given
        ArcFileData arcFileData = new ArcFileData(getInputStreamFromDataFile("test-data/arc-data/quit_date_by_gender.xlsx"), CONTENT_TYPE);
        when(mockStorageManager.getFileMetaData(capturedFileData.capture())).thenReturn(arcFileData);

        // when
        ContentNode node = transformer.process();

        // then
        assertNotNull(node.getNode(PubSysChartsectionTransformer.PUBLICATIONSYSTEM_DATAFILE));
        assertEquals(getChartConfigResponseJson(), node.getProperty(PubSysChartsectionTransformer.PUBLICATIONSYSTEM_CHARTCONFIG).getValue());
    }

    private String getChartConfigResponseJson() {
        return "{\"title\":{\"text\":\"Self-reported and CO validated quitters time series\"},"
                + "\"series\":[{\"name\":\"Male\",\"data\":[{\"name\":\"Number of quit attempts\",\"y\":75.788}]},"
                + "{\"name\":\"Female\",\"data\":[{\"name\":\"Number of quit attempts\",\"y\":103.027}]}],"
                + "\"chart\":{\"type\":\"bar\"},\"xAxis\":{\"title\":{},\"categories\":[\"Number of quit attempts\"]},"
                + "\"yAxis\":{\"title\":{\"text\":\"y Title\"}}}";
    }

    private InputStream getInputStreamFromDataFile(String stream) {
        return this.getClass().getClassLoader().getResourceAsStream(stream);
    }

    private PublicationsystemChartSection getPublicationSystemChartSection(String location) {
        PublicationsystemChartSection section = new PublicationsystemChartSection("Bar",
            "Self-reported and CO validated quitters time series",
                    location,
            "y Title");
        return section;
    }
}