package uk.nhs.digital.arc.plugin;

import org.apache.wicket.model.StringResourceModel;
import org.hippoecm.addon.workflow.StdWorkflow;
import org.hippoecm.addon.workflow.WorkflowDescriptorModel;
import org.hippoecm.frontend.dialog.IDialogService;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.service.render.RenderPlugin;
import org.hippoecm.frontend.session.UserSession;
import org.hippoecm.repository.api.WorkflowDescriptor;
import org.onehippo.cms7.services.HippoServiceRegistry;
import org.onehippo.cms7.services.eventbus.HippoEventBus;
import org.onehippo.repository.documentworkflow.DocumentWorkflow;
import org.onehippo.repository.events.HippoWorkflowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.arc.plugin.dialog.JsonReviewDialog;
import uk.nhs.digital.arc.plugin.util.DoctypeDetector;
import uk.nhs.digital.arc.process.ManifestProcessingSummary;
import uk.nhs.digital.arc.process.ManifestProcessor;
import uk.nhs.digital.externalstorage.workflow.AbstractExternalFileTask;

import java.io.IOException;
import javax.jcr.RepositoryException;

/**
 * This class is used by the UI plugin to manage the report creation functionality.
 *
 * It will, add the 'Create report' menu item, manage the display of the preview dialog and initiate the creation process
 *
 * Although the class appears not to be referenced in the codebase, it is in fact referenced in the YAML files used
 * to register plugin components in this application
 */
public class ArcDocumentWorkflowPlugin extends RenderPlugin<WorkflowDescriptor> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArcDocumentWorkflowPlugin.class);

    private boolean previewInError = false;

    public ArcDocumentWorkflowPlugin(IPluginContext context, IPluginConfig config) {
        super(context, config);

        add(new StdWorkflow<DocumentWorkflow>("create_report",
            new StringResourceModel("create_report", this),
            this.getPluginContext(),
            (WorkflowDescriptorModel) getModel()) {

            @Override
            public String getSubMenu() {
                //* Check path to figure out if this is a Publication doc type
                try {
                    if (DoctypeDetector.isContentPublication(getModel().getNode())) {
                        return "document";
                    }
                } catch (RepositoryException e) {
                    LOGGER.error("Error trying to determine document type", e);
                }

                return super.getSubMenu();
            }

            @Override
            protected String execute(DocumentWorkflow workflow) throws Exception {
                if (!previewInError) {
                    publishEvent();
                }

                return null;
            }

            private String getNodePath() throws RepositoryException {
                return getModel().getNode().getPath();
            }

            @Override
            protected IDialogService.Dialog createRequestDialog() {
                ManifestProcessingSummary manifestProcessingSummary = null;
                try {
                    String manifestLocation = DoctypeDetector.getManifestLocationValue(getModel().getNode());

                    manifestProcessingSummary = runProcessorInReviewMode(manifestLocation, getNodePath());
                    previewInError = manifestProcessingSummary.isInError();

                    return new JsonReviewDialog(this, manifestProcessingSummary.getConcatenatedMessages(), manifestProcessingSummary.isInError());
                } catch (RepositoryException e) {
                    LOGGER.error("Error trying to inspect the repository during preview mode", e);
                }

                return null;
            }

            /**
             * Initiate the event that will service this request
             */
            private void publishEvent() {
                final HippoEventBus eventBus = HippoServiceRegistry.getService(HippoEventBus.class);
                final String currentUser = UserSession.get().getJcrSession().getUserID();

                try {
                    if (eventBus != null) {
                        final String manifestLocation = DoctypeDetector.getManifestLocationValue(getModel().getNode());

                        LOGGER.debug("Now posting event for ARC create for manifest {}", manifestLocation);
                        eventBus.post(new HippoWorkflowEvent()
                            .className(AbstractExternalFileTask.class.getName())
                            .application("arc")
                            .timestamp(System.currentTimeMillis())
                            .user(currentUser)
                            .set("manifest_file", manifestLocation)
                            .set("node_path", getNodePath())
                            .set("session_user", currentUser)
                            .set("methodName", "arc_create")
                        );

                        LOGGER.debug("Event now posted for manifest {}", manifestLocation);
                    }
                } catch (RepositoryException rex) {
                    LOGGER.error("Error trying to locate the content node during event pushing", rex);
                }
            }

            private ManifestProcessingSummary runProcessorInReviewMode(String manifestPath, String nodePath) {
                ManifestProcessingSummary responseMessages = null;

                try {
                    ManifestProcessor processor = new ManifestProcessor(null, manifestPath, nodePath);
                    responseMessages = processor.readWrapperFromFile();
                } catch (IOException e) {
                    LOGGER.error("Error trying to read the manifest wrapper file", e);
                }

                return responseMessages;
            }
        });
    }
}
