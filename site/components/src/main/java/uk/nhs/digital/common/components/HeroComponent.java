package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.CommonComponent;
import uk.nhs.digital.common.components.info.HeroComponentInfo;
import uk.nhs.digital.common.contentrewriters.BrandRefreshContentRewriter;
import uk.nhs.digital.common.contentrewriters.StripTagsContentRewriter;

import javax.servlet.http.HttpServletRequest;

@ParametersInfo(type = HeroComponentInfo.class)
public class HeroComponent extends CommonComponent {
    private static final StripTagsContentRewriter stripTagsContentRewriter = new StripTagsContentRewriter();
    private static final BrandRefreshContentRewriter brContentRewriter = new BrandRefreshContentRewriter();

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        HeroComponentInfo componentInfo = getComponentParametersInfo(request);
        final String size = componentInfo.getSize();
        final String textAlignment = componentInfo.getTextAlignment();
        final Boolean colourBar = componentInfo.getColourBar();
        HippoBean document = getHippoBeanForPath(componentInfo.getDocument(), HippoBean.class);
        String colour = componentInfo.getColour();
        if ("Dark Blue".equalsIgnoreCase(colour)) {
            request.setAttribute("colour", "Dark Blue Multicolour");
        } else {
            request.setAttribute("colour", colour);
        }
        request.setAttribute("document", document);
        request.setAttribute("size", size);
        request.setAttribute("textAlignment", textAlignment);
        request.setAttribute("displayColourBar", colourBar);
        request.setAttribute("stripTagsContentRewriter", stripTagsContentRewriter);
        request.setAttribute("brContentRewriter", brContentRewriter);

        HttpServletRequest servletRequest = request.getRequestContext().getServletRequest();
        String title = document.getSingleProperty("website:title");

        if (servletRequest.getAttribute("pageTitle") == null && title != null) {
            servletRequest.setAttribute("pageTitle", title);
        }
    }
}
