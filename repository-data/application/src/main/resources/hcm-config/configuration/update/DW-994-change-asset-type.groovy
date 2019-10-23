package org.hippoecm.frontend.plugins.cms.admin.updater

import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.NodeIterator

/**
 * Change default asset type to custmized one
 */
class ChangeDefaultAssetTypeToCustomizedOne extends BaseNodeUpdateVisitor {

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

    boolean updateNode(Node n) {

      JcrUtils.ensureIsCheckedOut(n)

      def path = n.getPath()
      def nodeType = n.getPrimaryNodeType().getName()

      log.info("attempting to update node: " + path + " => current node type: " + nodeType)
      if ("hippogallery:exampleAssetSet".equals(nodeType)) {
        def newNodeType = "website:customizedAssetSet"
        n.setPrimaryType(newNodeType)
        log.info("  UPDATED TYPE OF node: " + path + " from: '" + nodeType + "' to: '" + newNodeType +"'")
        return true
      } else if ("hippogallery:stdAssetGallery".equals(nodeType))  {
        def property = n.getProperty("hippostd:gallerytype")
        if (property) {
          log.info("  UPDATED gallerytype property of node: " + path)
          String[] gallerytypes = ["website:customizedAssetSet"]
          n.setProperty("hippostd:gallerytype", gallerytypes)
          return true
        }
      }
      
      return false
    }

    boolean updateFieldAndSaveDocument(Node parentHippoHandleNode) {

        NodeIterator nodeIterator = parentHippoHandleNode.getNodes()
        int variantsUpdated = 0

        if (updateNode(parentHippoHandleNode)) {
          variantsUpdated += 1
        }

        // Iterate through each variant
        while(nodeIterator.hasNext()) {

            Node n = nodeIterator.nextNode()
          
            if (updateNode(n)) {
              variantsUpdated += 1
            }
        }

        return variantsUpdated > 0
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }
}
