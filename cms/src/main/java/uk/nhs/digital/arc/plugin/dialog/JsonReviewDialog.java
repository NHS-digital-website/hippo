package uk.nhs.digital.arc.plugin.dialog;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.value.IValueMap;
import org.hippoecm.addon.workflow.StdWorkflow;
import org.hippoecm.addon.workflow.WorkflowSNSException;
import org.hippoecm.frontend.dialog.Dialog;
import org.hippoecm.frontend.dialog.DialogConstants;
import org.hippoecm.repository.api.WorkflowException;
import org.onehippo.repository.documentworkflow.DocumentWorkflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.arc.process.ManifestProcessingSummary;

import javax.jcr.AccessDeniedException;

public class JsonReviewDialog extends Dialog<Void> {

    private static final Logger log = LoggerFactory.getLogger(JsonReviewDialog.class);

    private static final String PARSEERRORS_MESSAGE = "Errors found during parsing the manifest. Please take a look at the output below";
    private static final String ALLCLEAR_MESSAGE = "No errors found during parsing. Output below shows the manifest details that were parsed";
    private static final String RUNERRORS_MESSAGE = "Errors found during parsing or running the manifest. Please take a look at the output below";
    private static final String ALLRUN_MESSAGE = "No errors found during parsing or running. Output below shows the manifest details that were processed";

    private final StdWorkflow<DocumentWorkflow> invoker;
    private final String dialogTitle;

    public JsonReviewDialog(StdWorkflow<DocumentWorkflow> invoker, ManifestProcessingSummary summary, boolean parse, String dialogTitle) {
        this.invoker = invoker;
        this.dialogTitle = dialogTitle;

        TextArea<String> commentArea = new TextArea<>("output_area",Model.of(summary.getConcatenatedMessages()));
        commentArea.setOutputMarkupId(true);
        add(commentArea);

        Label msg = new Label("results_label", Model.of(getMessageForScenario(parse, summary.isInError())));
        msg.setOutputMarkupId(true);
        add(msg);

        setOkEnabled(false);
        setOkVisible(false);
        setCancelLabel(parse ? "Dismiss" : "Done");
    }

    private String getMessageForScenario(boolean parse, boolean isInError) {
        if (isInError) {
            if (parse) {
                return PARSEERRORS_MESSAGE;
            } else {
                return RUNERRORS_MESSAGE;
            }
        } else {
            if (parse) {
                return ALLCLEAR_MESSAGE;
            } else {
                return ALLRUN_MESSAGE;
            }
        }
    }

    @Override
    public IModel getTitle() {
        return new StringResourceModel(dialogTitle, this);
    }

    @Override
    public IValueMap getProperties() {
        return DialogConstants.LARGE_RELATIVE;
    }

    protected void onOk() {
        try {
            this.invoker.invokeWorkflow();
        } catch (WorkflowSNSException workflowSnsException) {
            log.warn("Could not execute workflow due to same-name-sibling issue: " + workflowSnsException.getMessage());
        } catch (WorkflowException workflowException) {
            log.warn("Could not execute workflow: " + workflowException.getMessage());
        } catch (AccessDeniedException accessDeniedException) {
            log.warn("Access denied: " + accessDeniedException.getMessage());
        } catch (Exception e) {
            log.error("Could not execute workflow.", e);
            this.error(e);
        }
    }
}