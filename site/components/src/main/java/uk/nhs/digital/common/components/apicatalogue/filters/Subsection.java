package uk.nhs.digital.common.components.apicatalogue.filters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.*;

public class Subsection extends Section {

    private final String taxonomyKey;
    private final String highlight;

    private boolean selected;
    private boolean selectable;

    @JsonCreator
    protected Subsection(
        @JsonProperty("displayName") final String displayName,
        @JsonProperty("taxonomyKey") final String taxonomyKey,
        @JsonProperty("description") final String description,
        @JsonProperty("highlight") final String highlight,
        @JsonProperty("entries") final Subsection... subsections
    ) {
        super(displayName, description, subsections);
        this.taxonomyKey = taxonomyKey;
        this.highlight = highlight;
    }

    @Override
    public String getKey() {
        return taxonomyKey;
    }

    public String getHighlight() {
        return highlight;
    }

    public boolean isHighlighted() {
        return !StringUtils.isBlank(highlight);
    }

    public boolean isSelected() {
        return selected;
    }

    public void select() {
        this.selected = true;
    }

    public void deselect() {
        this.selected = false;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable() {
        this.selectable = true;
    }

    public void setUnselectable() {
        this.selectable = false;
    }

    @Override
    public void accept(final FilterVisitor visitor) {
        visitor.visit(this);
    }

    @Override public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Subsection that = (Subsection) o;

        return new EqualsBuilder()
            .appendSuper(super.equals(o))
            .append(selected, that.selected)
            .append(selectable, that.selectable)
            .append(taxonomyKey, that.taxonomyKey)
            .append(highlight, that.highlight)
            .isEquals();
    }

    @Override public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .appendSuper(super.hashCode())
            .append(selected)
            .append(selectable)
            .append(taxonomyKey)
            .append(highlight)
            .toHashCode();
    }
}
