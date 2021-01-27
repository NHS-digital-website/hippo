package uk.nhs.digital.apispecs.handlebars;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import uk.nhs.digital.apispecs.commonmark.CommonmarkMarkdownConverter;

import java.util.Optional;

/**
 * <p>
 * 'Basic' helper which renders provided Markdown to HTML, following
 * <a href="https://github.com/commonmark/commonmark-java">CommonMark</a> specification.
 * </p>
 * <p>
 * Supports tables defined in the 'pipe' syntax from
 * <a href="https://help.github.com/articles/organizing-information-with-tables">GitHub Flavoured Markdown</a>.
 * Note that tables have to be preceded by a blank line (i.e. at least <em>two</em> 'new line' characters).
 * </p>
 * <p>
 * Headings denoted with '{@code #}' characters get their '{@code id}' attributes populated with values
 * automatically derived from the heading's texts, as per {@linkplain CommonmarkMarkdownConverter#toHtml(String, String)}.
 * Custom prefixes can be applied to those ids when the helper is invoked with optional parameter '{@code headingIdPrefix}'.
 * </p>
 * <p>
 * Invoke using triple brace characters to ensure that the emitted HTML is embedded without further escaping. Example invocation:
 * </p>
 * <pre>
 *     {{{markdown propertyWithMarkdownText headingIdPrefix="customPrefix__"}}}
 * </pre>
 */
public class MarkdownHelper implements Helper<String> {

    public static final String NAME = "markdown";

    private static final String PARAM_NAME_HEADING_ID_PREFIX = "headingIdPrefix";

    private final CommonmarkMarkdownConverter commonmarkMarkdownConverter;

    public MarkdownHelper(final CommonmarkMarkdownConverter commonmarkMarkdownConverter) {
        this.commonmarkMarkdownConverter = commonmarkMarkdownConverter;
    }

    @Override
    public String apply(final String markdown, final Options options) {

        try {
            final String headingIdPrefix = options.hash(PARAM_NAME_HEADING_ID_PREFIX);

            return render(markdown, headingIdPrefix);

        } catch (final Exception e) {
            throw new RuntimeException("Failed to render markdown.", e);
        }
    }

    private String render(final String markdown, final String headingIdPrefix) {
        return Optional.ofNullable(markdown)
            .map(markdownToRender -> commonmarkMarkdownConverter.toHtml(markdownToRender, headingIdPrefix))
            .orElse("");
    }
}
