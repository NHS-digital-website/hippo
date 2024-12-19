package uk.nhs.digital.common.components.catalogue.filters;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.*;
import uk.nhs.digital.common.util.CustomToStringStyle;

import java.util.*;
import java.util.stream.Stream;

public class Filters implements Walkable {

    @JsonIgnore
    private FiltersWalker filtersWalker = new DepthFirstFiltersWalker();

    private List<Section> sections = emptyList();

    private List<Section> allSectionsFlattened;
    private List<String> selectedFiltersKeys;

    public Filters initialisedWith(
        final Set<NavFilter> allFilterKeysOfAllDocsWhereEachDocTaggedWithAllSelectedFilterKeys,
        final List<String> selectedFilterKeys
    ) {
        this.selectedFiltersKeys = selectedFilterKeys;
        filtersWalker.walkVisitingAfterDescending(
            this,
            new StatusUpdatingFilterVisitor(allFilterKeysOfAllDocsWhereEachDocTaggedWithAllSelectedFilterKeys, new HashSet<>(selectedFilterKeys))
        );

        return this;
    }

    @Override
    public List<Section> children() {
        return getSections();
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Section> sectionsInOrderOfDeclaration() {

        if (allSectionsFlattened == null) {

            final SectionsCollectingFilterVisitor sectionsCollector = new SectionsCollectingFilterVisitor();

            filtersWalker.walkVisitingBeforeDescending(this, sectionsCollector);

            allSectionsFlattened = sectionsCollector.sections();
        }

        return allSectionsFlattened;
    }

    // Invoked from the template, hence shown by IDE as not used (but still needed).
    public List<String> selectedFiltersKeysMinus(final String filterKey) {
        return selectedFiltersKeys().stream()
            .filter(key -> !key.equals(filterKey))
            .collect(toList());
    }

    // Also invoked from the template.
    public List<String> selectedFiltersKeysPlus(final String filterKey) {
        final List<String> selectedFilterKeys = new ArrayList<>(selectedFiltersKeys());
        selectedFilterKeys.add(filterKey);

        return selectedFilterKeys;
    }

    // Also invoked from the template.
    public List<String> selectedFiltersKeys() {
        return this.selectedFiltersKeys;
    }

    public boolean isHighlighted(final String displayName) {
        return getSubsectionsStream()
            .filter(section -> section.getDisplayName().equals(displayName))
            .findAny()      //If filters are duplicated this method will pick up the highlight status of the instance the highest up the tree.
            .map(section -> section.isHighlighted())
            .orElse(false);
    }

    public String getHighlight(final String displayName) {
        return getSubsectionsStream()
            .filter(section -> section.getDisplayName().equals(displayName))
            .findAny()      //If filters are duplicated this method will pick up the highlight colour of the instance the highest up the tree.
            .map(section -> section.getHighlight())
            .orElse("light-grey");      //light-grey is returned if the highlight field is not populated as this is the default.
    }

    private Stream<Subsection> getSubsectionsStream() {
        return sectionsInOrderOfDeclaration().stream()
            .filter(Subsection.class::isInstance)
            .map(Subsection.class::cast);
    }

    public static Filters emptyInstance() {
        return new Filters();
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    @Override public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Filters filters = (Filters) o;

        return new EqualsBuilder().append(sections, filters.sections).isEquals();
    }

    @Override public int hashCode() {
        return new HashCodeBuilder(17, 37).append(sections).toHashCode();
    }

    @Override public String toString() {
        return new ToStringBuilder(this, CustomToStringStyle.INSTANCE).append("sections", sections).build();
    }

}
