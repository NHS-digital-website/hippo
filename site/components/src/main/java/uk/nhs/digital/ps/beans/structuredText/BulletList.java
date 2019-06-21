package uk.nhs.digital.ps.beans.structuredText;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BulletList extends Element {

    protected String rawText;

    protected List<String> items = new ArrayList<>();

    public BulletList(String text) {
        super(BulletList.class.getSimpleName());
        this.rawText = text;
        parse();
    }

    public List<String> getItems() {
        return items;
    }

    public String toString() {
        return rawText;
    }

    protected void parse() {
        items = Arrays.asList(rawText
            .trim()
            .split("(?m)\n?^ *[*-] +"));

        items = items.stream()
            // remove empty elements
            .filter(s -> !s.isEmpty())
            // remove single new line
            .map(s -> s.replaceAll("(\\s+\\n+\\s*|\\s*\\n+\\s+)", " "))
            .collect(toList());
    }

    public static boolean match(final String text) {
        return text.matches("(?s)^(?: *)[*-] +.*");
    }
}
