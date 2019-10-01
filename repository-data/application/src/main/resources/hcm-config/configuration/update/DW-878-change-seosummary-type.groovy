package org.hippoecm.frontend.plugins.cms.admin.updater

import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.NodeIterator

/**
 * Document type updated seosummary field from String to HTML (SeoSummary type).
 * This script copies data from old field to new.
 */
class ChangeSeoSummaryType extends BaseNodeUpdateVisitor {

    private static final String PROPERTY_SEO_SUMMARY_PUB = "publicationsystem:seosummary"

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

            //publicationsystem-based documents has other seosummary property
            String property_seo_summary = "website:seosummary"
            if (n.hasProperty(PROPERTY_SEO_SUMMARY_PUB) || n.hasNode(PROPERTY_SEO_SUMMARY_PUB)) {
                property_seo_summary = PROPERTY_SEO_SUMMARY_PUB
            }

            if (!n.hasProperty(property_seo_summary)) {
                continue
            }
          
            // Does node alraedy exist (ex. created manually)?  If so skip.
            if (n.hasNode(property_seo_summary)) {
                log.info("Node '" + property_seo_summary + "' already exists. Skipping : " + path)
                continue
            }

            String currentValue
            currentValue = n.getProperty(property_seo_summary).getString()

            log.info("attempting to update node: " + path + " => currentValue: " + currentValue)
          
            n.getProperty(property_seo_summary).remove()

            def newNode = n.addNode(property_seo_summary, "hippostd:html")
            newNode.setProperty("hippostd:content", currentValue)

            log.info("UPDATED seosummary field for:" + path)
            variantsUpdated += 1
        }

        return variantsUpdated > 0
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }
}
