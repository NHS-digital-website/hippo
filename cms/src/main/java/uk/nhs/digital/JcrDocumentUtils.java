package uk.nhs.digital;

import static uk.nhs.digital.JcrNodeUtils.validateIsOfTypeHandle;
import static uk.nhs.digital.toolbox.exception.ExceptionUtils.wrapCheckedException;

import org.hippoecm.repository.util.JcrUtils;
import org.hippoecm.repository.util.WorkflowUtils;
import org.onehippo.forge.content.exim.core.DocumentManager;
import org.onehippo.forge.content.exim.core.impl.WorkflowDocumentManagerImpl;
import org.onehippo.repository.documentworkflow.DocumentWorkflow;

import javax.jcr.Node;
import javax.jcr.Session;

public abstract class JcrDocumentUtils {

    private static final String WORKFLOW_CATEGORY_DEFAULT = "default";

    public static void publish(final Node documentHandleNode) {
        try {
            validateIsOfTypeHandle(documentHandleNode);

            final DocumentWorkflow workflow = WorkflowUtils
                .getWorkflow(documentHandleNode, WORKFLOW_CATEGORY_DEFAULT, DocumentWorkflow.class)
                .orElseThrow(() -> new RuntimeException(
                    "Could not find workflow of category " + WORKFLOW_CATEGORY_DEFAULT + " implementing " + DocumentWorkflow.class.getName()
                ));

            workflow.publish();

        } catch (final Exception e) {
            throw new RuntimeException("Failed to publish document " + JcrUtils.getNodePathQuietly(documentHandleNode), e);
        }
    }

    public static void saveQuietly(final Session session) {
        wrapCheckedException(session::save);
    }

    public static DocumentManager documentManagerFor(final Session session) {
        return new WorkflowDocumentManagerImpl(session);
    }
}
