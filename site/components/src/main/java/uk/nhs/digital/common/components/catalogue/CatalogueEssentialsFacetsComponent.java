package uk.nhs.digital.common.components.catalogue;

import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.components.catalogue.filters.Filters;
import uk.nhs.digital.common.components.info.CatalogueEssentialsFacetsComponentInfo;

import java.util.*;

@ParametersInfo(
        type = CatalogueEssentialsFacetsComponentInfo.class
)
public class CatalogueEssentialsFacetsComponent extends FilteredEssentialsFacetsComponent {

    private static final Logger log = LoggerFactory.getLogger(CatalogueEssentialsFacetsComponent.class);

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {

        long startTime = System.currentTimeMillis();
        log.debug("CatalogueEssentialsFacetsComponent - Start Time:" + startTime);

        super.doBeforeRender(request, response);
        HippoFacetNavigationBean facetBean = request.getModel("facets");
        HashMap<String, List<Object>> facetBeanMap = getFacetFilterMap(facetBean);
        request.setModel("facetFilterMap", facetBeanMap);

        CatalogueEssentialsFacetsComponentInfo parameterInfo = this.getComponentParametersInfo(request);
        CatalogueFilterManager catalogueFilterManager = new CatalogueFilterManager(parameterInfo.getTaxonomyFilterMappingDocumentPath());
        Filters rawFilters = catalogueFilterManager.getRawFilters(request);

        request.setAttribute("filters", rawFilters);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        log.debug("End of method: doBeforeRender in CatalogueEssentialsFacetsComponent  at " + endTime + " ms. Duration: " + duration + " ms");

    }

    static HashMap<String, List<Object>> getFacetFilterMap(HippoFacetNavigationBean facetBean) {
        HashMap<String, List<Object>> facetFilterMap = new HashMap<>();
        facetBean.getFolders().get(0).getFolders().parallelStream().forEach(i ->
            facetFilterMap.put(
                ((HippoFacetNavigationBean) i).getDisplayName(),
                Arrays.asList(new Object[]{(HippoFacetNavigationBean) i, i.isLeaf(),((HippoFacetNavigationBean) i).getCount()})
            )
        );
        return facetFilterMap;
    }

}
