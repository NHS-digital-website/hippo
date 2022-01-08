package uk.nhs.digital.common.components.apispecification.commonmark;

import org.commonmark.node.Node;
import org.commonmark.node.ThematicBreak;
import org.commonmark.renderer.html.AttributeProvider;

import java.util.Map;

public class ThematicBreakAttributeProvider implements AttributeProvider {

    @Override
    public void setAttributes(final Node node, final String tagName, final Map<String, String> attributes) {

        if (node instanceof ThematicBreak) {
            attributes.put("class", "nhsd-a-horizontal-rule");
        }
    }
}