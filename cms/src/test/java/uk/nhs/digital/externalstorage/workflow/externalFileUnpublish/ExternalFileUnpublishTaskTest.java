package uk.nhs.digital.externalstorage.workflow.externalFileUnpublish;

import static org.hippoecm.repository.HippoStdNodeType.HIPPOSTD_STATE;
import static org.hippoecm.repository.HippoStdNodeType.NT_FOLDER;
import static org.hippoecm.repository.HippoStdNodeType.PUBLISHED;
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

public class ExternalFileUnpublishTaskTest {

    @Mock private PooledS3Connector s3Connector;
    @Mock private WorkflowContext workflowContext;

    private ExternalFileUnpublishTask externalFileUnpublishTask;
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
        externalFileUnpublishTask = new ExternalFileUnpublishTask();

        given(workflowContext.getInternalWorkflowSession()).willReturn(session);
        externalFileUnpublishTask.setWorkflowContext(workflowContext);
    }

    @Test
    public void shouldUnpublishAnyDocumentResources() throws WorkflowException, RepositoryException {
        Node document = getDocumentHandleWithVariant(getRandomDocumentType(), PUBLISHED);
        MockJcr.addQueryResultHandler(session, searchResourcesHandle(getVariantPath(document), testExternalReference));

        executeTask(document, externalFileUnpublishTask);

        then(s3Connector).should().unpublishResource(testExternalReference);
        then(s3Connector).should(never()).publishResource(anyString());
    }

    @Test
    public void shouldUnpublishPublicationAndItsDatasetsResources() throws WorkflowException, RepositoryException {
        // add Dataset to JCR
        Node dataset = getDocumentHandleWithVariant(getDataset(), PUBLISHED);
        Node publication = getDocumentHandleWithVariant(getPublication(), PUBLISHED);
        String publicationResource = "ab/cdef12/publication-resource.pdf";

        MockJcr.addQueryResultHandler(session, searchResourcesHandle(getVariantPath(dataset), testExternalReference));
        MockJcr.addQueryResultHandler(session, searchResourcesHandle(getVariantPath(publication), publicationResource));

        MockJcr.addQueryResultHandler(session, searchDatasetsHandle(dataset.getNode(dataset.getName())));

        // when unpublishing upcoming publication
        executeTask(publication, externalFileUnpublishTask);

        // should be executed once for publication resource and once for dataset resource
        then(s3Connector).should(times(1)).unpublishResource(testExternalReference);
        then(s3Connector).should(times(1)).unpublishResource(publicationResource);
        then(s3Connector).should(never()).publishResource(anyString());
    }

    private String getVariantPath(Node node) throws RepositoryException {
        return node.getNode(node.getName()).getPath();
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

    private MockQueryResultHandler searchDatasetsHandle(Node dataset) {
        return mockQuery -> {
            if (mockQuery.getStatement().matches("SELECT \\* FROM \\[publicationsystem:dataset].*\\[hippostd:state]\\s*=\\s*'published'")) {
                return new MockQueryResult(Arrays.asList(dataset));
            }
            return null;
        };
    }

    private void executeTask(Node document, ExternalFileUnpublishTask task) throws WorkflowException {
        DocumentHandle documentHandle = new DocumentHandle(document);
        documentHandle.initialize();
        task.setDocumentHandle(documentHandle);
        task.setS3Connector(s3Connector);
        task.setVariantState(PUBLISHED);

        task.execute();
    }

    private Node addVariantNode(Node documentNode, String state) throws RepositoryException {
        String name = documentNode.getName();
        Node document = documentNode.addNode(name);
        document.setProperty("hippostd:state", state);

        return document;
    }

    private Node getPublication() throws RepositoryException {
        return rootNode.addNode("content", PublicationSystemConstants.NODE_TYPE_PUBLICATION);
    }

    private Node getRandomDocumentType() throws RepositoryException {
        return rootNode.addNode("test-document", "lorem:ipsum");
    }

    private Node getDataset() throws RepositoryException {
        return rootNode.addNode("test-document", PublicationSystemConstants.NODE_TYPE_DATASET);
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
