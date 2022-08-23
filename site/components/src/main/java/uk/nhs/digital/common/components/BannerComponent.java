package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.EssentialsDocumentComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsBannerComponentInfo;

import javax.servlet.http.HttpServletRequest;

@ParametersInfo(type = EssentialsBannerComponentInfo.class)
public class BannerComponent extends EssentialsDocumentComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        EssentialsBannerComponentInfo componentInfo = getComponentParametersInfo(request);
        HippoBean document = getHippoBeanForPath(componentInfo.getDocument(), HippoBean.class);

        HttpServletRequest servletRequest = request.getRequestContext().getServletRequest();
        String title = document.getSingleProperty("website:title");

        if (servletRequest.getAttribute("pageTitle") == null && title != null) {
            servletRequest.setAttribute("pageTitle", title);
        }
    }
}
