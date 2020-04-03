package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.FieldGroup;
import org.hippoecm.hst.core.parameters.FieldGroupList;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.CommonComponent;
import uk.nhs.digital.common.components.info.CallToActionComponentInfo;

@FieldGroupList({
    @FieldGroup(
        titleKey = "Call to Action Document",
        value = {
            "document",
        }
    )
    })
@ParametersInfo(type = CallToActionComponentInfo.class)
public class CallToActionComponent extends CommonComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        CallToActionComponentInfo info = getComponentParametersInfo(request);
        HippoBean document = getHippoBeanForPath(info.getDocument(), HippoBean.class);
        request.setAttribute("document", document);
    }
}
