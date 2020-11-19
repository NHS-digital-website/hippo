package uk.nhs.digital.website.beans;

import com.onehippo.cms7.eforms.hst.model.Form;
import com.onehippo.cms7.eforms.hst.model.TextField;

public class ApiTextField extends TextField {

    private String fieldId;

    public ApiTextField(ApiTextFieldBean bean, Form form) {
        super(bean, form);
        this.fieldId = bean.getFieldId();
    }

    public String getFieldId() {
        return fieldId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ApiTextField");
        sb.append("{fieldId=").append(fieldId);
        sb.append('}');
        return sb.toString();
    }
}
