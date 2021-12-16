package uk.nhs.digital.common.components.apicatalogue.filters;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static uk.nhs.digital.common.components.apicatalogue.filters.FiltersTestUtils.*;

import com.google.common.collect.ImmutableSet;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Set;

@RunWith(DataProviderRunner.class)
public class FiltersTest {

    @Test
    public void sectionsInOrderOfDeclaration_returnsSubsectionsInTheOrderTheyWereDeclared() {

        // given
        final Filters filters = baseFilters();

        final List<String> expectedOrderByKeys = asList(
            "section-5",
            "key-z",
            "key-d",
            "key-s",
            "key-u",
            "section-2",
            "key-3",
            "key-i",
            "key-t",
            "key-e"
        );

        // when
        final List<Section> actualFilters = filters.sectionsInOrderOfDeclaration();

        // then
        final List<String> actualKeysInOrder = actualFilters.stream()
            .map(Section::getKey)
            .collect(toList());

        assertThat("Subsections are returned in the order of declaration.",
            actualKeysInOrder,
            is(expectedOrderByKeys)
        );
    }

    @Test
    public void initialisedWith_producesFiltersWithStatusFlagsUpdated_dependingOnSelectedAndFilteredTags() {

        // NOTE: more exhaustive set of fine-grained test cases is implemented in StatusUpdatingFilterVisitorTest

        // given
        final Filters filters = baseFilters();
        final Filters expectedFilters = baseFilters();
        updateFilters(expectedFilters, Subsection::select, "key-s");
        updateFilters(expectedFilters, Section::expand, Subsection::expand, "key-d", "section-5");
        updateFilters(expectedFilters, Section::display, Subsection::display, "section-2", "key-i", "key-t");
        updateFilters(expectedFilters, Subsection::setSelectable, "key-t");

        final Set<String> filteredTaxonomyTags = ImmutableSet.of("key-t");
        final Set<String> selectedTags = ImmutableSet.of("key-s");

        // when
        final Filters actualFilters = filters.initialisedWith(filteredTaxonomyTags, selectedTags);

        // then
        assertThat("Filters have status flags updated depending on selected and filtered tags.",
            actualFilters,
            is(expectedFilters)
        );
    }

    @Test
    public void isHighlighted_returnsHighlightStatus_GivenDisplayName() {

        // given
        final Filters filters = baseFilters();

        final List<Boolean> expectedHighlightStatusResults = asList(
            false,
            true,
            true,
            false,
            true,
            false
        );

        // when
        final List<Boolean> actualHighlightStatusResults = asList(
            filters.isHighlighted("Subsection Z"),
            filters.isHighlighted("Subsection D"),
            filters.isHighlighted("Subsection S"),
            filters.isHighlighted("Subsection U"),
            filters.isHighlighted("Subsection T"),
            filters.isHighlighted("Subsection E")
        );

        // then
        assertThat("Filters returned correct highlight status.",actualHighlightStatusResults ,is(expectedHighlightStatusResults));
    }

    @Test
    public void getHighlight_returnsHighlightColour_GivenDisplayName() {

        // given
        final Filters filters = baseFilters();

        final List<String> expectedHighlightResults = asList(
            "light-grey",
            "colour-a",
            "colour-b",
            "light-grey",
            "colour-c",
            "light-grey"
        );

        // when
        final List<String> actualHighlightResults = asList(
            filters.getHighlight("Subsection Z"),
            filters.getHighlight("Subsection D"),
            filters.getHighlight("Subsection S"),
            filters.getHighlight("Subsection U"),
            filters.getHighlight("Subsection T"),
            filters.getHighlight("Subsection E")
        );

        // then
        assertThat("Filters returned correct highlight status.",actualHighlightResults ,is(expectedHighlightResults));
    }

    private Filters baseFilters() {
        return FiltersTestUtils.filters(
            section("Section 5",
                subsection("Subsection Z", "key-z"),
                subsection("Subsection D", "key-d", "colour-a",
                    subsection("Subsection S", "key-s", null, "colour-b"),
                    subsection("Subsection U", "key-u")
                )
            ),
            section("Section 2",
                subsection("Subsection 3", "key-3"),
                subsection("Subsection I", "key-i",
                    subsection("Subsection T", "key-t", null, "colour-c"),
                    subsection("Subsection E", "key-e")
                )
            )
        );
    }

}