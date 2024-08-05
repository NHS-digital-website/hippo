package uk.nhs.digital.common.components.catalogue.filters;

import static java.util.Collections.emptyList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.*;
import uk.nhs.digital.common.util.CustomToStringStyle;

import java.util.*;
import java.util.stream.Collectors;

public class Section implements Walkable {

    private final String taxonomyKey;
    private final String displayName;
    private final List<Subsection> entries;
    private final String description;
    private boolean expanded;
    private boolean displayed;
    private int count = 0;
    private boolean hideChildren;
    private int amountChildrenToShow;

    public boolean isShowMoreIndc() {
        return showMoreIndc;
    }

    public void setShowMoreIndc(boolean showMoreIndc) {
        this.showMoreIndc = showMoreIndc;
    }

    private boolean showMoreIndc;

    @JsonCreator
    protected Section(
        @JsonProperty("taxonomyKey") final String taxonomyKey,
        @JsonProperty("displayName") final String displayName,
        @JsonProperty("description") final String description,
        @JsonProperty("defaultExpanded") final String defaultExpanded,
        @JsonProperty("hideChildren") final String hideChildren,
        @JsonProperty("amountChildrenToShow") final String amountChildrenToShow,
        @JsonProperty("entries") final Subsection... entries
    ) {
        this.taxonomyKey = taxonomyKey;
        this.displayName = displayName;
        this.description = description;
        this.expanded = parseBooleanFromString(defaultExpanded);
        this.hideChildren = parseBooleanFromString(hideChildren);
        this.amountChildrenToShow = parseIntegerFromString(amountChildrenToShow);
        this.entries = Optional.ofNullable(entries).map(Arrays::asList).orElse(emptyList());
        this.entries.forEach(entry -> entry.setParentAndSubsectionVisibility(this));
    }

    public String getTaxonomyKey() {
        return taxonomyKey;
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

    public List<Subsection> getEntriesAndChildEntries() {
        List<Subsection> allEntries = new ArrayList<>(emptyList());
        entries.forEach(entry -> {
            allEntries.add(entry);
            allEntries.addAll(entry.getEntriesAndChildEntries());
        });
        return allEntries;
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

    public void setCount(int count) {
        this.count = count;
    }

    public String description() {
        return description;
    }

    public String count() {
        return String.valueOf(count);
    }

    public int getCount() {
        return count;
    }

    public boolean hasHiddenSubsections() {
        return displayedSubsections().size() < getEntriesAndChildEntries().stream().filter(Section::isDisplayed).count() && !anyHiddenSubsectionsSelected();
    }

    protected boolean anyHiddenSubsectionsSelected() {
        return getEntriesAndChildEntries().stream().filter(Section::isDisplayed)
                .filter(entry -> displayedSubsections().stream().noneMatch(child -> Objects.equals(entry.getKey(), child.getKey())))
                .anyMatch(Subsection::isSelected);
    }

    public boolean getHideChildren() {
        return this.hideChildren;
    }

    public int getAmountChildrenToShow() {
        return this.amountChildrenToShow;
    }

    public void setHideChildren(boolean hideChildren) {
        this.hideChildren = hideChildren;
    }

    protected void setAmountChildren(int amount) {
        this.amountChildrenToShow = amount;
    }

    public Set<String> getKeysInSection() {
        return getEntries().stream().flatMap(entry -> entry.getKeyAndChildKeys().stream()).collect(Collectors.toSet());
    }

    //Determine subsections to be displayed considering the hideChildren value and amount. Takes into account subsection entries.
    public List<Subsection> displayedSubsections() {
        if (this instanceof Subsection) {
            return ((Subsection) this).parent().displayedSubsections();
        } else {
            if (hideChildren) {
                List<Subsection> children = getEntriesAndChildEntries().stream().filter(Section::isDisplayed).collect(Collectors.toList());
                int childrenToDisplayAmount = Math.min(amountChildrenToShow, children.size());
                return children.subList(0, childrenToDisplayAmount);
            } else {
                return getEntriesAndChildEntries();
            }
        }
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
        return new ReflectionToStringBuilder(this, CustomToStringStyle.INSTANCE).build();
    }

    private String from(final String displayName) {
        return displayName.toLowerCase().replaceAll("\\s", "-");
    }

    private boolean parseBooleanFromString(String fromYaml) {
        try {
            return Boolean.parseBoolean(fromYaml);
        } catch (Exception e) {
            return false;
        }
    }

    private int parseIntegerFromString(String fromYaml) {
        try {
            return Integer.parseInt(fromYaml);
        } catch (Exception e) {
            return 0;
        }
    }
}
