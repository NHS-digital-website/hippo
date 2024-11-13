package org.hippoecm.frontend.plugins.cms.admin.updater

import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node

/**
 * Adds showServiceResult true on each document.
 */
class ServiceCatalogueCheckmark extends BaseNodeUpdateVisitor {

    boolean doUpdate(Node node) {

        // Query returns the variant nodes of the document
        // (which has the 3 variants)
        try {
            return updateNode(node)
        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }

    boolean updateNode(Node n) {

        JcrUtils.ensureIsCheckedOut(n)
        def path = n.getPath()

        def nodeType = n.getPrimaryNodeType().getName()
        if (n.hasProperty("common:showServiceResult")) {
            log.info("Skipped - Adding common:showServiceResult to node: " + path + " => current node type: " + nodeType)
            return false
        } else {
            log.info("Success - Adding common:showServiceResult to node: " + path + " => current node type: " + nodeType)
            n.setProperty("common:showServiceResult", true)
            return true
        }
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }
}
