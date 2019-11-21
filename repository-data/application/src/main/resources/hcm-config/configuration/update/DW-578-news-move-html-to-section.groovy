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
                return updateNode(node)
            }
        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }

    boolean updateNode(Node n) {

      def path = n.getPath()
      def nodeType = n.getPrimaryNodeType().getName()

      log.info("attempting to update node: " + path + " => current node type: " + nodeType)
      if ("website:news".equals(nodeType)) {

        JcrUtils.ensureIsCheckedOut(n)

        if (n.hasNode("website:body") && n.getNode("website:body").getProperty("hippostd:content").getString()) {
          def body = n.getNode("website:body")

          Node bodySection = n.addNode("website:sections", "website:section")

          Node bodyNode = bodySection.addNode("website:html", "hippostd:html")
          String bodyContent = body.getProperty("hippostd:content").getString();
          bodyNode.setProperty("hippostd:content", bodyContent)

          body.remove();

          log.info("  UPDATED: moved 'body' content into 'sections' for: " + path)
          return true
        }
      }
      
      return false
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }
}
