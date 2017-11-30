package uk.nhs.digital.ps.beans.structuredText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StructuredText {

    protected List<Element> elements = new ArrayList<>();

    public StructuredText(String text) {
        elements = parse(text);
    }

    public List<Element> getElements() {
        return elements;
    }

    public Paragraph getFirstParagraph() {
        return elements.stream()
            .filter(element -> element instanceof Paragraph)
            .map(element -> (Paragraph) element)
            .findFirst()
            .orElse(new Paragraph(""));
    }

    protected List<Element> parse(String text) {
        List<Element> elements = new ArrayList<>();

        text = text
            // eliminate all leading and trailing white space characters
            .trim()
            // normalise all new line characters to just one kind
            .replaceAll("\r\n", "\n")
            .replaceAll("\r", "\n")
            // eliminate sequences of new line chars over two characters long
            .replaceAll("\n{3,}", "\n\n");

        Arrays.asList(text.split("\n\n"))
            .forEach(t -> elements.add(new Paragraph(t)));

        return elements;
    }
}
