package uk.nhs.digital.ps.test.acceptance.data;

import uk.nhs.digital.ps.test.acceptance.models.*;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import static uk.nhs.digital.ps.test.acceptance.models.InformationType.OFFICIAL_STATISTICS;
import static uk.nhs.digital.ps.test.acceptance.models.PublicationBuilder.collectionOf;
import static uk.nhs.digital.ps.test.acceptance.models.PublicationBuilder.newPublication;
import static uk.nhs.digital.ps.test.acceptance.models.PublicationState.CREATED;
import static uk.nhs.digital.ps.test.acceptance.models.PublicationState.PUBLISHED;
import static uk.nhs.digital.ps.test.acceptance.models.PublicationSeriesBuilder.newPublicationSeries;

/**
 * <p>
 * Provides methods creating test data objects representing documents as defined by Hippo CMS repository YAML files.
 * </p><p>
 * In the future, this class could be turned into a 'loader' building the test data objects dynamically by reading
 * and interpreting the YAML files, thus eliminating the risk that its content could get out of sync with the files,
 * but, given that implementation of reliable loading mechanism could be rather involving, in the interest of time, the
 * current implementation actually constructs these objcects using hardcoded definitions that <em>match</em> the content
 * of the YAML files, without actually reading them.
 * </p><p>
 * Therefore, until dynamic loading is implemented, care must be taken that the implementation of individual factory
 * methods strictly corresponds to the content of corresponding files.
 * </p>
 */
public class ExpectedTestDataProvider {

    /**
     * @return New instance of publication series corresponding to YAML definition
     * {@code /content/documents/publicationsystem/publications/valid-publication-series.yaml}.
     */
    public static PublicationSeriesBuilder getValidPublicationSeries() {

        return newPublicationSeries()
            .withName("valid publication series")
            .withTitle("Time Series Lorem Ipsum Dolor")
            .withPublications(
                newPublication()
                    .withTitle("Lorem Ipsum Dolor 2012")
                    .withSummary("A published publication")
                    .withInformationType(OFFICIAL_STATISTICS)
                    .withNominalDate(asInstant("2013-01-10T01:00:00+01:00"))
                    .inState(PUBLISHED)
                    .withPubliclyAccessible(true),
                newPublication()
                    .withTitle("Lorem Ipsum Dolor 2013")
                    .withSummary("A published publication 2013")
                    .withInformationType(OFFICIAL_STATISTICS)
                    .withNominalDate(asInstant("2014-01-10T01:00:00+01:00"))
                    .inState(PUBLISHED)
                    .withPubliclyAccessible(true),
                newPublication()
                    .withTitle("Lorem Ipsum Dolor 2014")
                    .withSummary("published 2014 stats.")
                    .withInformationType(OFFICIAL_STATISTICS)
                    .withNominalDate(asInstant("2015-01-10T01:00:00+01:00"))
                    .inState(PUBLISHED)
                    .withPubliclyAccessible(true),
                newPublication()
                    .withTitle("Lorem Ipsum Dolor 2015")
                    .withSummary("published 2015 stats.")
                    .withInformationType(OFFICIAL_STATISTICS)
                    .withNominalDate(asInstant("2016-01-10T01:00:00+01:00"))
                    .inState(CREATED)
                    .withPubliclyAccessible(true)
            );
    }

    /**
     * @return New instance collection of latest 'live' documents from the ones defined in
     * {@code /content/documents/corporate-website/publication-system}.
     */
    public static PublicationBuilder.Collection getRecentPublishedLivePublications() {
        return collectionOf(
            newPublication()
                .withTitle("Apple orange pear")
                .withSummary("I like pears")
                .withNominalDate(asInstant("2017-11-08T00:00:00Z"))
                .inState(PUBLISHED)
                .withPubliclyAccessible(true),
            newPublication()
                .withTitle("No mention of interesting terms in the title")
                .withSummary("I like apples")
                .withNominalDate(asInstant("2017-11-08T00:00:00Z"))
                .inState(PUBLISHED)
                .withPubliclyAccessible(true),
            newPublication()
                .withTitle("Apple pear orange bear")
                .withSummary("I like apples")
                .withNominalDate(asInstant("2017-11-07T00:00:00Z"))
                .inState(PUBLISHED)
                .withPubliclyAccessible(true),
            newPublication()
                .withTitle("Apple orange")
                .withSummary("I like apples")
                .withNominalDate(asInstant("2017-11-07T00:00:00Z"))
                .inState(PUBLISHED)
                .withPubliclyAccessible(true),
            newPublication()
                .withTitle("publication with datasets")
                .withSummary("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis convallis non massa vel " +
                    "dictum. In vel ex sapien. Donec sit amet leo lobortis, tempor ex ut, luctus eros. In in eleifend" +
                    " orci, nec suscipit nisi. Maecenas sed velit condimentum, lobortis tortor id, ultricies metus. " +
                    "Integer odio ante, congue pulvinar ultrices nec, mollis vitae dolor. Curabitur euismod erat " +
                    "elit, quis facilisis neque eleifend id. Maecenas convallis vel mi nec bibendum. Donec ut erat " +
                    "dictum, molestie dolor non, aliquet nibh.")
                .withNominalDate(asInstant("2017-10-11T00:00:00Z"))
                .inState(PUBLISHED)
                .withPubliclyAccessible(true)
            );
    }

    /**
     * @return New instance of a published publication flagged as 'upcoming', corresponding to YAML definition
     * {@code /content/documents/corporate-website/publication-system/published-upcoming-publication.yaml}.
     */
    public static PublicationBuilder.Collection getPublishedUpcomingPublications() {
        return collectionOf(newPublication()
                .withName("published-upcoming-publication")
                .withTitle("Published Upcoming Publication")
                .withInformationType(InformationType.OFFICIAL_STATISTICS)
                .withNominalDate(asInstant("2017-06-01T09:30:00.000+01:00"))
                .inState(PUBLISHED)
                .withPubliclyAccessible(false),
            newPublication()
                .withName("upcoming-publication")
                .withTitle("Upcoming Publication")
                .withInformationType(InformationType.EXPERIMENTAL_STATISTICS)
                .withNominalDate(asInstant("2017-10-10T01:00:00.000+01:00"))
                .inState(PUBLISHED)
                .withPubliclyAccessible(false));
    }

    private static Instant asInstant(final String timestamp) {
        return Instant.from(DateTimeFormatter.ISO_DATE_TIME.parse(timestamp));
    }
}
