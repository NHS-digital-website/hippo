package uk.nhs.digital.arc.process;

import org.onehippo.forge.content.exim.core.DocumentManager;
import org.onehippo.forge.content.exim.core.impl.WorkflowDocumentManagerImpl;
import org.onehippo.forge.content.exim.core.impl.WorkflowDocumentVariantImportTask;

import javax.jcr.Session;

/**
 * Looks after task management for documents
 *
 */
public class ArcTaskManager {
    private final Session session;

    public ArcTaskManager(Session session) {
        this.session = session;
    }

    public WorkflowDocumentVariantImportTask getImportTask() {
        final DocumentManager documentManager = new WorkflowDocumentManagerImpl(session);
        final WorkflowDocumentVariantImportTask importTask = new WorkflowDocumentVariantImportTask(documentManager);

        return importTask;
    }
}
