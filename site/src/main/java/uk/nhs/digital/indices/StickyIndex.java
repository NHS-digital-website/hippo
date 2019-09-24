package uk.nhs.digital.indices;

public final class StickyIndex {

    private String title;
    private String label;

    public StickyIndex(String title) {
        this(title, title);

    }

    public StickyIndex(String title, String label) {
        this.title = title;
        this.label = label;
    }

    public String getTitle() {
        return title;
    }

    public String getLabel() {
        return label;
    }
}
