package uk.nhs.digital.ps.beans.structuredText;

public class Paragraph extends Element {

    protected String text;

    public Paragraph(String text) {
        super(Paragraph.class.getSimpleName());
        this.text = text;
    }

    public String toString() {
        return text;
    }
}
