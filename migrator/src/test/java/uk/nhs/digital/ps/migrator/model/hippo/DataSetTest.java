package uk.nhs.digital.ps.migrator.model.hippo;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(DataProviderRunner.class)
public class DataSetTest {

    @Test
    @UseDataProvider("validSummaries")
    public void summaryFormatTest(String[] testData) {
        String input = testData[0];
        String expected = testData[1];

        DataSet dataSet = new DataSet(null, "", "", input);
        assertThat(dataSet.getSummary(), is(expected));
    }

    @Test(expected = RuntimeException.class)
    @UseDataProvider("invalidSummaries")
    public void invalidSummaryFormatTest(String summary) {
        new DataSet(null, "", "", summary);
    }

    @DataProvider
    public static List<String[]> validSummaries() {
        return Arrays.asList(
            //           Input                                       Expected
            // Any number of new line chars > 2 are replaced with two new lines
            new String[]{"Summary for\n\n a data set",               "Summary for\\n\\n a data set"},
            new String[]{"Summary for\n\r\n a data set",             "Summary for\\n\\n a data set"},
            new String[]{"Summary for\n\n\n\r\n\n\r\n a data set",   "Summary for\\n\\n a data set"},
            new String[]{"Summary\n\n for\n\n\r\n a\n\n data set",   "Summary\\n\\n for\\n\\n a\\n\\n data set"},

            // New lines at the start and end are trimmed
            new String[]{"\n\rSummary for\n\n a data set\n",         "Summary for\\n\\n a data set"},
            new String[]{"\n\nSummary for a data set",               "Summary for a data set"},
            new String[]{"Summary for a data set\n\n\n",             "Summary for a data set"},

            // No double new lines so leave alone except for ecapsing special chars
            new String[]{"Summary for a data set",                   "Summary for a data set"},
            new String[]{"Summary for\n a \"data set\"",             "Summary for\\n a \\\"data set\\\""},
            new String[]{"Summary for\n\r a data set",               "Summary for\\n a data set"}
        );
    }

    @DataProvider
    public static List<String> invalidSummaries() {
        return Arrays.asList(
            // If html tags are input, we get an exception
            "<p>Summary for a data set",
            "Summary <br>for a data set",
            "Summary for a data <i>set</i>",
            "Sum&gt;mary for a data set",
            "Sum&lt;mary for\n\n a \n\r\n\rdata set"
        );
    }
}
