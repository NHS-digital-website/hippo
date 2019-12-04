package org.hippoecm.frontend.plugins.cms.admin.updater

import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.Value
import javax.jcr.NodeIterator

/**
 * Moving rolepicker of Person doctype to the new compund
 */
class MovePersonRolepickerToCompund extends BaseNodeUpdateVisitor {

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

      log.info("attempting to update node: " + path)
      if ("website:person".equals(nodeType)) {

        JcrUtils.ensureIsCheckedOut(n)

        if (n.hasNode("website:roles") && n.getNode("website:roles").hasNode("website:primaryrolepicker") && n.getNode("website:roles").getNode("website:primaryrolepicker").hasProperty("hippo:docbase")) {

          def pickerNodeIterator = n.getNode("website:roles").getNodes("website:primaryrolepicker")
          while(pickerNodeIterator.hasNext()){
              def pickerNode = pickerNodeIterator.nextNode();
              def previousRole = pickerNode.getProperty("hippo:docbase").getString();

              Node rolesNode = n.getNode("website:roles")

              Node rolepickerNode;
              rolepickerNode = rolesNode.addNode("website:rolepicker", "website:jobrolepicker");
              Node primaryrolepickerNode = rolepickerNode.addNode("website:primaryrolepicker", "hippo:mirror")
              primaryrolepickerNode.setProperty("hippo:docbase", previousRole);

              pickerNode.remove();

              log.info("  UPDATED: moved 'previousRole ("+previousRole+")' content for: " + path)
          }
          return true
        }
      }
      
      return false
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }
}
