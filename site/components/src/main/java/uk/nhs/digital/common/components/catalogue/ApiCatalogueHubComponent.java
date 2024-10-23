package uk.nhs.digital.common.components.catalogue;

import org.hippoecm.hst.content.beans.standard.*;
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

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {

        long startTime = System.currentTimeMillis();
        log.debug("Start Time:" + startTime);

        super.doBeforeRender(request, response);

        ApiCatalogueFilterManager apiCatalogueFilterManager = new ApiCatalogueFilterManager();

        List<Section> sections = apiCatalogueFilterManager.getRawFilters(request).getSections();

        Optional<Section> status = sections.stream()
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

        List<Section> nonStatusSections = sections.stream()
            .filter(section -> !"Status".equals(section.getDisplayName()))
            .collect(Collectors.toList());

        request.setAttribute("nonStatusSections", nonStatusSections);

        request.setAttribute("requestContext", request.getRequestContext());
        request.setAttribute("currentQuery", Optional.ofNullable(getAnyParameter(request, "query")).orElse(""));

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        log.info("End of method: doBeforeRender in ApiCatalogueHubComponent  at " + endTime + " ms. Duration: " + duration + " ms");
    }

    /* All documents with type apispecification or general or service that do not
     *  have the field showApiResult set as True get removed from the bloom reach
     * 'faceted-api-specification' facet.
     *  ----
     *  So any of the documents that fill those conditions get filtered out
     */
    @Override
    protected <T extends EssentialsListComponentInfo> Pageable<HippoBean> doFacetedSearch(HstRequest request, T paramInfo, HippoBean scope) {

        Pageable<HippoBean> pageable = DefaultPagination.emptyCollection();
        String relPath = SiteUtils.relativePathFrom(scope, request.getRequestContext());
        HippoFacetNavigationBean facetBean = ContentBeanUtils.getFacetNavigationBean(relPath, this.getSearchQuery(request));
        if (facetBean != null) {
            HippoResultSetBean resultSet = facetBean.getResultSet();
            if (resultSet != null) {
                HippoDocumentIterator<HippoBean> iterator = resultSet.getDocumentIterator(HippoBean.class);
                pageable = this.getPageableFactory().createPageable(iterator, facetBean.getResultSet().getDocumentSize(), paramInfo.getPageSize(), this.getCurrentPage(request));
                request.setAttribute("totalAvailable", facetBean.getResultSet().getDocumentSize());
            }
        }
        return (Pageable) pageable;
    }

}
