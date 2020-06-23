package uk.nhs.digital.apispecs.commonmark;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Arrays;
import java.util.List;

public class CommonmarkMarkdownConverter {

    List<Extension> extensions = Arrays.asList(TablesExtension.create());

    private final Parser parser = Parser.builder().extensions(extensions).build();
    private final HtmlRenderer renderer = HtmlRenderer.builder()
        .attributeProviderFactory(context -> new CodeAttributeProvider())
        .attributeProviderFactory(context -> new TableSortOffAttributeProvider())
        .extensions(extensions)
        .build();

    /**
     * Convert input markdown text to HTML.
     * Simple text is not wrapped in <p>...</p>.
     * @param markdown text with Markdown styles. If <code>null</code>, <code>""</code> is returned.
     * @return HTML rendering from the Markdown
     */
    public String toHtml(String markdown) {
        if (markdown == null) {
            return "";
        }
        Node document = parser.parse(markdown);
        String html = renderer.render(document);
        html = unwrapped(html);
        return html;
    }

    // The CommonMark library wraps the HTML with
    //  <p> ... html ... </p>\n
    // This method removes that markup wrapper if there are no other <p> elements,
    // do that Markdown can be used in non-block contexts such as operation summary etc.
    private static final String P_END = "</p>\n";
    private static final String P_START = "<p>";

    private String unwrapped(String html) {

        if (html.startsWith(P_START) && html.endsWith(P_END)
            && html.lastIndexOf(P_START) == 0) {
            return html.substring(P_START.length(),
                html.length() - P_END.length());
        } else {
            return html;
        }
    }
}
