package uk.nhs.digital.arc.process;

import org.onehippo.forge.content.exim.core.DocumentManager;
import org.onehippo.forge.content.exim.core.impl.WorkflowDocumentVariantImportTask;
import org.onehippo.forge.content.pojo.binder.ContentNodeBinder;
import org.onehippo.forge.content.pojo.model.ContentItem;

import javax.jcr.Node;
import javax.jcr.Value;

public class ArcWorkflowDocumentVariantImportTask extends WorkflowDocumentVariantImportTask {

    public ArcWorkflowDocumentVariantImportTask(DocumentManager documentManager) {
        super(documentManager);
    }

    @Override
    public ContentNodeBinder<Node, ContentItem, Value> getContentNodeBinder() {
        if (this.contentNodeBinder == null) {
            this.contentNodeBinder = new ArcJcrContentNodeBinder();
        }

        return this.contentNodeBinder;
    }
}
