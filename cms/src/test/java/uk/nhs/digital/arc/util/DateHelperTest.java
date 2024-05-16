package uk.nhs.digital.arc.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DateHelperTest {

    @Test
    public void testDateTooShortReturnsNull() {
        String date = "12-12-90";
        String response = DateHelper.massageDate(date);
        assertNull(response);
    }

    @Test
    public void testDateInWrongFormatReturnsNull() {
        String date = "12-12-1990";
        String response = DateHelper.massageDate(date);
        assertNull(response);
    }

    @Test
    public void testDateInCorrectShortFormatReturnsCorrectDate() {
        String date = "1990-12-12";
        String response = DateHelper.massageDate(date);
        assertEquals("1990-12-12T00:00:00.000Z", response);
    }

    @Test
    public void testDateInLongButOddFormatStillReturnsNull() {
        String date = "1990-12-12ABCDEF";
        String response = DateHelper.massageDate(date);
        assertNull(response);
    }

    @Test
    public void testDateInLongFormatAndTLiteralStillReturnsCorrectDate() {
        String date = "1990-12-12T00:00:01";
        String response = DateHelper.massageDate(date);
        assertEquals("1990-12-12T00:00:01.000Z", response);
    }

    @Test
    public void testDateInLongFormatAndSpaceStillReturnsCorrectDate() {
        String date = "1990-12-12 00:00:01";
        String response = DateHelper.massageDate(date);
        assertEquals("1990-12-12T00:00:01.000Z", response);
    }

    @Test
    public void testEmptyDateReturnsEmptyDate() {
        String date = "";
        String response = DateHelper.massageDate(date);
        assertNull(response);
    }

    @Test
    public void testNullDateReturnsNullDate() {
        String date = null;
        String response = DateHelper.massageDate(date);
        assertNull(response);
    }

    @Test
    public void testShortDateFrom19thCenturyReturnsEmptyDate() {
        String date = "1801-12-12";
        String response = DateHelper.massageDate(date);
        assertNull(response);
    }

    @Test
    public void testShortDateFrom22ndCenturyReturnsEmptyDate() {
        String date = "2101-12-12";
        String response = DateHelper.massageDate(date);
        assertNull(response);
    }

    @Test
    public void testShortDateWithSlashesReturnsDashesLongDate() {
        String date = "2001/12/12";
        String response = DateHelper.massageDate(date);
        assertEquals("2001-12-12T00:00:00.000Z", response);
    }

    @Test
    public void testLongDateWithSlashesAndTLiteralReturnsDashesLongDate() {
        String date = "2001/12/12T01:02:03";
        String response = DateHelper.massageDate(date);
        assertEquals("2001-12-12T01:02:03.000Z", response);
    }

    @Test
    public void testLongDateWithSlashesAndSpaceReturnsDashesLongDate() {
        String date = "2001/12/12 01:02:03";
        String response = DateHelper.massageDate(date);
        assertEquals("2001-12-12T01:02:03.000Z", response);
    }

    @Test
    public void testLongDateButPartialTimeReturnsDashesLongDate() {
        String date = "2001-12-12 01:02";
        String response = DateHelper.massageDate(date);
        assertEquals("2001-12-12T01:02:00.000Z", response);
    }

    @Test
    public void testLongDateTimeWithTrailingSpacesReturnsDashesLongDateTime() {
        String date = "2001-12-12 01:02:03     ";
        String response = DateHelper.massageDate(date);
        assertEquals("2001-12-12T01:02:03.000Z", response);
    }
}
