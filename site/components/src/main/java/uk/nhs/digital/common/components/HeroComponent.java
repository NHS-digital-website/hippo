package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.CommonComponent;
import uk.nhs.digital.common.components.info.HeroComponentInfo;

@ParametersInfo(type = HeroComponentInfo.class)
public class HeroComponent extends CommonComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        HeroComponentInfo componentInfo = getComponentParametersInfo(request);
        final String size = componentInfo.getSize();
        final String textAlignment = componentInfo.getTextAlignment();
        final Boolean colourBar = componentInfo.getColourBar();
        HippoBean document = getHippoBeanForPath(componentInfo.getDocument(), HippoBean.class);

        request.setAttribute("document", document);
        request.setAttribute("size", size);
        request.setAttribute("textAlignment", textAlignment);
        request.setAttribute("displayColourBar", colourBar);
    }
}
