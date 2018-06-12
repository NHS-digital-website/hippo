package uk.nhs.digital.common.util;

import java.time.Instant;
import java.util.function.Supplier;

public class TimeProvider {

    private static Supplier<Instant> nowInstantSupplier = Instant::now;

    public static Instant getNowInstant() {
        return nowInstantSupplier.get();
    }
}
