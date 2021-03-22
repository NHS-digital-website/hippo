package uk.nhs.digital.test.util;

import java.sql.Date;
import java.time.Instant;
import java.util.Calendar;

public class TimeTestUtils {

    public static Calendar calendarFrom(final String iso8601Time) {
        final Instant instant = Instant.parse(iso8601Time);
        return calendarFrom(instant);
    }

    public static Calendar calendarFrom(final Instant instant) {
        return new Calendar.Builder().setInstant(Date.from(instant)).build();
    }
}