package uk.nhs.digital.common.components;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.component.support.forms.FormMap;
import org.hippoecm.hst.component.support.forms.FormUtils;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.essentials.components.CommonComponent;

import java.util.Arrays;
import java.util.List;

public class CovidContactFormComponent extends CommonComponent {

    private static final String POSTCODE = "postcode";
    private static final String AGE = "age";
    private static final String NAME = "name";
    private static final String CONTACT_INFO = "contact";
    private static final List<String> FORM_FIELDS = Arrays
        .asList(POSTCODE, AGE, NAME, CONTACT_INFO);

    @Override
    public void doAction(HstRequest request, HstResponse response)
        throws HstComponentException {

        FormMap map = new FormMap(request, FORM_FIELDS);

        String postcode = map.getField(POSTCODE).getValue();

        if (StringUtils.isBlank(postcode)) {
            FormUtils.persistFormMap(request, response, map, null);
        }

        // POST the values
    }
}
