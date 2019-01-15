
import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.NodeIterator

/**
 * This script sets a default banner selection to news items.
 */
class AddsCreditBannerDefaultToNews extends BaseNodeUpdateVisitor {

    private static final String PROPERTY_CREDIT_BANNER = "website:creditbanner"

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
            if (n.hasProperty(PROPERTY_CREDIT_BANNER)) {
                log.info("The property already exists. Skipping : " + path)
                continue
            }
            log.info("attempting to set node: " + path)
            n.setProperty(PROPERTY_CREDIT_BANNER, "credit-information")
            variantsUpdated += 1
        }
        return variantsUpdated > 0
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }
}
