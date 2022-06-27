package uk.nhs.digital.arc.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DateHelperTest {

    @Test
    public void testDateTooShortReturnsEmptyDate() {
        String date = "12-12-90";
        String response = DateHelper.massageDate(date);
        assertEquals("", response);
    }

    @Test
    public void testDateInWrongFormatReturnsEmptyDate() {
        String date = "12-12-1990";
        String response = DateHelper.massageDate(date);
        assertEquals("", response);
    }

    @Test
    public void testDateInCorrectShortFormatReturnsCorrectDate() {
        String date = "1990-12-12";
        String response = DateHelper.massageDate(date);
        assertEquals("1990-12-12T00:00:00.000Z", response);
    }

    @Test
    public void testDateInLongButOddFormatStillReturnsCorrectDate() {
        String date = "1990-12-12ABCDEF";
        String response = DateHelper.massageDate(date);
        assertEquals("1990-12-12T00:00:00.000Z", response);
    }

    @Test
    public void testDateInLongFormatStillReturnsCorrectDate() {
        String date = "1990-12-12T00:00:01.000Z";
        String response = DateHelper.massageDate(date);
        assertEquals("1990-12-12T00:00:00.000Z", response);
    }

    @Test
    public void testEmptyDateReturnsEmptyDate() {
        String date = "";
        String response = DateHelper.massageDate(date);
        assertEquals("", response);
    }

    @Test
    public void testNullDateReturnsEmptyDate() {
        String date = null;
        String response = DateHelper.massageDate(date);
        assertEquals("", response);
    }

    @Test
    public void testShortDateFrom19thCenturyReturnsEmptyDate() {
        String date = "1801-12-12";
        String response = DateHelper.massageDate(date);
        assertEquals("", response);
    }

    @Test
    public void testShortDateFrom22ndCenturyReturnsEmptyDate() {
        String date = "2101-12-12";
        String response = DateHelper.massageDate(date);
        assertEquals("", response);
    }

    @Test
    public void testShortDateWithSlashesReturnsDashesShortDate() {
        String date = "2001/12/12";
        String response = DateHelper.massageDate(date);
        assertEquals("2001-12-12T00:00:00.000Z", response);
    }

    @Test
    public void testLongDateWithSlashesReturnsDashesShortDate() {
        String date = "2001/12/12T00:00:00.000Z";
        String response = DateHelper.massageDate(date);
        assertEquals("2001-12-12T00:00:00.000Z", response);
    }

    @Test
    public void testLongDateWithSlashesAndSpacesInTimeReturnsDashesShortDate() {
        String date = "2001/12/12 00:00:00";
        String response = DateHelper.massageDate(date);
        assertEquals("2001-12-12T00:00:00.000Z", response);
    }
}