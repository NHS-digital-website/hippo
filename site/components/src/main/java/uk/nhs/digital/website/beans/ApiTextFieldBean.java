package uk.nhs.digital.website.beans;

import com.onehippo.cms7.eforms.hst.beans.FieldType;
import com.onehippo.cms7.eforms.hst.beans.TextFieldBean;
import org.hippoecm.hst.content.beans.Node;

@Node(jcrType = "website:apiTextField")
public class ApiTextFieldBean extends TextFieldBean {

    public String getFieldId() {
        final String formId = getSingleProperty("website:fieldId");

        if (formId == null) {
            return "";
        }
        return formId;
    }

    @Override
    public FieldType getType() {
        return FieldType.TEXT;
    }

    @Override
    public String getFieldClass() {
        return "uk.nhs.digital.website.beans.ApiTextField";
    }
}
