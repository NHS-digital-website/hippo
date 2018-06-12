package uk.nhs.digital.ps.test.acceptance.data;

import static java.util.Arrays.asList;
import static uk.nhs.digital.ps.test.acceptance.models.GeographicCoverage.ENGLAND;
import static uk.nhs.digital.ps.test.acceptance.models.Granularity.COUNTY;
import static uk.nhs.digital.ps.test.acceptance.models.InformationType.EXPERIMENTAL_STATISTICS;
import static uk.nhs.digital.ps.test.acceptance.models.InformationType.OFFICIAL_STATISTICS;
import static uk.nhs.digital.ps.test.acceptance.models.PublicationBuilder.collectionOf;
import static uk.nhs.digital.ps.test.acceptance.models.PublicationBuilder.newPublication;
import static uk.nhs.digital.ps.test.acceptance.models.PublicationPageBuilder.newPublicationPage;
import static uk.nhs.digital.ps.test.acceptance.models.PublicationSeriesBuilder.newPublicationSeries;
import static uk.nhs.digital.ps.test.acceptance.models.PublicationState.CREATED;
import static uk.nhs.digital.ps.test.acceptance.models.PublicationState.PUBLISHED;

import uk.nhs.digital.ps.test.acceptance.models.*;
import uk.nhs.digital.ps.test.acceptance.models.section.*;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
                .withNominalDate(asInstant("2020-11-08T00:00:00Z"))
                .inState(PUBLISHED)
                .withPubliclyAccessible(true),
            newPublication()
                .withTitle("No mention of interesting terms in the title")
                .withSummary("I like apples")
                .withNominalDate(asInstant("2020-11-08T00:00:00Z"))
                .inState(PUBLISHED)
                .withPubliclyAccessible(true),
            newPublication()
                .withTitle("Apple pear orange bear")
                .withSummary("I like apples")
                .withNominalDate(asInstant("2020-11-07T00:00:00Z"))
                .inState(PUBLISHED)
                .withPubliclyAccessible(true),
            newPublication()
                .withTitle("Apple orange")
                .withSummary("I like apples")
                .withNominalDate(asInstant("2020-11-07T00:00:00Z"))
                .inState(PUBLISHED)
                .withPubliclyAccessible(true),
            newPublication()
                .withTitle("publication with datasets")
                .withSummary("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis"
                    + " convallis non massa vel dictum. In vel ex sapien. Donec sit amet leo"
                    + " lobortis, tempor ex ut, luctus eros. In in eleifend orci, nec suscipit nisi."
                    + " Maecenas sed velit condimentum, lobortis tortor id, ultricies metus."
                    + " Integer odio ante, congue pulvinar ultrices nec, mollis vitae dolor."
                    + " Curabitur euismod erat elit, quis facilisis neque eleifend id."
                    + " Maecenas convallis vel mi nec bibendum. Donec ut erat dictum, molestie dolor"
                    + " non, aliquet nibh.")
                .withNominalDate(asInstant("2020-10-11T00:00:00Z"))
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
                .withSummary("Published Upcoming Publication Summary")
                .withInformationType(InformationType.OFFICIAL_STATISTICS)
                .withNominalDate(asInstant("2025-06-18T09:30:00.000+01:00"))
                .inState(PUBLISHED)
                .withPubliclyAccessible(false),
            newPublication()
                .withName("upcoming-publication")
                .withTitle("Upcoming Publication")
                .withSummary("Upcoming Publication Summary")
                .withInformationType(InformationType.EXPERIMENTAL_STATISTICS)
                .withNominalDate(asInstant("2025-06-18T01:00:00.000+01:00"))
                .inState(PUBLISHED)
                .withPubliclyAccessible(false));
    }

    public static PublicationBuilder getSectionedPublication() {
        List<BodySection> bodySections = asList(
            new TextSection("Text section heading without body", null),
            new ImageSection("sectioned publication page robots",
                "Robots",
                "Image with link and caption",
                "https://google.com/"),
            new TextSection("Second text section with body below",
                "Far far away, behind the word mountains, far from the countries Vokalia and Consonantia, "
                    + "there live the blind texts. Separated they live in Bookmarksgrove right at the coast of "
                    + "the Semantics, a large language ocean. A small river named Duden flows by their place and"
                    + " supplies it with the necessary regelialia. It is a paradisematic country, in which roasted"
                    + " parts of sentences fly into your mouth. Even the all-powerful Pointing has no control about"
                    + " the blind texts it is an almost unorthographic life One day however a small line of blind"
                    + " text by the name of Lorem Ipsum decided to"),
            new TextSection(null,
                "Body without heading. Convallis, nascetur. Pede tristique nam interdum augue euismod nibh "
                    + "hendrerit. Ut. Condimentum libero facilisi sed. Inceptos natoque feugiat venenatis mattis "
                    + "turpis leo platea est mollis quam pretium lacus hymenaeos metus ac. Convallis tristique. "
                    + "Suscipit accumsan tempor ante natoque fermentum nisl, iaculis natoque molestie magna mattis"
                    + " porttitor ligula Consequat mattis facilisis amet montes consequat vel dis class quisque netus"
                    + " montes. Placerat nam bibendum libero consectetuer. Mattis aliquam. Consequat."),
            new ImageSection("sectioned publication page snowman",
                "Snowman"),
            new RelatedLinkSection("BBC Homepage", "https://www.bbc.co.uk/"),
            new RelatedLinkSection("Sky website", "http://www.sky.com/"),
            new TextSection("Lower down heading", "Lorem ipsum diam felis ante nullam velit curabitur "
                + "sociosqu convallis himenaeos aliquet ut, massa eros imperdiet etiam fusce curabitur vestibulum class"
                + " ad litora platea phasellus sapien nec maecenas vivamus aliquam leo augue maecenas per, praesent et"
                + " porttitor arcu ut luctus vulputate neque, risus cubilia odio mattis quisque volutpat rhoncus vestibulum"
                + " turpis, elit tellus fringilla viverra auctor condimentum elit blandit nunc fusce sollicitudin cras"
                + " lectus amet placerat fusce viverra, sagittis primis at faucibus varius orci proin consequat litora,"
                + " et primis torquent donec consequat lobortis molestie dapibus lorem mattis integer quisque faucibus"
                + " neque, ullamcorper platea enim per nostra, dolor id vitae quam."),
            new ChartSection("Stacked Bar Chart"),
            new ChartSection("Pie Chart"),
            new ChartSection("Scatter Plot"),
            new ChartSection("Funnel Plot"),
            new TextSection("Lowest Heading on the first page", "Maecenas tincidunt at mi sit amet fringilla."
                + " Nam ut tortor in mi rhoncus congue. Nunc eget varius ligula. Suspendisse et tincidunt libero. Sed"
                + " maximus, mauris quis eleifend iaculis, leo odio feugiat ex, quis varius orci est in purus. In mollis"
                + " justo eu pulvinar tincidunt. Quisque quam nisl, vulputate sed lectus at, mattis viverra magna. Nam"
                + " tincidunt nec lorem id egestas. In nec nisi tristique, volutpat quam vel, ornare leo. Phasellus vel"
                + " diam viverra, hendrerit eros vitae, vestibulum nisl. Nulla semper finibus leo a ullamcorper. Pellentesque"
                + " id dui non nisl ullamcorper egestas. Pellentesque erat tellus, vehicula vel aliquam a, lacinia sed eros."
                + " Praesent bibendum auctor dapibus. Phasellus scelerisque neque vitae aliquam varius. Nunc faucibus lorem eu"
                + " condimentum mollis. Nam feugiat viverra odio quis scelerisque. Mauris euismod efficitur magna, non gravida"
                + " tortor vestibulum quis. Nullam venenatis lacinia nulla ultricies varius. Aenean aliquam feugiat semper. Fusce"
                + " hendrerit ultricies est vitae dignissim. Nunc elit leo, eleifend a auctor ac, ullamcorper a ligula. Maecenas"
                + " sollicitudin dui ut aliquam ullamcorper. Suspendisse non odio nec nisl rhoncus euismod. Integer dapibus purus"
                + " ut tellus interdum cursus. Fusce lacinia sodales accumsan. In mattis gravida velit vitae rutrum. Etiam at orci"
                + " eget nisl sollicitudin iaculis. Aliquam tincidunt condimentum urna. In."),
            new ImagePairSection(
                new ImageSection("sectioned publication page iphone", "Half image"),
                null
            ),
            new ImageSection("sectioned publication page robots 2", "Full image"),
            new ImagePairSection(
                new ImageSection("sectioned publication page business", "Half image"),
                new ImageSection("sectioned publication page computer", "Half with caption and link", "Google Link", "https://google.com/")
            ),
            new ImageSection("sectioned publication page arms", "Full image"),
            new ChartSection("Counties"),
            new ChartSection("Countries")
        );

        List<PublicationPageBuilder> pages = asList(
            newPublicationPage()
                .withName("first page")
                .withTitle("First Page For Sectioned Pub")
                .withBodySections(bodySections),
            newPublicationPage()
                .withName("second page")
                .withTitle("Second page title"),
            newPublicationPage()
                .withName("a third page")
                .withTitle("Page 3 title")
        );

        return newPublication()
            .withTitle("Sectioned publication with pages")
            .withSummary("Summary for the sectioned publication")
            .withNominalDate(asInstant("2018-02-28T01:00:00.000+01:00"))
            .withPublicationPages(pages)
            .withInformationType(EXPERIMENTAL_STATISTICS)
            .withGeographicCoverage(ENGLAND)
            .withGranularity(COUNTY)
            .withKeyFactImages(asList(
                new ImageSection("sectioned publication robots",
                    "Robots",
                    "Image with link and caption",
                    "https://google.com/"),
                new ImagePairSection(new ImageSection("sectioned publication snowman",
                    "Snowman",
                    null,
                    null),
                    null)
            ));
    }

    private static Instant asInstant(final String timestamp) {
        return Instant.from(DateTimeFormatter.ISO_DATE_TIME.parse(timestamp));
    }
}
