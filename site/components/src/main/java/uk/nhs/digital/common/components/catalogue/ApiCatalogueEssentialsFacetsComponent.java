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

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ParametersInfo(
        type = EssentialsFacetsComponentInfo.class
)
public class ApiCatalogueEssentialsFacetsComponent extends EssentialsFacetsComponent {

    private static final Logger log = LoggerFactory.getLogger(ApiCatalogueComponent.class);

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {

        long startTime = System.currentTimeMillis();
        log.debug("ApiCatalogueEssentialsFacetsComponent - Start Time:" + startTime);

        CacheManager cacheManager1 = ApiCatalogueFilterCacheManager.loadFacetBeanCache();
        Cache<String, HippoFacetNavigationBean> cache1 = cacheManager1.getCache("apiFacetBeanCache", String.class, HippoFacetNavigationBean.class);
        HippoFacetNavigationBean facetBean = null;
        if (null == cache1.get("facetBeanCache")) {
            log.info("Cache data not fetched!!!");
        } else {
            facetBean = cache1.get("facetBeanCache");
            log.info("Cache data fetched!!!");
        }

        request.setModel("facets", facetBean);
        ConcurrentHashMap<String, List<Object>> facetBeanMap = getFacetFiltermap(facetBean);
        request.setModel("facets1", facetBeanMap);

        ApiCatalogueFilterManager apiCatalogueFilterManager = new ApiCatalogueFilterManager();
        Filters rawFilters = apiCatalogueFilterManager.getRawFilters(request);

        request.setAttribute("filtersModel",getFiltersBasedOnFacetResults(rawFilters,facetBeanMap));

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        log.info("End of method: doBeforeRender in ApiCatalogueEssentialsFacetsComponent  at " + endTime + " ms. Duration: " + duration + " ms");
    }

    private Filters getFiltersBasedOnFacetResults(Filters rawFilters, ConcurrentHashMap<String, List<Object>> facetBeanMap) {
        rawFilters.getSections().forEach(section ->
            {
                AtomicInteger subSectionCounter = new AtomicInteger(0);
                section.getEntries().forEach(subsection -> {
                        if (subsection.getTaxonomyKey() != null && facetBeanMap.get(subsection.getTaxonomyKey()) != null
                            && !facetBeanMap.get(subsection.getTaxonomyKey()).toString().isEmpty()) {
                            section.display();
                            subsection.setCount(subSectionCounter.incrementAndGet()); //subsection.select();
                            // This condition is to display 'show more' button
                            if (subSectionCounter.get() >= section.getAmountChildrenToShow() && section.getHideChildren()) {
                                section.setShowMoreIndc(true);
                            }
                            // This condition is to display parent/first level filter...
                            if (subSectionCounter.get() <= section.getAmountChildrenToShow() || section.getAmountChildrenToShow() == 0 && !section.getHideChildren()) {
                                subsection.display();
                            }
                            subsection.getEntries().forEach(subsectionEntry -> {
                                if (subsectionEntry.getTaxonomyKey() != null && facetBeanMap.get(subsectionEntry.getTaxonomyKey()) != null
                                    && !facetBeanMap.get(subsectionEntry.getTaxonomyKey()).toString().isEmpty()) {
                                    subsectionEntry.setCount(subSectionCounter.incrementAndGet());
                                }
                                //This condition is to display first level filter..
                                if (subSectionCounter.get() <= subsectionEntry.getAmountChildrenToShow()
                                    && subsectionEntry.getAmountChildrenToShow() == 0
                                    && !subsectionEntry.getHideChildren() && subSectionCounter.get() <= section.getAmountChildrenToShow()) {
                                    subsectionEntry.display();
                                }
                                // This condition is to display second level child filter, eg: Central under API in Integration Type...
                                if (subsectionEntry.getCount() >= section.getAmountChildrenToShow() && section.getAmountChildrenToShow() == 0) {
                                    subsectionEntry.display();
                                }
                            });
                        }
                    }
                );
            }
        );
        return rawFilters;
    }

    private ConcurrentHashMap<String, List<Object>> getFacetFiltermap(HippoFacetNavigationBean facetBean) {
        ConcurrentHashMap<String, List<Object>> facetFilterMap = new ConcurrentHashMap();
        facetBean.getFolders().get(0).getFolders().parallelStream().forEach(i ->
            facetFilterMap.put(
                ((HippoFacetNavigationBean) i).getDisplayName(),
                Arrays.asList(new Object[]{(HippoFacetNavigationBean) i, i.isLeaf()})
            ) //Arrays.asList((HippoFacetNavigationBean) i//((HippoFacetNavigationBean) i).getCount()
        );
        return facetFilterMap;
    }

}
