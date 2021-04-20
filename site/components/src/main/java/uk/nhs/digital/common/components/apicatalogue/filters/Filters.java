package uk.nhs.digital.common.components.apicatalogue.filters;

import static java.util.Collections.emptyList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.*;

import java.util.List;
import java.util.Set;

public class Filters implements Walkable {

    @JsonIgnore
    private FiltersWalker filtersWalker = new DepthFirstFiltersWalker();

    private List<Section> sections = emptyList();

    private List<Section> allSectionsFlattened;

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

    public Filters initialisedWith(
        final Set<String> filteredTaxonomyTags,
        final Set<String> selectedTags
    ) {
        final FilterVisitor visitor = new StatusUpdatingFilterVisitor(filteredTaxonomyTags, selectedTags);

        filtersWalker.walkVisitingAfterDescending(this, visitor);

        return this;
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
