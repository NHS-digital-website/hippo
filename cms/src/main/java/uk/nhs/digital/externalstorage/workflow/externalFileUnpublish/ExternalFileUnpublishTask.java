package uk.nhs.digital.externalstorage.workflow.externalFileUnpublish;

import static org.hippoecm.repository.HippoStdNodeType.PUBLISHED;
import static uk.nhs.digital.ps.PublicationSystemConstants.INDEX_FILE_NAME;

import uk.nhs.digital.externalstorage.ExternalStorageConstants;
import uk.nhs.digital.externalstorage.s3.PooledS3Connector;
import uk.nhs.digital.externalstorage.workflow.AbstractExternalFileTask;
import uk.nhs.digital.ps.PublicationSystemConstants;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

public class ExternalFileUnpublishTask extends AbstractExternalFileTask {

    protected void processResourceNodes(final PooledS3Connector s3Connector, final NodeIterator resourceNodes) throws RepositoryException {
        Node variantNode = getVariant().getNode(getWorkflowContext().getInternalWorkflowSession());

        if (isPublication(variantNode)) {
            // if un-publishing publication we need to also un-publish its dataset resources.
            unpublishPublicationResources(s3Connector, resourceNodes);
        } else {
            // no additional logic for other document types. Un-published == private attachments
            unpublishResources(s3Connector, resourceNodes);
        }
    }

    private void unpublishPublicationResources(final PooledS3Connector s3Connector, NodeIterator resourceNodes) throws RepositoryException {
        Node variantNode = getVariant().getNode(getWorkflowContext().getInternalWorkflowSession());

        unpublishResources(s3Connector, resourceNodes);

        // now find all datasets and publish resources
        // only if publication name is "content"
        if (INDEX_FILE_NAME.equals(variantNode.getName())) {
            NodeIterator nodes = findPublicationDatasetsVariant(variantNode, PUBLISHED);

            while (nodes.hasNext()) {
                unpublishResources(s3Connector, findResourceNodes(nodes.nextNode()));
            }
        }
    }

    private void unpublishResources(final PooledS3Connector s3Connector, NodeIterator resourceNodes) throws RepositoryException {
        for (Node node; resourceNodes.hasNext(); ) {
            node = resourceNodes.nextNode();
            if (node.hasProperty(ExternalStorageConstants.PROPERTY_EXTERNAL_STORAGE_REFERENCE)) {
                String externalResource = node
                    .getProperty(ExternalStorageConstants.PROPERTY_EXTERNAL_STORAGE_REFERENCE)
                    .getString();

                s3Connector.unpublishResource(externalResource);
            }
        }
    }


    private boolean isPublication(Node variantNode) throws RepositoryException {
        return variantNode.isNodeType(PublicationSystemConstants.NODE_TYPE_PUBLICATION)
            || variantNode.isNodeType(PublicationSystemConstants.NODE_TYPE_LEGACY_PUBLICATION);
    }
}
