package uk.nhs.digital.common.components.catalogue.filters;

public interface FilterVisitor {

    void visit(Section section);

    void visit(Subsection subsection);
}
