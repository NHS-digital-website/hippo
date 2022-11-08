package uk.nhs.digital.arc.plugin;

import org.apache.wicket.model.IModel;
import org.hippoecm.addon.workflow.WorkflowDescriptorModel;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.skin.Icon;

public class ArcParsingWorkflow extends StdArcWorkflow {
    public ArcParsingWorkflow(String id, IModel<String> name,
                              IPluginContext pluginContext, WorkflowDescriptorModel workflowModel) {
        super(id, name, pluginContext, workflowModel);
        this.dialogTitle = "json-dialog-parsing-title";
        this.parsing = true;
        this.icon = Icon.FILE_TEXT;
    }
}
