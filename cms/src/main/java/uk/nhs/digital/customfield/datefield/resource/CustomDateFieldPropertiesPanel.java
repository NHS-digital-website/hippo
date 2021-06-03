package uk.nhs.digital.customfield.datefield.resource;

import com.onehippo.cms7.eforms.cms.form.FormPlugin;
import com.onehippo.cms7.eforms.cms.model.AbstractFieldModel;
import com.onehippo.cms7.eforms.cms.properties.panels.AbstractFieldPropertiesPanel;

import javax.jcr.RepositoryException;

public class CustomDateFieldPropertiesPanel extends AbstractFieldPropertiesPanel {

    public CustomDateFieldPropertiesPanel(final String id, final FormPlugin formPlugin,
                                          final AbstractFieldModel fieldModel) throws RepositoryException {
        super(id, formPlugin, fieldModel);
    }
}

