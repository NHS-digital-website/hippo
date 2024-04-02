package uk.nhs.digital.common.components.catalogue.filters;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StatusUpdatingFilterVisitor implements FilterVisitor {
    private Set<NavFilter> filteredTags;
    private Set<String> selectedTags;

    public StatusUpdatingFilterVisitor(final Set<NavFilter> filteredTags, final Set<String> selectedTags) {
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
        updateCount(subsection);
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

    private void updateCount(final Subsection subsection) {
        Optional<NavFilter> navFilter =
                filteredTags.stream()
                        .filter(filter -> Objects.equals(filter.filterKey, subsection.getKey())).collect(Collectors.toList()).stream()
                        .findFirst();
        int count = navFilter.map(filter -> filter.count).orElse(0);
        subsection.setCount(count);
        if (subsection.getEntries().size() != 0 && count == 0) {
            subsection.setUnselectable();
        }
    }

    private Predicate<Subsection> hasSelectedTag = subsection -> selectedTags.contains(subsection.getKey());
    private Predicate<Subsection> hasFilteredTag = subsection -> filteredTags.stream().anyMatch(navFilter -> Objects.equals(navFilter.filterKey, subsection.getKey()));
    private Predicate<Section> hasDisplayedChild = section -> section.getEntries().stream().anyMatch(Subsection::isDisplayed);
}

