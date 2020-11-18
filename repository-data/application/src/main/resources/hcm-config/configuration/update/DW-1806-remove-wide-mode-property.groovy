package org.hippoecm.frontend.plugins.cms.admin.updater

import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node

class UpdaterTemplate extends BaseNodeUpdateVisitor {

    private static final String PROPERTY_WIDE_MODE = 'website:wideMode'
    private static final String PROPERTY_NAVIGATION_CONTROLLER = 'website:navigationcontroller'

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
        if ("website:general".equals(nodeType)) {
            JcrUtils.ensureIsCheckedOut(node)
            if (node.hasProperty(PROPERTY_WIDE_MODE)) {
                if (node.getProperty(PROPERTY_WIDE_MODE).getString() == 'true') {
                    log.info("attempting to update: " + path + " => current node type: " + nodeType + " => setting " + PROPERTY_NAVIGATION_CONTROLLER + " to 'withoutNavWide'")
                    node.setProperty(PROPERTY_NAVIGATION_CONTROLLER, 'withoutNavWide')
                }
                log.info("attempting to update: " + path + " => current node type: " + nodeType + " => removing " + PROPERTY_WIDE_MODE)
                node.getProperty(PROPERTY_WIDE_MODE).remove();
                return true
            }
        }
        return false
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException("Not implemented.")
    }

}
