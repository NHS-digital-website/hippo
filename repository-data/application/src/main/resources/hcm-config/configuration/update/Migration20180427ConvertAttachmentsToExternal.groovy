package org.hippoecm.frontend.plugins.cms.admin.updater

import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node

/**
 * Swap the pre uploaded attachment node to using the S3 resource instead of the embedded one
 */
class Migration20180427ConvertAttachmentsToExternal extends BaseNodeUpdateVisitor {

    private static final String NODE_NAME_RESOURCE = "publicationsystem:attachmentResource"
    private static final String NODE_TYPE_EXTERNAL_RESOURCE = "externalstorage:resource"

    private static final String NODE_TYPE_OLD_ATTACHMENT = "publicationsystem:attachment"
    private static final String NODE_TYPE_EXTERNAL_ATTACHMENT = "publicationsystem:extattachment"

    private static final String PROPERTY_EXTERNAL_STORAGE_REFERENCE = "externalstorage:reference"

    boolean doUpdate(Node attachmentNode) {
        try {
            Node resourceNode = attachmentNode.getNode(NODE_NAME_RESOURCE)
            if (!attachmentNode.isNodeType(NODE_TYPE_OLD_ATTACHMENT) ||
                !resourceNode.hasProperty(PROPERTY_EXTERNAL_STORAGE_REFERENCE)) {
                throw new RuntimeException("Node is not ready to be converted: ${attachmentNode.path}")
            }

            log.info("Attempting to convert node ${attachmentNode.path}")

            JcrUtils.ensureIsCheckedOut(attachmentNode)
            attachmentNode.setPrimaryType(NODE_TYPE_EXTERNAL_ATTACHMENT)

            JcrUtils.ensureIsCheckedOut(resourceNode)
            resourceNode.setPrimaryType(NODE_TYPE_EXTERNAL_RESOURCE)

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
