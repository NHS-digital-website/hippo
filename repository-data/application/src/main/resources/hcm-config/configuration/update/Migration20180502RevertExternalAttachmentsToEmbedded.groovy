package org.hippoecm.frontend.plugins.cms.admin.updater

import org.hippoecm.repository.HippoStdNodeType
import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.nodetype.ConstraintViolationException

/**
 * Revert the nodes from external attachments to embedded ones for if anything goes wrong with
 * activating the S3 integration
 */
class Migration20180502RevertExternalAttachmentsToEmbedded extends BaseNodeUpdateVisitor {

    private static final String NODE_NAME_RESOURCE = "publicationsystem:attachmentResource"
    private static final String NODE_TYPE_OLD_RESOURCE = "publicationsystem:resource"

    private static final String NODE_TYPE_OLD_ATTACHMENT = "publicationsystem:attachment"
    private static final String NODE_TYPE_EXTERNAL_ATTACHMENT = "publicationsystem:extattachment"
    private static final String CONSTRAINT_ERROR_MESSAGE = "no matching property definition found for {http://digital.nhs.uk/jcr/externalstorage/nt/1.0}reference"

    boolean doUpdate(Node attachmentNode) {
        try {
            if (!attachmentNode.isNodeType(NODE_TYPE_EXTERNAL_ATTACHMENT)) {
                throw new RuntimeException("Node is not ready to be converted: ${attachmentNode.path}")
            }

            log.info("Attempting to convert node ${attachmentNode.path}")

            JcrUtils.ensureIsCheckedOut(attachmentNode)
            attachmentNode.setPrimaryType(NODE_TYPE_OLD_ATTACHMENT)

            Node resourceNode = attachmentNode.getNode(NODE_NAME_RESOURCE)
            JcrUtils.ensureIsCheckedOut(resourceNode)

            // Because the old resource node type doesn't have the relaxed mixin type,
            // we get an exception here because we have got properties on this node
            // which are not allowed. To get around this, we suppress the exception and
            // add the relaxed mixin afterwards manually
            try {
                resourceNode.setPrimaryType(NODE_TYPE_OLD_RESOURCE)
            } catch (ConstraintViolationException cve) {
                if (cve.getMessage().equals(CONSTRAINT_ERROR_MESSAGE)) {
                    log.info("Suppressing Constraint error and adding relaxed mixin")
                    resourceNode.addMixin(HippoStdNodeType.NT_RELAXED)
                } else {
                    throw cve
                }
            }

            return true
        } catch (e) {
            log.error("Failed to process record.", e)
            return false
        }
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException("Can not undo this migration")
    }
}
