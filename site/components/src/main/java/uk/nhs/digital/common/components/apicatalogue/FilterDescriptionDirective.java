package uk.nhs.digital.common.components.apicatalogue;

import freemarker.core.Environment;
import freemarker.template.*;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.IOException;
import java.util.Map;

public class FilterDescriptionDirective implements TemplateDirectiveModel {

    private final Parser parser = Parser.builder().build();

    private final HtmlRenderer renderer = HtmlRenderer.builder()
        .attributeProviderFactory(context -> new HyperlinkAttributeProvider())
        .sanitizeUrls(true)
        .escapeHtml(true)
        .build();

    @Override public void execute(final Environment environment,
                                  final Map parameters,
                                  final TemplateModel[] loopVars,  // ignored
                                  final TemplateDirectiveBody body // ignored
    ) throws TemplateException, IOException {

        final String markdownDescription = ((SimpleScalar) parameters.get(Param.description.name())).getAsString();

        final String htmlDescription = toHtml(markdownDescription);

        environment.getOut().append(htmlDescription);
    }

    private String toHtml(final String markdownDescription) {

        final Node node = parser.parse(markdownDescription);

        return renderer.render(node);
    }

    enum Param {
        description
    }

    private static class HyperlinkAttributeProvider implements AttributeProvider {
        @Override public void setAttributes(final Node node, final String tagName, final Map<String, String> attributes) {
            if (node instanceof Link) {
                attributes.put("class", "nhsd-a-link");
                attributes.put("target", "_blank");
            }
        }
    }
}
