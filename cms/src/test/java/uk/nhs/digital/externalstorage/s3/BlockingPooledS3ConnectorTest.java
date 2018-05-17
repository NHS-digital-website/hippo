package uk.nhs.digital.externalstorage.s3;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

public class BlockingPooledS3ConnectorTest {

    @Mock private S3SdkConnector s3SdkConnector;
    @Mock private ExecutorService uploadExecutorService;
    @Mock private ExecutorService downloadExecutorService;
    @Mock private S3TransfersReportingTracker logger;
    @Mock private Future future;

    @Captor private ArgumentCaptor<Callable<S3ObjectMetadata>> scheduledUploadTaskArgumentCaptor;
    @Captor private ArgumentCaptor<Callable<S3File>> scheduledDownloadTaskArgumentCaptor;


    private BlockingPooledS3Connector s3proxy;
    private String expectedObjectPath;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        expectedObjectPath = newRandomString();

        s3proxy = new BlockingPooledS3Connector(
            s3SdkConnector,
            downloadExecutorService,
            uploadExecutorService,
            logger
        );
    }

    @Test
    public void delegatesPublishResourceDirectlyToStandardConnector() throws Exception {

        // when
        s3proxy.publishResource(expectedObjectPath);

        // then
        then(s3SdkConnector).should().publishResource(expectedObjectPath);
    }

    @Test
    public void delegatesUnPublishResourceDirectlyToStandardConnector() throws Exception {

        // when
        s3proxy.unpublishResource(expectedObjectPath);

        // then
        then(s3SdkConnector).should().unpublishResource(expectedObjectPath);
    }

    @Test
    public void delegatesCopyResourceDirectlyToStandardConnector() throws Exception {

        // given
        final String fileName = newRandomString();

        // when
        s3proxy.copyFile(expectedObjectPath, fileName);

        // then
        then(s3SdkConnector).should().copyFile(expectedObjectPath, fileName);
    }

    @Test
    public void schedulesUploadFileRequest() throws Exception {

        // given
        given(uploadExecutorService.submit(scheduledUploadTaskArgumentCaptor.capture())).willReturn(future);

        final S3ObjectMetadata expectedS3ObjectMetadata = mock(S3ObjectMetadata.class);
        given(future.get()).willReturn(expectedS3ObjectMetadata);

        final InputStream expectedUploadInputStream = mock(InputStream.class);
        final String expectedFileName = newRandomString();
        final String expectedMimeType = newRandomString();

        // when
        final S3ObjectMetadata actualS3ObjectMetadata = s3proxy.upload(
            () -> expectedUploadInputStream,
            expectedFileName,
            expectedMimeType
        );

        // then
        then(future).should().get(); // ensures execution was delegated to provided ExecutorService
        scheduledUploadTaskArgumentCaptor.getValue().call(); // ensures that the scheduled Runnable actually gets called

        then(s3SdkConnector).should().uploadFile(expectedUploadInputStream, expectedFileName, expectedMimeType);
        assertThat("Returns uploaded object metadata produced by the connector.",
            actualS3ObjectMetadata, is(expectedS3ObjectMetadata)
        );
    }

    @Test
    public void schedulesDownloadFileRequest() throws Exception {

        // given
        given(downloadExecutorService.submit(scheduledDownloadTaskArgumentCaptor.capture())).willReturn(future);

        final S3File expectedDownloadedFile = mock(S3File.class);
        given(s3SdkConnector.downloadFile(expectedObjectPath)).willReturn(expectedDownloadedFile);
        given(future.get()).willReturn(expectedDownloadedFile);

        // when
        final AtomicReference<S3File> actualS3File = new AtomicReference<>();
        s3proxy.download(
            expectedObjectPath,
            s3FileReceivedFromS3SdkConnector -> actualS3File.set(s3FileReceivedFromS3SdkConnector)
        );

        // then
        then(future).should().get(); // ensures execution was delegated to provided ExecutorService
        scheduledDownloadTaskArgumentCaptor.getValue().call(); // ensures that the scheduled Callable actually gets called

        then(s3SdkConnector).should().downloadFile(expectedObjectPath);
        assertThat("Download consumer task has been called for downloaded file.",
            actualS3File.get(), is(sameInstance(expectedDownloadedFile)));
    }

    private static String newRandomString() {
        return UUID.randomUUID().toString();
    }

}
