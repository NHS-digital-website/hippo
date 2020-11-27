package uk.nhs.digital.form;

import com.onehippo.cms7.eforms.cms.extensions.AbstractFormExtensionPlugin;
import com.onehippo.cms7.eforms.cms.util.JcrUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.service.IEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;

public class FormApiSettingsPlugin extends AbstractFormExtensionPlugin {

    private static final String PROP_API_TOPIC_ID = "eforms:topicId";
    private static final String PROP_API_ENABLED = "eforms:govdeliveryapi";
    private static final String PROP_API_SCRIPT_SERVICE_ENABLED = "eforms:govdeliveryScriptService";

    private static final Logger LOGGER = LoggerFactory.getLogger(FormApiSettingsPlugin.class);

    private final IEditor.Mode mode;
    private boolean apiEnabledForm;
    private boolean apiScriptServiceEnabled;
    private String topicId;

    public FormApiSettingsPlugin(IPluginContext context, IPluginConfig config) throws RepositoryException {
        super(context, config);

        mode = IEditor.Mode.fromString(config.getString("mode"));
        apiEnabledForm = isApiEnabledForm();
        apiScriptServiceEnabled = isApiScriptServiceEnabled();
        topicId = getTopicId();

        final CheckBox apiEnabledField = new CheckBox("enabled", Model.of(apiEnabledForm));
        apiEnabledField.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                try {
                    if (apiEnabledForm) {
                        getFormDocument().getNode().setProperty(PROP_API_ENABLED, "false");
                    } else {
                        getFormDocument().getNode().setProperty(PROP_API_ENABLED, "true");
                    }
                } catch (RepositoryException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        });

        final CheckBox apiScriptServiceEnabledField = new CheckBox("apiScriptServiceEnabled", Model.of(apiScriptServiceEnabled));
        apiScriptServiceEnabledField.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                try {
                    if (apiScriptServiceEnabled) {
                        getFormDocument().getNode().setProperty(PROP_API_SCRIPT_SERVICE_ENABLED, "false");
                    } else {
                        getFormDocument().getNode().setProperty(PROP_API_SCRIPT_SERVICE_ENABLED, "true");
                    }
                } catch (RepositoryException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        });

        final TextField<String> topicIdField = new TextField<>("topicId", new PropertyModel<>(this, "topicId"));
        topicIdField.setOutputMarkupId(true);
        topicIdField.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                try {
                    getFormDocument().getNode().setProperty(PROP_API_TOPIC_ID, topicId);
                } catch (RepositoryException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        });

        if (mode != IEditor.Mode.EDIT) {
            apiEnabledField.setEnabled(false);
            apiScriptServiceEnabledField.setEnabled(false);
            topicIdField.setEnabled(false);
        }

        add(apiEnabledField);
        add(apiScriptServiceEnabledField);
        add(topicIdField);
    }

    private boolean isApiScriptServiceEnabled() {
        return Boolean.parseBoolean(JcrUtils.getStringProperty(getFormDocument().getNode(), PROP_API_SCRIPT_SERVICE_ENABLED));
    }

    private boolean isApiEnabledForm() {
        return Boolean.parseBoolean(JcrUtils.getStringProperty(getFormDocument().getNode(), PROP_API_ENABLED));
    }

    private String getTopicId() {
        return JcrUtils.getStringProperty(getFormDocument().getNode(), PROP_API_TOPIC_ID);
    }

}
