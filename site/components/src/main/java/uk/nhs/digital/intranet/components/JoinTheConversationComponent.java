package uk.nhs.digital.intranet.components;


import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.CommonComponent;
import uk.nhs.digital.intranet.components.info.JoinTheConversationComponentInfo;


@ParametersInfo(
    type = JoinTheConversationComponentInfo.class
)
public class JoinTheConversationComponent extends CommonComponent {

    @Override
    public void doBeforeRender(HstRequest request,
        HstResponse response) {
        final JoinTheConversationComponentInfo paramInfo = getComponentParametersInfo(request);
        request.setAttribute(REQUEST_ATTR_PARAM_INFO, paramInfo);
        super.doBeforeRender(request, response);
    }
}
