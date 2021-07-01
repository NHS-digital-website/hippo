package uk.nhs.digital.apispecs.commonmark;

import com.google.common.collect.ImmutableMap;
import org.commonmark.ext.gfm.tables.*;
import org.commonmark.node.*;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class TableBlockNodeRenderer implements NodeRenderer {
    private static HtmlWriter html;

    TableBlockNodeRenderer(HtmlNodeRendererContext context) {
        this.html = context.getWriter();
    }

    @Override
    public Set<Class<? extends Node>> getNodeTypes() {
        return Collections.singleton(TableBlock.class);
    }

    @Override
    public void render(Node node) {
        TableHead head = (TableHead) node.getFirstChild();
        TableBody body = (TableBody) node.getLastChild();
        html.tag("div", Collections.singletonMap("class", "nhsd-m-table nhsd-t-body"));

        // full-width view
        renderFullWidthTable(head, body);
        // responsive view
        renderResponsiveViewTable(head, body);

        html.tag("/div");
    }

    private void renderFullWidthTable(TableHead head, TableBody body) {
        html.tag("table", ImmutableMap.of(
            "data-responsive","",
            "data-no-sort", "",
            "class","nhsd-!t-display-hide nhsd-!t-display-s-show-table"
        ));
        renderTableHead(head);
        renderTableBody(body);
        html.tag("/table");
    }

    private void renderTableHead(TableHead head) {
        html.tag("thead");
        TableRow row = (TableRow) head.getFirstChild();
        while (row != null) {
            renderTableRow(row);
            row = (TableRow) row.getNext();
        }
        html.tag("/thead");
    }

    private void renderTableBody(TableBody body) {
        html.tag("tbody");
        TableRow row = (TableRow) body.getFirstChild();
        while (row != null) {
            renderTableRow(row);
            row = (TableRow) row.getNext();
        }
        html.tag("/tbody");
    }

    private void renderTableRow(TableRow row) {
        html.tag("tr");
        TableCell cell = (TableCell) row.getFirstChild();
        String openingTag = cell.isHeader() ? "th" : "td";
        String closingTag = String.format("/%s", openingTag);

        while (cell != null) {
            html.tag(openingTag);
            renderCellContent(cell);
            html.tag(closingTag);
            cell = (TableCell) cell.getNext();
        }
        html.tag("/tr");
    }

    private void renderCellContent(TableCell cell) {
        Node cellContent = cell.getFirstChild();
        while (cellContent != null) {
            renderInlineContent(cellContent);
            cellContent = cellContent.getNext();
        }
    }

    private static void renderInlineContent(Node cellContent) {
        for (InlineContentTypes type: InlineContentTypes.values()) {
            if (cellContent.getClass().equals(type.contentType)) {
                type.handler.accept(cellContent);
                break;
            }
        }
    }

    private void renderResponsiveViewTable(TableHead head, TableBody body) {
        ArrayList<String> headers = getHeadersFromTableHead(head);

        html.tag("div", Collections.singletonMap("class", "nhsd-m-table__mobile-list"));
        html.tag("ul", Collections.singletonMap("class", "nhsd-!t-display-s-hide"));
        TableRow row = (TableRow) body.getFirstChild();
        while (row != null) {
            renderTableCard(headers, row);
            row = (TableRow) row.getNext();
        }
        html.tag("/ul");
        html.tag("/div");
    }

    private void renderTableCard(ArrayList<String> headers, TableRow row) {
        ArrayList<TableCell> data = getTableCells(row);
        html.tag("li");
        html.tag("ul", Collections.singletonMap("class", "nhsd-m-table__mobile-list-item"));
        for (int i = 0; i < headers.size(); i++) {
            html.tag("li");
            html.tag("span");
            html.tag("b");
            html.text(headers.get(i));
            html.tag("/b");
            html.tag("/span");
            html.tag("span");
            renderCellContent(data.get(i));
            html.tag("/span");
            html.tag("/li");
        }
        html.tag("/ul");
        html.tag("/li");
    }

    private ArrayList<String> getHeadersFromTableHead(TableHead head) {
        TableRow headerRow = (TableRow) head.getFirstChild();
        ArrayList<String> headers = new ArrayList<>();
        TableCell cell = (TableCell) headerRow.getFirstChild();
        while (cell != null) {
            headers.add(((Text) cell.getFirstChild()).getLiteral());
            cell = (TableCell) cell.getNext();
        }
        return headers;
    }

    private ArrayList<TableCell> getTableCells(TableRow row) {
        ArrayList<TableCell> data = new ArrayList<>();
        TableCell cell = (TableCell) row.getFirstChild();
        while (cell != null) {
            data.add(cell);
            cell = (TableCell) cell.getNext();
        }
        return data;
    }

    private enum InlineContentTypes {
        TEXT(Text.class, renderText),
        CODE(Code.class, renderCode),
        EMPHASIS(Emphasis.class, renderEmphasis),
        STRONG_EMPHASIS(StrongEmphasis.class, renderStrongEmphasis),
        LINK(Link.class, renderLink),
        IMAGE(Image.class, renderImage),
        HTML_INLINE(HtmlInline.class, renderHtmlInline),
        HARD_LINE_BREAK(HardLineBreak.class, renderHardLineBreak),
        SOFT_LINE_BREAK(SoftLineBreak.class, renderSoftLineBreak);

        private final Class<? extends Node> contentType;
        private final Consumer handler;

        InlineContentTypes(
            final Class<? extends Node> contentType,
            final Consumer<? extends Node> handler
        ) {
            this.contentType = contentType;
            this.handler = handler;
        }
    }

    private static final Consumer<Text> renderText = node -> html.text(node.getLiteral());
    private static final Consumer<Code> renderCode = node -> {
        html.tag("span", Collections.singletonMap("class", "nhsd-a-text-highlight nhsd-a-text-highlight--code"));
        html.text(node.getLiteral());
        html.tag("/span");
    };
    private static final Consumer<Emphasis> renderEmphasis = node -> {
        html.tag("em");
        renderInlineContent(node.getFirstChild());
        html.tag("/em");
    };
    private static final Consumer<StrongEmphasis> renderStrongEmphasis = node -> {
        html.tag("strong");
        renderInlineContent(node.getFirstChild());
        html.tag("/strong");
    };
    private static final Consumer<Link> renderLink = node -> {
        html.tag("a", ImmutableMap.of(
            "class","nhsd-a-link",
            "href", Optional.ofNullable(node.getDestination()).orElse(""),
            "title", Optional.ofNullable(node.getTitle()).orElse("")
        ));
        renderInlineContent(node.getFirstChild());
        html.tag("/a");
    };
    private static final Consumer<Image> renderImage = node -> {
        html.tag("img", ImmutableMap.of(
            "src", node.getDestination(),
            "title", Optional.ofNullable(node.getTitle()).orElse(""),
            "alt", Optional.ofNullable(((Text) node.getFirstChild()).getLiteral()).orElse("")
        ), true);
    };
    private static final Consumer<HtmlInline> renderHtmlInline = node -> html.text(node.getLiteral());
    private static final Consumer<HardLineBreak> renderHardLineBreak = node -> html.line();
    private static final Consumer<SoftLineBreak> renderSoftLineBreak = node -> html.line();
}
