package uk.nhs.digital.common.components.catalogue;

import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.EssentialsFacetsComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsFacetsComponentInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.components.catalogue.filters.Filters;
import uk.nhs.digital.common.components.catalogue.filters.Section;
import uk.nhs.digital.common.components.catalogue.filters.Subsection;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
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

        super.doBeforeRender(request, response);
        HippoFacetNavigationBean facetBean = request.getModel("facets");

        request.setModel("facets", facetBean);
        ConcurrentHashMap<String, List<Object>> facetBeanMap = getFacetFilterMap(facetBean);
        request.setModel("facets1", facetBeanMap);

        // Note remove ApiCatalogueComponent after when work is done.
        ApiCatalogueComponent apiCatalogueComponent = new ApiCatalogueComponent();
        boolean showRetired = apiCatalogueComponent.shouldShowRetired(request);

        //ConcurrentHashMap<String, Integer> retiredCountsMap = showRetired ? new ConcurrentHashMap<>() : getRetiredCountsMap(facetBean);
        //request.setModel("retiredCounts", retiredCountsMap);

        ApiCatalogueFilterManager apiCatalogueFilterManager = new ApiCatalogueFilterManager();
        Filters rawFilters = apiCatalogueFilterManager.getRawFilters(request);

        request.setAttribute("filtersModel",getFiltersBasedOnFacetResults(rawFilters,facetBeanMap));
        request.setAttribute("showRetired", showRetired);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        log.info("End of method: doBeforeRender in ApiCatalogueEssentialsFacetsComponent  at " + endTime + " ms. Duration: " + duration + " ms");
    }

    private Filters getFiltersBasedOnFacetResults(final Filters rawFilters, ConcurrentHashMap<String, List<Object>> facetBeanMap) {
        rawFilters.getSections().forEach(section -> {
            List<Runnable> deferredOperations = new ArrayList<>();
            AtomicInteger subSectionCounter = new AtomicInteger(0);
            AtomicBoolean display = new AtomicBoolean(false);
            section.getEntries().forEach(subsection -> {

                // This doesn't run display now!
                // The loop needs to finish because if one or more of the loop's iterations
                // determine this to be true, then all subsections are given the value.
                // Deferred operations are collected and executed after the loop completes.
                deferredOperations.add(subsection::display);

                if (isTaxonomyKeyPresentInFacet(subsection,facetBeanMap) || isApiOrApiStandardFilter(subsection)) {
                    section.display();

                    if (!isApiOrApiStandardFilter(subsection)) {
                        subsection.setCount(subSectionCounter.incrementAndGet());
                    }
                    // Display 'show more' button if necessary
                    displayShowMoreButton(subSectionCounter,section);

                    // Display parent/first level filter
                    displayFirstLevelParentFilter(subSectionCounter,section,subsection,facetBeanMap);

                    // Display child/second level child filter within subsection
                    displaySecondLevelChildFilter(subsection,facetBeanMap,subSectionCounter,section, display, deferredOperations);

                    if (isTaxonomyKeyPresentInFacet(subsection,facetBeanMap) && facetBeanMap.get(subsection.getTaxonomyKey()).get(1) != null
                        && Objects.equals(facetBeanMap.get(subsection.getTaxonomyKey()).get(1), true)) {
                        display.set(true);
                    }
                }
            });
            if (display.get()) {
                deferredOperations.forEach(Runnable::run);
                section.setShowMoreIndc(false);
            }
        });
        return rawFilters;
    }

    private void displayShowMoreButton(AtomicInteger subSectionCounter, Section section) {
        if (subSectionCounter.get() >= section.getAmountChildrenToShow() && section.getHideChildren()) {
            section.setShowMoreIndc(true);
        }
    }

    private void displayFirstLevelParentFilter(AtomicInteger subSectionCounter, Section section, Subsection subsection, ConcurrentHashMap<String, List<Object>> facetBeanMap) {
        if (subSectionCounter.get() <= section.getAmountChildrenToShow()
            || section.getAmountChildrenToShow() == 0 && !section.getHideChildren()) {
            subsection.display();
        }
        if (isTaxonomyKeyPresentInFacet(subsection,facetBeanMap)
            && Objects.equals(facetBeanMap.get(subsection.getTaxonomyKey()).get(1), true)) {
            section.expand();
        }
        if (isApiStandardFilter(subsection)) {
            subsection.setSelectable();
        }
        if (isApisFilter(subsection) && facetBeanMap.get("apis_1") == null) {
            subsection.setSelectable();
        }
    }

    private void displaySecondLevelChildFilter(Subsection subsection, ConcurrentHashMap<String, List<Object>> facetBeanMap, AtomicInteger subSectionCounter, Section section,
                                          AtomicBoolean display, List<Runnable> deferredOperations) {
        subsection.getEntries().forEach(subsectionEntry -> {
            deferredOperations.add(subsectionEntry::display);

            if (isTaxonomyKeyPresentInFacet(subsectionEntry,facetBeanMap)) {
                subsectionEntry.setCount(subSectionCounter.incrementAndGet());
            }

            if (subSectionCounter.get() <= subsectionEntry.getAmountChildrenToShow()
                && subsectionEntry.getAmountChildrenToShow() == 0
                && !subsectionEntry.getHideChildren()
                && subSectionCounter.get() <= section.getAmountChildrenToShow()) {
                display.set(true);
            }

            if (subsectionEntry.getCount() >= section.getAmountChildrenToShow()
                && section.getAmountChildrenToShow() == 0) {
                display.set(true);
            }

            if (isTaxonomyKeyPresentInFacet(subsectionEntry,facetBeanMap)
                && Objects.equals(facetBeanMap.get(subsectionEntry.getTaxonomyKey()).get(1), true)) {
                section.expand();
                subsectionEntry.display();
                display.set(true);
            }

        });
    }

    private boolean isTaxonomyKeyPresentInFacet(Subsection subsectionEntry, ConcurrentHashMap<String, List<Object>> facetBeanMap) {
        return subsectionEntry.getTaxonomyKey() != null && facetBeanMap.get(subsectionEntry.getTaxonomyKey()) != null
            && !facetBeanMap.get(subsectionEntry.getTaxonomyKey()).isEmpty();
    }

    private boolean isApiOrApiStandardFilter(Subsection subsection) {
        return subsection.getTaxonomyKey().equalsIgnoreCase("apis_1")
            || subsection.getTaxonomyKey().equalsIgnoreCase("api-standards");
    }

    private boolean isApiStandardFilter(Subsection subsection) {
        return subsection.getTaxonomyKey().equalsIgnoreCase("api-standards");
    }

    private boolean isApisFilter(Subsection subsection) {
        return subsection.getTaxonomyKey().equalsIgnoreCase("apis_1");
    }

    /*private boolean isTaxonomyKeyPresentInFacet(Subsection subsectionEntry, ConcurrentHashMap<String, List<Object>> facetBeanMap) {
        return Optional.ofNullable(subsectionEntry.getTaxonomyKey())
            .map(key -> (facetBeanMap.containsKey(key) && !facetBeanMap.get(key).isEmpty())
                || key.equalsIgnoreCase("apis_1") || key.equalsIgnoreCase("api-standards"))
            .orElse(false);
    }*/

    private ConcurrentHashMap<String, List<Object>> getFacetFilterMap(HippoFacetNavigationBean facetBean) {
        ConcurrentHashMap<String, List<Object>> facetFilterMap = new ConcurrentHashMap();
        facetBean.getFolders().get(0).getFolders().parallelStream().forEach(i ->
            facetFilterMap.put(
                ((HippoFacetNavigationBean) i).getDisplayName(),
                Arrays.asList(new Object[]{(HippoFacetNavigationBean) i, i.isLeaf(),((HippoFacetNavigationBean) i).getCount()})
            )
        );
        return facetFilterMap;
    }

    /*private ConcurrentHashMap<String, Integer> getRetiredCountsMap(HippoFacetNavigationBean facetBean) {
        AtomicReference<ConcurrentHashMap<String, Integer>> retiredCounter = new AtomicReference<>(new ConcurrentHashMap<>());
        facetBean.getFolders().get(0).getFolders().forEach(i -> {
            //if (((HippoFacetNavigationBean) i).getDisplayName().toLowerCase().contains("retired")) {
            String displayName = ((HippoFacetNavigationBean) i).getDisplayName();
            if (((HippoFacetNavigationBean) i).getResultSet() != null) {
                ((HippoFacetNavigationBean) i).getResultSet().getDocuments().forEach(doc -> {
                    getRetiredCountFromApiSpecAndGeneralDoc(doc, retiredCounter, displayName);
                });
            } else if (((HippoFacetSubNavigation) ((HippoFacetNavigationBean) i)).getAncestors() != null) {
                ((HippoFacetSubNavigation) (HippoFacetNavigationBean) i).getAncestors().get(0).getResultSet().getDocuments().forEach(doc -> {
                    getRetiredCountFromApiSpecAndGeneralDoc(doc, retiredCounter, displayName);
                });
            }
        });
        return retiredCounter.get();
    }*/

    /*private void getRetiredCountFromApiSpecAndGeneralDoc(HippoDocumentBean doc, AtomicReference<ConcurrentHashMap<String, Integer>> retiredCounter, String displayName) {
        if (doc instanceof ApiSpecification) {
            ApiSpecification apiSpec = (ApiSpecification) doc;
            outerLoop:
            for (String key : apiSpec.getKeys()) {
                if (key.toLowerCase().contains("retired")) {
                    retiredCounter.get().merge(displayName, 1, Integer::sum);
                    break outerLoop;
                }
            }
        }
        if (doc instanceof General) {
            General general = (General) doc;
            outerLoop:
            for (String key : general.getKeys()) {
                if (key.toLowerCase().contains("retired")) {
                    retiredCounter.get().merge(displayName, 1, Integer::sum);
                    break outerLoop;
                }
            }
        }
    }*/

}
