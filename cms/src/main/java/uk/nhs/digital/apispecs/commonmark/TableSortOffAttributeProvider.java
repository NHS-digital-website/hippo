package uk.nhs.digital.apispecs.commonmark;

import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.node.Node;
import org.commonmark.renderer.html.AttributeProvider;

import java.util.Map;

public class TableSortOffAttributeProvider implements AttributeProvider {

    @Override
    public void setAttributes(final Node node, final String tagName, final Map<String, String> attributes) {

        if (node instanceof TableBlock) {

            attributes.put("data-disablesort", "true");
        }
    }
}
