package uk.nhs.digital.test.util;

import static java.util.Arrays.asList;

import uk.nhs.digital.common.util.TimeProvider;

import java.time.Instant;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

/**
 * Helps with tests of code that rely on real time and obtain 'now' from {@linkplain TimeProvider}.
 */
public class TimeProviderTestUtils {

    private static final String NOW_SUPPLIER_FIELD_NAME = "nowInstantSupplier";

    /**
     * <p>
     * Make sure to reset the {@linkplain TimeProvider} after each test that use {@linkplain TimeProviderTestUtils}
     * to ensure that other tests that may rely on real time do not fail.
     * <p>
     * To this end, call this method from methods annotated with {@linkplain org.junit.After @After} or
     * {@linkplain org.junit.AfterClass @AfterClass}.
     */
    public static void resetTimeProvider() {
        ReflectionTestUtils.setFieldOnClass(TimeProvider.class, NOW_SUPPLIER_FIELD_NAME, (Supplier<Instant>) Instant::now);
    }

    /**
     * <p>
     * Programs {@linkplain TimeProvider} to always return 'now' fixed to given {@code instant}
     * (until {@linkplain #resetTimeProvider()} is invoked).
     * <p>
     * Make sure to invoke {@linkplain #resetTimeProvider} after your tests if you use the current method!
     */
    public static Instant nextNowIs(final Instant instant) {

        nextNowsAre(instant);

        return instant;
    }

    /**
     * <p>
     * Programs {@linkplain TimeProvider} to always return 'now' fixed to given {@code iso8601Time}
     * (until {@linkplain #resetTimeProvider()} is invoked).
     * <p>
     * Make sure to invoke {@linkplain #resetTimeProvider} after your tests if you use the current method!
     *
     * @param iso8601Time Timestamps given as {@linkplain Instant#parse(CharSequence) ISO-8601}
     *                     compliant string.
     */
    public static Instant nextNowIs(final String iso8601Time) {
        return nextNowIs(Instant.parse(iso8601Time));
    }

    /**
     * <p>
     * Programs {@linkplain TimeProvider} so that calls to {@linkplain TimeProvider#getNowInstant()}
     * will return 'now' values as per the sequence given by {@code iso8601Times}
     * (until {@linkplain #resetTimeProvider()} is invoked).
     * <p>
     * Make sure to invoke {@linkplain #resetTimeProvider} after your tests if you use the current method!
     *
     * @param iso8601Times Timestamps given as {@linkplain Instant#parse(CharSequence) ISO-8601}
     *                     compliant string.
     */
    public static List<Instant> nextNowsAre(final String... iso8601Times) {

        final Instant[] instants = Arrays.stream(iso8601Times).map(Instant::parse).toArray(Instant[]::new);

        return nextNowsAre(instants);
    }

    /**
     * <p>
     * Programs {@linkplain TimeProvider} so that calls to {@linkplain TimeProvider#getNowInstant()}
     * will return 'now' values as per the sequence given by {@code instants}
     * (until {@linkplain #resetTimeProvider()} is invoked).
     * <p>
     * Make sure to invoke {@linkplain #resetTimeProvider} after your tests if you use the current method!
     */
    public static List<Instant> nextNowsAre(final Instant... instants) {

        final Iterator<Instant> iterator = asList(instants).iterator();

        final Supplier<Instant> instantSupplier = () -> {
            if (!iterator.hasNext()) {
                throw new RuntimeException("More requests for 'now' were made than were programmed through " + TimeProviderTestUtils.class);
            }

            return iterator.next();
        };

        setNowInstantSupplier(instantSupplier);

        return asList(instants);
    }

    private static void setNowInstantSupplier(final Supplier<Instant> instantSupplier) {
        ReflectionTestUtils.setFieldOnClass(TimeProvider.class, NOW_SUPPLIER_FIELD_NAME, instantSupplier);
    }
}
