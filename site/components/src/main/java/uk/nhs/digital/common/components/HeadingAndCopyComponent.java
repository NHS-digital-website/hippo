package uk.nhs.digital.common.components;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.CommonComponent;
import uk.nhs.digital.common.components.info.HeadingAndCopyComponentInfo;
import uk.nhs.digital.website.beans.Calltoaction;

@ParametersInfo (type = HeadingAndCopyComponentInfo.class)
public class HeadingAndCopyComponent extends CommonComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        final HeadingAndCopyComponentInfo info = getComponentParametersInfo(request);

        Calltoaction source = getHippoBeanForPath(info.getSourceDocument(), Calltoaction.class);
        request.setAttribute("source", source);
        request.setAttribute("alignment", info.getAlignment());

    }
}
