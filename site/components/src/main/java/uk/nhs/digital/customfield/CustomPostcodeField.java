package uk.nhs.digital.customfield;

import com.onehippo.cms7.eforms.hst.model.AbstractField;
import com.onehippo.cms7.eforms.hst.model.Form;
import uk.nhs.digital.customfield.validation.PostcodeRule;
import uk.nhs.digital.website.beans.CustomPostcodeFieldBean;


public class CustomPostcodeField extends AbstractField {

    public CustomPostcodeField(CustomPostcodeFieldBean bean, Form form) {
        super(bean, form);
        setMandatory(bean.isMandatory());
    }

    public void createValidationRules() {
        super.createValidationRules();
        PostcodeRule rule = new PostcodeRule();
        rule.setFieldType("postcodefield");
        this.addValidationRule(rule);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CustomField");
        return sb.toString();
    }

}

