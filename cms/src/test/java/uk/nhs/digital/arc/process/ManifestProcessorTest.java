package uk.nhs.digital.arc.process;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.nhs.digital.arc.storage.ArcStorageManager;
import uk.nhs.digital.arc.util.FilePathData;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class ManifestProcessorTest {

    @Mock
    ArcStorageManager storageManager;

    @Before
    public void before() {
        openMocks(this);
    }

    @Test
    public void testBadManifestLocationWillReturnError() throws IOException {
        ManifestProcessor proc = new ManifestProcessor(null, "folder/file.json", "/content/data");
        ManifestProcessingSummary outcome = proc.readWrapperFromFile();

        assertNotNull(outcome);
        assertTrue(outcome.isInError());
        assertEquals("Could not locate manifest file in the location: folder/file.json", outcome.getConcatenatedMessages());
    }

    @Test
    public void testEmptyPageElementsCauseErrorCondition() throws IOException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("test-data/arc-json/manifest_no_pages.json");

        // given
        ManifestProcessor proc = new ManifestProcessor(null, "s3://bucket/folder/manifest_no_pages.json", "/content/data", storageManager);

        when(storageManager.fileExists("s3://bucket/folder/manifest_no_pages.json")).thenReturn(true);
        when(storageManager.getFileInputStream(any(FilePathData.class))).thenReturn(in);

        // when
        ManifestProcessingSummary outcome = proc.readWrapperFromFile();

        // then
        assertNotNull(outcome);
        assertTrue(outcome.isInError());
        assertEquals("Could not locate any page sections in the file: s3://bucket/folder/manifest_no_pages.json", outcome.getConcatenatedMessages());
    }

    @Test
    public void testUnqualifiedDocBaseIsUsedWhenCheckingForFilenames() throws IOException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("test-data/arc-json/manifest-bad-content-location.json");

        // given
        ManifestProcessor proc = new ManifestProcessor(null, "s3://bucket/folder/manifest-bad-content-location.json", "/content/data", storageManager);

        when(storageManager.fileExists("s3://bucket/folder/manifest-bad-content-location.json")).thenReturn(true);
        when(storageManager.fileExists("s3://bucket/folder/report-folder/file-not-exist.json")).thenReturn(false);
        when(storageManager.getFileInputStream(any(FilePathData.class))).thenReturn(in);

        // when
        ManifestProcessingSummary outcome = proc.readWrapperFromFile();

        // then
        assertNotNull(outcome);
        assertTrue(outcome.isInError());
        assertTrue(outcome.getConcatenatedMessages()
            .contains("** Could not find the segment: s3://bucket/folder/report-folder/file-not-exist.json - check docbase and filename form a valid path"));
    }

    @Test
    public void testFullyQualifiedDocBaseIsUsedWhenCheckingForFilenames() throws IOException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("test-data/arc-json/manifest-good-content-location.json");

        // given
        ManifestProcessor proc = new ManifestProcessor(null, "s3://bucket/folder/manifest-good-content-location.json", "/content/data", storageManager);

        when(storageManager.fileExists("s3://bucket/folder/manifest-good-content-location.json")).thenReturn(true);
        when(storageManager.fileExists("s3://bucket/folder/report-folder/file-exist.json")).thenReturn(false);
        when(storageManager.getFileInputStream(any(FilePathData.class))).thenReturn(in);

        // when
        ManifestProcessingSummary outcome = proc.readWrapperFromFile();

        // then
        assertNotNull(outcome);
        assertTrue(outcome.isInError());
        assertTrue(outcome.getConcatenatedMessages()
            .contains("** Could not find the segment: s3://bucket/folder/report-folder/file-exist.json - check docbase and filename form a valid path"));
    }

    @Test
    public void testPreviewModeEvaluatesFilePaths() throws IOException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("test-data/arc-json/manifest-good-content-location.json");

        // given
        ManifestProcessor proc = new ManifestProcessor(null, "s3://bucket/folder/manifest-good-content-location.json", "/content/data", storageManager);

        when(storageManager.fileExists("s3://bucket/folder/manifest-good-content-location.json")).thenReturn(true);
        when(storageManager.getFileInputStream(any(FilePathData.class)))
            .thenReturn(in)
                .thenReturn(getInputStreamForFileExists());

        FilePathData fileExistFilePathData = new FilePathData("s3://bucket/folder/report-folder/file-exist.json");
        when(storageManager.fileExists(eq(fileExistFilePathData))).thenReturn(true);

        // when
        ManifestProcessingSummary outcome = proc.readWrapperFromFile();

        // then
        assertNotNull(outcome);
        assertFalse(outcome.isInError());
        assertTrue(outcome.getConcatenatedMessages()
            .contains("... parsed OK"));
    }

    private InputStream getInputStreamForFileExists() throws IOException {
        String json = "{"
            + "\"doctype_REQ\": \"PublicationSystem:Publication\","
            + "\"summary_REQ\": \"This is summary text\","
            + "\"nominal_date_REQ\": \"2022-01-13\","
            + "\"title_REQ\": \"This is the content title, October 2021\","
            + "\"publically_accessible_REQ\": \"true\""
            + "}";

        return IOUtils.toInputStream(json, Charset.forName("UTF-8"));
    }
}
