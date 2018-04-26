package uk.nhs.digital.externalstorage.workflow.externalFileUnpublish;

import org.apache.commons.scxml2.SCXMLExpressionException;
import org.apache.commons.scxml2.model.ModelException;
import org.hippoecm.repository.HippoStdNodeType;
import org.onehippo.cms7.services.HippoServiceRegistry;
import org.onehippo.repository.documentworkflow.action.AbstractDocumentTaskAction;
import uk.nhs.digital.externalstorage.s3.PooledS3Connector;

public class ExternalFileUnpublishAction extends AbstractDocumentTaskAction<ExternalFileUnpublishTask> {

    @Override
    protected ExternalFileUnpublishTask createWorkflowTask() {
        return new ExternalFileUnpublishTask();
    }

    @Override
    protected void initTask(ExternalFileUnpublishTask task) throws ModelException, SCXMLExpressionException {
        super.initTask(task);
        task.setS3Connector(HippoServiceRegistry.getService(PooledS3Connector.class));
        task.setVariantState(HippoStdNodeType.PUBLISHED);
    }
}
