package uk.nhs.digital;


public abstract class ExceptionUtils {

    public static <T> T wrapCheckedException(final CheckedExceptionThrowingSupplier<T> supplier) {
        T suppliedValue = null;
        try {
            suppliedValue = supplier.get();
        } catch (final Exception originalException) {
            wrapAndRethrowIfChecked(originalException);
        }
        return suppliedValue;
    }

    public static void wrapCheckedException(final CheckedExceptionThrowingConsumer consumer) {
        try {
            consumer.apply();
        } catch (final Exception originalException) {
            wrapAndRethrowIfChecked(originalException);
        }
    }

    private static void wrapAndRethrowIfChecked(final Exception originalException) {
        if (isCheckedException(originalException)) {
            throw new UncheckedWrappingException(originalException);
        } else {
            throw (RuntimeException) originalException;
        }
    }

    private static boolean isCheckedException(final Exception e) {
        return !(e instanceof RuntimeException);
    }

    @FunctionalInterface
    public interface CheckedExceptionThrowingSupplier<T> {
        T get() throws Exception;
    }

    @FunctionalInterface
    public interface CheckedExceptionThrowingConsumer {
        void apply() throws Exception;
    }

    static class UncheckedWrappingException extends RuntimeException {

        public UncheckedWrappingException(final Exception checkedException) {
            super(checkedException);
        }
    }
}
