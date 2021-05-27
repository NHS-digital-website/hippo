package uk.nhs.digital.customfield;

import com.onehippo.cms7.eforms.hst.model.AbstractField;
import com.onehippo.cms7.eforms.hst.model.Form;
import com.onehippo.cms7.eforms.hst.validation.rules.DateFormatRule;
import uk.nhs.digital.website.beans.CustomDateFieldBean;


public class CustomDateField extends AbstractField {

    public CustomDateField(CustomDateFieldBean bean, Form form) {
        super(bean, form);
        setMandatory(bean.isMandatory());
    }

    public void createValidationRules() {
        super.createValidationRules();
        DateFormatRule rule = new DateFormatRule();
        rule.setRuleData("dd-MM-yyyy");
        this.addValidationRule(rule);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CustomDateField");
        return sb.toString();
    }

}

