package uk.nhs.digital.test.util;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.*;
import static uk.nhs.digital.test.util.StringTestUtils.Placeholders.Placeholder.placeholder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StringTestUtils {

    private static final String UUID_REGEX = "[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}";

    /**
     * <p>
     * A convenience, wrapper around {@linkplain org.apache.commons.lang3.StringUtils#replaceEach(String, String[], String[])}.
     * <p>
     * Useful in tests where several test cases may want to load a large text content (e.g. JSON),
     * where only one or two small fragments (e.g. JSON properties' values) should differ between the test cases,
     * with the rest of the content remaining the same.
     * <p>
     * In most cases a chain of {@linkplain String#replaceAll(String, String)} should suffice but this class
     * allows to incrementally build configuration of the replacements in one place and then resolve them (lazily) all in one
     * go by calling {@linkplain #resolveIn(String)} later.
     * <p>
     * <b>Usage example:</b> for JSON content stored in variable {@code jsonTemplate},
     * and with placeholders '{@code propertyPlaceholderA}' and '{@code valuePlaceholderC}':
     * <pre>
     *
     * {
     *     "property-A":           "value-A",
     *     "propertyPlaceholderA": "value-B",
     *     "property-C":           "valuePlaceholderC"
     * }
     * </pre>
     * ...invoking the static {@linkplain Placeholders#placeholders()} method with the following placeholders
     * and values to substitute:
     * <pre>
     *
     *     final String jsonWithResolvedPlaceholders =
     *         placeholders()
     *             .with("propertyPlaceholderA", "property-B")
     *             .with("valuePlaceholderC",    "value-C")
     *             .resolveIn(jsonTemplate);
     * </pre>
     * ...will result in the content of {@code jsonWithResolvedPlaceholders} looking like this:
     * <pre>
     *
     * {
     *     "property-A": "value-A",
     *     "property-B": "value-B",
     *     "property-C": "value-C"
     * }
     * </pre>
     */
    public static class Placeholders {
        private final List<Placeholder> placeholders = new ArrayList<>();

        private Placeholders() {
            // no-op
        }

        private Placeholders(final ArrayList<Placeholder> newPlaceholders) {
            placeholders.addAll(newPlaceholders);
        }

        public static Placeholders placeholders() {
            return new Placeholders();
        }

        public Placeholders with(final String name, final Object value) {

            final ArrayList<Placeholder> newPlaceholders = new ArrayList<>(this.placeholders);

            newPlaceholders.add(placeholder(name, value));

            return new Placeholders(newPlaceholders);

        }

        public String resolveIn(final String stringWithPlaceholdersToResolve) {
            return replaceEach(stringWithPlaceholdersToResolve, names(), values());
        }

        private String[] names() {
            return placeholders.stream().map(Placeholder::name).collect(toList()).toArray(new String[]{});
        }

        private String[] values() {
            return placeholders.stream().map(Placeholder::value).collect(toList()).toArray(new String[]{});
        }

        static class Placeholder {
            private final String name;
            private final String value;

            private Placeholder(final String name, final String value) {
                this.name = name;
                this.value = value;
            }

            public static Placeholder placeholder(final String name, final Object value) {
                return new Placeholder(name, String.valueOf(value));
            }

            public String name() {
                return name;
            }

            public String value() {
                return value;
            }
        }
    }

    /**
     * <p>
     * Trims leading and trailing spaces from lines in the given text,
     * and removes blank lines altogether.
     * <p>
     * Useful for making assertions of texts where blank lines
     * as well as leading and trailing spaces do not matter,
     * such as when comparing HTML content. Ignoring white
     * spaces in such test cases makes the tests less brittle
     * by ignoring changes such as changes to indentation
     * of HTML content.
     */
    public static String ignoringWhiteSpacesIn(final String text) {
        return stream(
            split(text, LF)
        )
            .filter(line -> !isBlank(line))
            .map(String::trim)
            .collect(Collectors.joining(LF));
    }

    public static String ignoringUuids(final String html) {
        return html
            .replaceAll("data-schema-uuid=\"" + UUID_REGEX + "\"", "data-schema-uuid=\"\"")
            .replaceAll("Children\\('" + UUID_REGEX + "'\\)", "Children('')")
            .replaceAll("All\\('" + UUID_REGEX + "'\\)", "All('')");
    }
}
