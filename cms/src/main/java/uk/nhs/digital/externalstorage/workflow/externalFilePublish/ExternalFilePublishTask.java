package uk.nhs.digital.externalstorage.workflow.externalFilePublish;

import static org.hippoecm.repository.HippoStdNodeType.PUBLISHED;
import static uk.nhs.digital.ps.PublicationSystemConstants.INDEX_FILE_NAME;

import org.hippoecm.repository.api.WorkflowException;
import org.onehippo.repository.documentworkflow.DocumentHandle;
import org.onehippo.repository.documentworkflow.DocumentVariant;
import uk.nhs.digital.externalstorage.ExternalStorageConstants;
import uk.nhs.digital.externalstorage.s3.PooledS3Connector;
import uk.nhs.digital.externalstorage.workflow.AbstractExternalFileTask;
import uk.nhs.digital.ps.PublicationSystemConstants;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

public class ExternalFilePublishTask extends AbstractExternalFileTask {

    private static final String DOCUMENTS_ROOT_FOLDER = "/content/documents";

    protected void processResourceNodes(final PooledS3Connector s3Connector, final NodeIterator resourceNodes) throws RepositoryException, WorkflowException {
        Node variantNode = getVariant().getNode(getWorkflowContext().getInternalWorkflowSession());

        if (isPublication(variantNode)) {
            // if publishing publication we need to check "PUBLICLY ACCESSIBLE" flag, and also update all datasets
            setResourcePermissionOnPublication(s3Connector, resourceNodes);
        } else if (isDataset(variantNode)) {
            // when publishing dataset, we need to check parent LIVE publication state
            publishResources(s3Connector, resourceNodes, isParentPublicationFinalised(variantNode));
        } else {
            // no additional logic for other document types. Published == public attachments
            publishResources(s3Connector, resourceNodes, true);
        }
    }

    private void publishResources(PooledS3Connector s3Connector, NodeIterator resourceNodes, boolean shouldBePublic) throws RepositoryException {
        for (Node node; resourceNodes.hasNext(); ) {
            node = resourceNodes.nextNode();
            if (node.hasProperty(ExternalStorageConstants.PROPERTY_EXTERNAL_STORAGE_REFERENCE)) {
                String externalResource = node
                    .getProperty(ExternalStorageConstants.PROPERTY_EXTERNAL_STORAGE_REFERENCE)
                    .getString();

                if (shouldBePublic) {
                    s3Connector.publishResource(externalResource);
                } else {
                    s3Connector.unpublishResource(externalResource);
                }
            }
        }
    }

    private void setResourcePermissionOnPublication(PooledS3Connector s3Connector, NodeIterator resourceNodes) throws RepositoryException {
        Node variantNode = getVariant().getNode(getWorkflowContext().getInternalWorkflowSession());

        publishResources(s3Connector, resourceNodes, isPublicationFinalised(variantNode));

        // now find all datasets and publish resources
        // only if publication name is "content"
        if (INDEX_FILE_NAME.equals(variantNode.getName())) {
            NodeIterator nodes = findPublicationDatasetsVariant(variantNode, PUBLISHED);

            while (nodes.hasNext()) {
                publishResources(s3Connector, findResourceNodes(nodes.nextNode()), isPublicationFinalised(variantNode));
            }
        }
    }

    private boolean isParentPublicationFinalised(Node datasetVariant) throws RepositoryException, WorkflowException {
        DocumentVariant parentPublication = findPublicationInFolder(datasetVariant.getParent().getParent());

        if (parentPublication == null) {
            // no parent publication found
            return false;
        }

        return parentPublication.getNode(getWorkflowContext().getInternalWorkflowSession())
            .getProperty(PublicationSystemConstants.PROPERTY_PUBLICLY_ACCESSIBLE)
            .getBoolean();
    }

    private DocumentVariant findPublicationInFolder(Node folder) throws RepositoryException, WorkflowException {
        if (DOCUMENTS_ROOT_FOLDER.equals(folder.getPath())) {
            return null;
        }

        NodeIterator nodes = folder.getNodes();
        while (nodes.hasNext()) {
            Node node = nodes.nextNode();
            if (isContentHandle(node)) {
                DocumentHandle documentHandle = new DocumentHandle(node);
                documentHandle.initialize();

                return documentHandle.getDocuments().get(variantState);
            }
        }

        return findPublicationInFolder(folder.getParent());
    }

    private boolean isContentHandle(Node node) throws RepositoryException {
        return node.isNodeType("hippo:handle")
            && INDEX_FILE_NAME.equals(node.getName());
    }

    private boolean isPublicationFinalised(Node publicationVariant) throws RepositoryException {
        return publicationVariant.getProperty(PublicationSystemConstants.PROPERTY_PUBLICLY_ACCESSIBLE).getBoolean();
    }

    private boolean isDataset(Node variantNode) throws RepositoryException {
        return  variantNode.isNodeType(PublicationSystemConstants.NODE_TYPE_DATASET);
    }

    private boolean isPublication(Node variantNode) throws RepositoryException {
        return variantNode.isNodeType(PublicationSystemConstants.NODE_TYPE_PUBLICATION)
            || variantNode.isNodeType(PublicationSystemConstants.NODE_TYPE_LEGACY_PUBLICATION);
    }
}
