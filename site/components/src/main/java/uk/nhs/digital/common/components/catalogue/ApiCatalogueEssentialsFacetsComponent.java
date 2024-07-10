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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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

        ApiCatalogueFilterManager apiCatalogueFilterManager = new ApiCatalogueFilterManager();
        Filters rawFilters = apiCatalogueFilterManager.getRawFilters(request);

        request.setAttribute("filtersModel",getFiltersBasedOnFacetResults(rawFilters,facetBeanMap));

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

                if (subsection.getTaxonomyKey() != null && facetBeanMap.get(subsection.getTaxonomyKey()) != null
                    && !facetBeanMap.get(subsection.getTaxonomyKey()).isEmpty()) {

                    section.display();
                    subsection.setCount(subSectionCounter.incrementAndGet());

                    // Display 'show more' button if necessary
                    displayShowMoreButton(subSectionCounter,section);

                    // Display parent/first level filter
                    displayFirstLevelFilter(subSectionCounter,section,subsection,facetBeanMap);

                    // Display child/second level child filter within subsection
                    displaySubSectionEntries(subsection,facetBeanMap,subSectionCounter,section, display, deferredOperations);

                    if (facetBeanMap.get(subsection.getTaxonomyKey()).get(1) != null
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

    private void displaySubSectionEntries(Subsection subsection, ConcurrentHashMap<String, List<Object>> facetBeanMap, AtomicInteger subSectionCounter, Section section,
                                          AtomicBoolean display, List<Runnable> deferredOperations) {
        subsection.getEntries().forEach(subsectionEntry -> {
            deferredOperations.add(subsectionEntry::display);
            if (subsectionEntry.getTaxonomyKey() != null && facetBeanMap.get(subsectionEntry.getTaxonomyKey()) != null
                && !facetBeanMap.get(subsectionEntry.getTaxonomyKey()).isEmpty()) {
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

            if (subsectionEntry.getTaxonomyKey() != null && facetBeanMap.get(subsectionEntry.getTaxonomyKey()) != null
                && !facetBeanMap.get(subsectionEntry.getTaxonomyKey()).isEmpty()
                && Objects.equals(facetBeanMap.get(subsectionEntry.getTaxonomyKey()).get(1), true)) {
                section.expand();
            }

            if (subsectionEntry.getTaxonomyKey() != null && facetBeanMap.get(subsectionEntry.getTaxonomyKey()) != null
                && !facetBeanMap.get(subsectionEntry.getTaxonomyKey()).isEmpty()
                && Objects.equals(facetBeanMap.get(subsectionEntry.getTaxonomyKey()).get(1), true)) {
                display.set(true);
            }

        });
    }

    private void displayFirstLevelFilter(AtomicInteger subSectionCounter, Section section, Subsection subsection, ConcurrentHashMap<String, List<Object>> facetBeanMap) {
        if (subSectionCounter.get() <= section.getAmountChildrenToShow()
            || section.getAmountChildrenToShow() == 0 && !section.getHideChildren()) {
            subsection.display();
        }
        if (subsection.getTaxonomyKey() != null && facetBeanMap.get(subsection.getTaxonomyKey()) != null
            && !facetBeanMap.get(subsection.getTaxonomyKey()).isEmpty()
            && Objects.equals(facetBeanMap.get(subsection.getTaxonomyKey()).get(1), true)) {
            // display.set(true);
            section.expand();
        }
    }

    private void displayShowMoreButton(AtomicInteger subSectionCounter, Section section) {
        if (subSectionCounter.get() >= section.getAmountChildrenToShow() && section.getHideChildren()) {
            section.setShowMoreIndc(true);
        }
    }

    private ConcurrentHashMap<String, List<Object>> getFacetFilterMap(HippoFacetNavigationBean facetBean) {
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
