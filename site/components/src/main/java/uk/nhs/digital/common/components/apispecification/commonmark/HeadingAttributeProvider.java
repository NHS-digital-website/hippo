package uk.nhs.digital.common.components.apispecification.commonmark;

import com.google.common.collect.ImmutableMap;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.renderer.html.AttributeProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HeadingAttributeProvider implements AttributeProvider {

    private final String headingIdPrefix;

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static final Map<Integer, String> HEADING_CLASSES = new ImmutableMap.Builder()
        .put(1, "nhsd-t-heading-xxl")
        .put(2, "nhsd-t-heading-xl")
        .put(3, "nhsd-t-heading-l")
        .put(4, "nhsd-t-heading-m")
        .put(5, "nhsd-t-heading-s")
        .put(6, "nhsd-t-heading-xs")
        .build();

    private IdGenerator idGenerator = new IdGenerator();

    /**
     * See {@linkplain HeadingAttributeProvider} for details.
     *
     * @param headingIdPrefix Custom prefix to prepend the auto-generated id with. If {@code null},
     *                        {@code id} attribute will not be rendered.
     */
    public HeadingAttributeProvider(final String headingIdPrefix) {
        this.headingIdPrefix = headingIdPrefix;
    }

    @Override
    public void setAttributes(final Node node, final String tagName, final Map<String, String> attributes) {
        Optional.of(node)
            .filter(Heading.class::isInstance)
            .map(Heading.class::cast)
            .map(heading -> {
                final int headingLevel = heading.getLevel();
                final String cssClass = headingLevel > 6 ? "nhsd-t-heading-xs" : HEADING_CLASSES.get(headingLevel);
                attributes.put("class", cssClass);
                return heading;
            })
            .map(Heading::getFirstChild)
            .filter(Text.class::isInstance)
            .map(Text.class::cast)
            .map(Text::getLiteral)
            .map(String::trim)
            .flatMap(headingText -> Optional.ofNullable(headingIdPrefix).map(idPrefix -> idPrefix + idFrom(headingText)))
            .ifPresent(headingId -> attributes.put("id", headingId));
    }

    private String idFrom(final String literal) {
        return idGenerator.idFrom(literal);
    }

    private static class IdGenerator {

        private final Map<String, Integer> generatedIds = new HashMap<>();

        String idFrom(final String literal) {

            final Integer newOrdinal = nextOrdinalFor(literal);

            final String ordinalSuffix = newOrdinal > 0 ? "-" + newOrdinal : "";

            final String newId = literal.toLowerCase().replaceAll("\\s", "-") + ordinalSuffix;

            generatedIds.put(newId, newOrdinal);

            return newId;
        }

        private Integer nextOrdinalFor(final String literal) {

            if (!generatedIds.containsKey(literal)) {
                generatedIds.put(literal, 0);

                return 0;
            }

            int lastOrdinal = generatedIds.get(literal);

            int nextOrdinal = lastOrdinal + 1;

            generatedIds.put(literal, nextOrdinal);

            return nextOrdinal;
        }
    }
}
