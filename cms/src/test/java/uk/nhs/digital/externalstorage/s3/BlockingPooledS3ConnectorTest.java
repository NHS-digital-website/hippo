package uk.nhs.digital.externalstorage.s3;

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
import java.util.concurrent.atomic.AtomicBoolean;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

public class BlockingPooledS3ConnectorTest {

    @Mock private S3SdkConnector s3SdkConnector;
    @Mock private ExecutorService uploadExecutorService;
    @Mock private ExecutorService downloadExecutorService;
    @Mock private Future future;

    @Captor private ArgumentCaptor<Callable<S3ObjectMetadata>> scheduledUploadTaskArgumentCaptor;
    @Captor private ArgumentCaptor<Runnable> scheduledDownloadTaskArgumentCaptor;


    private BlockingPooledS3Connector s3proxy;
    private String expectedObjectPath;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        expectedObjectPath = newRandomString();

        s3proxy = new BlockingPooledS3Connector(
            s3SdkConnector,
            downloadExecutorService,
            uploadExecutorService
        );
    }

    @Test
    public void delegatesPublishedResourceDirectlyToStandardConnector() throws Exception {

        // given
        final boolean expectedActionResult = randomBoolean();
        given(s3SdkConnector.publishResource(expectedObjectPath)).willReturn(expectedActionResult);

        // when
        final boolean actualActionResult = s3proxy.publishResource(expectedObjectPath);

        // then
        then(s3SdkConnector).should().publishResource(expectedObjectPath);
        assertThat("Reported result is as received from the delegate.", actualActionResult, is(expectedActionResult));
    }

    @Test
    public void delegatesUnPublishedResourceDirectlyToStandardConnector() throws Exception {

        // given
        final boolean expectedActionResult = randomBoolean();
        given(s3SdkConnector.unpublishResource(expectedObjectPath)).willReturn(expectedActionResult);

        // when
        final boolean actualActionResult = s3proxy.unpublishResource(expectedObjectPath);

        // then
        then(s3SdkConnector).should().unpublishResource(expectedObjectPath);
        assertThat("Reported result is as received from the delegate.", actualActionResult, is(expectedActionResult));
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
        final S3ObjectMetadata actualS3ObjectMetadata = s3proxy.scheduleUpload(
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
        final AtomicBoolean isTaskExecuted = new AtomicBoolean(false);

        // when
        s3proxy.scheduleDownload(expectedObjectPath, s3File -> isTaskExecuted.set(true));

        // then
        then(future).should().get(); // ensures execution was delegated to provided ExecutorService
        scheduledDownloadTaskArgumentCaptor.getValue().run(); // ensures that the scheduled Runnable actually gets called

        then(s3SdkConnector).should().downloadFile(expectedObjectPath);
        assertThat("Download consumer task has been executed.", isTaskExecuted.get(), is(true));
    }

    private static String newRandomString() {
        return UUID.randomUUID().toString();
    }

    private static boolean randomBoolean() {
        return Math.round(Math.random() * 10) % 2 == 0;
    }
}
