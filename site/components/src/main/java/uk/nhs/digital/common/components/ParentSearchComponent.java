package uk.nhs.digital.common.components;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.CommonComponent;
import uk.nhs.digital.common.components.info.ParentSearchComponentInfo;


@ParametersInfo(type = ParentSearchComponentInfo.class)
public class ParentSearchComponent extends CommonComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        ParentSearchComponentInfo paramInfo = getComponentParametersInfo(request);

        final boolean contentSearchEnabled = paramInfo.isContentSearchEnabled();
        final long contentSearchTimeOut = paramInfo.getContentSearchTimeOut();
        final boolean fallbackEnabled = paramInfo.isFallbackEnabled();
        final boolean contentSearchOverride = getAnyBooleanParam(request, "contentSearch", false);

        request.setAttribute("contentSearchEnabled", contentSearchEnabled);
        request.setAttribute("contentSearchTimeOut", contentSearchTimeOut);
        request.setAttribute("fallbackEnabled", fallbackEnabled);
        request.setAttribute("contentSearchOverride", contentSearchOverride);

    }

}
