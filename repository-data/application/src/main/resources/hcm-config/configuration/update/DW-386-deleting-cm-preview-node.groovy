package org.hippoecm.frontend.plugins.cms.admin.updater

import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node


/**
 * This script deletes the preview node of the channel manager passed as parameter
 */
class DeletingCmPreviewNode extends BaseNodeUpdateVisitor {

    boolean doUpdate(Node node) {
        try {
            log.info("attempting to remove node: " + node.getPath())

            node.remove()

            return true

        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('If you have a backup of the preview node, then upload it using the console')
    }
}
