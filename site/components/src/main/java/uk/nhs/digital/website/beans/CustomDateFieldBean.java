package uk.nhs.digital.website.beans;

import com.onehippo.cms7.eforms.hst.beans.AbstractFieldBean;
import com.onehippo.cms7.eforms.hst.beans.FieldType;
import org.hippoecm.hst.content.beans.Node;
import uk.nhs.digital.customfield.CustomDateField;

@Node(jcrType = "website:customdatefield")
public class CustomDateFieldBean extends AbstractFieldBean {

    @Override
    public String getFieldClass() {
        return CustomDateField.class.getName();
    }

    @Override
    public FieldType getType() {
        return FieldType.TEXT;
    }
}
