package org.hippoecm.frontend.plugins.cms.admin.updater

import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node

/**
 * This migration sets all the documents passed in to searchable
 */
class Migration20180319UpcomingPublicationsSearchable extends BaseNodeUpdateVisitor {

    private static final String PROPERTY_SEARCHABLE = "common:searchable"

    boolean doUpdate(Node node) {
        try {
            log.info("attempting to migrate node: " + node.getPath())

            JcrUtils.ensureIsCheckedOut(node)
            node.setProperty(PROPERTY_SEARCHABLE, true)

            return true
        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException("Can't undo update")
    }
}
