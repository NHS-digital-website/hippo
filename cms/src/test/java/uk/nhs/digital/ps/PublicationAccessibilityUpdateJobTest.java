package uk.nhs.digital.ps;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.time.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class PublicationAccessibilityUpdateJobTest {

    private final PublicationAccessibilityUpdateJob job = new PublicationAccessibilityUpdateJob();

    @Test
    public void testHasReleaseTimePassed_BeforeReleaseTime() {
        LocalDateTime testTime = LocalDateTime.now(PublicationSystemConstants.LONDON_ZONE_ID)
            .withHour(PublicationSystemConstants.HOUR_OF_PUBLICATION_RELEASE)
            .withMinute(PublicationSystemConstants.MINUTE_OF_PUBLICATION_RELEASE)
            .plusDays(1);

        Calendar calendar = GregorianCalendar.from(ZonedDateTime.of(testTime, PublicationSystemConstants.LONDON_ZONE_ID));
        assertFalse(job.hasReleaseTimePassed(calendar));
    }

    @Test
    public void testHasReleaseTimePassed_ExactlyAtReleaseTime() {
        LocalDateTime testTime = LocalDateTime.now(PublicationSystemConstants.LONDON_ZONE_ID)
            .withHour(PublicationSystemConstants.HOUR_OF_PUBLICATION_RELEASE)
            .withMinute(PublicationSystemConstants.MINUTE_OF_PUBLICATION_RELEASE)
            .withSecond(0);

        Calendar calendar = GregorianCalendar.from(ZonedDateTime.of(testTime, PublicationSystemConstants.LONDON_ZONE_ID));
        assertTrue("Should be true exactly at release time", job.hasReleaseTimePassed(calendar));
    }

    @Test
    public void testHasReleaseTimePassed_AfterReleaseTime() {
        LocalDateTime testTime = LocalDateTime.now(PublicationSystemConstants.LONDON_ZONE_ID)
            .withHour(PublicationSystemConstants.HOUR_OF_PUBLICATION_RELEASE)
            .withMinute(PublicationSystemConstants.MINUTE_OF_PUBLICATION_RELEASE)
            .plusMinutes(1);

        Calendar calendar = GregorianCalendar.from(ZonedDateTime.of(testTime, PublicationSystemConstants.LONDON_ZONE_ID));
        assertTrue(job.hasReleaseTimePassed(calendar));
    }

    @Test
    public void testHasReleaseTimePassed_PastDate() {
        LocalDateTime testTime = LocalDateTime.now(PublicationSystemConstants.LONDON_ZONE_ID)
            .minusDays(1)
            .withHour(PublicationSystemConstants.HOUR_OF_PUBLICATION_RELEASE)
            .withMinute(PublicationSystemConstants.MINUTE_OF_PUBLICATION_RELEASE);

        Calendar calendar = GregorianCalendar.from(ZonedDateTime.of(testTime, PublicationSystemConstants.LONDON_ZONE_ID));
        assertTrue(job.hasReleaseTimePassed(calendar));
    }

    @Test
    public void testHasReleaseTimePassed_FutureDate() {
        LocalDateTime testTime = LocalDateTime.now(PublicationSystemConstants.LONDON_ZONE_ID)
            .plusDays(1)
            .withHour(PublicationSystemConstants.HOUR_OF_PUBLICATION_RELEASE)
            .withMinute(PublicationSystemConstants.MINUTE_OF_PUBLICATION_RELEASE);

        Calendar calendar = GregorianCalendar.from(ZonedDateTime.of(testTime, PublicationSystemConstants.LONDON_ZONE_ID));
        assertFalse(job.hasReleaseTimePassed(calendar));
    }
}
