package uk.nhs.digital.common.components.apispecification.commonmark;

import com.google.common.collect.ImmutableMap;
import org.commonmark.node.FencedCodeBlock;
import org.commonmark.node.Node;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlWriter;

import java.util.Collections;
import java.util.Set;

public class FencedCodeBlockNodeRenderer implements NodeRenderer {
    private final HtmlWriter html;

    FencedCodeBlockNodeRenderer(HtmlNodeRendererContext context) {
        this.html = context.getWriter();
    }

    @Override
    public Set<Class<? extends Node>> getNodeTypes() {
        // Return the node types we want to use this renderer for.
        return Collections.singleton(FencedCodeBlock.class);
    }

    @Override
    public void render(Node node) {
        // We only handle one type as per getNodeTypes, so we can just cast it here.
        final FencedCodeBlock codeBlock = (FencedCodeBlock) node;
        final String codeLanguage = codeBlock.getInfo();
        final String literal = codeBlock.getLiteral();

        html.tag("article", Collections.singletonMap("class", "nhsd-o-code-viewer nhsd-t-body"));
        html.tag("div", ImmutableMap.of(
            "class", "nhsd-o-code-viewer__tab-content",
            "role", "tabpanel",
            "aria-hidden", "true",
            "aria-labelledby", "tab-html-content"
        ));
        if (codeLanguage != null) {
            html.tag("p", ImmutableMap.of("class", "nhsd-t-heading-s nhsd-!t-margin-3","data-hide-tab-header", ""));
            html.text(codeLanguage.toUpperCase());
            html.tag("/p");
        }
        html.tag("div", Collections.singletonMap("class", "nhsd-o-code-viewer__code nhsd-o-code-viewer__code__slim"));
        html.tag("div", Collections.singletonMap("class", "nhsd-o-code-viewer__code-content nhsd-o-code-viewer__code-content__slim"));
        html.tag("pre", Collections.singletonMap("class", "line-numbers"));
        if (codeLanguage != null) {
            html.tag("code", Collections.singletonMap("class", String.format("language-%s", codeLanguage)));
        } else {
            html.tag("code");
        }
        html.text(literal);
        html.tag("/code");
        html.tag("/pre");
        html.tag("/div");
        html.tag("/div");
        html.tag("/div");
        html.tag("/article");
    }
}
