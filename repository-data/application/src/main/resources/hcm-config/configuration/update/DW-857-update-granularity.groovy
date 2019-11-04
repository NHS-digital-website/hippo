package org.hippoecm.frontend.plugins.cms.admin.updater

import org.apache.jackrabbit.value.StringValue
import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.Value
import javax.jcr.NodeIterator

/**
 * Change default asset type to custmized one
 */
class UpdateGranularity extends BaseNodeUpdateVisitor {

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
      if (("publicationsystem:legacypublication".equals(nodeType) ||
          "publicationsystem:publication".equals(nodeType) ||
          "publicationsystem:dataset".equals(nodeType)) && 
          n.hasProperty("publicationsystem:Granularity"))  {
        def property = n.getProperty("publicationsystem:Granularity")
        if (property) {
          Value[] values = property.getValues()
          Value[] newValues = new Value [ values.size()]

          def boolean replaced = false

          for (int i=0; i<values.length; i++) {
            newValues[i] = values[i];
            if(values[i].getString().contains("Councils with Social Services Responsibilities (CASSRs)")) {
               newValues[i]  = new StringValue("Councils with Adult Social Services Responsibilities (CASSRs)")
               replaced = true 
            }
          }

          if (replaced) {
            log.info("  UPDATED Granularity property of node: " + path)
            n.setProperty("publicationsystem:Granularity", newValues)
            return true
          }
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
