package org.hippoecm.frontend.plugins.cms.admin.updater

import org.apache.jackrabbit.value.StringValue
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.Value
/**
 *
 *
 */
class ResolveFinalLinkDestination extends BaseNodeUpdateVisitor {

    boolean doUpdate(Node node) {
        try {
            log.info("attempting to update node: " + node.getPath())

            // TODO

            return true
        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }

    boolean undoUpdate(Node node) {
        log.error("UNDO is not available")
        return false
    }
}
