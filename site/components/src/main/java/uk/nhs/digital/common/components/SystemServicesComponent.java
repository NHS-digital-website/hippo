package uk.nhs.digital.common.components;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsListComponentInfo;
import uk.nhs.digital.website.beans.VisualHub;

@ParametersInfo(type = EssentialsListComponentInfo.class)
public class SystemServicesComponent extends CommonComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);
        final HstRequestContext ctx = request.getRequestContext();
        VisualHub visualHub = (VisualHub) ctx.getContentBean();
        if (visualHub != null) {
            request.setAttribute("document", visualHub);
        }

    }


}
