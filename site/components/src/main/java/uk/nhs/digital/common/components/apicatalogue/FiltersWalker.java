package uk.nhs.digital.common.components.apicatalogue;

public interface FiltersWalker {

    void walk(Filters filters, FilterVisitor filterVisitor, boolean visitFirstThenWalk);
}
