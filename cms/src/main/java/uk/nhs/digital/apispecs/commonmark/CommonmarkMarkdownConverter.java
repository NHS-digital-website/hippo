package uk.nhs.digital.apispecs.commonmark;

import static java.util.Collections.singletonList;
import static org.apache.commons.lang.StringUtils.removeStart;
import static org.apache.commons.lang3.StringUtils.removeEnd;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.List;
import java.util.Optional;

public class CommonmarkMarkdownConverter {


    /**
     * <p>
     * Renders Markdown as HTML.
     * </p>
     * <p>
     * Headings get '{@code id}' attributes populated with values calculated from
     * the heading's text. Useful as targets for hyperlinks.
     * </p>
     * <p>
     * For example, heading:
     * </p>
     * <pre>
     *     ## Legal use
     * </pre>
     * <p>
     * ...will be rendered as:
     * </p>
     * <pre>
     *     &lt;h2 id=&quot;legal-use&quot;&gt;Legal use&lt;/h2&gt;
     * </pre>
     *
     * @param markdown Markdown to render as HTML.
     * @return Rendered HTML.
     */
    public String toHtml(final String markdown) {
        return toHtml(markdown, "");
    }

    /**
     * <p>
     * Renders Markdown as HTML.
     * </p>
     * <p>
     * Headings get '{@code id}' attributes populated with values calculated from
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
     *
     * @param markdown Markdown to render as HTML.
     */
    public String toHtml(final String markdown, final String headingIpPrefix) {
        return Optional.ofNullable(markdown)
            .map(md -> renderMarkdownToHtml(md, headingIpPrefix))
            .map(this::trimSurroundingParagraphsTags)
            .orElse("");
    }

    private String renderMarkdownToHtml(final String md, final String headingIpPrefix) {

        final List<Extension> extensions = singletonList(TablesExtension.create());

        final Parser parser = Parser.builder().extensions(extensions).build();

        final HtmlRenderer renderer = HtmlRenderer.builder()
            .attributeProviderFactory(context -> new CodeAttributeProvider())
            .attributeProviderFactory(context -> new HeadingAttributeProvider(headingIpPrefix))
            .attributeProviderFactory(context -> new TableSortOffAttributeProvider())
            .extensions(extensions)
            .build();

        final Node document = parser.parse(md);

        return renderer.render(document);
    }


    /**
     * See comments against {@linkplain io.swagger.codegen.v3.utils.Markdown#unwrapped(String)}
     * which this method is based on.
     */
    private String trimSurroundingParagraphsTags(final String html) {
        return Optional.of(html)
            .map(String::trim)
            .map(text -> removeStart(text, "<p>"))
            .map(text -> removeEnd(text, "</p>"))
            .orElse("");
    }
}
