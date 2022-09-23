package uk.nhs.digital.arc.plugin;

import org.apache.wicket.model.IModel;
import org.hippoecm.addon.workflow.WorkflowDescriptorModel;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.skin.Icon;

public class ArcDocumentWorkflow extends StdArcWorkflow {
    public ArcDocumentWorkflow(String id, IModel<String> name,
                               IPluginContext pluginContext, WorkflowDescriptorModel workflowModel) {
        super(id, name, pluginContext, workflowModel);
        this.dialogTitle = "json-dialog-title";
        this.icon = Icon.GEAR;
    }
}
