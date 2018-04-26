package org.hippoecm.frontend.plugins.cms.admin.updater

import org.hippoecm.frontend.editor.compare.StreamComparer
import org.hippoecm.repository.api.HippoWorkspace
import org.hippoecm.repository.api.WorkflowManager
import org.hippoecm.repository.util.JcrUtils
import org.hippoecm.repository.util.NodeIterable
import org.onehippo.cms7.services.HippoServiceRegistry
import org.onehippo.repository.documentworkflow.DocumentHandle
import org.onehippo.repository.update.BaseNodeUpdateVisitor
import uk.nhs.digital.externalstorage.s3.S3ObjectMetadata
import uk.nhs.digital.externalstorage.s3.PooledS3Connector

import javax.jcr.Binary
import javax.jcr.Node
import javax.jcr.RepositoryException
import javax.jcr.Session
import java.time.Instant
import java.time.ZonedDateTime
import java.util.concurrent.atomic.AtomicBoolean

import static org.apache.jackrabbit.JcrConstants.JCR_LASTMODIFIED
import static org.hippoecm.repository.HippoStdNodeType.*
import static org.hippoecm.repository.HippoStdPubWfNodeType.HIPPOSTDPUBWF_PUBLICATION_DATE
/**
 * Upload embeded resources to S3 and set the externalstorage properties on the nodes
 */
class Migration20180425UploadResourcesToS3 extends BaseNodeUpdateVisitor {

    private static final String NODE_NAME_RESOURCE = "publicationsystem:attachmentResource";
    private static final String NODE_TYPE_OLD_ATTACHMENT = "publicationsystem:attachment";
    private static final String STATE_NOT_PUBLISHED = "new"

    private static final String PROPERTY_EXTERNAL_STORAGE_REFERENCE = "externalstorage:reference";
    private static final String PROPERTY_EXTERNAL_STORAGE_SIZE = "externalstorage:size";
    private static final String PROPERTY_EXTERNAL_STORAGE_PUBLIC_URL = "externalstorage:url";
    private static final String PROPERTY_LAST_MODIFIED = "jcr:lastModified";
    private static final String PROPERTY_MIME_TYPE = "jcr:mimeType";
    private static final String PROPERTY_FILE_NAME = "hippo:filename";

    private PooledS3Connector s3Connector
    private StreamComparer streamComparer
    private WorkflowManager workflowManager
    private Session session
    private Calendar modifiedDate
    private Instant lastRunDate
    private log

    @Override
    void initialize(Session session) {
        this.session = session;
        s3Connector = HippoServiceRegistry.getService(PooledS3Connector.class)
        streamComparer = new StreamComparer()
        workflowManager = ((HippoWorkspace) session.getWorkspace()).getWorkflowManager();
        modifiedDate = Calendar.getInstance() // want this to be the same for all variants so the doc doesn't look modified in the CMS
        log = super.log // make log accessible to our inner classes

        // We need to rerun the script to pick up and files that have changed/been published since the initial run
        // The format of the parameter map is:
        // { lastRunDate : "2018-05-01T10:52:32.662Z" }
        def param = parametersMap.get("lastRunDate")
        lastRunDate = param == null ? null : ZonedDateTime.parse(param as String).toInstant()
    }

    boolean doUpdate(Node documentNode) {
        try {
            DocumentHandle handle = new DocumentHandle(documentNode)
            handle.initialize()

            return new DocumentProcessor(handle).process()
        } catch (e) {
            log.error("Failed to process record.", e)
            return false
        }
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException("Can not undo this migration")
    }

    private class DocumentProcessor {

        private DocumentHandle handle
        private HashMap<Integer, S3ObjectMetadata> hashToS3Metadata

        DocumentProcessor(handle) {
            this.handle = handle
            hashToS3Metadata = new HashMap<>()
        }

        boolean process() {
            AtomicBoolean processed = new AtomicBoolean(false)

            handle.getDocuments()
                .values()
                .each { variant ->

                Node variantNode = variant.getNode(session)
                new NodeIterable(variantNode.getNodes())
                    .each { subNode -> convertNode(subNode, variantNode, processed) }
            }

            return processed.get()
        }

        private void convertNode(Node node, Node variantNode, AtomicBoolean processed) {
            if (node.isNodeType(NODE_TYPE_OLD_ATTACHMENT)) {
                Node resourceNode = node.getNode(NODE_NAME_RESOURCE)

                boolean isLiveVariant = variantNode.getProperty(HIPPOSTD_STATE).getString().equals(PUBLISHED) &&
                    !variantNode.getProperty(HIPPOSTD_STATESUMMARY).getString().equals(STATE_NOT_PUBLISHED)

                if (resourceNode.hasProperty(PROPERTY_EXTERNAL_STORAGE_REFERENCE)
                    && !changedSinceLastRun(resourceNode, JCR_LASTMODIFIED)) {
                    log.info("node already migrated: " + resourceNode.getPath())

                    // If we have been published since the last run we need to make our resources public as well
                    if (isLiveVariant
                        && changedSinceLastRun(variantNode, HIPPOSTDPUBWF_PUBLICATION_DATE)) {
                        String reference = resourceNode.getProperty(PROPERTY_EXTERNAL_STORAGE_REFERENCE).getString()
                        log.info("publishing recently published resource: " + reference)
                        s3Connector.publishResource(reference)
                    }
                } else {
                    log.info("attempting to migrate node: " + resourceNode.getPath())

                    S3ObjectMetadata metadata = uploadToS3(resourceNode)
                    setResourceProperties(resourceNode, metadata)

                    // If we are on the published variant and it is live then publish the S3 resource
                    if (isLiveVariant) {
                        log.info("publishing resource: " + metadata.reference)
                        s3Connector.publishResource(metadata.reference)
                    }

                    processed.set(true)
                }
            }

            new NodeIterable(node.getNodes())
                .each { childNode -> convertNode(childNode, variantNode, processed) }
        }

        private boolean changedSinceLastRun(Node node, String property) {
            return lastRunDate == null ? false : node.getProperty(property).getDate().toInstant().isAfter(lastRunDate)
        }

        private S3ObjectMetadata uploadToS3(Node node) {
            Binary binary = node.getProperty("jcr:data").getBinary()
            String fileName = node.getProperty(PROPERTY_FILE_NAME).getString()
            String mimeType = node.getProperty(PROPERTY_MIME_TYPE).getString()

            int hashCode
            InputStream stream
            try {
                // We only want to upload a given file once, even if it is referenced by multiple variants
                stream = binary.getStream()
                hashCode = Objects.hash(streamComparer.getHashCode(stream), fileName, mimeType)
            } finally {
                if (stream != null) {
                    stream.close()
                }
            }

            try {
                stream = binary.getStream()

                S3ObjectMetadata metadata = hashToS3Metadata.get(hashCode)
                if (metadata == null) {
                    log.info("About to upload to s3. file: $fileName mime: $mimeType hash: $hashCode")
                    metadata = s3Connector.upload({ stream }, fileName, mimeType)
                    log.info("Finished uploading to s3. url: ${metadata.url} hash: $hashCode")

                    hashToS3Metadata.put(hashCode, metadata)
                }

                return metadata
            } finally {
                if (stream != null) {
                    stream.close()
                }
            }
        }

        private void setResourceProperties(Node node, S3ObjectMetadata metadata) throws RepositoryException {
            JcrUtils.ensureIsCheckedOut(node)

            // Need the relaxed mixin so we can add properties that aren't part of this nodes definition
            node.addMixin(NT_RELAXED)

            node.setProperty(PROPERTY_LAST_MODIFIED, modifiedDate)
            node.setProperty(PROPERTY_EXTERNAL_STORAGE_SIZE, metadata.getSize())
            node.setProperty(PROPERTY_EXTERNAL_STORAGE_REFERENCE, metadata.getReference())
            node.setProperty(PROPERTY_EXTERNAL_STORAGE_PUBLIC_URL, metadata.getUrl())
        }
    }
}
