package uk.nhs.digital.test.util;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.*;
import static uk.nhs.digital.test.util.StringTestUtils.Placeholders.Placeholder.placeholder;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class StringTestUtils {

    /**
     * <p>
     * A convenience, wrapper around {@linkplain StringUtils#replaceEach(String, String[], String[])}.
     * <p>
     * Useful in tests where several test cases may want to load a large text content (e.g. JSON),
     * where only one or two small fragments (e.g. JSON properties' values) should differ between the test cases,
     * with the rest of the content remaining the same.
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

    public static String ignoringBlankLinesIn(final String text) {
        return stream(
            split(text, LF)
        )
            .filter(line -> !isBlank(line))
            .collect(joining(LF));
    }
}
