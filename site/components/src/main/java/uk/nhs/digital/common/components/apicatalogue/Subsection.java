package uk.nhs.digital.common.components.apicatalogue;

public class Subsection extends Section {

    private final String taxonomyKey;

    private boolean selected;
    private boolean selectable;

    private Subsection(final String displayName, final String taxonomyKey, final Subsection... subsections) {
        super(displayName, subsections);
        this.taxonomyKey = taxonomyKey;
    }

    @Override
    public String getKey() {
        return taxonomyKey;
    }

    public boolean isSelected() {
        return selected;
    }

    public static Subsection subsection(final String displayName, final String taxonomyKey, final Subsection... subsections) {
        return new Subsection(displayName, taxonomyKey, subsections);
    }

    public static Subsection subsection(final String displayName, final Subsection... subsections) {
        return new Subsection(displayName, null, subsections);
    }

    public static Subsection subsection(final String displayName, final String taxonomyKey) {
        return new Subsection(displayName, taxonomyKey);
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
}
