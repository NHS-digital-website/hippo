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
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@ParametersInfo(
        type = EssentialsFacetsComponentInfo.class
)
public class ApiCatalogueEssentialsFacetsComponent extends EssentialsFacetsComponent {

    private static final Logger log = LoggerFactory.getLogger(ApiCatalogueEssentialsFacetsComponent.class);

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {

        long startTime = System.currentTimeMillis();
        log.debug("ApiCatalogueEssentialsFacetsComponent - Start Time:" + startTime);

        super.doBeforeRender(request, response);
        HippoFacetNavigationBean facetBean = request.getModel("facets");

        request.setModel("facets", facetBean);
        HashMap<String, List<Object>> facetBeanMap = getFacetFilterMap(facetBean);
        request.setModel("facets1", facetBeanMap);

        ApiCatalogueFilterManager apiCatalogueFilterManager = new ApiCatalogueFilterManager();

        Filters rawFilters = apiCatalogueFilterManager.getRawFilters(request);

        request.setAttribute("filtersModel",getFiltersBasedOnFacetResults(rawFilters,facetBeanMap));

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        log.info("End of method: doBeforeRender in ApiCatalogueEssentialsFacetsComponent  at " + endTime + " ms. Duration: " + duration + " ms");
    }

    Filters getFiltersBasedOnFacetResults(final Filters rawFilters, HashMap<String, List<Object>> facetBeanMap) {
        if (rawFilters != null) {
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

                    if (isTaxonomyKeyPresentInFacet(subsection, facetBeanMap) || isExceptionalFilter(subsection)) {
                        section.display();

                        if (!isExceptionalFilter(subsection)) {
                            subsection.setCount(subSectionCounter.incrementAndGet());
                        }
                        // Display 'show more' button if necessary
                        displayShowMoreButton(subSectionCounter, section);

                        // Display parent/first level filter
                        displayFirstLevelParentFilter(subSectionCounter, section, subsection, facetBeanMap);

                        // Display child/second level child filter within subsection
                        displaySecondLevelChildFilter(subsection, facetBeanMap, subSectionCounter, section, display, deferredOperations);

                        if (isTaxonomyKeyPresentInFacet(subsection, facetBeanMap) && facetBeanMap.get(subsection.getTaxonomyKey()).get(1) != null
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
        }
        return rawFilters;
    }

    void displayShowMoreButton(AtomicInteger subSectionCounter, Section section) {
        if (subSectionCounter.get() >= section.getAmountChildrenToShow() && section.getHideChildren()) {
            section.setShowMoreIndc(true);
        }
    }

    void displayFirstLevelParentFilter(AtomicInteger subSectionCounter, Section section, Subsection subsection, HashMap<String, List<Object>> facetBeanMap) {
        if (subSectionCounter.get() <= section.getAmountChildrenToShow()
            || section.getAmountChildrenToShow() == 0 && !section.getHideChildren()) {
            subsection.display();
        }
        if (isTaxonomyKeyPresentInFacet(subsection,facetBeanMap)
            && Objects.equals(facetBeanMap.get(subsection.getTaxonomyKey()).get(1), true)) {
            section.expand();
        }
        if (isGrayedOutFilter(subsection)) {
            subsection.setSelectable();
        }
        if (isApisFilter(subsection) && facetBeanMap.get("apis_1") == null) {
            subsection.setSelectable();
        }
    }

    void displaySecondLevelChildFilter(Subsection subsection, HashMap<String, List<Object>> facetBeanMap, AtomicInteger subSectionCounter, Section section,
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

    boolean isExceptionalFilter(Subsection subsection) {
        return subsection.getTaxonomyKey().equalsIgnoreCase("apis_1")
            || subsection.getTaxonomyKey().equalsIgnoreCase("api-standards")
            || subsection.getTaxonomyKey().equalsIgnoreCase("medication-management");
    }

    boolean isGrayedOutFilter(Subsection subsection) {
        return subsection.getTaxonomyKey().equalsIgnoreCase("api-standards")
            || subsection.getTaxonomyKey().equalsIgnoreCase("medication-management");
    }

    boolean isApisFilter(Subsection subsection) {
        return subsection.getTaxonomyKey().equalsIgnoreCase("apis_1");
    }

    boolean isTaxonomyKeyPresentInFacet(Subsection subsectionEntry, HashMap<String, List<Object>> facetBeanMap) {
        return Optional.ofNullable(subsectionEntry.getTaxonomyKey())
            .map(key -> facetBeanMap.containsKey(key) && !facetBeanMap.get(key).isEmpty())
            .orElse(false);
    }

    HashMap<String, List<Object>> getFacetFilterMap(HippoFacetNavigationBean facetBean) {
        HashMap<String, List<Object>> facetFilterMap = new HashMap<>();
        if (facetBean.getFolders().size() > 0) {
            facetBean.getFolders().get(0).getFolders().parallelStream().forEach(i ->
                facetFilterMap.put(
                    ((HippoFacetNavigationBean) i).getDisplayName(),
                    Arrays.asList(new Object[]{(HippoFacetNavigationBean) i, i.isLeaf(), ((HippoFacetNavigationBean) i).getCount()})
                )
            );
        }
        return facetFilterMap;
    }

}


