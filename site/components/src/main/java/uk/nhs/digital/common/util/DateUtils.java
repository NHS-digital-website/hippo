package uk.nhs.digital.common.util;

import java.time.Duration;
import java.time.format.DateTimeParseException;

public class DateUtils {
    public static Duration durationFromIso(final String durationIso)
        throws IllegalArgumentException, DateTimeParseException {

        String durationIsoTrimmed = null;
        try {
            durationIsoTrimmed = durationIso.trim();
            Duration duration = Duration.parse(durationIsoTrimmed);
            return duration;
        } catch (final Exception e) {
            throw new IllegalArgumentException("Duration is not in a valid ISO-8601 format: \"" + durationIsoTrimmed + "\"", e);
        }
    }
}
