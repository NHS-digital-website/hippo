package uk.nhs.digital.ps.beans;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import uk.nhs.digital.ps.site.exceptions.DataRestrictionViolationException;

import java.time.LocalDate;

public class RestrictableDateTest {

    private LocalDate localDate;

    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        localDate = LocalDate.now();
    }

    @Test
    public void createsFullDate() throws Exception {

        // given
        // setUp

        // when
        final RestrictableDate actualDate = RestrictableDate.fullDateFrom(localDate);

        // then
        assertRestrictableDate(actualDate, false, localDate);
    }

    @Test
    public void createsRestrictedDate() throws Exception {

        // given
        // setUp

        // when
        final RestrictableDate actualDate = RestrictableDate.restrictedDateFrom(localDate);

        // then
        RestrictableDateTest.assertRestrictableDate(actualDate, true, localDate);
    }

    @Test
    public void reportsError_whenAskedForDayComponentOfRestrictedDate() throws Exception {

        // given
        final RestrictableDate restrictableDate = RestrictableDate.restrictedDateFrom(localDate);

        expectedException.expect(DataRestrictionViolationException.class);
        expectedException.expectMessage("Restricted date does not contain day of month component."
            + " Consult 'isRestricted()' before requesting day of month value.");

        // when
        restrictableDate.getDayOfMonth();

        // then
        // framework verifies exception expectations set in 'given'
    }

    public static void assertRestrictableDate(final RestrictableDate actualDate,
                                            final boolean expectedIsRestricted,
                                            final LocalDate expectedDate) {

        assertThat("Date restriction status.",
            actualDate.isRestricted() ? "RESTRICTED" : "FULL",
            is(expectedIsRestricted ? "RESTRICTED" : "FULL")
        );

        assertThat("Year is correct.", actualDate.getYear(), is(expectedDate.getYear()));
        assertThat("Month is correct.", actualDate.getMonth(), is(expectedDate.getMonth()));

        if (!expectedIsRestricted) {
            assertThat("Day is correct.", actualDate.getDayOfMonth(), is(expectedDate.getDayOfMonth()));
        }
    }
}
