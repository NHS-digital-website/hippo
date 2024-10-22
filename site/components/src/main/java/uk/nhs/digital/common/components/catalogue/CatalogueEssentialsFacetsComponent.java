package uk.nhs.digital.common.components.catalogue;

import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.EssentialsFacetsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.components.catalogue.filters.Filters;

@ParametersInfo(
        type = CatalogueEssentialsFacetsComponentInfo.class
)
public class CatalogueEssentialsFacetsComponent extends EssentialsFacetsComponent {

    private static final Logger log = LoggerFactory.getLogger(CatalogueEssentialsFacetsComponent.class);

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {

        long startTime = System.currentTimeMillis();
        log.debug("CatalogueEssentialsFacetsComponent - Start Time:" + startTime);

        super.doBeforeRender(request, response);

        HippoFacetNavigationBean facetBean = request.getModel("facets");
        request.setModel("facets", facetBean);

        CatalogueEssentialsFacetsComponentInfo parameterInfo = this.getComponentParametersInfo(request);
        CatalogueFilterManager catalogueFilterManager = new CatalogueFilterManager(parameterInfo.getTaxonomyFilterMappingDocumentPath());
        Filters rawFilters = catalogueFilterManager.getRawFilters(request);
        request.setAttribute("filtersModel", rawFilters);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        log.debug("End of method: doBeforeRender in CatalogueEssentialsFacetsComponent  at " + endTime + " ms. Duration: " + duration + " ms");
    }
}
