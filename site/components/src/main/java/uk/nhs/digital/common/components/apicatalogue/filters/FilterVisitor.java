package uk.nhs.digital.common.components.apicatalogue.filters;

public interface FilterVisitor {

    void visit(Section section);

    void visit(Subsection subsection);
}
