package uk.nhs.digital.ps.beans.structuredText;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;

public class BulletList extends Element {

    private final String rawText;
    private final List<String> items;

    public BulletList(final String text) {
        super(BulletList.class.getSimpleName());
        this.rawText = text;
        this.items = splitTheBulletedList(this.rawText);
    }

    private List<String> splitTheBulletedList(final String rawText) {
        return Arrays.asList(rawText
            // Split the bulleted list
            .split("(?m)^\\s*?[*\\-]")
        ).stream()
            // Don't worry about empty elements
            .filter(s -> !s.isEmpty())
            // Join the lines in each split
            .map(s -> s.replaceAll("\n+", " "))
            // Reduce multiple spaces to one
            .map(s -> s.replaceAll("\\s+", " "))
            // Trim the ends
            .map(s -> s.trim())
            // And return a list
            .collect(toList());
    }

    public List<String> getItems() {
        return items;
    }

    public String toString() {
        return rawText;
    }

    public static boolean match(final String text) {
        return text.matches("(?s)^(?: *)[*-] +.*");
    }
}
