package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsBannerComponentInfo;
import uk.nhs.digital.common.util.DocumentUtils;

@ParametersInfo(type = EssentialsBannerComponentInfo.class)
public class BannerComponent extends CommonComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        EssentialsBannerComponentInfo componentInfo = getComponentParametersInfo(request);
        HippoBean document = getHippoBeanForPath(componentInfo.getDocument(), HippoBean.class);

        request.setAttribute("document", document);
        request.setAttribute("cparam", componentInfo);
        DocumentUtils.setMetaTags(request, this);

    }
}
