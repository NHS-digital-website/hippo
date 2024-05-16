package uk.nhs.digital.common.components.catalogue.filters;

public class DepthFirstFiltersWalker implements FiltersWalker {

    @Override
    public void walkVisitingBeforeDescending(final Walkable walkable, final FilterVisitor filterVisitor) {
        walkable.children().forEach(section -> {

            section.accept(filterVisitor);

            walkVisitingBeforeDescending(section, filterVisitor);
        });
    }

    @Override
    public void walkVisitingAfterDescending(final Walkable walkable, final FilterVisitor filterVisitor) {
        walkable.children().forEach(section -> {

            walkVisitingAfterDescending(section, filterVisitor);

            section.accept(filterVisitor);
        });
    }
}
