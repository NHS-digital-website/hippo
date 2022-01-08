package uk.nhs.digital.common.components.apispecification.commonmark;

import org.commonmark.node.Node;
import org.commonmark.node.StrongEmphasis;
import org.commonmark.renderer.html.AttributeProvider;

import java.util.Map;

public class StrongEmphasisAttributeProvider implements AttributeProvider {

    @Override
    public void setAttributes(final Node node, final String tagName, final Map<String, String> attributes) {
        if (node instanceof StrongEmphasis) {
            attributes.put("class", "nhsd-!t-font-weight-bold");
        }
    }
}
