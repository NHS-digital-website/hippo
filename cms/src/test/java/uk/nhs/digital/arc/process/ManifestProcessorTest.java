package uk.nhs.digital.arc.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.onehippo.forge.content.exim.core.impl.WorkflowDocumentVariantImportTask;
import uk.nhs.digital.arc.storage.ArcStorageManager;
import uk.nhs.digital.arc.util.FilePathData;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class ManifestProcessorTest {

    @Mock
    ArcStorageManager storageManager;

    @Mock
    Session mockSession;

    @Mock
    NodeIterator mockPageNodeIterator;

    @Mock
    Node mockNode;

    @Mock
    Node mockRootNode;

    @Before
    public void before() {
        openMocks(this);
    }

    @Test
    public void testBadManifestLocationWillReturnError() throws IOException {
        ManifestProcessor proc = new ManifestProcessor(false, null, "folder/file.json", "/content/data", storageManager, null);
        ManifestProcessingSummary outcome = proc.readWrapperFromFile();

        assertNotNull(outcome);
        assertTrue(outcome.isInError());
        assertTrue(outcome.getConcatenatedMessages().contains("Could not locate manifest file in the location: folder/file.json"));
    }

    @Test
    public void testEmptyPageElementsCauseErrorCondition() throws IOException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("test-data/arc-json/manifest_no_pages.json");

        // given
        ManifestProcessor proc = new ManifestProcessor(true, null, "s3://bucket/folder/manifest_no_pages.json", "/content/data", storageManager, null);

        when(storageManager.fileExists("s3://bucket/folder/manifest_no_pages.json")).thenReturn(true);
        when(storageManager.getFileInputStream(any(FilePathData.class))).thenReturn(in);

        // when
        ManifestProcessingSummary outcome = proc.readWrapperFromFile();

        // then
        assertNotNull(outcome);
        assertTrue(outcome.isInError());
        assertEquals("s3://bucket/folder/root-folder", outcome.getDocbase());
        assertTrue(outcome.isParsing());
        assertTrue(outcome.getConcatenatedMessages().contains("Could not locate any page sections in the file 's3://bucket/folder/manifest_no_pages.json'"));
    }

    @Test
    public void testContentInManifestFoundAndCreated() throws IOException, RepositoryException {
        final InputStream manifestStream = this.getClass().getClassLoader().getResourceAsStream("test-data/arc-json/manifest-good-content-location.json");
        final InputStream contentStream = this.getClass().getClassLoader().getResourceAsStream("test-data/arc-json/publication_no_urls.json");
        final FilePathData pubNoUrls = new FilePathData("s3://bucket/folder/report-folder/publication_no_urls.json");
        final ArcTaskManager mockTaskManager = mock(ArcTaskManager.class);

        // given

        when(storageManager.fileExists("s3://bucket/folder/manifest.json")).thenReturn(true);
        when(storageManager.fileExists(eq(pubNoUrls))).thenReturn(true);
        when(storageManager.getFileInputStream(any(FilePathData.class))).thenReturn(manifestStream, contentStream);

        when(mockSession.getNode(any(String.class))).thenReturn(mockNode);
        when(mockSession.getRootNode()).thenReturn(mockRootNode);
        when(mockNode.getNodes()).thenReturn(mockPageNodeIterator);
        when(mockPageNodeIterator.hasNext()).thenReturn(false);

        //* given for folder derivation in HippoNodeUtils
        setupMocksForNonParsingActions(mockTaskManager);

        // when
        ManifestProcessor proc = new ManifestProcessor(false, mockSession, "s3://bucket/folder/manifest.json", "/content/data", storageManager, mockTaskManager);
        ManifestProcessingSummary outcome = proc.readWrapperFromFile();

        // then
        assertNotNull(outcome);
        assertFalse(outcome.isInError());
        assertEquals("s3://bucket/folder/report-folder", outcome.getDocbase());
        assertFalse(outcome.isParsing());
        assertTrue(outcome.getConcatenatedMessages().contains( "All messages for this activity have been written to the file 's3://bucket/folder/report-folder/"));
    }

    private void setupMocksForNonParsingActions(ArcTaskManager mockTaskManager) throws RepositoryException {
        Node mockRootNode = mock(Node.class);
        Node mockChildRootNode = mock(Node.class);
        Node mockPubChildRootNode = mock(Node.class);
        NodeIterator mockRootNodeIterator = mock(NodeIterator.class);
        NodeIterator mockRootPubNodeIterator = mock(NodeIterator.class);

        WorkflowDocumentVariantImportTask mockImportTask = mock(WorkflowDocumentVariantImportTask.class);
        ArcJcrContentNodeBinder mockContentNodeBinder = mock(ArcJcrContentNodeBinder.class);

        //* given for folder derivation in HippoNodeUtils
        when(mockRootNode.hasNode("content")).thenReturn(true);
        when(mockRootNode.getNodes("content")).thenReturn(mockRootNodeIterator);
        when(mockRootNodeIterator.hasNext()).thenReturn(true);
        when(mockRootNodeIterator.nextNode()).thenReturn(mockChildRootNode);
        when(mockChildRootNode.getName()).thenReturn("content");
        when(mockChildRootNode.isNodeType("hippostd:folder")).thenReturn(true);

        when(mockChildRootNode.hasNode("publication")).thenReturn(true);
        when(mockChildRootNode.getNodes("publication")).thenReturn(mockRootPubNodeIterator);
        when(mockRootPubNodeIterator.hasNext()).thenReturn(true);
        when(mockRootPubNodeIterator.nextNode()).thenReturn(mockPubChildRootNode);
        when(mockPubChildRootNode.getName()).thenReturn("publication");
        when(mockPubChildRootNode.isNodeType("hippo:handle")).thenReturn(true);
        when(mockPubChildRootNode.getPath()).thenReturn("/content/publication");

        //* Task manager passed to ManifestProcessor, so we have to use that instance rather than creat a Mock here
        when(mockTaskManager.getImportTask()).thenReturn(mockImportTask);

        when(mockImportTask.getContentNodeBinder()).thenReturn(mockContentNodeBinder);
        when(mockContentNodeBinder.getExternalStorageReferences()).thenReturn(new ArrayList<>());

    }

    @Test
    public void testUnqualifiedDocBaseIsUsedWhenCheckingForFilenames() throws IOException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("test-data/arc-json/manifest-bad-content-location.json");

        // given
        ManifestProcessor proc = new ManifestProcessor(true, null, "s3://bucket/folder/manifest-bad-content-location.json", "/content/data", storageManager, null);

        when(storageManager.fileExists("s3://bucket/folder/manifest-bad-content-location.json")).thenReturn(true);
        when(storageManager.fileExists("s3://bucket/folder/report-folder/file-not-exist.json")).thenReturn(false);
        when(storageManager.getFileInputStream(any(FilePathData.class))).thenReturn(in);

        // when
        ManifestProcessingSummary outcome = proc.readWrapperFromFile();

        // then
        assertNotNull(outcome);
        assertTrue(outcome.isInError());
        assertTrue(outcome.getConcatenatedMessages()
            .contains("** Could not find the manifest segment 's3://bucket/folder/report-folder/file-not-exist.json' which is referenced in the main manifest as 'content'"));
    }

    @Test
    public void testFullyQualifiedDocBaseIsUsedWhenCheckingForFilenames() throws IOException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("test-data/arc-json/manifest-good-content-location.json");

        // given
        ManifestProcessor proc = new ManifestProcessor(true, null, "s3://bucket/folder/manifest-good-content-location.json", "/content/data", storageManager, null);

        when(storageManager.fileExists("s3://bucket/folder/manifest-good-content-location.json")).thenReturn(true);
        when(storageManager.fileExists(eq(new FilePathData("s3://bucket/folder/report-folder/publication_no_urls.json")))).thenReturn(false);
        when(storageManager.getFileInputStream(any(FilePathData.class))).thenReturn(in);

        // when
        ManifestProcessingSummary outcome = proc.readWrapperFromFile();

        // then
        assertNotNull(outcome);
        assertTrue(outcome.isInError());
        assertTrue(outcome.getConcatenatedMessages()
            .contains("** Could not find the manifest segment 's3://bucket/folder/report-folder/publication_no_urls.json' which is referenced in the main manifest as 'publication'"
                + " - check docbase and filename form a valid path"));
    }

    @Test
    public void testPreviewModeEvaluatesFilePaths() throws IOException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("test-data/arc-json/manifest-good-content-location.json");
        InputStream contentStream = this.getClass().getClassLoader().getResourceAsStream("test-data/arc-json/publication_no_urls.json");

        // given
        ManifestProcessor proc = new ManifestProcessor(true, null, "s3://bucket/folder/manifest-good-content-location.json", "/content/data", storageManager, null);

        when(storageManager.fileExists("s3://bucket/folder/manifest-good-content-location.json")).thenReturn(true);
        when(storageManager.getFileInputStream(any(FilePathData.class)))
            .thenReturn(in)
            .thenReturn(contentStream);

        FilePathData fileExistFilePathData = new FilePathData("s3://bucket/folder/report-folder/publication_no_urls.json");
        when(storageManager.fileExists(eq(fileExistFilePathData))).thenReturn(true);

        // when
        ManifestProcessingSummary outcome = proc.readWrapperFromFile();

        // then
        assertNotNull(outcome);
        assertFalse(outcome.isInError());
        assertTrue(outcome.getConcatenatedMessages()
            .contains("... parsed OK"));
    }

    private InputStream getPublicationInputStreamForFileExists() throws IOException {
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
