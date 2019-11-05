package org.hippoecm.frontend.plugins.cms.admin.updater

import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.Value
import javax.jcr.NodeIterator

/**
 * Move News doctype 'body' content into 'sections'
 */
class MoveNewsBodyIntoSections extends BaseNodeUpdateVisitor {

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

      //log.info("attempting to update node: " + path + " => current node type: " + nodeType)
      if ("website:news".equals(nodeType)) {
        if (n.hasNode("website:body") && n.getNode("website:body").getProperty("hippostd:content").getString()) {
          def body = n.getNode("website:body")

          Node bodySection = n.addNode("website:sections", "website:section")

          Node bodyNode = bodySection.addNode("website:html", "hippostd:html")
          String bodyContent = body.getProperty("hippostd:content").getString();
          bodyNode.setProperty("hippostd:content", bodyContent)

          body.remove();

          log.info("  UPDATED: moved 'body' content into 'sections' for: " + path + "; bodyContent: " + bodyContent)
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
