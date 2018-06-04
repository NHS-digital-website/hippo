package org.hippoecm.frontend.plugins.cms.admin.updater

import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node

/**
 * This migration causes the search rank to be set to 1 to push these docs to the top of
 * 'relevance' searches
 */
class ChangesToSearchRank extends BaseNodeUpdateVisitor {

    private static final String PROPERTY_SEARCH_RANK = "common:searchRank"

    boolean doUpdate(Node node) {
        try {
            log.info("attempting to migrate node: " + node.getPath())

            int value = parametersMap.get(node.getPrimaryNodeType().getName()) as int

            JcrUtils.ensureIsCheckedOut(node)
            node.setProperty(PROPERTY_SEARCH_RANK, value)

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
            if (node.hasProperty(PROPERTY_SEARCH_RANK)) {
                node.getProperty(PROPERTY_SEARCH_RANK).remove()
                log.info("removed property " + PROPERTY_SEARCH_RANK)
            }

            return true
        } catch (e) {
            log.error("Failed to (undo) process record.", e)
        }

        return false
    }
}
