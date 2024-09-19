package org.hippoecm.frontend.plugins.cms.admin.updater

import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node

/**
 * Adds searchableType true on each document.
 */
class SearchableType extends BaseNodeUpdateVisitor {

    boolean doUpdate(Node node) {

        // Query returns the variant nodes of the document
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
        if (n.hasProperty("common:searchableType")) {
            log.info("Skipped - Adding common:searchableType to node: " + path + " => current node type: " + nodeType)
            return false
        } else {
            log.info("Success - Adding common:searchableType to node: " + path + " => current node type: " + nodeType)
            n.setProperty("common:searchableType", true)
            return true
        }
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }
}
