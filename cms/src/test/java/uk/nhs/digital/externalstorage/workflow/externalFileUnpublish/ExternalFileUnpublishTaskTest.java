package uk.nhs.digital.externalstorage.workflow.externalFileUnpublish;

import static org.hippoecm.repository.HippoStdNodeType.PUBLISHED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.initMocks;

import org.apache.sling.testing.mock.jcr.MockJcr;
import org.hippoecm.repository.api.WorkflowContext;
import org.hippoecm.repository.api.WorkflowException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.onehippo.repository.documentworkflow.DocumentHandle;
import uk.nhs.digital.externalstorage.ExternalStorageConstants;
import uk.nhs.digital.externalstorage.s3.S3Connector;

import java.util.ArrayList;
import java.util.List;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class ExternalFileUnpublishTaskTest {

    @Mock private S3Connector s3Connector;
    @Mock private WorkflowContext workflowContext;

    private ExternalFileUnpublishTask externalFileUnpublishTask;
    private Session session;

    private Node rootNode;

    @Before
    public void setUp() throws RepositoryException {
        initMocks(this);
        given(s3Connector.unpublishResource(any(String.class)))
            .willReturn(true);

        Repository repository = MockJcr.newRepository();
        session = repository.login();
        rootNode = session.getRootNode();
        externalFileUnpublishTask = new ExternalFileUnpublishTask();
        externalFileUnpublishTask.setS3Connector(s3Connector);

        given(workflowContext.getInternalWorkflowSession()).willReturn(session);
        externalFileUnpublishTask.setWorkflowContext(workflowContext);
    }

    @Test
    public void shouldCallS3() throws WorkflowException, RepositoryException {
        Node docNode = rootNode.addNode("test-document");
        addSubDocument(docNode, PUBLISHED);
        MockJcr.setQueryResult(session, getQueryResults());

        executeTask(docNode, externalFileUnpublishTask);

        then(s3Connector).should().unpublishResource("the/one");
    }

    private void executeTask(Node document, ExternalFileUnpublishTask task) throws WorkflowException {
        DocumentHandle documentHandle = new DocumentHandle(document);
        documentHandle.initialize();
        task.setDocumentHandle(documentHandle);
        task.setS3Connector(s3Connector);
        task.setVariantState(PUBLISHED);

        task.execute();
    }

    private Node addSubDocument(Node documentNode, String state) throws RepositoryException {
        String name = documentNode.getName();
        Node document = documentNode.addNode(name);
        document.setProperty("hippostd:state", state);

        return document;
    }

    private List<Node> getQueryResults() throws RepositoryException {
        List<Node> nodes = new ArrayList<>();

        Node r = rootNode.addNode("resources");

        Node o = r.addNode("one");
        o.setProperty(ExternalStorageConstants.PROPERTY_EXTERNAL_STORAGE_REFERENCE, "the/one");
        nodes.add(o);

        return nodes;
    }
}
