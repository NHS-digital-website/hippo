package uk.nhs.digital.common.components.apispecification.commonmark;

import org.commonmark.node.BulletList;
import org.commonmark.node.ListBlock;
import org.commonmark.node.Node;
import org.commonmark.node.OrderedList;
import org.commonmark.renderer.html.AttributeProvider;

import java.util.Map;

public class ListAttributeProvider implements AttributeProvider {

    @Override
    public void setAttributes(final Node node, final String tagName, final Map<String, String> attributes) {
        if (node instanceof ListBlock) {
            if (node instanceof BulletList) {
                attributes.put("class", "nhsd-t-list nhsd-t-list--bullet");
            }
            if (node instanceof OrderedList) {
                attributes.put("class", "nhsd-t-list nhsd-t-list--number");
            }
        }

    }
}
