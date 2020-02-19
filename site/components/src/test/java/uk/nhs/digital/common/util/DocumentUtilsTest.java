package uk.nhs.digital.common.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

import org.junit.Test;
import org.mockito.Mockito;
import uk.nhs.digital.website.beans.SupplementaryInformation;
import uk.nhs.digital.website.beans.Update;

import java.time.LocalDate;
import java.util.*;

public class DocumentUtilsTest {

    @Test
    public void includesUpdatesWithNullExpiryDate() {
        final Update update = Mockito.mock(Update.class);
        given(update.getExpirydate()).willReturn(null);

        final List<Update> result = DocumentUtils
            .getFilteredAndSortedUpdates(Collections.singletonList(update));

        assertEquals(1, result.size());
        assertEquals(update, result.get(0));
    }

    @Test
    public void excludesUpdatesWithPastExpiryDate() {
        final Update update1 = Mockito.mock(Update.class);
        final Update update2 = Mockito.mock(Update.class);
        int nextYear = LocalDate.now().getYear() + 1;
        Calendar calendar1 = new GregorianCalendar(2020, Calendar.JANUARY, 1);
        Calendar calendar2 = new GregorianCalendar(nextYear, Calendar.JANUARY, 1);
        given(update1.getExpirydate()).willReturn(calendar1);
        given(update2.getExpirydate()).willReturn(calendar2);

        final List<Update> result = DocumentUtils
            .getFilteredAndSortedUpdates(Arrays.asList(update1, update2));

        assertEquals(1, result.size());
        assertEquals(update2, result.get(0));
    }

    @Test
    public void ordersBySeverityAndThenByExpiryDate() {

        final Update update1 = Mockito.mock(Update.class);
        final Update update2 = Mockito.mock(Update.class);
        final Update update3 = Mockito.mock(Update.class);
        final Update update4 = Mockito.mock(Update.class);
        final Update update5 = Mockito.mock(Update.class);
        final Update update6 = Mockito.mock(Update.class);
        int nextYear = LocalDate.now().getYear() + 1;

        final Calendar calendar1 = new GregorianCalendar(nextYear,
            Calendar.DECEMBER, 10);
        final Calendar calendar2 = new GregorianCalendar(nextYear, Calendar.JANUARY,
            10);
        final Calendar calendar3 = new GregorianCalendar(nextYear, Calendar.MARCH,
            10);
        final Calendar calendar4 = new GregorianCalendar(nextYear,
            Calendar.FEBRUARY, 10);
        final Calendar calendar5 = new GregorianCalendar(nextYear, Calendar.JUNE,
            10);

        given(update1.getExpirydate()).willReturn(calendar1);
        given(update1.getSeverity())
            .willReturn(Update.Severity.IMPORTANT.getText());
        given(update2.getExpirydate()).willReturn(calendar2);
        given(update2.getSeverity()).willReturn(Update.Severity.CRITICAL.getText());
        given(update3.getExpirydate()).willReturn(calendar3);
        given(update3.getSeverity()).willReturn(Update.Severity.CRITICAL.getText());
        given(update4.getExpirydate()).willReturn(calendar4);
        given(update4.getSeverity())
            .willReturn(Update.Severity.IMPORTANT.getText());
        given(update5.getExpirydate()).willReturn(calendar5);
        given(update5.getSeverity())
            .willReturn(Update.Severity.INFORMATION.getText());
        given(update6.getExpirydate()).willReturn(null);
        given(update6.getSeverity())
            .willReturn(Update.Severity.IMPORTANT.getText());

        final List<Update> sortedUpdates = DocumentUtils
            .getFilteredAndSortedUpdates(Arrays
                .asList(update1, update2, update3, update4, update5, update6));

        assertEquals(6, sortedUpdates.size());
        assertEquals(update2, sortedUpdates.get(0));
        assertEquals(update3, sortedUpdates.get(1));
        assertEquals(update4, sortedUpdates.get(2));
        assertEquals(update1, sortedUpdates.get(3));
        assertEquals(update6, sortedUpdates.get(4));
        assertEquals(update5, sortedUpdates.get(5));
    }

    @Test
    public void ordersByPublishedDateMostRecentFirst() {
        final SupplementaryInformation supp1 = Mockito
            .mock(SupplementaryInformation.class);
        final SupplementaryInformation supp2 = Mockito
            .mock(SupplementaryInformation.class);
        final SupplementaryInformation supp3 = Mockito
            .mock(SupplementaryInformation.class);

        given(supp1.getPublishedDate())
            .willReturn(new GregorianCalendar(2019, Calendar.JANUARY, 10));
        given(supp2.getPublishedDate())
            .willReturn(new GregorianCalendar(2020, Calendar.JANUARY, 10));
        given(supp3.getPublishedDate())
            .willReturn(new GregorianCalendar(2019, Calendar.JANUARY, 11));

        final List<SupplementaryInformation> sorted = DocumentUtils
            .getSortedSupplementaryInformation(Arrays.asList(supp1, supp2, supp3));

        assertEquals(supp2, sorted.get(0));
        assertEquals(supp3, sorted.get(1));
        assertEquals(supp1, sorted.get(2));
    }
}

