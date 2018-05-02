package org.hippoecm.frontend.plugins.cms.admin.updater

import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
/**
 * Revert the nodes from external attachments to embedded ones for if anything goes wrong with
 * activating the S3 integration
 */
class Migration20180502RevertExternalAttachmentsToEmbedded extends BaseNodeUpdateVisitor {

    private static final String NODE_NAME_RESOURCE = "publicationsystem:attachmentResource"
    private static final String NODE_TYPE_OLD_RESOURCE = "publicationsystem:resource"

    private static final String NODE_TYPE_OLD_ATTACHMENT = "publicationsystem:attachment"
    private static final String NODE_TYPE_EXTERNAL_ATTACHMENT = "publicationsystem:extattachment"

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
            resourceNode.setPrimaryType(NODE_TYPE_OLD_RESOURCE)

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
