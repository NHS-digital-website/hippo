package uk.nhs.digital.test.util;

public class ExceptionTestUtils {

    public static <T> T wrapCheckedException(final ExceptionTestUtils.CheckedExceptionThrowingSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void wrapCheckedException(final ExceptionTestUtils.CheckedExceptionThrowingConsumer consumer) {
        try {
            consumer.apply();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public interface CheckedExceptionThrowingSupplier<T> {
        T get() throws Exception;
    }

    public interface CheckedExceptionThrowingConsumer {
        void apply() throws Exception;
    }
}
