package uk.nhs.digital.form;

import com.onehippo.cms7.eforms.cms.fieldextensions.AbstractFieldExtensionPlugin;
import com.onehippo.cms7.eforms.cms.fieldextensions.model.FieldExtensionModel;
import com.onehippo.cms7.eforms.cms.model.SingleValuePropertyModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;

public class FieldExtensionFieldId extends AbstractFieldExtensionPlugin {

    public FieldExtensionFieldId(final String id, final FieldExtensionModel extensionModel, final IPluginConfig config, final IPluginContext context) {
        super(id, extensionModel, config, context);
        final Label label = new Label("label", "Api Field ID");
        final TextField<String> textField = new TextField<>("field-Id", new SingleValuePropertyModel<String>(extensionModel.getNodeModel(), "field-Id"));
        textField.setEnabled(true);
        add(textField);
        add(label);
        setOutputMarkupId(true);
    }

}
