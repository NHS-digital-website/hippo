package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.CommonComponent;
import uk.nhs.digital.common.components.info.HeaderComponentInfo;
import uk.nhs.digital.common.contentrewriters.StripTagsContentRewriter;
import uk.nhs.digital.common.util.DocumentUtils;

@ParametersInfo(type = HeaderComponentInfo.class)
public class HeaderComponent extends CommonComponent {
    private static final StripTagsContentRewriter stripTagsContentRewriter = new StripTagsContentRewriter();

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        final HeaderComponentInfo componentParametersInfo = getComponentParametersInfo(request);

        String bannerDocPath = componentParametersInfo.getBannerDoc();
        HippoDocument banner = this.getHippoBeanForPath(bannerDocPath, HippoDocument.class);
        request.setAttribute("banner", banner);

        String colour = componentParametersInfo.getColour();
        if ("Dark Blue".equalsIgnoreCase(colour)) {
            request.setAttribute("colour", "Dark Blue Multicolour");
        } else {
            request.setAttribute("colour", colour);
        }

        String digiblockPosition = componentParametersInfo.getDigiblockPosition();
        request.setAttribute("digiblockposition", digiblockPosition);

        String alignment = componentParametersInfo.getAlignment();
        request.setAttribute("alignment", alignment);

        String button1text = componentParametersInfo.getButton1Text();
        String button1Url = componentParametersInfo.getButton1Url();
        request.setAttribute("button1Text", button1text);
        request.setAttribute("button1Url", button1Url);

        String button2text = componentParametersInfo.getButton2Text();
        String button2Url = componentParametersInfo.getButton2Url();
        request.setAttribute("button2Text", button2text);
        request.setAttribute("button2Url", button2Url);
        request.getRequestContext().setAttribute("headerPresent", true);

        request.setAttribute("stripTagsContentRewriter", stripTagsContentRewriter);
        DocumentUtils.setMetaTags(request, this);
    }
}
