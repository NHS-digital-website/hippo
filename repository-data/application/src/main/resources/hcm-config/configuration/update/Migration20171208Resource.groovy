package org.hippoecm.frontend.plugins.cms.admin.updater

import javax.jcr.*
import org.onehippo.repository.update.BaseNodeUpdateVisitor

/**
 * This migration migrate "hippo:resource" type to "publicationsystem:resource".
 * "publication:resource" type has "hippo:text" property always set to empty String
 * which make PDF attachments NOT searchable.
 */
class Migration20171208Resource extends BaseNodeUpdateVisitor {

    boolean doUpdate(Node node) {
        try {
            log.info("Node type: " + node.getPrimaryNodeType().getName());
            if ("publicationsystem:publication".equals(node.getPrimaryNodeType().getName())) {
                return renameSubnode(node, "publicationsystem:attachments", "publicationsystem:resource",
                    "publicationsystem:Attachments-v2");
            } else if ("publicationsystem:dataset".equals(node.getPrimaryNodeType().getName())) {
                return renameSubnode(node, "publicationsystem:Files", "publicationsystem:resource",
                    "publicationsystem:Files-v2");
            }

        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }

    boolean undoUpdate(Node node) {
        try {
            log.info("Node type: " + node.getPrimaryNodeType().getName());
            if ("publicationsystem:publication".equals(node.getPrimaryNodeType().getName())) {
                return renameSubnode(node, "publicationsystem:Attachments-v2", "hippo:resource",
                    "publicationsystem:attachments");
            } else if ("publicationsystem:dataset".equals(node.getPrimaryNodeType().getName())) {
                return renameSubnode(node, "publicationsystem:Files-v2", "hippo:resource",
                    "publicationsystem:Files");
            }

        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }

    boolean renameSubnode(Node node, String from, String toType, String toName) {
        NodeIterator i = node.getNodes()
        Node n;

        while(i.hasNext()) {
            n = i.nextNode();
            if (from.equals(n.getName())) {
                log.info("convering " + from + " to " + toType);
                n.setPrimaryType(toType);
                n.setProperty("hippo:text", "");

                n.getSession()
                    .move(n.getPath(), n.getParent().getPath() + "/" + toName);
            }
        }
    }
}
