package uk.nhs.digital.arc.util;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import uk.nhs.digital.arc.process.ProcessOutcome;
import uk.nhs.digital.arc.storage.ArcStorageManager;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;

public class ManifestUtilsTest extends TestCase {

    public static final String DATA_BUCKET_FILE_TXT = "s3://hippo-data-bucket/large-file-report/file.txt";
    public static final String DATA_BUCKET_SUBFOLDER = "s3://hippo-data-bucket/large-file-report/subfolder";
    public static final String DOCBASE = "docbase";
    public static final String FILE_A = "file_a";
    @Mock
    ArcStorageManager storageManager;

    @Before
    public void setUp() {
        openMocks(this);
    }

    @Test
    public void testNullDocbaseGivesNullResponse() {
        assertNull(ManifestUtils.getDocbaseUsingManifestLocationIfRequired(null, new FilePathData("file.txt")));
    }

    @Test
    public void testDocbaseWithoutS3ProtocolButProvidingAdditionalAbsoluteFilePathDataGivesCorrectDocbase() {
        String response = ManifestUtils.getDocbaseUsingManifestLocationIfRequired("subfolder",
            new FilePathData(DATA_BUCKET_FILE_TXT));
        assertEquals(DATA_BUCKET_SUBFOLDER, response);
    }

    @Test
    public void testDocbaseWithS3ProtocolAndProvidingAdditionalAbsoluteFilePathDataGivesOriginalDocbase() {
        String response = ManifestUtils.getDocbaseUsingManifestLocationIfRequired(DATA_BUCKET_SUBFOLDER,
            new FilePathData(DATA_BUCKET_FILE_TXT));
        assertEquals(DATA_BUCKET_SUBFOLDER, response);
    }

    @Test
    public void testValidityOfUrlsCorrectlyIdentified() {
        //* given
        FilePathData filePathA = new FilePathData(DOCBASE, FILE_A);
        when(storageManager.fileExists(filePathA)).thenReturn(true);

        //* when
        ProcessOutcome processOutcome = new ProcessOutcome("Start");
        ManifestUtils.checkValidityOfUrls(storageManager, DOCBASE, Arrays.asList(FILE_A), processOutcome);

        //* then
        assertEquals("Start\tFound the file 'file_a' in the correct location\n", processOutcome.getMessageLine());
    }

    @Test
    public void testFindsInvalidUrl() {
        //* given
        FilePathData filePathA = new FilePathData(DOCBASE, FILE_A);
        when(storageManager.fileExists(filePathA)).thenReturn(false);

        //* when
        ProcessOutcome processOutcome = new ProcessOutcome("Start");
        ManifestUtils.checkValidityOfUrls(storageManager, DOCBASE, Arrays.asList(FILE_A), processOutcome);

        //* then
        assertEquals("Start\t** Unable to find file 'file_a' in the location you specified\n", processOutcome.getMessageLine());
    }

    @Test
    public void testCreatesLocationName() {

        //* given
        String instantCreated = "2014-12-22T10:15:30Z";
        String instantExpected = "2014-12-22_10:15:30";
        Clock clock = Clock.fixed(Instant.parse(instantCreated), ZoneId.of("UTC"));
        Instant instant = Instant.now(clock);

        //* when
        String response = ManifestUtils.getOutputFileLocation(instant, "XXXXX");

        //* then
        assertEquals(String.format("%s_outcome_XXXXX.txt", instantExpected), response);
    }
}