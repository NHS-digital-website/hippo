package uk.nhs.digital.common.components;

import org.hippoecm.hst.container.*;
import org.hippoecm.hst.core.component.*;
import org.onehippo.forge.selection.hst.contentbean.*;
import org.onehippo.forge.selection.hst.util.*;

public class ApiEndpointComponent extends ContentRewriterComponent {
    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        final ValueList paramaterTypeValueList = SelectionUtil.getValueListByIdentifier("parametertypeList", RequestContextProvider.get());
        if (paramaterTypeValueList != null) {
            request.setAttribute("parametertypeList", SelectionUtil.valueListAsMap(paramaterTypeValueList));
        }
        final ValueList releaseStatusesValueList = SelectionUtil.getValueListByIdentifier("releasestatuses", RequestContextProvider.get());
        if (releaseStatusesValueList != null) {
            request.setAttribute("releasestatuses", SelectionUtil.valueListAsMap(releaseStatusesValueList));
        }
    }
}
