package uk.nhs.digital.apispecs.commonmark.config;

import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.AttributeProviderContext;
import org.commonmark.renderer.html.AttributeProviderFactory;
import org.commonmark.renderer.html.HtmlRenderer;
import uk.nhs.digital.apispecs.commonmark.CodeAttributeProvider;

public class CodeAttributeProviderConfig implements AttributeProviderConfig {

    @Override
    public HtmlRenderer getHtmlRenderer() {

        return HtmlRenderer.builder()
            .attributeProviderFactory(new AttributeProviderFactory() {
                public AttributeProvider create(AttributeProviderContext context) {
                    return new CodeAttributeProvider();
                }
            })
            .build();
    }
}
