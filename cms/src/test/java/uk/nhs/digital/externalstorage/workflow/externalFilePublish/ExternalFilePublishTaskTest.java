package uk.nhs.digital.externalstorage.workflow.externalFilePublish;

import static org.hippoecm.repository.HippoStdNodeType.UNPUBLISHED;
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
import uk.nhs.digital.externalstorage.s3.SchedulingS3Connector;

import java.util.ArrayList;
import java.util.List;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class ExternalFilePublishTaskTest {

    @Mock private SchedulingS3Connector s3Connector;
    @Mock private WorkflowContext workflowContext;

    private ExternalFilePublishTask externalFilePublishTask;
    private Session session;

    private Node rootNode;

    @Before
    public void setUp() throws RepositoryException {
        initMocks(this);
        given(s3Connector.publishResource(any(String.class)))
            .willReturn(true);

        Repository repository = MockJcr.newRepository();
        session = repository.login();
        rootNode = session.getRootNode();
        externalFilePublishTask = new ExternalFilePublishTask();

        given(workflowContext.getInternalWorkflowSession()).willReturn(session);
        externalFilePublishTask.setWorkflowContext(workflowContext);
    }

    @Test
    public void shouldCallS3() throws WorkflowException, RepositoryException {
        Node docNode = rootNode.addNode("test-document");
        addVariantNode(docNode, UNPUBLISHED);
        MockJcr.setQueryResult(session, getQueryResults());

        executeTask(docNode, externalFilePublishTask);

        then(s3Connector).should().publishResource("the/one");
    }

    private void executeTask(Node document, ExternalFilePublishTask task) throws WorkflowException {
        DocumentHandle documentHandle = new DocumentHandle(document);
        documentHandle.initialize();
        task.setDocumentHandle(documentHandle);
        task.setS3Connector(s3Connector);
        task.setVariantState(UNPUBLISHED);

        task.execute();
    }

    private Node addVariantNode(Node documentNode, String state) throws RepositoryException {
        String name = documentNode.getName();
        Node document = documentNode.addNode(name);
        document.setProperty("hippostd:state", state);

        return document;
    }

    private List<Node> getQueryResults() throws RepositoryException {
        List<Node> nodes = new ArrayList<>();

        Node node = rootNode
            .addNode("resources")
            .addNode("one");
        node.setProperty(ExternalStorageConstants.PROPERTY_EXTERNAL_STORAGE_REFERENCE, "the/one");

        nodes.add(node);

        return nodes;
    }
}
