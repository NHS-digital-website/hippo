package uk.nhs.digital.arc.plugin;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.hippoecm.addon.workflow.StdWorkflow;
import org.hippoecm.addon.workflow.WorkflowDescriptorModel;
import org.hippoecm.frontend.dialog.IDialogService;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugins.standards.icon.HippoIcon;
import org.hippoecm.frontend.session.UserSession;
import org.hippoecm.frontend.skin.Icon;
import org.hippoecm.repository.api.HippoSession;
import org.hippoecm.repository.api.Workflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.arc.plugin.dialog.JsonReviewDialog;
import uk.nhs.digital.arc.plugin.util.DoctypeDetector;
import uk.nhs.digital.arc.process.ArcTaskManager;
import uk.nhs.digital.arc.process.ManifestProcessingSummary;
import uk.nhs.digital.arc.process.ManifestProcessor;
import uk.nhs.digital.arc.storage.ParsingStorageManager;
import uk.nhs.digital.arc.storage.S3StorageManager;

import java.io.IOException;
import javax.jcr.RepositoryException;

public abstract class StdArcWorkflow<T extends Workflow> extends StdWorkflow {
    private static final Logger LOGGER = LoggerFactory.getLogger(StdArcWorkflow.class);

    protected boolean parsing = false;
    protected String dialogTitle = null;
    protected Icon icon = null;

    public StdArcWorkflow(String id, IModel<String> name,
                          IPluginContext pluginContext, WorkflowDescriptorModel workflowModel) {
        super(id, name, pluginContext, workflowModel);
    }

    private String getNodePath() throws RepositoryException {
        return getModel().getNode().getPath();
    }

    @Override
    protected Component getIcon(String id) {
        return HippoIcon.fromSprite(id, icon);
    }

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
    protected IDialogService.Dialog createRequestDialog() {
        ManifestProcessingSummary manifestProcessingSummary = null;
        try {
            String manifestLocation = DoctypeDetector.getManifestLocationValue(getModel().getNode());

            manifestProcessingSummary = runProcessorInParsingModeOnly(manifestLocation, getNodePath());
            if (!parsing && !manifestProcessingSummary.isInError()) {
                manifestProcessingSummary = runProcessorInProcessingModeOnly(manifestLocation, getNodePath());
            }

            return new JsonReviewDialog(this, manifestProcessingSummary,
                parsing,
                dialogTitle);
        } catch (RepositoryException e) {
            LOGGER.error("Error trying to inspect the repository during parsing mode", e);
        }

        return null;
    }

    private ManifestProcessingSummary runProcessorInParsingModeOnly(String manifestPath, String nodePath) {
        return runProcessor(true, manifestPath, nodePath);
    }

    private ManifestProcessingSummary runProcessorInProcessingModeOnly(String manifestPath, String nodePath) {
        return runProcessor(false, manifestPath, nodePath);
    }

    private ManifestProcessingSummary runProcessor(boolean parsing, String manifestPath, String nodePath) {
        ManifestProcessingSummary responseMessages = null;

        try {
            HippoSession session = UserSession.get().getJcrSession();
            S3StorageManager storageManager = parsing ? new ParsingStorageManager() : new S3StorageManager();
            ArcTaskManager taskManager = new ArcTaskManager(session);

            ManifestProcessor processor = new ManifestProcessor(parsing, session, manifestPath, nodePath, storageManager, taskManager);

            responseMessages = processor.readWrapperFromFile();

            if (responseMessages.docbaseWasFound()) {
                storageManager.writeOutcomeFile(responseMessages.getDocbase(), responseMessages.getOutputFileLocation(), responseMessages.getExtendedConcatenatedMessages());
            }

        } catch (IOException e) {
            LOGGER.error("Error trying to read the manifest wrapper file '{}'", manifestPath, e);
        }

        return responseMessages;
    }
}
