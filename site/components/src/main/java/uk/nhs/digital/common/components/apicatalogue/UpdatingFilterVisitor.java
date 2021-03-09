package uk.nhs.digital.common.components.apicatalogue;

import java.util.Set;
import java.util.function.Predicate;

public class UpdatingFilterVisitor implements FilterVisitor {
    private Set<String> filteredTags;
    private Set<String> selectedTags;

    public UpdatingFilterVisitor(final Set<String> filteredTags, final Set<String> selectedTags) {
        this.filteredTags = filteredTags;
        this.selectedTags = selectedTags;
    }

    @Override
    public void visit(final Section section) {
        updateDisplayed(section);
        updateExpanded(section);
    }

    @Override
    public void visit(final Subsection subsection) {
        updateDisplayed(subsection);
        updateSelectability(subsection);
        updateSelected(subsection);
        updateExpanded(subsection);
    }

    private void updateDisplayed(final Section section) {
        if (hasDisplayedChild.test(section)) {
            section.display();
        } else {
            section.hide();
        }
    }

    private void updateDisplayed(final Subsection subsection) {
        if (hasFilteredTag.or(hasDisplayedChild).test(subsection)) {
            subsection.display();
        } else {
            subsection.hide();
        }
    }

    private void updateExpanded(final Section section) {
        if (section.getEntries().stream().anyMatch(entry -> entry.isSelected() || entry.isExpanded())) {
            section.expand();
        } else {
            section.collapse();
        }
    }

    private void updateSelected(final Subsection subsection) {
        if (hasSelectedTag.test(subsection)) {
            subsection.select();
        } else {
            subsection.deselect();
        }
    }

    private void updateSelectability(final Subsection subsection) {
        if (hasFilteredTag.test(subsection)) {
            subsection.setSelectable();
        } else {
            subsection.setUnselectable();
        }
    }

    private Predicate<Subsection> hasSelectedTag = subsection -> selectedTags.contains(subsection.getKey());
    private Predicate<Subsection> hasFilteredTag = subsection -> filteredTags.contains(subsection.getKey());
    private Predicate<Section> hasDisplayedChild = section -> section.getEntries().stream().anyMatch(Subsection::isDisplayed);
}

