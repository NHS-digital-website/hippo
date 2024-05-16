package uk.nhs.digital.arc.plugin;

import org.apache.wicket.model.StringResourceModel;
import org.hippoecm.addon.workflow.WorkflowDescriptorModel;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.service.render.RenderPlugin;
import org.hippoecm.repository.api.WorkflowDescriptor;

/**
 * This class is used by the UI plugin to manage the initiation of parsing of a report
 *
 * It will, add the 'Parse report' menu item, manage the display of the preview dialog and initiate the creation process
 *
 * Although the class appears not to be referenced in the codebase, it is in fact referenced in the YAML files used
 * to register plugin components in this application
 */
public class ArcParsingWorkflowPlugin extends RenderPlugin<WorkflowDescriptor> {
    private static String PARSING_REPORT = "parsing_report";

    public ArcParsingWorkflowPlugin(IPluginContext context, IPluginConfig config) {
        super(context, config);

        StdArcWorkflow stdArcWorkflow = new ArcParsingWorkflow(PARSING_REPORT, new StringResourceModel(PARSING_REPORT, this),
            this.getPluginContext(),
            (WorkflowDescriptorModel) getModel());
        add(stdArcWorkflow);
    }
}
