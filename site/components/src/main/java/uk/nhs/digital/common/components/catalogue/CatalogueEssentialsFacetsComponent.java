package uk.nhs.digital.common.components.catalogue;

import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.components.catalogue.filters.Filters;
import uk.nhs.digital.common.components.catalogue.filters.Section;
import uk.nhs.digital.common.components.catalogue.filters.Subsection;
import uk.nhs.digital.common.components.info.CatalogueEssentialsFacetsComponentInfo;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

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
        request.setModel("facets1", facetBeanMap);

        CatalogueEssentialsFacetsComponentInfo parameterInfo = this.getComponentParametersInfo(request);
        CatalogueFilterManager catalogueFilterManager = new CatalogueFilterManager(parameterInfo.getTaxonomyFilterMappingDocumentPath());
        Filters rawFilters = catalogueFilterManager.getRawFilters(request);

        request.setAttribute("filters", rawFilters);

        request.setAttribute("filtersModel",getFiltersBasedOnFacetResults(rawFilters,facetBeanMap));

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

    private Filters getFiltersBasedOnFacetResults(final Filters rawFilters, HashMap<String, List<Object>> facetBeanMap) {
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

                if (isTaxonomyKeyPresentInFacet(subsection,facetBeanMap) || isExceptionalFilter(subsection)) {
                    section.display();

                    if (!isExceptionalFilter(subsection)) {
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

    protected boolean isTaxonomyKeyPresentInFacet(Subsection subsectionEntry, HashMap<String, List<Object>> facetBeanMap) {
        return Optional.ofNullable(subsectionEntry.getTaxonomyKey())
            .map(key -> facetBeanMap.containsKey(key) && !facetBeanMap.get(key).isEmpty())
            .orElse(false);
    }

    protected boolean isExceptionalFilter(Subsection subsection) {
        // Default behaviour, override in subclasses if needed
        return false;
    }

    protected void displayShowMoreButton(AtomicInteger subSectionCounter, Section section) {
        if (subSectionCounter.get() >= section.getAmountChildrenToShow() && section.getHideChildren()) {
            section.setShowMoreIndc(true);
        }
    }

    protected boolean isGrayedOutFilter(Subsection subsection) {
        // Default behaviour, override in subclasses if needed
        return false;
    }

    protected  void displayFirstLevelParentFilter(AtomicInteger subSectionCounter, Section section, Subsection subsection, HashMap<String, List<Object>> facetBeanMap) {
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
    }

    private void displaySecondLevelChildFilter(Subsection subsection, HashMap<String, List<Object>> facetBeanMap, AtomicInteger subSectionCounter, Section section,
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

}
