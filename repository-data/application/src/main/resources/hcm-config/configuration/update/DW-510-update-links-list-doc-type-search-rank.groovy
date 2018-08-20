package org.hippoecm.frontend.plugins.cms.admin.updater

import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.NodeIterator
import javax.jcr.Session

/**
 * Update Link Lists (componentlist) documents, setting a
 * common:searchRank of 3.
 */
class UpdateLinksListDocTypeSearchRank extends BaseNodeUpdateVisitor {

    private static final String COMMON_SEARCHRANK_PROPERTY = "common:searchRank"
    Session session

    void initialize(Session session) {
        this.session = session
    }

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

            log.info("attempting to update node: " + n.getPath())

            JcrUtils.ensureIsCheckedOut(n)
            if (n.hasProperty(COMMON_SEARCHRANK_PROPERTY)) {
                n.getProperty(COMMON_SEARCHRANK_PROPERTY).remove()
            }
            n.setProperty(COMMON_SEARCHRANK_PROPERTY, 3l)

            variantsUpdated += 1
        }

        return variantsUpdated > 0
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException();
    }
}
