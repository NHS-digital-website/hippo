package uk.nhs.digital.common.components;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.CommonComponent;
import uk.nhs.digital.common.components.info.WhiteSpaceComponentInfo;

@ParametersInfo(type = WhiteSpaceComponentInfo.class)
public class WhiteSpaceComponent extends CommonComponent {
    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        final WhiteSpaceComponentInfo componentParametersInfo = getComponentParametersInfo(request);

        String size = componentParametersInfo.getSize();
        request.setAttribute("size", size);
    }
}

