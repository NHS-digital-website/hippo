package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.EssentialsListPickerComponent;
import uk.nhs.digital.common.components.info.WrappedListPickerComponentInfo;

@ParametersInfo(
    type = WrappedListPickerComponentInfo.class
    )
public class WrappedListPickerComponent extends EssentialsListPickerComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        final WrappedListPickerComponentInfo info = getComponentParametersInfo(request);
        final HippoBean document = getHippoBeanForPath(info.getWrappingDocument(), HippoBean.class);

        request.setAttribute("wrappingDocument", document);
    }
}
