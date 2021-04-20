package uk.nhs.digital.common.components.apicatalogue.filters;

public interface FiltersWalker {

    void walkVisitingBeforeDescending(Walkable walkable, FilterVisitor filterVisitor);

    void walkVisitingAfterDescending(Walkable walkable, FilterVisitor filterVisitor);
}
