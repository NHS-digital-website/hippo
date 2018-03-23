package uk.nhs.digital.externalstorage.s3;

import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * <p>
 * Processes Amazon S3 file upload and download requests, making sure that only a pre-configured
 * number of them can be actively processed at the same time. Uses separate pools of threads for
 * upload and download, blocking requests (and the calling threads) until pooled threads become available
 * to serve them.
 * </p><p>
 * This class is a proxy for {@linkplain S3SdkConnector}. Note that only upload and download requests are
 * handled as scheduled tasks as they are relatively  long running and consume more resources. All other
 * requests are considered 'cheap' and short running and typically requiring an immediate response, therefore calls to
 * methods serving these are passed on to the {@linkplain S3SdkConnector} immediately and without scheduling.
 * </p><p>
 * Since a non-blocking IO is <em>not</em> being used here, the traditional model of
 * one-HTTP-serving-thread-per-user-request applies. This means that this mechanism <em>does not</em> help reducing the
 * number of HTTP request serving threads being created (managed by application container, such as Tomcat) and waiting
 * dormant until S3-serving-thread (managed by the application) becomes available. What it <em>does</em> help with is
 * reducing the CPU load and memory usage coming from active processing of upload and download streams, which is the
 * most resource hungry phase of serving the upload and download requests. For example, upload process reads the
 * incoming file into memory in 5MB chunks, sending each chunk to S3 before reading another. It's easy to see that, if
 * there was no limit of on the number of actively served requests the risk of using up all available memory is high in
 * cases where many large file upload requests are initiated in a short space of time.
 * </p>
 */
public class BlockingPooledS3Connector implements SchedulingS3Connector {

    private final S3Connector s3Connector;
    private final ExecutorService downloadExecutorService;
    private final ExecutorService uploadExecutorService;

    public BlockingPooledS3Connector(final S3Connector s3Connector,
                                     final ExecutorService downloadExecutorService,
                                     final ExecutorService uploadExecutorService
    ) {
        this.s3Connector = s3Connector;
        this.downloadExecutorService = downloadExecutorService;
        this.uploadExecutorService = uploadExecutorService;
    }

    /**
     * Instantly triggers permissions on the given S3 file to be set to 'public' by delegating the call directly to
     * {@linkplain S3SdkConnector#publishResource} method.
     */
    @Override
    public boolean publishResource(final String objectPath) {
        return s3Connector.publishResource(objectPath);
    }

    /**
     * Instantly triggers permissions on the given S3 file to be set to 'restricted' by delegating the call directly to
     * {@linkplain S3SdkConnector#unpublishResource} method.
     */
    @Override
    public boolean unpublishResource(final String objectPath) {
        return s3Connector.unpublishResource(objectPath);
    }

    /**
     * <p>
     * Schedules an upload to S3 as a task to be executed as soon as one of the pooled threads becomes available.
     * Blocks the calling thread until the upload is complete.
     * </p><p>
     * </p>
     *
     * @param inputStreamSupplier Provider of the input stream of the file being uploaded. Wrapping the stream in
     *                            a supplier allows to access the stream as late as possible, that is when the task
     *                            is actually being executed rather than at the point of it being scheduled.
     *                            If the method of obtaining the stream declares a checked exception, a conveninence
     *                            method {@linkplain SchedulingS3Connector#wrapCheckedException} can be used to avoid
     *                            having to deal with try-catch boilerplate code.
     * @param fileName            Name of the uploaded file.
     * @param mimeType            Type of the uploaded file.
     */
    @Override
    public S3ObjectMetadata scheduleUpload(final Supplier<InputStream> inputStreamSupplier,
                                           final String fileName,
                                           final String mimeType
    ) {
        return schedule(uploadExecutorService, () -> {
            final S3ObjectMetadata uploadedS3Object = s3Connector.uploadFile(
                inputStreamSupplier.get(), fileName, mimeType
            );

            return uploadedS3Object;
        });
    }

    /**
     * <p>
     * Schedules a download from S3 as a task to be executed as soon as one of the pooled threads becomes available,
     * leaving the processing of the download input stream to the callback given as an argument. Blocks the calling
     * thread until the callback finishes processing the download.
     * <p/><p>
     * The callback receives a reference to {@linkplain S3File} which exposes download input stream via
     * {@linkplain S3File#getContent()}.
     * <p/><p>
     * <strong>Important</strong>:
     * <ul>
     * <li>Make sure that the processing of that stream takes place within the callback, to ensure that it's being
     * processed by the pooled thread; failing to do it this way (say, by returning the stream to outside of the
     * callback and processing it there) defeats the limitation of only a pre-defined number of requests being
     * actively processed concurrently, thus risking starving the whole application of resources.</li>
     * <li>Close the input stream as soon as you're done with it, preferably within the callback.</li>
     * </ul>
     * </p>
     *
     * @param s3Reference      Path to the file in S3.
     * @param downloadConsumer Callback to execute once the download actually gets initiated
     *                         and the download input stream becomes available.
     */
    @Override
    public void scheduleDownload(final String s3Reference, final Consumer<S3File> downloadConsumer) {

        schedule(downloadExecutorService, () -> {
            final S3File s3File = s3Connector.downloadFile(s3Reference);
            downloadConsumer.accept(s3File);
        });
    }

    private void schedule(final ExecutorService executorService, final Runnable scheduledTask) {
        try {
            executorService.submit(scheduledTask).get();
        } catch (final Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private <V> V schedule(final ExecutorService executorService,
                           final Callable<V> scheduledTask
    ) {
        try {
            return executorService.submit(scheduledTask).get();
        } catch (final Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
