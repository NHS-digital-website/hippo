package uk.nhs.digital.common.components.catalogue.filters;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static uk.nhs.digital.common.components.catalogue.filters.FiltersTestUtils.section;
import static uk.nhs.digital.common.components.catalogue.filters.FiltersTestUtils.subsection;

import com.google.common.collect.ImmutableSet;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RunWith(DataProviderRunner.class)
public class StatusUpdatingFilterVisitorTest {

    @Test
    public void marksSectionAsDisplayed_whenAnyChildSubsectionTagIsDisplayed() {

        // given
        final Section section = section("section", "Section",
            subsection("tag-a","Tag A", "tag-a"),
            subsection("tag-b","Tag B", "tag-b")
        );

        updateSubsection(section, "tag-b", Section::display);

        final StatusUpdatingFilterVisitor filterVisitor = visitorWith(
            irrelevantFilteredTags(),
            irrelevantSelectedTags(),
            0
        );

        // when
        filterVisitor.visit(section);

        // then
        assertThat("Section is displayed.", section.isDisplayed(), is(true));
    }

    @Test
    public void marksSectionAsNotDisplayed_whenNoChildTagIsDisplayed() {

        // given
        final Section section = section("section", "Section",
            subsection("tag-a","Tag A", "tag-a"),
            subsection("tag-a","Tag B", "tag-b")
        );

        updateSubsections(section, Section::hide, "tag-a", "tag-b");

        final StatusUpdatingFilterVisitor filterVisitor = visitorWith(
            irrelevantFilteredTags(),
            irrelevantSelectedTags(),
            0
        );

        // when
        filterVisitor.visit(section);

        // then
        assertThat("Section is not displayed.", section.isDisplayed(), is(false));
    }

    @Test
    public void marksSectionAsExpanded_whenAChildSubsectionIsSelected() {

        // given
        final Section actualSection = section("section", "Section",
            subsection("tag-a","Tag A", "tag-a"),
            subsection("tag-b", "Tag B", "tag-b")
        );

        updateSubsection(actualSection, "tag-b", Subsection::select);

        final StatusUpdatingFilterVisitor filterVisitor = visitorWith(
            irrelevantFilteredTags(),
            irrelevantSelectedTags(),
            0
        );

        // when
        filterVisitor.visit(actualSection);

        // then
        assertThat("Section is expanded.", actualSection.isExpanded(), is(true));
    }

    @Test
    public void marksSectionAsExpanded_whenAChildSubsectionIsExpanded() {

        // given
        final Section actualSection = section("section", "Section",
            subsection("tag-a", "Tag A", "tag-a"),
            subsection("tag-b","Tag B", "tag-b")
        );

        updateSubsection(actualSection, "tag-b", Subsection::expand);

        final StatusUpdatingFilterVisitor filterVisitor = visitorWith(
            irrelevantFilteredTags(),
            irrelevantSelectedTags(),
            0
        );

        // when
        filterVisitor.visit(actualSection);

        // then
        assertThat("Section is expanded.", actualSection.isExpanded(), is(true));
    }

    @Test
    public void marksSectionAsCollapsed_whenNoChildTagsAreSelected_andSectionDoesNotDefaultExpanded() {

        // given
        final Section section = section("section", "Section",
            subsection("tag-a", "Tag A", "tag-a"),
            subsection("tag-b", "Tag B", "tag-b")
        );

        final StatusUpdatingFilterVisitor filterVisitor = visitorWith(
            irrelevantSelectedTags(),
            irrelevantSelectedTags(),
            0
        );

        // when
        filterVisitor.visit(section);

        // then
        assertThat("Section is not expanded.", section.isExpanded(), is(false));
    }

    @Test
    public void doesNotMarkSectionAsCollapsed_whenSectionIsDefaultExpanded() {
        // given
        final Section section = section("Section", "true", "false",
            subsection("tag-a", "Tag A", "tag-a"),
            subsection("tag-b","Tag B", "tag-b")
        );

        final StatusUpdatingFilterVisitor filterVisitor = visitorWith(
            irrelevantSelectedTags(),
            irrelevantSelectedTags(),
            0
        );

        // when
        filterVisitor.visit(section);

        // then
        assertThat("Section is expanded.", section.isExpanded(), is(true));
    }

    @Test
    public void marksSubsectionAsDisplayed_whenAnyOfItsSubsectionsIsDisplayed() {

        // given
        final Subsection subsection = subsection("Subsection",
            subsection("tag-a","Tag A", "tag-a"),
            subsection("tag-b","Tag B", "tag-b")
        );

        updateSubsection(subsection, "tag-b", Subsection::display);

        final StatusUpdatingFilterVisitor filterVisitor = visitorWith(
            irrelevantFilteredTags(),
            irrelevantSelectedTags(),
            0
        );

        // when
        filterVisitor.visit(subsection);

        // then
        assertThat("Subsection is displayed.", subsection.isDisplayed(), is(true));
    }

    @Test
    public void marksSubsectionAsDisplayed_whenItsTagIsFiltered() {

        // given
        final Subsection subsection = subsection("tag-a","Tag A", "tag-a",
            subsection("tag-b","Tag B", "tag-b"),
            subsection("tag-c","Tag C", "tag-c")
        );

        final StatusUpdatingFilterVisitor filterVisitor = visitorWith(
            filteredTags("tag-a", "tag-d"),
            irrelevantSelectedTags(),
            0
        );

        // when
        filterVisitor.visit(subsection);

        // then
        assertThat("Subsection is marked as displayed.", subsection.isDisplayed(), is(true));
    }

    @Test
    public void marksSubsectionAsDisplayed_whenItsChildSubsectionIsDisplayed() {

        // given
        final Subsection subsection = subsection("Tag A", "tag-a",
            subsection("tag-a","Tag B", "tag-b"),
            subsection("tag-c","Tag C", "tag-c")
        );

        updateSubsection(subsection, "tag-c", Subsection::display);

        final StatusUpdatingFilterVisitor filterVisitor = visitorWith(
            irrelevantFilteredTags(),
            irrelevantSelectedTags(),
            0
        );

        // when
        filterVisitor.visit(subsection);

        // then
        assertThat("Subsection is marked as displayed.", subsection.isDisplayed(), is(true));
    }

    @Test
    public void marksSubsectionAsNotDisplayed_whenItsTagIsNotFilteredNorAnyChildSubsectionIsDisplayed() {

        // given
        final Subsection subsection = subsection("tag-a", "Tag A", "tag-a",
            subsection("tag-b","Tag B", "tag-b"),
            subsection("tag-c","Tag C", "tag-c")
        );

        final StatusUpdatingFilterVisitor filterVisitor = visitorWith(
            irrelevantFilteredTags(),
            irrelevantSelectedTags(),
            0
        );

        // when
        filterVisitor.visit(subsection);

        // then
        assertThat("Subsection is marked as not displayed.", subsection.isDisplayed(), is(false));
    }

    @Test
    public void marksSubsectionAsSelectable_whenItsTagIsFiltered() {

        // given
        final Subsection subsection = subsection("tag-a","Tag A", "tag-a",
            subsection("tag-b","Tag B", "tag-b"),
            subsection("tag-c","Tag C", "tag-c")
        );

        final StatusUpdatingFilterVisitor filterVisitor = visitorWith(
            filteredTags("tag-a", "tag-d"),
            irrelevantSelectedTags(),
            1
        );

        // when
        filterVisitor.visit(subsection);

        // then
        assertThat("Subsection is marked as selectable.", subsection.isSelectable(), is(true));
    }

    @Test
    public void marksSubsectionAsNotSelectable_whenItsTagIsNotFiltered() {

        // given
        final Subsection subsection = subsection("Tag A", "tag-a",
            subsection("tag-b","Tag B", "tag-b"),
            subsection("tag-c","Tag C", "tag-c")
        );

        final StatusUpdatingFilterVisitor filterVisitor = visitorWith(
            filteredTags("tag-c", "tag-d"),
            irrelevantSelectedTags(),
            0
        );

        // when
        filterVisitor.visit(subsection);

        // then
        assertThat("Subsection is marked as not selectable.", subsection.isSelectable(), is(false));
    }

    @Test
    public void marksSubsectionAsSelected_whenItsTagIsSelected() {

        // given
        final Subsection subsection = subsection("tag-a","Tag A", "tag-a",
            subsection("tag-b", "Tag B", "tag-b"),
            subsection("tag-c","Tag C", "tag-c")
        );

        final StatusUpdatingFilterVisitor filterVisitor = visitorWith(
            irrelevantFilteredTags(),
            selectedTags("tag-a", "tag-d"),
            0
        );

        // when
        filterVisitor.visit(subsection);

        // then
        assertThat("Subsection is marked as selected.", subsection.isSelected(), is(true));
    }

    @Test
    public void marksSubsectionAsNotSelected_whenItsTagIsNotSelected() {

        // given
        final Subsection subsection = subsection("Tag A", "tag-a",
            subsection("tag-b","Tag B", "tag-b"),
            subsection("tag-c", "Tag C", "tag-c")
        );

        final StatusUpdatingFilterVisitor filterVisitor = visitorWith(
            irrelevantFilteredTags(),
            selectedTags("tag-c", "tag-d"),
            0
        );

        // when
        filterVisitor.visit(subsection);

        // then
        assertThat("Subsection is marked as not selected.", subsection.isSelected(), is(false));
    }

    public static void updateSubsections(
        final Section section,
        final Consumer<Subsection> mutator,
        final String... keys
    ) {
        Arrays.stream(keys).forEach(subsection -> updateSubsection(section, subsection, mutator));
    }

    public static void updateSubsection(
        final Section section,
        final String key,
        final Consumer<Subsection> mutator
    ) {
        section.getEntries().stream()
            .filter(subsection -> subsection.getKey().equals(key))
            .findFirst()
            .ifPresent(mutator);
    }

    private StatusUpdatingFilterVisitor visitorWith(final List<String> filteredTags, final List<String> selectedTags, final int tagCount) {
        return new StatusUpdatingFilterVisitor(
                ImmutableSet.copyOf(
                        filteredTags
                                .stream()
                                .map(tag -> new NavFilter(tag, tagCount))
                                .collect(Collectors.toList())
                ),
                ImmutableSet.copyOf(selectedTags)
        );
    }

    private List<String> filteredTags(final String... tags) {
        return asList(tags);
    }

    private List<String> selectedTags(final String... tags) {
        return asList(tags);
    }

    private List<String> irrelevantSelectedTags() {
        return selectedTags("irrelevant-tag-a", "irrelevant-tag-b");
    }

    private List<String> irrelevantFilteredTags() {
        return filteredTags("irrelevant-tag-c", "irrelevant-tag-d");
    }
}
