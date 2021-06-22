package uk.nhs.digital.customfield.postcodefield.model;

import com.onehippo.cms7.eforms.cms.model.AbstractFieldModel;
import org.hippoecm.frontend.model.JcrNodeModel;

import javax.jcr.RepositoryException;

public class CustomPostcodeFieldModel extends AbstractFieldModel {

    public CustomPostcodeFieldModel(JcrNodeModel nodeModel) throws RepositoryException {
        super(nodeModel);
    }

    @Override
    public String getFieldType() {
        return "custompostcodefield";
    }


}
