package org.hippoecm.frontend.plugins.cms.admin.updater

import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.NodeIterator

/**
* Update Summary field from String to Rich Text.
* This script copies data from old field to new, and removes old field
*/
class UpdateSummaryToLimitedRichText extends BaseNodeUpdateVisitor {

    private static final String PROPERTY_SUMMARY = "website:summary"

    boolean doUpdate(Node node) {

        // Query returns the hippo:handle node for the document
        // (which has the 3 variants)

        try {
            if (node.hasNodes()) {
                return updateFieldAndSaveDocument(node)
            }
        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }

    boolean updateFieldAndSaveDocument(Node parentHippoHandleNode) {

        NodeIterator nodeIterator = parentHippoHandleNode.getNodes()
        int variantsUpdated = 0

        // Iterate through each variant
        while(nodeIterator.hasNext()) {

            Node n = nodeIterator.nextNode()
            String path = n.getPath()

            JcrUtils.ensureIsCheckedOut(n)
            if (!n.hasProperty(PROPERTY_SUMMARY)) {
                continue
            }

            String currentValue
            currentValue = n.getProperty(PROPERTY_SUMMARY).getString()

            log.info("attempting to update node: " + path)

            // Does node already have this new property?  If so skip.
            if (n.hasNode(PROPERTY_SUMMARY)) {
                log.info("New property already exists. Skipping : " + path)
                continue
            }

            // create new html summary node property and assign current value
            def newNode = n.addNode(PROPERTY_SUMMARY, "hippostd:html")
            newNode.setProperty("hippostd:content", "<p>" + currentValue + "</p>")

            // Remove old summary property
            n.getProperty(PROPERTY_SUMMARY).remove()

            log.info("UPDATED Summary field for:" + path)
            variantsUpdated += 1
        }

        return variantsUpdated > 0
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }
}
