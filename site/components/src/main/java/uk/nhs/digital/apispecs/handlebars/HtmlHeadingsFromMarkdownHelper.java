package uk.nhs.digital.apispecs.handlebars;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang.StringUtils.removeStart;
import static uk.nhs.digital.ExceptionUtils.wrapCheckedException;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * <p>
 * 'Block' type helper which, for each <b>level 2</b> heading present in the given Markdown, invokes
 * the 'main' template block with a context object containing value of '{@code id}' and the text of the heading.
 * Useful to generate hyperlinks that could be grouped in a navigation panel and pointing to actual headings
 * in the corresponding sections of content panel.
 * </p>
 * <p>
 * Note that only headings of level 2 are taken into account and only if they are defined with the '{@code ##}'
 * notation in Markdown.
 * </p>
 * <p>
 * For example, for Markdown:
 * </p>
 * <pre>
 *     # Title
 *     ## Overview
 *     Some Text
 *     ## Legal use
 *     Some more text
 *     ### Some level three heading
 * </pre>
 * <p>...invocation:</p>
 * <pre>
 *     {{#headingsFromMarkdown markdown}}
 *     &lt;li&gt;
 *         &lt;a href=&quot;#{{id}}&quot;&gt;{{text}}&lt;/a&gt;
 *     &lt;/li&gt;
 *     {{/headingsFromMarkdown}}
 * </pre>
 * <p>...will render:</p>
 * <pre>
 *     &lt;li&gt;
 *         &lt;a href=&quot;#overview&quot;&gt;Overview&lt;/a&gt;
 *     &lt;/li&gt;
 *     &lt;li&gt;
 *         &lt;a href=&quot;#legal-use&quot;&gt;Legal use&lt;/a&gt;
 *     &lt;/li&gt;
 * </pre>
 */
public class HtmlHeadingsFromMarkdownHelper implements Helper<String> {

    public static final String NAME = "headingsFromMarkdown";

    private static final Predicate<String> LEVEL_TWO_HEADING_LINES = line -> line.matches("^##[^#].+$");

    public static HtmlHeadingsFromMarkdownHelper INSTANCE = new HtmlHeadingsFromMarkdownHelper();

    private HtmlHeadingsFromMarkdownHelper() {
        // no-op; made private to promote the use of INSTANCE
    }

    @Override
    public Options.Buffer apply(final String markdown, final Options options) {

        try {
            return htmlHeadingsRenderedFromMarkdown(markdown, options);

        } catch (final Exception e) {
            throw new TemplateRenderingException("Failed to render headings from Markdown " + markdown + ".", e);
        }
    }

    private Options.Buffer htmlHeadingsRenderedFromMarkdown(final String markdown, final Options options) {

        final Options.Buffer buffer = options.buffer();

        final List<HeadingModel> headingModels = headingsFrom(markdown);

        headingModels.forEach(headingModel -> wrapCheckedException(() -> buffer.append(options.fn(headingModel))));

        return buffer;
    }

    private List<HeadingModel> headingsFrom(final String markdown) {

        final List<String> lines = asLines(markdown);

        return headingsFrom(lines);
    }

    private List<HeadingModel> headingsFrom(final List<String> lines) {
        return lines.stream()
            .map(String::trim)
            .filter(LEVEL_TWO_HEADING_LINES)
            .map(line -> removeStart(line, "##"))
            .map(String::trim)
            .map(headingText -> HeadingModel.with(toHeadingId(headingText), headingText))
            .collect(toList());
    }

    private List<String> asLines(final String markdown) {
        return Optional.ofNullable(markdown)
            .map(text -> text.split("\\n|\\r"))
            .map(Arrays::asList)
            .orElse(emptyList());
    }

    private String toHeadingId(final String headingText) {
        return headingText.toLowerCase().replaceAll("\\s", "-");
    }

    static class HeadingModel {

        private String id;
        private String text;

        private HeadingModel(final String id, final String text) {
            this.id = id;
            this.text = text;
        }

        public static HeadingModel with(final String id, final String text) {
            return new HeadingModel(id, text);
        }

        public String getId() {
            return id;
        }

        public String getText() {
            return text;
        }

        @Override public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final HeadingModel headingModel = (HeadingModel) o;

            return new EqualsBuilder().append(id, headingModel.id).append(text, headingModel.text).isEquals();
        }

        @Override public int hashCode() {
            return new HashCodeBuilder(17, 37).append(id).append(text).toHashCode();
        }

        @Override public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("text", text)
                .toString();
        }
    }
}
