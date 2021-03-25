package uk.nhs.digital.common.components;

import org.hippoecm.hst.container.*;
import org.hippoecm.hst.core.component.*;
import org.onehippo.forge.selection.hst.contentbean.*;
import org.onehippo.forge.selection.hst.util.*;

public class ApiMasterComponent extends ContentRewriterComponent {
    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        final ValueList releaseStatusesValueList =
            SelectionUtil.getValueListByIdentifier("releasestatuses", RequestContextProvider.get());
        if (releaseStatusesValueList != null) {
            request.setAttribute("releasestatuses", SelectionUtil.valueListAsMap(releaseStatusesValueList));
        }
    }
}
