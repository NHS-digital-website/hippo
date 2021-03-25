package uk.nhs.digital.common.components;

import org.hippoecm.hst.container.*;
import org.hippoecm.hst.core.component.*;
import org.onehippo.forge.selection.hst.contentbean.*;
import org.onehippo.forge.selection.hst.util.*;

public class PersonComponent extends ContentRewriterComponent {
    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        final ValueList suppressdataValueList =
            SelectionUtil.getValueListByIdentifier("suppressdata", RequestContextProvider.get());
        if (suppressdataValueList != null) {
            request.setAttribute("suppressdata", SelectionUtil.valueListAsMap(suppressdataValueList));
        }

        final ValueList imagedistributiontaggingValueList =
            SelectionUtil.getValueListByIdentifier("imagedistributiontagging", RequestContextProvider.get());
        if (imagedistributiontaggingValueList != null) {
            request.setAttribute("imagedistributiontagging", SelectionUtil.valueListAsMap(imagedistributiontaggingValueList));
        }

        final ValueList imagesourcepermissionValueList =
            SelectionUtil.getValueListByIdentifier("imagesourcepermission", RequestContextProvider.get());
        if (imagesourcepermissionValueList != null) {
            request.setAttribute("imagesourcepermission", SelectionUtil.valueListAsMap(imagedistributiontaggingValueList));
        }
    }
}
