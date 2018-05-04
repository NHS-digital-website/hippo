package uk.nhs.digital.common.components;

import org.apache.commons.lang.*;
import org.hippoecm.hst.content.beans.standard.*;
import org.hippoecm.hst.core.component.*;
import org.hippoecm.hst.util.*;

public class GdprSummaryComponent extends GdprComponent {

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);
        //getting the facetrelativepath configured in the hst:pages configuration
        String facetRelativePath = getComponentParameter("facetrelativepath");
        //the folder path property, if included, MUST start with a slash
        String subFolderPath = StringUtils.defaultIfEmpty(getComponentParameter("facetfoldername"), "");
        if (StringUtils.isNotEmpty(facetRelativePath)) {
            //if the facetRelativePath is not empty, get the facet navigation bean
            HippoFacetNavigationBean facetNav =
                ContentBeanUtils.getFacetNavigationBean("/" + request.getRequestContext().getSiteContentBasePath(), facetRelativePath + subFolderPath, "");
            //attaching the facet navigation bean to the request
            request.setAttribute("facets", facetNav);
        }
    }
}
