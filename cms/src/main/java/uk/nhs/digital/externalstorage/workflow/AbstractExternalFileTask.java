package uk.nhs.digital.externalstorage.workflow;

import org.hippoecm.frontend.session.UserSession;
import org.hippoecm.repository.api.WorkflowException;
import org.onehippo.cms7.services.HippoServiceRegistry;
import org.onehippo.cms7.services.eventbus.HippoEventBus;
import org.onehippo.repository.documentworkflow.DocumentVariant;
import org.onehippo.repository.documentworkflow.task.AbstractDocumentTask;
import org.onehippo.repository.events.HippoWorkflowEvent;
import uk.nhs.digital.JcrQueryHelper;
import uk.nhs.digital.externalstorage.ExternalStorageConstants;
import uk.nhs.digital.externalstorage.s3.PooledS3Connector;
import uk.nhs.digital.ps.PublicationSystemConstants;

import java.rmi.RemoteException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.QueryResult;

public abstract class AbstractExternalFileTask extends AbstractDocumentTask {

    protected String variantState;

    private PooledS3Connector s3Connector;

    public AbstractExternalFileTask() {
        super();
    }

    public void setS3Connector(PooledS3Connector s3Connector) {
        this.s3Connector = s3Connector;
    }

    public void setVariantState(String state) {
        variantState = state;
    }

    protected DocumentVariant getVariant() {
        return getDocumentHandle().getDocuments().get(variantState);
    }

    @Override
    public Object doExecute() throws WorkflowException, RepositoryException, RemoteException {

        if (getVariant() == null || !getVariant().hasNode()) {
            throw new WorkflowException("No variant provided");
        }

        Node variantNode = getVariant().getNode(getWorkflowContext().getInternalWorkflowSession());

        processResourceNodes(s3Connector, findResourceNodes(variantNode));

        return null;
    }

    protected NodeIterator findResourceNodes(Node node) throws RepositoryException {
        QueryResult res = JcrQueryHelper.findDescendantNodes(node, ExternalStorageConstants.NODE_TYPE_EXTERNAL_RESOURCE);

        return res.getNodes();
    }

    protected  NodeIterator findPublicationDatasetsVariant(Node node, String variant) throws RepositoryException {
        Node folder = node.getParent().getParent();

        QueryResult res = JcrQueryHelper.findDescendantVariants(folder, PublicationSystemConstants.NODE_TYPE_DATASET, variant);

        return res.getNodes();
    }

    protected abstract void processResourceNodes(PooledS3Connector s3, final NodeIterator resourceNodes) throws RepositoryException, WorkflowException;

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
