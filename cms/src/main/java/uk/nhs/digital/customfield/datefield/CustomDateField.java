package uk.nhs.digital.customfield.datefield;

import com.onehippo.cms7.eforms.cms.fields.Field;
import com.onehippo.cms7.eforms.cms.model.AbstractFieldModel;
import com.onehippo.cms7.eforms.cms.panels.AbstractFieldPanel;
import com.onehippo.cms7.eforms.cms.properties.panels.AbstractFieldPropertiesPanel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import uk.nhs.digital.customfield.datefield.model.CustomDateFieldModel;
import uk.nhs.digital.customfield.datefield.resource.CustomDateFieldPanel;
import uk.nhs.digital.customfield.datefield.resource.CustomDateFieldPropertiesPanel;


public class CustomDateField implements Field {

    @Override
    public String getId() {
        return "customdatefield";
    }

    @Override
    public String getName() {
        return "Custom Date Field";
    }

    @Override
    public String getNodeName() {
        return "website:customdatefield";
    }

    @Override
    public ResourceReference getIcon() {
        return new PackageResourceReference(CustomDateField.class, "datefield.png");
    }

    @Override
    public Class<? extends AbstractFieldModel> getModel() {
        return CustomDateFieldModel.class;
    }

    @Override
    public Class<? extends AbstractFieldPanel> getPanel() {
        return CustomDateFieldPanel.class;
    }

    @Override
    public Class<? extends AbstractFieldPropertiesPanel> getPropertiesPanel() {

        return CustomDateFieldPropertiesPanel.class;
    }
}
