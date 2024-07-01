package uk.nhs.digital.common.components.catalogue;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.EssentialsFacetsComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsFacetsComponentInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.components.catalogue.filters.Filters;

@ParametersInfo(
        type = EssentialsFacetsComponentInfo.class
)
public class ApiCatalogueEssentialsFacetsComponent extends EssentialsFacetsComponent {

    private static final Logger log = LoggerFactory.getLogger(ApiCatalogueComponent.class);
    private static final String TAXONOMY_FILTERS_MAPPING_DOCUMENT_PATH = "/content/documents/administration/website/developer-hub/taxonomy-filters-mapping";

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {

        long startTime = System.currentTimeMillis();
        log.debug("ApiCatalogueEssentialsFacetsComponent - Start Time:" + startTime);

        CacheManager cacheManager = ApiCatalogueFilterCacheManager.loadFilterCache();
        Filters rawFilters = null;
        Cache<String, Filters> cache = cacheManager.getCache("apiFilterCache", String.class, Filters.class);
        ApiCatalogueComponent apiCatalogueComponent = new ApiCatalogueComponent();

        if (null == cache.get("rawFiltersCache")) {
            rawFilters = apiCatalogueComponent.rawFilters(apiCatalogueComponent.sessionFrom(request), TAXONOMY_FILTERS_MAPPING_DOCUMENT_PATH, log);
            cache.put("rawFiltersCache", rawFilters);
            log.info("YAML coversion data has been fetched!!!");
        } else {
            rawFilters = cache.get("rawFiltersCache");
            log.info("Cache data has been fetched!!!");
        }

        CacheManager cacheManager1 = ApiCatalogueFilterCacheManager.loadFacetBeanCache();
        Cache<String, HippoFacetNavigationBean> cache1 = cacheManager1.getCache("apiFacetBeanCache", String.class, HippoFacetNavigationBean.class);
        HippoFacetNavigationBean facetBean = null;
        if (null == cache1.get("facetBeanCache")) {
            log.info("Cache data not fetched!!!");
        } else {
            facetBean = cache1.get("facetBeanCache");
            log.info("Cache data fetched!!!");
        }

        request.setAttribute("statusKeys", rawFilters.getSections().get(4).getEntries());
        request.setAttribute("filtersModel",rawFilters);
        request.setAttribute("facets", facetBean);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        log.info("End of method: doBeforeRender in ApiCatalogueEssentialsFacetsComponent  at " + endTime + " ms. Duration: " + duration + " ms");
    }

}
