package uk.nhs.digital.common.components.catalogue;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoDocumentIterator;
import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.content.beans.standard.HippoResultSetBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.util.ContentBeanUtils;
import org.onehippo.cms7.essentials.components.EssentialsListComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsListComponentInfo;
import org.onehippo.cms7.essentials.components.paging.DefaultPagination;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import org.onehippo.cms7.essentials.components.utils.SiteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.components.catalogue.filters.Section;
import uk.nhs.digital.common.components.info.ApiCatalogueHubComponentInfo;

import java.util.*;
import java.util.stream.Collectors;

@ParametersInfo(
    type = ApiCatalogueHubComponentInfo.class
)
public class ApiCatalogueHubComponent extends EssentialsListComponent {

    private static final Logger log = LoggerFactory.getLogger(ApiCatalogueHubComponent.class);

    boolean showRetired = false;

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {

        long startTime = System.currentTimeMillis();
        log.debug("Start Time:" + startTime);

        // Note remove ApiCatalogueComponent after when work is done.
        ApiCatalogueComponent apiCatalogueComponent = new ApiCatalogueComponent();
        showRetired = apiCatalogueComponent.shouldShowRetired(request);

        super.doBeforeRender(request, response);

        ApiCatalogueFilterManager apiCatalogueFilterManager = new ApiCatalogueFilterManager();
        Optional<Section> status = apiCatalogueFilterManager.getRawFilters(request).getSections().stream()
            .filter(section -> "Status".equals(section.getDisplayName()))
            .findFirst();

        status.ifPresent(section -> {
            Map<String, Section> sectionMap = section.getEntries().stream()
                .collect(Collectors.toMap(
                    Section::getTaxonomyKey,
                    entry -> entry
                ));
            request.setAttribute("apiStatusEntries", sectionMap);
        });

        log.debug("showRetired:" + showRetired);
        request.setAttribute("showRetired", showRetired);
        request.setAttribute("requestContext", request.getRequestContext());
        request.setAttribute("currentQuery", Optional.ofNullable(getAnyParameter(request, "query")).orElse(""));

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        log.info("End of method: doBeforeRender in ApiCatalogueHubComponent  at " + endTime + " ms. Duration: " + duration + " ms");
    }

    @Override
    protected <T extends EssentialsListComponentInfo> Pageable<HippoBean> doFacetedSearch(HstRequest request, T paramInfo, HippoBean scope) {

        Pageable<HippoBean> pageable = DefaultPagination.emptyCollection();
        String relPath = SiteUtils.relativePathFrom(scope, request.getRequestContext());
        HippoFacetNavigationBean facetBean = ContentBeanUtils.getFacetNavigationBean(relPath, this.getSearchQuery(request));
        if (facetBean != null) {
            HippoResultSetBean resultSet = facetBean.getResultSet();
            if (resultSet != null) {
                /*List<HippoBean> filteredList = filterItems(resultSet.getDocumentIterator(HippoBean.class), hippoBean -> {
                    *//*String[] keys = hippoBean.getMultipleProperty("hippotaxonomy:keys");
                    if (keys != null && !showRetired) {
                        return ! Arrays.stream(keys).map(String::toLowerCase).collect(Collectors.toList()).contains("retired");
                    }*//*
                    return true;
                });*/

                //HippoDocumentIterator<HippoBean> iterator = new CustomHippoDocumentIterator(filteredList.stream().iterator());
                HippoDocumentIterator<HippoBean> iterator = resultSet.getDocumentIterator(HippoBean.class);
                pageable = this.getPageableFactory().createPageable(iterator, facetBean.getResultSet().getDocumentSize(), paramInfo.getPageSize(), this.getCurrentPage(request));
                request.setAttribute("totalAvailable", facetBean.getResultSet().getDocumentSize());
            }
        }
        return (Pageable) pageable;
    }

    /*private <T> List<T> filterItems(Iterator<T> iterator, Predicate<T> shouldKeep) {
        Spliterator<T> spliterator = Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED);
        return StreamSupport.stream(spliterator, false)
            .filter(shouldKeep)
            .collect(Collectors.toList());
    }*/
}
