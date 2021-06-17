package uk.nhs.digital.apispecs.commonmark;

import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.renderer.html.AttributeProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * Populates heading's attribute '{@code id}' with values calculated from
 * the heading's text, prepended with provided prefix. Useful as targets for hyperlinks,
 * where the prefix provides additional context and uniqueness to the ids.
 * </p>
 * <p>
 * For example, for '{@code headingIpPrefix}' given as '{@code customPrefix__}' heading:
 * </p>
 * <pre>
 *     ## Legal use
 * </pre>
 * <p>
 * ...will be rendered as:
 * </p>
 * <pre>
 *     &lt;h2 id=&quot;customPrefix__legal-use&quot;&gt;Legal use&lt;/h2&gt;
 * </pre>
 */
public class HeadingAttributeProvider implements AttributeProvider {

    private final String headingIdPrefix;
    private static final Map<Integer, String> HEADING_CLASSES = headingClasses();

    private static Map<Integer, String> headingClasses() {
        final Map<Integer, String> headingClasses = new HashMap<>();
        headingClasses.put(1, "nhsd-t-heading-xxl");
        headingClasses.put(2, "nhsd-t-heading-xl");
        headingClasses.put(3, "nhsd-t-heading-l");
        headingClasses.put(4, "nhsd-t-heading-m");
        headingClasses.put(5, "nhsd-t-heading-s");
        headingClasses.put(6, "nhsd-t-heading-xs");
        return headingClasses;
    }

    /**
     * See {@linkplain HeadingAttributeProvider} for details.
     *
     * @param headingIdPrefix Custom prefix to prepend the auto-generated id with. Can be {@code null},
     *                        in which case the id value will not be prefixed with anything.
     */
    public HeadingAttributeProvider(final String headingIdPrefix) {
        this.headingIdPrefix = Optional.ofNullable(headingIdPrefix).orElse("");
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
            .map(headingText -> headingIdPrefix + idFrom(headingText))
            .ifPresent(headingId -> {
                attributes.put("id", headingId);
            });
    }

    private String idFrom(final String literal) {
        return literal.toLowerCase().replaceAll("\\s", "-");
    }
}
