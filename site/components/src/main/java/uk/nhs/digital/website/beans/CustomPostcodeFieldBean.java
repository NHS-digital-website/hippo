package uk.nhs.digital.website.beans;

import com.onehippo.cms7.eforms.hst.beans.FieldType;
import com.onehippo.cms7.eforms.hst.beans.TextFieldBean;
import org.hippoecm.hst.content.beans.Node;
import uk.nhs.digital.customfield.CustomPostcodeField;

@Node(jcrType = "website:custompostcodefield")
public class CustomPostcodeFieldBean extends TextFieldBean {

    @Override
    public String getFieldClass() {
        return CustomPostcodeField.class.getName();
    }

    @Override
    public FieldType getType() {
        return FieldType.TEXT;
    }

    @Override
    public String getAutocomplete() {
        return getSingleProperty("autocomplete");
    }
}
