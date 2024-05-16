package uk.nhs.digital.common.util;

import java.time.Duration;
import java.time.format.DateTimeParseException;

public class DateUtils {
    public static Duration durationFromIso(final String durationIso) throws IllegalArgumentException {
        try {
            String durationIsoTrimmed = durationIso.trim();
            Duration duration = Duration.parse(durationIsoTrimmed);
            return duration;
        } catch (final DateTimeParseException e) {
            throw new IllegalArgumentException("Duration is not in a valid ISO-8601 format: \"" + e.getParsedString() + "\"", e);
        } catch (final Exception e) {
            throw new IllegalArgumentException("Duration failed to parse: \"" + durationIso + "\"", e);
        }
    }
}
