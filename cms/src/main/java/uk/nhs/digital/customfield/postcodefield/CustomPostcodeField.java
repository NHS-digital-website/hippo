package uk.nhs.digital.customfield.postcodefield;

import com.onehippo.cms7.eforms.cms.fields.Field;
import com.onehippo.cms7.eforms.cms.model.AbstractFieldModel;
import com.onehippo.cms7.eforms.cms.panels.AbstractFieldPanel;
import com.onehippo.cms7.eforms.cms.properties.panels.AbstractFieldPropertiesPanel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import uk.nhs.digital.customfield.postcodefield.model.CustomPostcodeFieldModel;
import uk.nhs.digital.customfield.postcodefield.resource.CustomPostcodeFieldPanel;
import uk.nhs.digital.customfield.postcodefield.resource.CustomPostcodeFieldPropertiesPanel;


public class CustomPostcodeField implements Field {

    @Override
    public String getId() {
        return "custompostcodefield";
    }

    @Override
    public String getName() {
        return "Postcode Field";
    }

    @Override
    public String getNodeName() {
        return "website:custompostcodefield";
    }

    @Override
    public ResourceReference getIcon() {
        return new PackageResourceReference(CustomPostcodeField.class, "postcodefield.png");
    }

    @Override
    public Class<? extends AbstractFieldModel> getModel() {
        return CustomPostcodeFieldModel.class;
    }

    @Override
    public Class<? extends AbstractFieldPanel> getPanel() {
        return CustomPostcodeFieldPanel.class;
    }

    @Override
    public Class<? extends AbstractFieldPropertiesPanel> getPropertiesPanel() {

        return CustomPostcodeFieldPropertiesPanel.class;
    }
}
