package uk.nhs.digital.externalstorage.workflow.externalFileCopy;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hippoecm.repository.HippoStdNodeType.NT_FOLDER;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;
import static uk.nhs.digital.externalstorage.ExternalStorageConstants.*;

import org.apache.sling.testing.mock.jcr.MockJcr;
import org.apache.sling.testing.mock.jcr.MockQueryResult;
import org.hippoecm.repository.api.Document;
import org.hippoecm.repository.api.WorkflowContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.nhs.digital.externalstorage.s3.PooledS3Connector;
import uk.nhs.digital.externalstorage.s3.S3ObjectMetadata;

import java.util.UUID;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class ExternalFileCopyTaskTest {

    @Mock private WorkflowContext workflowContext;
    @Mock private PooledS3Connector s3Connector;

    private Repository repository;
    private Session workflowSession;

    private ExternalFileCopyTask externalFileCopyTask;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        repository = MockJcr.newRepository();
        workflowSession = repository.login();

        externalFileCopyTask = new ExternalFileCopyTask();

        given(workflowContext.getInternalWorkflowSession()).willReturn(workflowSession);
        externalFileCopyTask.setWorkflowContext(workflowContext);
    }

    @Test
    public void copiesS3FilesAsPrivate_onDocumentCopy() throws Exception {

        // given

        // node structure:
        // /
        // +- target-folder                            (hippostd:folder)
        //    +- copied-document-handle                (hippo:handle)
        //       +- copied-doc-unpublished-variant     (irrelevant:doctype)
        //          +- copied-doc-ext-attachment       (publicationsystem:extattachment)
        //          |  +- copied-doc-ext-resource      (externalstorage:resource)
        //          |     - hippo:filename             = file name A
        //          |     - externalstorage:url        = file url A
        //          |     - externalstorage:reference  = file reference A
        //          +- copied-doc-ext-attachment[2]    (publicationsystem:extattachment)
        //             +- copied-doc-ext-resource      (externalstorage:resource)
        //                - hippo:filename             = file name B
        //                - externalstorage:url        = file url B
        //                - externalstorage:reference  = file reference B

        final String copiedDocumentName = newRandomString();

        final Node targetFolderNode = workflowSession.getRootNode()
            .addNode("target-folder", NT_FOLDER);
        final Node copiedDocumentHandleNode = targetFolderNode
            .addNode(copiedDocumentName, "hippo:handle");
        final Node copiedDocUnpublishedVariant = copiedDocumentHandleNode
            .addNode("copied-doc-unpublished-variant", "irrelevant:doctype");

        externalFileCopyTask.setCopiedDocumentName(copiedDocumentName);
        externalFileCopyTask.setTargetFolder(new Document(targetFolderNode));
        externalFileCopyTask.setWorkflowContext(workflowContext);

        // file A =============================

        final String oldS3ObjectReferenceA = newRandomString();
        final String newS3ObjectReferenceA = newRandomString();
        final String newS3ObjectUrlA = newRandomString();
        final String fileNameA = newRandomString();

        final Node copiedDocExtResourceNodeA = newResourceNode(copiedDocUnpublishedVariant, "",
            oldS3ObjectReferenceA,
            newS3ObjectReferenceA,
            newS3ObjectUrlA,
            fileNameA
        );

        // file B =============================

        final String oldS3ObjectReferenceB = newRandomString();
        final String newS3ObjectReferenceB = newRandomString();
        final String newS3ObjectUrlB = newRandomString();
        final String fileNameB = newRandomString();

        final Node copiedDocExtResourceNodeB = newResourceNode(copiedDocUnpublishedVariant, "[2]",
            oldS3ObjectReferenceB, newS3ObjectReferenceB, newS3ObjectUrlB, fileNameB);

        // query for resource nodes ===========

        final String copiedDocumentHandleNodePath = copiedDocumentHandleNode.getPath();
        MockJcr.addQueryResultHandler(
            workflowSession,
            mockQuery -> mockQuery.getStatement().matches(
                "SELECT \\* FROM \\[externalstorage:resource].*ISDESCENDANTNODE \\(\\['" + copiedDocumentHandleNodePath + "']\\).*"
            )
                ? new MockQueryResult(asList(copiedDocExtResourceNodeA, copiedDocExtResourceNodeB))
                : null
        );

        // when
        externalFileCopyTask.processResourceNodes(s3Connector, null);

        // then
        assertS3Interactions(oldS3ObjectReferenceA, newS3ObjectReferenceA, newS3ObjectUrlA, fileNameA,
            copiedDocExtResourceNodeA);

        assertS3Interactions(oldS3ObjectReferenceB, newS3ObjectReferenceB, newS3ObjectUrlB, fileNameB,
            copiedDocExtResourceNodeB);
    }

    private Node newResourceNode(final Node copiedDocUnpublishedVariant,
                                 final String pathSuffix,
                                 final String oldS3ObjectReference,
                                 final String newS3ObjectReference,
                                 final String newS3ObjectUrl,
                                 final String fileName
    ) throws RepositoryException {

        final Node copiedDocExtResourceNodeA = copiedDocUnpublishedVariant
            .addNode("copied-doc-ext-attachment" + pathSuffix, "publicationsystem:extattachment")
            .addNode("copied-doc-ext-resource", "externalstorage:resource");

        copiedDocExtResourceNodeA.setProperty(PROPERTY_EXTERNAL_STORAGE_FILE_NAME, fileName);
        copiedDocExtResourceNodeA.setProperty(PROPERTY_EXTERNAL_STORAGE_REFERENCE, oldS3ObjectReference);

        final S3ObjectMetadata s3ObjectMetadataA = mock(S3ObjectMetadata.class);
        given(s3ObjectMetadataA.getReference()).willReturn(newS3ObjectReference);
        given(s3ObjectMetadataA.getUrl()).willReturn(newS3ObjectUrl);

        given(s3Connector.copyFile(oldS3ObjectReference, fileName)).willReturn(s3ObjectMetadataA);
        return copiedDocExtResourceNodeA;
    }

    private void assertS3Interactions(final String oldS3ObjectReferenceA,
                                      final String newS3ObjectReferenceA,
                                      final String newS3ObjectUrlA,
                                      final String fileNameA,
                                      final Node copiedDocExtResourceNode
    ) throws RepositoryException {
        then(s3Connector).should().copyFile(oldS3ObjectReferenceA, fileNameA);
        then(s3Connector).should().unpublishResource(newS3ObjectReferenceA);

        assertThat("Target resource node updated with copied S3 file reference.",
            copiedDocExtResourceNode.getProperty(PROPERTY_EXTERNAL_STORAGE_REFERENCE).getString(),
            is(newS3ObjectReferenceA)
        );

        assertThat("Target resource node updated with copied S3 file URL.",
            copiedDocExtResourceNode.getProperty(PROPERTY_EXTERNAL_STORAGE_PUBLIC_URL).getString(),
            is(newS3ObjectUrlA)
        );
    }

    private String newRandomString() {
        return UUID.randomUUID().toString();
    }
}
