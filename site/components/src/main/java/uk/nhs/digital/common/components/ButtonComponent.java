package uk.nhs.digital.common.components;

import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.CommonComponent;
import uk.nhs.digital.common.components.info.ButtonComponentInfo;

@ParametersInfo(type = ButtonComponentInfo.class)
public class ButtonComponent extends CommonComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        ButtonComponentInfo componentInfo = getComponentParametersInfo(request);
        final String button1Title = componentInfo.getButton1Title();
        final String button2Title = componentInfo.getButton2Title();
        final String button3Title = componentInfo.getButton3Title();

        final String button1Link = componentInfo.getButton1Link();
        final String button2Link = componentInfo.getButton2Link();
        final String button3Link = componentInfo.getButton3Link();

        if (StringUtils.isNotBlank(button1Title) && StringUtils.isNotBlank(button1Link)) {
            request.setAttribute("button1Title", button1Title);
            request.setAttribute("button1Link", button1Link);
        }

        if (StringUtils.isNotBlank(button2Title) && StringUtils.isNotBlank(button2Link)) {
            request.setAttribute("button2Title", button2Title);
            request.setAttribute("button2Link", button2Link);
        }

        if (StringUtils.isNotBlank(button3Title) && StringUtils.isNotBlank(button3Link)) {
            request.setAttribute("button3Title", button3Title);
            request.setAttribute("button3Link", button3Link);
        }

        final String heading = componentInfo.getHeader();
        final String alignment = componentInfo.getAlignment();

        request.setAttribute("heading", heading);
        request.setAttribute("alignment", alignment);
    }
}
