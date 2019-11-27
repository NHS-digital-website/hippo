package uk.nhs.digital.common.components;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;

import java.util.Arrays;

public class RoadmapLinkedBeansComponent extends BaseGaContentComponent {

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);
        request.setAttribute("selectedTypes", Arrays.asList(getSelectedTypes(request)));
    }

    /**
     * Fetch the values of type parameters from the URL query string
     *
     * @param request containing the type parameters
     * @return array of type parameters if at least one exists, otherwise empty
     */
    protected String[] getSelectedTypes(HstRequest request) {
        return getPublicRequestParameters(request, "type");
    }



}
