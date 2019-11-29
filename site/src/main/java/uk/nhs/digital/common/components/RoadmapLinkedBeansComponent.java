package uk.nhs.digital.common.components;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;

import java.util.Arrays;

public class RoadmapLinkedBeansComponent extends BaseGaContentComponent {

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);
        request.setAttribute("sortBy", getPublicRequestParameter(request, "sort-by"));
        request.setAttribute("selectedTypes", Arrays.asList(getPublicRequestParameters(request, "type")));
    }

}
