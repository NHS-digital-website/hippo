package uk.nhs.digital.common.components.apicatalogue;

public class DepthFirstFiltersWalker implements FiltersWalker {

    @Override
    public void walk(
        final Filters filters,
        final FilterVisitor filterVisitor,
        final boolean visitFirstThenWalk
    ) {
        filters.getSections().forEach(section -> {

            if (visitFirstThenWalk) {
                section.accept(filterVisitor);
            }

            walk(section, filterVisitor, visitFirstThenWalk);

            if (!visitFirstThenWalk) {
                section.accept(filterVisitor);
            }
        });
    }

    private void walk(
        final Section section,
        final FilterVisitor filterVisitor,
        final boolean visitFirstThenWalk
    ) {
        section.getEntries().forEach(subsection -> {

            if (visitFirstThenWalk) {
                subsection.accept(filterVisitor);
            }

            walk(subsection, filterVisitor, visitFirstThenWalk);

            if (!visitFirstThenWalk) {
                subsection.accept(filterVisitor);
            }
        });
    }

}
