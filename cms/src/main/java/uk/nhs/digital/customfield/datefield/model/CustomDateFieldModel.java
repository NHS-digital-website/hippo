package uk.nhs.digital.customfield.datefield.model;

import com.onehippo.cms7.eforms.cms.model.AbstractFieldModel;
import org.hippoecm.frontend.model.JcrNodeModel;

import javax.jcr.RepositoryException;

public class CustomDateFieldModel extends AbstractFieldModel {

    public CustomDateFieldModel(JcrNodeModel nodeModel) throws RepositoryException {
        super(nodeModel);
    }

    @Override
    public String getFieldType() {
        return "customdatefield";
    }


}
