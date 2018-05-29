package uk.nhs.digital.externalstorage.workflow.externalFilePublish;

import static org.hippoecm.repository.HippoStdNodeType.HIPPOSTD_STATE;
import static org.hippoecm.repository.HippoStdNodeType.NT_FOLDER;
import static org.hippoecm.repository.HippoStdNodeType.PUBLISHED;
import static org.hippoecm.repository.HippoStdNodeType.UNPUBLISHED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.MockitoAnnotations.initMocks;

import org.apache.sling.testing.mock.jcr.MockJcr;
import org.apache.sling.testing.mock.jcr.MockQueryResult;
import org.apache.sling.testing.mock.jcr.MockQueryResultHandler;
import org.hippoecm.repository.api.WorkflowContext;
import org.hippoecm.repository.api.WorkflowException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.onehippo.repository.documentworkflow.DocumentHandle;
import uk.nhs.digital.externalstorage.ExternalStorageConstants;
import uk.nhs.digital.externalstorage.s3.PooledS3Connector;
import uk.nhs.digital.ps.PublicationSystemConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class ExternalFilePublishTaskTest {

    @Mock private PooledS3Connector s3Connector;
    @Mock private WorkflowContext workflowContext;

    private ExternalFilePublishTask externalFilePublishTask;
    private Session session;
    private Node rootNode;
    private Node testFolder;
    private final String testExternalReference = "ab/1c2d3e/one.pdf";

    @Before
    public void setUp() throws RepositoryException {
        initMocks(this);

        Repository repository = MockJcr.newRepository();
        session = repository.login();
        rootNode = session.getRootNode();
        testFolder = rootNode.addNode("lorem-ipsum", NT_FOLDER);
        externalFilePublishTask = new ExternalFilePublishTask();

        given(workflowContext.getInternalWorkflowSession()).willReturn(session);
        externalFilePublishTask.setWorkflowContext(workflowContext);
    }

    @Test
    public void shouldPublishAnyDocumentResource() throws WorkflowException, RepositoryException {
        Node document = getDocumentHandleWithVariant(getRandomDocumentType(), UNPUBLISHED);
        MockJcr.addQueryResultHandler(session, searchResourcesHandle(getVariantPath(document), testExternalReference));

        executeTask(document, externalFilePublishTask);

        then(s3Connector).should().publishResource(testExternalReference);
        then(s3Connector).should(never()).unpublishResource(anyString());
    }

    @Test
    public void shouldPublishFinalisedStandalonePublicationResources() throws WorkflowException, RepositoryException {
        Node document = getDocumentHandleWithVariant(getFinalisedPublication(), UNPUBLISHED);
        MockJcr.addQueryResultHandler(session, searchResourcesHandle(getVariantPath(document), testExternalReference));

        executeTask(document, externalFilePublishTask);

        then(s3Connector).should().publishResource(testExternalReference);
        then(s3Connector).should(never()).unpublishResource(anyString());
    }

    @Test
    public void shouldUnpublishUpcomingStandalonePublicationResources() throws WorkflowException, RepositoryException {
        Node document = getDocumentHandleWithVariant(getUpcomingPublication(), UNPUBLISHED);
        MockJcr.addQueryResultHandler(session, searchResourcesHandle(getVariantPath(document), testExternalReference));

        executeTask(document, externalFilePublishTask);

        then(s3Connector).should(never()).publishResource(anyString());
        then(s3Connector).should().unpublishResource(testExternalReference);
    }

    @Test
    public void shouldUnpublishDatasetResourcesPartOfUpcomingPublication() throws WorkflowException, RepositoryException {
        Node publication = getDocumentHandleWithVariant(getUpcomingPublication(), UNPUBLISHED);
        Node dataset = getDocumentHandleWithVariant(getDataset(), UNPUBLISHED);
        String publicationResource = "ab/cdef12/lorem.pdf";
        MockJcr.addQueryResultHandler(session, searchResourcesHandle(getVariantPath(dataset), testExternalReference));
        MockJcr.addQueryResultHandler(session, searchResourcesHandle(getVariantPath(publication), publicationResource));

        executeTask(dataset, externalFilePublishTask);

        then(s3Connector).should(never()).publishResource(anyString());
        then(s3Connector).should().unpublishResource(testExternalReference);
        then(s3Connector).should(never()).unpublishResource(publicationResource);
    }

    @Test
    public void shouldPublishDatasetResourcesPartOfFinalisedPublication() throws WorkflowException, RepositoryException {
        Node publication = getDocumentHandleWithVariant(getFinalisedPublication(), UNPUBLISHED);
        Node dataset = getDocumentHandleWithVariant(getDataset(), UNPUBLISHED);
        String publicationResource = "ab/cdef12/lorem.pdf";

        MockJcr.addQueryResultHandler(session, searchResourcesHandle(getVariantPath(dataset), testExternalReference));
        MockJcr.addQueryResultHandler(session, searchResourcesHandle(getVariantPath(publication), publicationResource));

        // when
        executeTask(dataset, externalFilePublishTask);

        then(s3Connector).should().publishResource(testExternalReference);
        then(s3Connector).should(never()).publishResource(publicationResource);
        then(s3Connector).should(never()).unpublishResource(anyString());
    }

    @Test
    public void shouldPublishFinalisedPublicationAndItsDatasetsResources() throws WorkflowException, RepositoryException {
        // add Dataset to JCR
        Node dataset = getDocumentHandleWithVariant(getDataset(), PUBLISHED);
        Node publication = getDocumentHandleWithVariant(getFinalisedPublication(), UNPUBLISHED);
        String publicationResource = "ab/cdef12/lorem.pdf";

        MockJcr.addQueryResultHandler(session, searchResourcesHandle(getVariantPath(dataset), testExternalReference));
        MockJcr.addQueryResultHandler(session, searchResourcesHandle(getVariantPath(publication), publicationResource));
        MockJcr.addQueryResultHandler(session, searchDatasetsHandle(dataset.getNode(dataset.getName())));

        // when publishing finalised publication
        executeTask(publication, externalFilePublishTask);

        // should be executed once for publication and once for dataset
        then(s3Connector).should(times(1)).publishResource(testExternalReference);
        then(s3Connector).should(times(1)).publishResource(publicationResource);
        then(s3Connector).should(never()).unpublishResource(anyString());
    }

    @Test
    public void shouldUnpublishUpcomingPublicationAndItsDatasetsResources() throws WorkflowException, RepositoryException {
        // add Dataset to JCR
        Node dataset = getDocumentHandleWithVariant(getDataset(), PUBLISHED);
        Node publication = getDocumentHandleWithVariant(getUpcomingPublication(), UNPUBLISHED);
        String publicationResource = "ab/cdef12/lorem.pdf";

        MockJcr.addQueryResultHandler(session, searchResourcesHandle(getVariantPath(dataset), testExternalReference));
        MockJcr.addQueryResultHandler(session, searchResourcesHandle(getVariantPath(publication), publicationResource));
        MockJcr.addQueryResultHandler(session, searchDatasetsHandle(dataset.getNode(dataset.getName())));

        // when publishing upcoming publication
        executeTask(publication, externalFilePublishTask);

        // should be executed once for publication and once for dataset
        then(s3Connector).should(times(1)).unpublishResource(testExternalReference);
        then(s3Connector).should(times(1)).unpublishResource(publicationResource);
        then(s3Connector).should(never()).publishResource(anyString());
    }

    private String getVariantPath(Node node) throws RepositoryException {
        return node.getNode(node.getName()).getPath();
    }

    private MockQueryResultHandler searchDatasetsHandle(Node dataset) {
        return mockQuery -> {
            if (mockQuery.getStatement().matches("SELECT \\* FROM \\[publicationsystem:dataset].*\\[hippostd:state]\\s?=\\s?'published'")) {
                return new MockQueryResult(Arrays.asList(dataset));
            }
            return null;
        };
    }

    private MockQueryResultHandler searchResourcesHandle(final String nodePath, final String externalReference) {
        return mockQuery -> {
            String jcrSafeNodePath = org.apache.jackrabbit.util.Text.escapePath(nodePath);

            if (mockQuery.getStatement().matches("SELECT \\* FROM \\[externalstorage:resource].*ISDESCENDANTNODE \\(\\['" + jcrSafeNodePath + "']\\).*")) {
                return new MockQueryResult(getQueryResultsWithSingleExternalResource(externalReference));
            }
            return null;
        };
    }

    private Node getFinalisedPublication() throws RepositoryException {
        Node publicationVariant = rootNode.addNode("content", PublicationSystemConstants.NODE_TYPE_PUBLICATION);

        publicationVariant.setProperty(PublicationSystemConstants.PROPERTY_PUBLICLY_ACCESSIBLE, true);

        return publicationVariant;
    }

    private Node getUpcomingPublication() throws RepositoryException {
        Node publicationVariant = rootNode.addNode("content", PublicationSystemConstants.NODE_TYPE_PUBLICATION);

        publicationVariant.setProperty(PublicationSystemConstants.PROPERTY_PUBLICLY_ACCESSIBLE, false);

        return publicationVariant;
    }

    private Node getRandomDocumentType() throws RepositoryException {
        Node randomTypeVariant = rootNode.addNode("test-document", "lorem:ipsum");

        return randomTypeVariant;
    }

    private Node getDataset() throws RepositoryException {
        Node datasetVariant = rootNode.addNode("test-document", PublicationSystemConstants.NODE_TYPE_DATASET);

        return datasetVariant;
    }

    private void executeTask(Node document, ExternalFilePublishTask task) throws WorkflowException {
        DocumentHandle documentHandle = new DocumentHandle(document);
        documentHandle.initialize();
        task.setDocumentHandle(documentHandle);
        task.setS3Connector(s3Connector);
        task.setVariantState(UNPUBLISHED);

        task.execute();
    }

    private Node getDocumentHandleWithVariant(Node documentVariant, String state) throws RepositoryException {
        String name = documentVariant.getName();
        Node handle = testFolder.addNode(name, "hippo:handle");

        // create new variant
        Node variant = handle.addNode(name, documentVariant.getPrimaryNodeType().getName());
        variant.setProperty(HIPPOSTD_STATE, state);

        // copy properties to variant
        PropertyIterator properties = documentVariant.getProperties();
        for (Property prop; properties.hasNext();) {
            prop = properties.nextProperty();
            variant.setProperty(prop.getName(), prop.getValue());
        }

        return handle;
    }

    private List<Node> getQueryResultsWithSingleExternalResource(String reference) {
        List<Node> nodes = new ArrayList<>();
        Node resources;

        try {
            resources = rootNode.addNode("resources");
            Node one = resources.addNode("one");
            one.setProperty(ExternalStorageConstants.PROPERTY_EXTERNAL_STORAGE_REFERENCE, reference);
            nodes.add(one);
        } catch (RepositoryException repositoryEx) {
            throw new RuntimeException(repositoryEx);
        }

        return nodes;
    }
}
