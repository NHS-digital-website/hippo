package uk.nhs.digital.common.components.catalogue.filters;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

public class SectionsCollectingFilterVisitor implements FilterVisitor {

    private List<Section> sections = new ArrayList<>();

    @Override
    public void visit(final Section section) {
        sections.add(section);
    }

    @Override public void visit(final Subsection subsection) {
        sections.add(subsection);
    }

    List<Section> sections() {
        return unmodifiableList(sections);
    }
}
