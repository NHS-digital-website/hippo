package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import uk.nhs.digital.common.components.info.WrappedListPickerComponentInfo;

import java.util.Calendar;

@ParametersInfo(
    type = WrappedListPickerComponentInfo.class
    )
public class WrappedListPickerComponent extends ListPickerComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        final WrappedListPickerComponentInfo info = getComponentParametersInfo(request);
        final HippoBean document = getHippoBeanForPath(info.getWrappingDocument(), HippoBean.class);
        final Boolean showIcon = info.getShowIcon();

        request.setAttribute("wrappingDocument", document);
        request.setAttribute("currentDate", Calendar.getInstance());
        request.setAttribute("showIcon", showIcon);
    }
}
