package uk.nhs.digital.common.components.apispecification.commonmark;

import com.google.common.collect.ImmutableMap;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.node.Node;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlWriter;

import java.util.Collections;
import java.util.Set;

public class TableBlockNodeRenderer implements NodeRenderer {
    private static HtmlWriter html;
    private final HtmlNodeRendererContext context;

    TableBlockNodeRenderer(HtmlNodeRendererContext context) {
        this.html = context.getWriter();
        this.context = context;
    }

    @Override
    public Set<Class<? extends Node>> getNodeTypes() {
        return Collections.singleton(TableBlock.class);
    }

    @Override
    public void render(Node node) {
        html.tag("div", Collections.singletonMap("class", "nhsd-m-table nhsd-t-body"));

        html.tag("table", ImmutableMap.of(
            "data-responsive","",
            "data-no-sort", ""
        ));

        renderChildren(node);

        html.tag("/table");

        html.tag("/div");
    }

    private void renderChildren(Node parent) {
        Node node = parent.getFirstChild();
        while (node != null) {
            Node next = node.getNext();
            context.render(node);
            node = next;
        }
    }
}
