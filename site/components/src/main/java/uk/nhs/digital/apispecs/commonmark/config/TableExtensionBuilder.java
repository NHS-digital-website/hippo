package uk.nhs.digital.apispecs.commonmark.config;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Arrays;
import java.util.List;

public class TableExtensionBuilder implements ExtensionBuilder {

    List<Extension> extensions = Arrays.asList(TablesExtension.create());

    @Override
    public Parser getExtendedParser() {
        return Parser.builder()
            .extensions(extensions)
            .build();
    }

    @Override
    public HtmlRenderer getExtendedHtmlRenderer() {
        return HtmlRenderer.builder()
            .extensions(extensions)
            .build();
    }
}
