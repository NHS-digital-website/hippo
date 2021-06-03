package uk.nhs.digital.website.beans;

import com.onehippo.cms7.eforms.hst.beans.AbstractFieldBean;
import com.onehippo.cms7.eforms.hst.beans.FieldType;
import org.hippoecm.hst.content.beans.Node;
import uk.nhs.digital.customfield.CustomPostcodeField;

@Node(jcrType = "website:custompostcodefield")
public class CustomPostcodeFieldBean extends AbstractFieldBean {

    @Override
    public String getFieldClass() {
        return CustomPostcodeField.class.getName();
    }

    @Override
    public FieldType getType() {
        return FieldType.TEXT;
    }
}
