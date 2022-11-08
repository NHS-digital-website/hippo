package uk.nhs.digital.arc.plugin;

import org.apache.wicket.model.StringResourceModel;
import org.hippoecm.addon.workflow.WorkflowDescriptorModel;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.service.render.RenderPlugin;
import org.hippoecm.repository.api.WorkflowDescriptor;

/**
 * This class is used by the UI plugin to manage the report creation functionality.
 *
 * It will, add the 'Create report' menu item, manage the display of the preview dialog and initiate the creation process
 *
 * Although the class appears not to be referenced in the codebase, it is in fact referenced in the YAML files used
 * to register plugin components in this application
 */
public class ArcDocumentWorkflowPlugin extends RenderPlugin<WorkflowDescriptor> {
    private static final String CREATE_REPORT = "create_report";
    //protected String resourceModel = "create_report";

    public ArcDocumentWorkflowPlugin(IPluginContext context, IPluginConfig config) {
        super(context, config);

        StdArcWorkflow stdArcWorkflow = new ArcDocumentWorkflow(CREATE_REPORT, new StringResourceModel(CREATE_REPORT, this),
            this.getPluginContext(),
            (WorkflowDescriptorModel) getModel());
        add(stdArcWorkflow);
    }
}
