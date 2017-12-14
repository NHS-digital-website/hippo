package org.hippoecm.frontend.plugins.cms.admin.updater

import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.NodeIterator

/**
 * Migrate publicationsystem:resource elements to publicationsystem:attachment that
 * includes a human readable name for the resource
 */
class Migration20171213Attachment extends BaseNodeUpdateVisitor {

    boolean doUpdate(Node node) {
        try {
            String nodeType = node.getPrimaryNodeType().getName()
            log.info("Node type: " + nodeType)
            if ("publicationsystem:publication".equals(nodeType)) {
                return convertSubnode(node, "publicationsystem:Attachments-v2", "publicationsystem:Attachments-v3")
            } else if ("publicationsystem:dataset".equals(nodeType)) {
                return convertSubnode(node, "publicationsystem:Files-v2", "publicationsystem:Files-v3")
            }
        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException("Not implemented.")
    }

    boolean convertSubnode(Node parentNode, String from, String toName) {
        NodeIterator i = parentNode.getNodes()
        boolean processed = false

        while(i.hasNext()) {
            Node node = i.nextNode()
            if (from.equals(node.getName())) {
                log.info("converting " + node.getPath())

                Node newNode = parentNode.addNode(toName, "publicationsystem:attachment")
                newNode.setProperty("publicationsystem:displayName", "")

                node.getSession()
                    .move(node.getPath(), newNode.getPath() + "/publicationsystem:attachmentResource")

                processed = true
            }
        }

        return processed
    }
}
