package org.hippoecm.frontend.plugins.cms.admin.updater

import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node

class UpdaterTemplate extends BaseNodeUpdateVisitor {

    private static final String PROPERTY_NAVIGATION_CONTROLLER = 'website:navigationcontroller'
    private static final String DEFAULT_SELECTION = 'withNav'

    boolean doUpdate(Node node) {
        try {
            if (node.hasNodes()) {
                return updateNode(node)
            }
        } catch (e) {
            log.error("Failed to process record.", e)
        }
        return false
    }

    boolean updateNode(Node node) {
        def path = node.getPath()
        def nodeType = node.getPrimaryNodeType().getName()
        if ("website:general" == nodeType || "website:service" == nodeType) {
            log.info("checking node: " + path + " => current node type: " + nodeType)
            JcrUtils.ensureIsCheckedOut(node)
            if (!node.hasProperty(PROPERTY_NAVIGATION_CONTROLLER) || !node.getProperty(PROPERTY_NAVIGATION_CONTROLLER)?.getString()) {
                log.info("attempting to update node: " + path + " => current node type: " + nodeType)
                node.setProperty(PROPERTY_NAVIGATION_CONTROLLER, DEFAULT_SELECTION)
                return true
            }
        }
        return false
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException("Not implemented.")
    }

}
