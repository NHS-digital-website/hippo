package uk.nhs.digital.customfield.postcodefield.resource;

import com.onehippo.cms7.eforms.cms.form.FormPlugin;
import com.onehippo.cms7.eforms.cms.model.AbstractFieldModel;
import com.onehippo.cms7.eforms.cms.properties.panels.AbstractFieldPropertiesPanel;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.PropertyModel;
import uk.nhs.digital.customfield.postcodefield.model.CustomPostcodeFieldModel;

import java.util.Arrays;
import java.util.List;
import javax.jcr.RepositoryException;

public class CustomPostcodeFieldPropertiesPanel extends AbstractFieldPropertiesPanel {

    private static final List<String> AUTOCOMPLETE_OPTIONS = Arrays.asList("off", "on", "postal-code");

    public CustomPostcodeFieldPropertiesPanel(final String id, final FormPlugin formPlugin,
                                              final AbstractFieldModel fieldModel) throws RepositoryException {
        super(id, formPlugin, fieldModel);

        // Add autocomplete dropdown
        CustomPostcodeFieldModel model = (CustomPostcodeFieldModel) fieldModel;
        DropDownChoice<String> autocompleteChoice = new DropDownChoice<>("autocomplete",
                new PropertyModel<>(model, "autocomplete"),
                AUTOCOMPLETE_OPTIONS);
        add(autocompleteChoice);
    }
}
