package uk.nhs.digital.externalstorage.workflow.externalFileCopy;

import static org.slf4j.LoggerFactory.getLogger;
import static uk.nhs.digital.externalstorage.ExternalStorageConstants.PROPERTY_EXTERNAL_STORAGE_FILE_NAME;
import static uk.nhs.digital.externalstorage.ExternalStorageConstants.PROPERTY_EXTERNAL_STORAGE_PUBLIC_URL;
import static uk.nhs.digital.externalstorage.ExternalStorageConstants.PROPERTY_EXTERNAL_STORAGE_REFERENCE;

import org.hippoecm.repository.HippoStdNodeType;
import org.hippoecm.repository.api.Document;
import org.hippoecm.repository.api.WorkflowException;
import org.slf4j.Logger;
import uk.nhs.digital.externalstorage.s3.PooledS3Connector;
import uk.nhs.digital.externalstorage.s3.S3ObjectMetadata;
import uk.nhs.digital.externalstorage.workflow.AbstractExternalFileTask;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

public class ExternalFileCopyTask extends AbstractExternalFileTask {

    private static final Logger log = getLogger(ExternalFileCopyTask.class);

    private String copiedDocumentName;
    private Document copiedDocumentFolder;

    ExternalFileCopyTask() {
        variantState = HippoStdNodeType.UNPUBLISHED;
    }

    void setCopiedDocumentName(String copiedDocumentName) {
        this.copiedDocumentName = copiedDocumentName;
    }

    void setTargetFolder(final Document targetFolder) {
        this.copiedDocumentFolder = targetFolder;
    }

    @Override
    protected void processResourceNodes(final PooledS3Connector s3,
                                        final NodeIterator resourceNodes
    ) throws RepositoryException, WorkflowException {

        final Node copiedDocumentFolderNode = copiedDocumentFolder.getNode(getWorkflowContext().getInternalWorkflowSession());
        final Node copiedDocumentHandleNode = copiedDocumentFolderNode.getNode(copiedDocumentName);

        // When a document gets copied, only one variant is created, regardless of whether the
        // source document had been published or not; that variant has hippostd:state=unpublished.

        for (final NodeIterator resourceNodeIterator = findResourceNodes(copiedDocumentHandleNode);
            resourceNodeIterator.hasNext();) {
            final Node copiedResourceNode = resourceNodeIterator.nextNode();

            final String oldReference = copiedResourceNode.getProperty(PROPERTY_EXTERNAL_STORAGE_REFERENCE).getString();

            final S3ObjectMetadata s3ObjectMetadata = s3.copyFile(
                oldReference,
                copiedResourceNode.getProperty(PROPERTY_EXTERNAL_STORAGE_FILE_NAME).getString()
            );

            s3.unpublishResource(s3ObjectMetadata.getReference());

            copiedResourceNode.setProperty(PROPERTY_EXTERNAL_STORAGE_REFERENCE, s3ObjectMetadata.getReference());
            copiedResourceNode.setProperty(PROPERTY_EXTERNAL_STORAGE_PUBLIC_URL, s3ObjectMetadata.getUrl());

            log.debug("Copied external resource {} as private {}", oldReference, s3ObjectMetadata);
        }
    }
}
