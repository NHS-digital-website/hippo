package uk.nhs.digital.common;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.jackrabbit.value.BooleanValue;
import org.apache.jackrabbit.value.DateValue;
import org.apache.jackrabbit.value.LongValue;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

@RunWith(DataProviderRunner.class)
public class OrderedSearchDateDerivedDataFunctionTest {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    private static final String ORDERED_SEARCH_DATE = "orderedSearchDate";

    private OrderedSearchDateDerivedDataFunction function = new OrderedSearchDateDerivedDataFunction();

    @Test
    public void upcomingDateConvertedToNegativeMillis() throws Exception {
        // given
        Map<String, Value[]> map = generateInputMap("20-03-2020", false);

        // when
        Map<String, Value[]> result = function.compute(map);

        // then
        long expectedValue = -parseDate("20-03-2020").getTimeInMillis();
        assertThat("Computed value is negative date millis", result, hasEntry(ORDERED_SEARCH_DATE, new Value[]{new LongValue(expectedValue)}));
    }

    @Test
    public void publishedDateConvertedToPositiveMillis() throws Exception {
        // given
        Map<String, Value[]> map = generateInputMap("10-08-2010", true);

        // when
        Map<String, Value[]> result = function.compute(map);

        // then
        long expectedValue = parseDate("10-08-2010").getTimeInMillis();
        assertThat("Computed value is positive date millis", result, hasEntry(ORDERED_SEARCH_DATE, new Value[]{new LongValue(expectedValue)}));
    }

    @Test
    public void documentWithoutAccessibleFlagConvertedToPositiveMillis() throws Exception {
        // given
        Map<String, Value[]> map = generateInputMap("18-11-2013", null);

        // when
        Map<String, Value[]> result = function.compute(map);

        // then
        long expectedValue = parseDate("18-11-2013").getTimeInMillis();
        assertThat("Computed value is positive date millis", result, hasEntry(ORDERED_SEARCH_DATE, new Value[]{new LongValue(expectedValue)}));
    }

    @Test
    @UseDataProvider("publiclyAccessibleValues")
    public void documentWithoutDateDoesNotGetComputedValue(Boolean publiclyAccessible) throws Exception {
        // given
        Map<String, Value[]> inputMap = generateInputMap(null, publiclyAccessible);

        // then
        assertNull("With no date input you get no computed value", function.compute(inputMap).get(ORDERED_SEARCH_DATE));
    }

    @Test
    public void datesSortedByComputedValueAreOrderedAsExpected() throws Exception {
        // given
        Map<String, Map<String, Value[]>> dateToInput = new HashMap<>();
        addInput(dateToInput, "25-02-2020", null);
        addInput(dateToInput, "09-03-2020", true);
        addInput(dateToInput, "10-03-2020", false);
        addInput(dateToInput, "11-03-2020", true);
        addInput(dateToInput, "12-03-2020", false);
        addInput(dateToInput, "15-03-2020", null);
        addInput(dateToInput, "20-05-2023", true);
        addInput(dateToInput, "21-06-2023", false);

        // when
        List<String> sortedDates = dateToInput.entrySet().stream()
            .map(entry -> new MutablePair<>(entry.getKey(), computeValue(entry.getValue())))
            .sorted(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        // We sort from highest to lowest so need to reverse
        Collections.reverse(sortedDates);

        // then
        assertThat("The order should be published first in descending order followed by upcoming in ascending date order",
            sortedDates, contains(
                "20-05-2023",
                "15-03-2020",
                "11-03-2020",
                "09-03-2020",
                "25-02-2020",
                "10-03-2020",
                "12-03-2020",
                "21-06-2023"
            ));
    }

    private void addInput(Map<String, Map<String, Value[]>> map, String date, Boolean publiclyAccessible) throws ParseException {
        map.put(date, generateInputMap(date, publiclyAccessible));
    }

    private long computeValue(Map<String, Value[]> input) {
        try {
            return function.compute(input).get(ORDERED_SEARCH_DATE)[0].getLong();
        } catch (RepositoryException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Map<String, Value[]> generateInputMap(String dateStr, Boolean publiclyAccessible) throws ParseException {
        Map<String, Value[]> map = new HashMap<>();

        if (dateStr != null) {
            map.put("date", new Value[]{new DateValue(parseDate(dateStr))});
        }

        if (publiclyAccessible != null) {
            map.put("publiclyAccessible", new Value[]{new BooleanValue(publiclyAccessible)});
        }
        return map;
    }

    private Calendar parseDate(String dateStr) throws ParseException {
        Calendar date = Calendar.getInstance();
        date.setTime(DATE_FORMAT.parse(dateStr));
        return date;
    }

    @DataProvider
    public static Object[] publiclyAccessibleValues() {
        return new Boolean[] {
            true,
            false,
            null
        };
    }
}
