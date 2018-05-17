package uk.nhs.digital.externalstorage.workflow.externalFileCopy;

import org.apache.commons.scxml2.SCXMLExpressionException;
import org.apache.commons.scxml2.model.ModelException;
import org.onehippo.cms7.services.HippoServiceRegistry;
import org.onehippo.repository.documentworkflow.action.AbstractDocumentTaskAction;
import uk.nhs.digital.externalstorage.s3.PooledS3Connector;

public class ExternalFileCopyAction extends AbstractDocumentTaskAction<ExternalFileCopyTask> {

    @Override
    protected ExternalFileCopyTask createWorkflowTask() {
        return new ExternalFileCopyTask();
    }

    public String getNewNameExpr() {
        return getParameter("newNameExpr");
    }

    @SuppressWarnings("unused")
    public void setNewNameExpr(String newNameExpr) {
        setParameter("newNameExpr", newNameExpr);
    }

    public String getDestinationExpr() {
        return getParameter("destinationExpr");
    }

    @SuppressWarnings("unused")
    public void setDestinationExpr(String destinationExpr) {
        setParameter("destinationExpr", destinationExpr);
    }

    @Override
    protected void initTask(final ExternalFileCopyTask task) throws ModelException, SCXMLExpressionException {
        super.initTask(task);

        task.setTargetFolder(eval(getDestinationExpr()));
        task.setCopiedDocumentName(eval(getNewNameExpr()));

        task.setS3Connector(HippoServiceRegistry.getService(PooledS3Connector.class));
    }
}
