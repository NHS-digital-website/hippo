package uk.nhs.digital.common.components.apispecification.commonmark;

import org.commonmark.node.Code;
import org.commonmark.node.Node;
import org.commonmark.renderer.html.AttributeProvider;

import java.util.Map;

public class CodeAttributeProvider implements AttributeProvider {

    @Override
    public void setAttributes(final Node node, final String tagName, final Map<String, String> attributes) {

        if (node instanceof Code) {
            attributes.put("class", "nhsd-a-text-highlight nhsd-a-text-highlight--code");
        }
    }
}
