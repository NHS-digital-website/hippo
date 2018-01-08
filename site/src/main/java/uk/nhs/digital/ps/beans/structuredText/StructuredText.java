package uk.nhs.digital.ps.beans.structuredText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    private List<Element> parse(String text) {

        text = text
            // eliminate all leading and trailing white space characters
            .trim()
            // normalise all new line characters to just one kind
            .replaceAll("\r\n", "\n")
            .replaceAll("\r", "\n")
            // eliminate sequences of new line chars over two characters long
            .replaceAll("\n{3,}", "\n\n");

        return Arrays.stream(text.split("\n\n"))
            .filter(t -> !t.isEmpty())
            .map(t -> BulletList.match(t) ? new BulletList(t) : new Paragraph(t))
            .collect(Collectors.toList());
    }
}
