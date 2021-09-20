package uk.nhs.digital.common.components.apicatalogue.filters;

import static java.util.Collections.emptyList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Section implements Walkable {

    private final String displayName;
    private final List<Subsection> entries;
    private final String description;

    private boolean expanded;
    private boolean displayed;

    @JsonCreator
    protected Section(
        @JsonProperty("displayName") final String displayName,
        @JsonProperty("description") final String description,
        @JsonProperty("entries") final Subsection... entries
    ) {
        this.displayName = displayName;
        this.description = description;
        this.entries = Optional.ofNullable(entries).map(Arrays::asList).orElse(emptyList());
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

    public String description() {
        return description;
    }

    @Override public List<Section> children() {
        return getEntries().stream().map(Section.class::cast).collect(Collectors.toList());
    }

    @Override public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Section section = (Section) o;

        return new EqualsBuilder()
            .append(expanded, section.expanded)
            .append(displayed, section.displayed)
            .append(displayName, section.displayName)
            .append(entries, section.entries)
            .append(description, section.description)
            .isEquals();
    }

    @Override public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(displayName).append(entries).append(expanded).append(displayed)
            .toHashCode();
    }

    @Override public String toString() {
        return new ReflectionToStringBuilder(this, new MultilineRecursiveToStringStyle() {
            {
                setUseShortClassName(true);
                setUseIdentityHashCode(false);
            }
        }).build();
    }

    private String from(final String displayName) {
        return displayName.toLowerCase().replaceAll("\\s", "-");
    }
}
