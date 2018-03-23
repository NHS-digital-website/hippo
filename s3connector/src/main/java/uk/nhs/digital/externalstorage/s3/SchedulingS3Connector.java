package uk.nhs.digital.externalstorage.s3;

import org.onehippo.cms7.services.SingletonService;

import java.io.InputStream;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * See the implementing {@linkplain uk.nhs.digital.externalstorage.s3.BlockingPooledS3Connector} class for details.
 */
@SingletonService
public interface SchedulingS3Connector {

    boolean publishResource(String objectPath);

    boolean unpublishResource(String objectPath);

    void scheduleDownload(String s3Reference, Consumer<S3File> downloadConsumer);

    S3ObjectMetadata scheduleUpload(Supplier<InputStream> inputStreamSupplier,
                                    String fileName,
                                    String mimeType);

    /**
     * Takes a supplier throwing checked exception and wraps it in a try-catch clause, rethrowing the original
     * exception wrapped in {@linkplain RuntimeException}, thus preventing obscuring the calling code with a boilerplate
     * try-catch-rethrow code at the point of calling.
     *
     * @param checkedExceptionThrowingSupplier The supplier to wrap.
     * @param <T>                              Type of the value returned by the supplier.
     * @return Original supplier wrapped in a rethrowing code.
     */
    static <T> Supplier<T> wrapCheckedException(CheckedExceptionThrowingSupplier<T> checkedExceptionThrowingSupplier) {
        return () -> {
            try {
                return checkedExceptionThrowingSupplier.get();
            } catch (final Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    interface CheckedExceptionThrowingSupplier<T> {
        T get() throws Exception;
    }
}
