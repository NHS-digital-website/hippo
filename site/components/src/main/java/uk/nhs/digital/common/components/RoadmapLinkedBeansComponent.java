package uk.nhs.digital.common.components;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;

import java.util.Arrays;

public class RoadmapLinkedBeansComponent extends ContentRewriterComponent {

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);
        request.setAttribute("orderBy", getSortByOrDefault(getPublicRequestParameter(request, "order-by")));
        request.setAttribute("selectedTypes", Arrays.asList(getPublicRequestParameters(request, "type")));
    }

    private String getSortByOrDefault(String order) {
        if (order != null && order.equalsIgnoreCase("start-date")) {
            return "startDate";
        }
        return "endDate";
    }

}
