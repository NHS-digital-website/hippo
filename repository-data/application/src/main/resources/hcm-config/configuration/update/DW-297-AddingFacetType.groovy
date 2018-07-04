package org.hippoecm.frontend.plugins.cms.admin.updater

import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node

/**
 * This script is adding the facet type property to existing docs in order to appear
 * in the search filter
 */
class AddingFacetType extends BaseNodeUpdateVisitor {

    private static final String PROPERTY_FACET_TYPE = "common:FacetType"

    boolean doUpdate(Node node) {
        try {
            log.info("attempting to migrate node: " + node.getPath())

            String value = parametersMap.get(node.getPrimaryNodeType().getName()) as String

            JcrUtils.ensureIsCheckedOut(node)
            node.setProperty(PROPERTY_FACET_TYPE, value)

            return true
        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }

    boolean undoUpdate(Node node) {
        try {
            log.info("attempting to undo migration for node: " + node.getPath())

            JcrUtils.ensureIsCheckedOut(node)
            if (node.hasProperty(PROPERTY_FACET_TYPE)) {
                node.getProperty(PROPERTY_FACET_TYPE).remove()
                log.info("removed property " + PROPERTY_FACET_TYPE)
            }

            return true
        } catch (e) {
            log.error("Failed to (undo) process record.", e)
        }

        return false
    }
}
