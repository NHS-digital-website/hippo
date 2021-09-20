package uk.nhs.digital.common.components.apicatalogue.filters;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import com.google.common.collect.ImmutableSet;
import uk.nhs.digital.test.util.ReflectionTestUtils;

import java.util.Set;
import java.util.function.Consumer;

public class FiltersTestUtils {

    public static Filters filters(final Section... sections) {
        final Filters filters = new Filters();

        // In production Filters is instantiated and has its fields populated
        // through reflection too, due to being deserialized from YAML by Jackson,
        // and this reflection-based injection simply emulates this approach.
        ReflectionTestUtils.setField(filters, "sections", asList(sections));

        return filters;
    }

    public static Section section(final String displayName, final Subsection... subsections) {
        return new Section(displayName, null, subsections);
    }

    public static Section section(final String displayName, final String description, final Subsection... subsections) {
        return new Section(displayName, description, subsections);
    }

    public static Subsection subsection(final String displayName, final Subsection... subsections) {
        return new Subsection(displayName, null, null, subsections);
    }

    public static Subsection subsection(final String displayName, final String taxonomyKey) {
        return new Subsection(displayName, taxonomyKey, null);
    }

    public static Subsection subsection(final String displayName, final String taxonomyKey, final Subsection... subsections) {
        return new Subsection(displayName, taxonomyKey, null, subsections);
    }

    public static Subsection subsection(final String displayName, final String taxonomyKey, final String description) {
        return new Subsection(displayName, taxonomyKey, description);
    }

    public static void updateFilters(
        final Filters expectedFilters,
        final Consumer<Subsection> subsectionMutator,
        final String... keys
    ) {
        updateFilters(expectedFilters, section -> { }, subsectionMutator, keys);
    }

    public static void updateFilters(
        final Filters expectedFilters,
        final Consumer<Section> sectionMutator,
        final Consumer<Subsection> subsectionMutator,
        final String... keys
    ) {
        final FilterVisitor filterVisitor = filterVisitorWith(sectionMutator, subsectionMutator, keys);

        new DepthFirstFiltersWalker().walkVisitingAfterDescending(expectedFilters, filterVisitor);
    }

    public static FilterVisitor filterVisitorWith(
        final Consumer<Section> sectionMutator,
        final Consumer<Subsection> subsectionMutator,
        final String... keys) {

        final Set<String> keysSet = ImmutableSet.copyOf(keys);

        return new FilterVisitor() {
            @Override public void visit(final Section section) {
                if (keysSet.contains(section.getKey())) {
                    sectionMutator.accept(section);
                }
            }

            @Override public void visit(final Subsection subsection) {
                if (keysSet.contains(subsection.getKey())) {
                    subsectionMutator.accept(subsection);
                }
            }
        };
    }

}
