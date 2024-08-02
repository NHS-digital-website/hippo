package org.hippoecm.frontend.plugins.cms.admin.updater

import org.apache.jackrabbit.value.StringValue
import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.Value
import javax.jcr.NodeIterator

/**
 * Removes publicationsystem:Granularity from Publications, Datasets and Legacy Publications.
 */
class RemoveGranularity extends BaseNodeUpdateVisitor {

    boolean doUpdate(Node node) {

        // Query returns the hippo:handle node for the document
        // (which has the 3 variants)
        try {
            if (node.hasNodes()) {
                return updateNode(node)
            }
        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }

    boolean updateNode(Node n) {

        JcrUtils.ensureIsCheckedOut(n)
        def path = n.getPath()
        def nodeType = n.getPrimaryNodeType().getName()
        if ("publicationsystem:publication".equals(nodeType)) {
            if (n.hasProperty("publicationsystem:Granularity")) {
                log.info("Success - Removing publicationsystem:Granularity from node: " + path + " => current node type: " + nodeType)
                n.getProperty("publicationsystem:Granularity").remove()
                return true
            } else
                log.info("Skipped - Removing publicationsystem:Granularity from node: " + path + " => current node type: " + nodeType)
        }
        return false
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }
}
