package uk.nhs.digital.common.components.apispecification.commonmark;

import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.renderer.html.AttributeProvider;

import java.util.Map;

public class HyperlinkAttributeProvider implements AttributeProvider {

    @Override
    public void setAttributes(final Node node, final String tagName, final Map<String, String> attributes) {
        if (node instanceof Link) {
            attributes.put("class", "nhsd-a-link");
        }
    }
}
