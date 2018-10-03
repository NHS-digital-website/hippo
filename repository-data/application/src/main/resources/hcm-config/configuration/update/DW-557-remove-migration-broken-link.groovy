package org.hippoecm.frontend.plugins.cms.admin.updater

import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.Session

/**
 * Remove one of the broken links brought over by the migration
 */
class RemoveMigrationBrokenLink extends BaseNodeUpdateVisitor {

    Session session

    void initialize(Session session) {
        this.session = session
    }

    boolean doUpdate(Node n) {

        try {
            log.info("attempting to remove node: " + n.getPath())

            JcrUtils.ensureIsCheckedOut(n)
            n.remove()

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
