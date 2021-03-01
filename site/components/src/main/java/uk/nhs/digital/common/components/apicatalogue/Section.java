package uk.nhs.digital.common.components.apicatalogue;

import static java.util.Collections.emptyList;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Section {
    private final String displayName;
    private final List<Subsection> entries;

    private boolean expanded;
    private boolean displayed;

    protected Section(final String displayName, final Subsection... subsections) {
        this.displayName = displayName;
        entries = Optional.ofNullable(subsections).map(Arrays::asList).orElse(emptyList());
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getKey() {
        return from(displayName);
    }

    public boolean isExpanded() {
        return expanded;
    }

    public List<Subsection> getEntries() {
        return entries;
    }

    public static Section section(final String displayName, final Subsection... subsections) {
        return new Section(displayName, subsections);
    }

    public void accept(final FilterVisitor visitor) {
        visitor.visit(this);
    }

    public void expand() {
        this.expanded = true;
    }

    public void collapse() {
        this.expanded = false;
    }

    public boolean isDisplayed() {
        return displayed;
    }

    public void display() {
        this.displayed = true;
    }

    public void hide() {
        this.displayed = false;
    }

    private String from(final String displayName) {
        return displayName.toLowerCase().replaceAll("\\s", "-");
    }
}
