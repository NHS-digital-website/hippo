package uk.nhs.digital.ps.test.acceptance.data;

import uk.nhs.digital.ps.test.acceptance.models.*;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import static uk.nhs.digital.ps.test.acceptance.models.InformationType.OFFICIAL_STATISTICS;
import static uk.nhs.digital.ps.test.acceptance.models.PublicationBuilder.newPublication;
import static uk.nhs.digital.ps.test.acceptance.models.PublicationStatus.CREATED;
import static uk.nhs.digital.ps.test.acceptance.models.PublicationStatus.PUBLISHED;
import static uk.nhs.digital.ps.test.acceptance.models.TimeSeriesBuilder.newTimeSeries;

/**
 * <p>
 * Provides methods used to create test data objects based on static resources such as Hippo CMS repository YAML files.
 * </p>
 * <p>
 * Ideally, methods of this class should read such source files (hence the 'Loader' part of the class' name) and build
 * test data 'models' dynamically based on the files' content. However, given that the implementation of reliable
 * loading mechanism could be rather involving, in the interest of time, the current implementation actually constructs
 * new test data objects in a way that they <em>match</em> the content of the YAML files, without actually reading the
 * files. Therefore, as long as this approach is followed, it's important to be careful that the factory methods
 * construct objects accurately reflecting content of the source files.
 * </p>
 */
public class TestDataLoader {

    /**
     * @return New instance of time series corresponding to YAML definition
     * {@code /content/documents/publicationsystem/publications/valid-publication-series} in {@code publication.yaml}
     */
    public static TimeSeriesBuilder loadValidTimeSeries() {

        // KEEP IN SYNC WITH publications.yaml OR IMPLEMENT DYNAMIC BUILDING FROM THE FILE

        return newTimeSeries()
            .withName("valid publication series")
            .withTitle("Time Series Lorem Ipsum Dolor")
            .withPublications(
                newPublication()
                    .withTitle("Lorem Ipsum Dolor 2012")
                    .withSummary("A released publication")
                    .withInformationType(OFFICIAL_STATISTICS)
                    .withNominalDate(asInstant("2013-01-10T01:00:00+01:00"))
                    .withStatus(PUBLISHED),
                newPublication()
                    .withTitle("Lorem Ipsum Dolor 2013")
                    .withSummary("A released publication 2013")
                    .withInformationType(OFFICIAL_STATISTICS)
                    .withNominalDate(asInstant("2014-01-10T01:00:00+01:00"))
                    .withStatus(PUBLISHED),
                newPublication()
                    .withTitle("Lorem Ipsum Dolor 2014")
                    .withSummary("Released 2014 stats.")
                    .withInformationType(OFFICIAL_STATISTICS)
                    .withNominalDate(asInstant("2015-01-10T01:00:00+01:00"))
                    .withStatus(PUBLISHED),
                newPublication()
                    .withTitle("Lorem Ipsum Dolor 2015")
                    .withSummary("Released 2015 stats.")
                    .withInformationType(OFFICIAL_STATISTICS)
                    .withNominalDate(asInstant("2016-01-10T01:00:00+01:00"))
                    .withStatus(CREATED)
            );
    }

    private static Instant asInstant(final String timestamp) {
        return Instant.from(DateTimeFormatter.ISO_DATE_TIME.parse(timestamp));
    }
}
