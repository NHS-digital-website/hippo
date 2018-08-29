package uk.nhs.digital.ps.test.acceptance.util;

import static java.text.MessageFormat.format;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.Assert.assertThat;

import org.hamcrest.Matcher;

import java.time.Instant;
import java.util.function.Supplier;

public abstract class AssertionHelper {

    private static final int TIMEOUT_IN_SECS = 10;
    private static final int POLL_DELAY_IN_MILLIS = 100;

    /**
     * <p>
     * Repeatedly attempts to assert given actual and expected values until the assertion is met or a timeout is
     * reached, delegating to {@linkplain org.junit.Assert#assertThat(String, Object, Matcher)}.
     * </p><p>
     * When the assertion is resolved before the timeout the method simply exits; otherwise it re-throws the
     * exception caught during most recent assertion attempt.
     * </p>
     */
    public static <T> void assertWithinTimeoutThat(final String assertionFailureMessage,
                                                   final Supplier<T> actualValueSupplier,
                                                   final Matcher<T> matcher) {

        Throwable throwable;

        final Instant timeout = Instant.now().plus(TIMEOUT_IN_SECS, SECONDS);

        do {
            throwable = null;

            try {

                assertThat(assertionFailureMessage, actualValueSupplier.get(), matcher);

            } catch (final Exception | AssertionError exception) {
                // We don't want to suppress java.lang.Error instances other than AssertionError, hence going with the
                // above selection rather than with blanket Throwable; this is so that we do not snuff out unexpected
                // errors (we do expect Exceptions and AssertionErrors during normal course of resolving assertions
                // but anything other from the Throwable hierarchy indicates abnormal conditions that we don't want to
                // ignore but want to fail on early, instead).

                try {
                    MILLISECONDS.sleep(POLL_DELAY_IN_MILLIS);
                } catch (final InterruptedException ie) {
                    throw new Error(ie);
                }
                throwable = exception;
            }
        }
        while (throwable != null && Instant.now().isBefore(timeout));

        if (throwable != null) {
            final String message = format("Assertion failed to evaluate within timeout of {1} seconds. Assertion: {0}",
                throwable.getMessage(), TIMEOUT_IN_SECS);

            throw new AssertionError(message, throwable);
        }
    }

}
