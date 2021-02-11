package uk.nhs.digital.common.components.apicatalogue;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.Set;

public class Filters {

    private final FiltersWalker filtersWalker;

    private final List<Section> sections;

    private List<Section> allSectionsFlattened;

    private Filters(final FiltersWalker filtersWalker, final List<Section> sections) {
        this.filtersWalker = filtersWalker;
        this.sections = sections;
    }

    public List<Section> getSections() {
        return sections;
    }

    public static Filters filters(
        final FiltersWalker filtersWalker,
        final Section... sections
    ) {
        return new Filters(filtersWalker, asList(sections));
    }

    public List<Section> sectionsInOrderOfDeclaration() {

        if (allSectionsFlattened == null) {

            final SectionsCollectingFilterVisitor sectionsCollector = new SectionsCollectingFilterVisitor();

            filtersWalker.walk(this, sectionsCollector, true);

            allSectionsFlattened = sectionsCollector.sections();
        }

        return allSectionsFlattened;
    }

    Filters initialisedWith(final Set<String> filteredTaxonomyTags,
                            final Set<String> selectedTags) {

        final FilterVisitor visitor = new UpdatingFilterVisitor(filteredTaxonomyTags, selectedTags);

        filtersWalker.walk(this, visitor, false);

        return this;
    }
}
