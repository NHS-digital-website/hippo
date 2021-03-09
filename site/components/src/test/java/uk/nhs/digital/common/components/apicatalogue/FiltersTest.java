package uk.nhs.digital.common.components.apicatalogue;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static uk.nhs.digital.common.components.apicatalogue.Section.section;
import static uk.nhs.digital.common.components.apicatalogue.Subsection.subsection;

import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(DataProviderRunner.class)
public class FiltersTest {

    @Test
    public void returnsSubsectionsInDeclarationOrder() {

        // given
        Filters filters = Filters.filters(
            new DepthFirstFiltersWalker(),
            section("Section 5",
                subsection("Subsection Z", "key-z"),
                subsection("Subsection D", "key-d",
                    subsection("Subsection S", "key-s"),
                    subsection("Subsection U", "key-u")
                )
            ),
            section("Section 2",
                subsection("Subsection 3", "key-3"),
                subsection("Subsection I", "key-i",
                    subsection("Subsection T", "key-t"),
                    subsection("Subsection E", "key-e")
                )
            )
        );

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
        assertSubsectionsAreInOrder(actualFilters, expectedOrderByKeys);
    }

    private void assertSubsectionsAreInOrder(final List<Section> actualSubsections, final List<String> expectedKeysInOrder) {
        final List<String> actualKeysInOrder = actualSubsections.stream()
            .map(Section::getKey)
            .collect(Collectors.toList());

        assertThat("Subsections are returned in the order of declaration",
            actualKeysInOrder,
            is(expectedKeysInOrder)
        );
    }
}