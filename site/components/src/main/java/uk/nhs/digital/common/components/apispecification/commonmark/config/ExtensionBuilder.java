package uk.nhs.digital.common.components.apispecification.commonmark.config;

import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public interface ExtensionBuilder {

    Parser getExtendedParser();

    HtmlRenderer getExtendedHtmlRenderer();
}
