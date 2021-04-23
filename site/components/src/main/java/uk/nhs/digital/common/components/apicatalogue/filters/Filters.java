package uk.nhs.digital.common.components.apicatalogue.filters;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toSet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;
import java.util.Set;

public class Filters implements Walkable {

    @JsonIgnore
    private FiltersWalker filtersWalker = new DepthFirstFiltersWalker();

    private List<Section> sections = emptyList();

    private List<Section> allSectionsFlattened;
    private Set<String> selectedFiltersKeys;

    public Filters initialisedWith(
        final Set<String> filteredTaxonomyTags,
        final Set<String> selectedTags
    ) {
        final FilterVisitor visitor = new StatusUpdatingFilterVisitor(filteredTaxonomyTags, selectedTags);

        filtersWalker.walkVisitingAfterDescending(this, visitor);

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
    public Set<String> selectedFiltersKeysMinus(final String filterKey) {
        return selectedFiltersKeys().stream()
            .filter(key -> !key.equals(filterKey))
            .collect(toSet());
    }

    // Also invoked from the template.
    public Set<String> selectedFiltersKeys() {

        if (selectedFiltersKeys == null) {

            selectedFiltersKeys = sectionsInOrderOfDeclaration().stream()
                .filter(Subsection.class::isInstance)
                .map(Subsection.class::cast)
                .filter(Subsection::isSelected)
                .map(Subsection::getKey)
                .collect(toSet());
        }

        return selectedFiltersKeys;
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
        return new ToStringBuilder(this, new MultilineRecursiveToStringStyle() {
            {
                setUseShortClassName(true);
                setUseIdentityHashCode(false);
            }
        })
            .append("sections", sections)
            .build();
    }
}
