package org.hippoecm.frontend.plugins.cms.admin.updater

import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.Session

/**
 * Add new property to existing website:section nodes
 */
class AddNumberedListWebsiteSection extends BaseNodeUpdateVisitor {

    private static final String NUMBERED_LIST_PROPERTY = "website:numberedList"
    Session session

    void initialize(Session session) {
        this.session = session
    }

    boolean doUpdate(Node n) {

        try {
            log.info("attempting to update node: " + n.getPath())

            JcrUtils.ensureIsCheckedOut(n)
            n.setProperty(NUMBERED_LIST_PROPERTY, false)

            return true
        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException();
    }
}
