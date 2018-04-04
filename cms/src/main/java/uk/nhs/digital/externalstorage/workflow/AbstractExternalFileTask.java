package uk.nhs.digital.externalstorage.workflow;

import org.hippoecm.frontend.session.UserSession;
import org.hippoecm.repository.api.WorkflowException;
import org.onehippo.cms7.services.HippoServiceRegistry;
import org.onehippo.cms7.services.eventbus.HippoEventBus;
import org.onehippo.repository.documentworkflow.DocumentVariant;
import org.onehippo.repository.documentworkflow.task.AbstractDocumentTask;
import org.onehippo.repository.events.HippoWorkflowEvent;
import uk.nhs.digital.externalstorage.ExternalStorageConstants;
import uk.nhs.digital.externalstorage.s3.S3Connector;

import java.rmi.RemoteException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;

public abstract class AbstractExternalFileTask extends AbstractDocumentTask {

    private String variantState;

    private S3Connector s3Connector;

    public AbstractExternalFileTask() {
        super();
    }

    public void setS3Connector(S3Connector s3Connector) {
        this.s3Connector = s3Connector;
    }

    public void setVariantState(String state) {
        variantState = state;
    }

    private DocumentVariant getVariant() {
        return getDocumentHandle().getDocuments().get(variantState);
    }

    @Override
    public Object doExecute() throws WorkflowException, RepositoryException, RemoteException {

        if (getVariant() == null || !getVariant().hasNode()) {
            throw new WorkflowException("No variant provided");
        }

        Node variantNode = getVariant().getNode(getWorkflowContext().getInternalWorkflowSession());

        for (NodeIterator i = findResourceNodes(variantNode); i.hasNext(); ) {
            Node doc = i.nextNode();
            if (doc.hasProperty(ExternalStorageConstants.PROPERTY_EXTERNAL_STORAGE_REFERENCE)) {
                String externalResource = doc
                    .getProperty(ExternalStorageConstants.PROPERTY_EXTERNAL_STORAGE_REFERENCE)
                    .getString();
                setTargetStatus(s3Connector, externalResource);
            }
        }

        return null;
    }

    private NodeIterator findResourceNodes(Node node) throws RepositoryException {
        String documentPath = node.getPath();

        //variantNode.
        String query = "SELECT * FROM [" + ExternalStorageConstants.NODE_TYPE_EXTERNAL_RESOURCE + "] "
            + "WHERE ISDESCENDANTNODE (['" + documentPath + "'])";

        QueryResult res = node.getSession()
            .getWorkspace()
            .getQueryManager()
            .createQuery(query, Query.JCR_SQL2)
            .execute();

        return res.getNodes();
    }

    protected abstract void setTargetStatus(S3Connector s3, final String objectKey);

    public void logInCmsActivityStream(final String documentPath, final String message) {
        final HippoEventBus eventBus = HippoServiceRegistry.getService(HippoEventBus.class);
        final String currentUser = UserSession.get().getJcrSession().getUserID();

        if (eventBus != null) {
            eventBus.post(new HippoWorkflowEvent()
                .className(AbstractExternalFileTask.class.getName())
                .methodName("errorOnPublish")
                .application("cms")
                .timestamp(System.currentTimeMillis())
                .user(currentUser)
                .set("documentPath", documentPath)
                .message(message)
            );
        }
    }
}
