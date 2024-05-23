package org.hippoecm.frontend.plugins.cms.admin.updater

import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node

/**
 * Removes publicationsystem:InformationTypes   from Publications.
 */
class RemoveInformationTypes  extends BaseNodeUpdateVisitor {

    boolean doUpdate(Node node) {

        // Query returns the hippo:handle node for the document
        // (which has the 3 variants)
        try {
            if (node.hasNodes()) {
                return updateNode(node)
            }
        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }

    boolean updateNode(Node n) {

        JcrUtils.ensureIsCheckedOut(n)
        def path = n.getPath()
        def nodeType = n.getPrimaryNodeType().getName()
        if ("publicationsystem:publication".equals(nodeType)) {
            if (n.hasProperty("publicationsystem:InformationTypes")) {
                log.info("Success - Removing publicationsystem:InformationType from node: " + path + " => current node type: " + nodeType)
                n.getProperty("publicationsystem:InformationType").remove()
                return true
            } else
                log.info("Skipped - Removing publicationsystem:InformationType from node: " + path + " => current node type: " + nodeType)
        }
        return false
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }
}