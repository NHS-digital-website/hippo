package uk.nhs.digital.ps.migrator.task;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import uk.nhs.digital.ps.migrator.config.ExecutionParameters;
import uk.nhs.digital.ps.migrator.model.hippo.TaxonomyMigrator;
import uk.nhs.digital.ps.migrator.report.IncidentType;
import uk.nhs.digital.ps.migrator.report.MigrationReport;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(DataProviderRunner.class)
public class ImportableItemsFactoryTest {


    @Mock ExecutionParameters executionParameters;
    @Mock MigrationReport migrationReport;
    @Mock TaxonomyMigrator taxonomyMigrator;

    private ImportableItemsFactory importableItemsFactory;


    @Before
    public void setUp() throws Exception {
        initMocks(this);

        importableItemsFactory = new ImportableItemsFactory(executionParameters, migrationReport, taxonomyMigrator);
    }

    @Test
    @UseDataProvider("summariesWithHtmlMarkup")
    public void summariesWithHtmlMarkupTest(final String summaryWithHtmlMarkup) {

        // given
        final String pCode = "aTestPCodeValue";

        // when
        importableItemsFactory.formatDatasetSummary(summaryWithHtmlMarkup, pCode);

        // then
        verify(migrationReport).report(pCode, IncidentType.HTML_IN_SUMMARY, summaryWithHtmlMarkup);
    }

    @Test
    @UseDataProvider("multilineSummaries")
    public void formatSummaryTest(final String[] testData) throws Exception {

        // given
        final String inputSummary = testData[0];
        final String expectedSummary = testData[1];

        // when
        final String actualSummary = importableItemsFactory.formatDatasetSummary(inputSummary, "aTestPCode");


        // then
        assertThat("Summary is formatted correctly", actualSummary, startsWith(expectedSummary));
    }


    @DataProvider
    public static List<String[]> multilineSummaries() {
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
    public static List<String> summariesWithHtmlMarkup() {
        return Arrays.asList(
            "<p>Summary for a data set",
            "Summary <br/>for a data set",
            "Summary for a data <i>set</i>",
            "Sum&lt;mary&gt; for\n\n a \n\r\n\rdata set"
        );
    }
}
