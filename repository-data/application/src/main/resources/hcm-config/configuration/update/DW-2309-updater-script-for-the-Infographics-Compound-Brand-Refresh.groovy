package org.hippoecm.frontend.plugins.cms.admin.updater

import org.onehippo.repository.update.BaseNodeUpdateVisitor
import javax.jcr.Node
import javax.jcr.RepositoryException
import javax.jcr.Session

class UpdateInfographicsCompound extends BaseNodeUpdateVisitor {

    boolean logSkippedNodePaths() {
        return false // don't log skipped node paths
    }

    boolean skipCheckoutNodes() {
        return false // return true for readonly visitors and/or updates unrelated to versioned content
    }

    Node firstNode(final Session session) throws RepositoryException {
        return null // implement when using custom node selection/navigation
    }

    Node nextNode() throws RepositoryException {
        return null // implement when using custom node selection/navigation
    }

    boolean doUpdate(Node node) {
        log.debug "Updating node ${node.path}"

        if (node.hasProperty("website:colour")) {
            String colour = node.getProperty("website:colour").getString();
            if (colour.equals(" Light blue")) {
                node.setProperty("website:colour", " Yellow");
                return true
            } else if (colour.equals(" White")) {
                node.setProperty("website:colour", " Grey");
                return true
            }
        }


        return false
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }

}
