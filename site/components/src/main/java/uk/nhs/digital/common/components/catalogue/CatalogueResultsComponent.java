package uk.nhs.digital.common.components.catalogue;

import static uk.nhs.digital.common.components.catalogue.CatalogueEssentialsFacetsComponent.getFacetFilterMap;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoDocumentIterator;
import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.content.beans.standard.HippoResultSetBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.core.request.ResolvedSiteMapItem;
import org.hippoecm.hst.util.ContentBeanUtils;
import org.hippoecm.hst.util.PathUtils;
import org.onehippo.cms7.essentials.components.EssentialsListComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsListComponentInfo;
import org.onehippo.cms7.essentials.components.paging.DefaultPagination;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import org.onehippo.cms7.essentials.components.utils.SiteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.components.catalogue.filters.Section;
import uk.nhs.digital.common.components.info.CatalogueResultsComponentInfo;

import java.util.*;
import java.util.stream.Collectors;


@ParametersInfo(
    type = CatalogueResultsComponentInfo.class
)
public class CatalogueResultsComponent extends EssentialsListComponent {

    private static final Logger log = LoggerFactory.getLogger(CatalogueResultsComponent.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {

        long startTime = System.currentTimeMillis();
        log.debug("Start Time:" + startTime);

        super.doBeforeRender(request, response);

        CatalogueResultsComponentInfo parameterInfo = this.getComponentParametersInfo(request);
        CatalogueFilterManager catalogueFilterManager = new CatalogueFilterManager(parameterInfo.getTaxonomyFilterMappingDocumentPath());

        List<Section> sections = catalogueFilterManager.getRawFilters(request).getSections();

        Map<String, Section> sectionEntries = sections.stream()
            .flatMap(section -> section.getEntriesAndChildEntries().stream())
            .collect(Collectors.toMap(
                Section::getTaxonomyKey,
                entry -> entry,
                (existing, replacement) -> existing  // Keep the existing entry in case of duplicate keys, but
            ));

        request.setAttribute("sectionEntries", sectionEntries);

        request.setAttribute("requestContext", request.getRequestContext());
        request.setAttribute("currentQuery", Optional.ofNullable(getAnyParameter(request, "query")).orElse(""));

        String queryParam = this.cleanupSearchQuery(this.getAnyParameter(request, "query"));
        HippoFacetNavigationBean hippoFacetNavigationBean = this.getFacetNavigationBean(
            request.getRequestContext(), parameterInfo.getFacetsName(), queryParam);
        request.setModel("facets", hippoFacetNavigationBean);
        request.setModel("facets1", getFacetFilterMap(hippoFacetNavigationBean));

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        log.info("End of method: doBeforeRender in ApiCatalogueHubComponent  at " + endTime + " ms. Duration: " + duration + " ms");
    }



    @Override
    protected <T extends EssentialsListComponentInfo> Pageable<HippoBean> doFacetedSearch(HstRequest request, T paramInfo, HippoBean scope) {

        Pageable<HippoBean> pageable = DefaultPagination.emptyCollection();
        String relPath = SiteUtils.relativePathFrom(scope, request.getRequestContext());
        String searchQuery = this.getSearchQuery(request);

        if (searchQuery != null && !searchQuery.isEmpty()) {
            searchQuery = "xpath(//*[jcr:contains(@website:title,'" + searchQuery + "') or jcr:contains(@website:shortsummary,'" + searchQuery + "')])";
        }

        HippoFacetNavigationBean facetBean = ContentBeanUtils.getFacetNavigationBean(relPath, searchQuery);

        if (facetBean != null) {
            HippoResultSetBean resultSet = facetBean.getResultSet();
            if (resultSet != null) {
                HippoDocumentIterator<HippoBean> iterator = resultSet.getDocumentIterator(HippoBean.class);
                pageable = this.getPageableFactory().createPageable(iterator, facetBean.getResultSet().getDocumentSize(), paramInfo.getPageSize(), this.getCurrentPage(request));
                request.setAttribute("totalAvailable", facetBean.getCount());
            }
        }
        return pageable;
    }

    private HippoFacetNavigationBean getFacetNavigationBean(HstRequestContext context, String path, String query) {
        if (Strings.isNullOrEmpty(path)) {
            log.warn("Facetpath was empty {}", path);
            return null;
        } else {
            ResolvedSiteMapItem resolvedSiteMapItem = context.getResolvedSiteMapItem();
            String resolvedContentPath = PathUtils.normalizePath(resolvedSiteMapItem.getRelativeContentPath());
            String parsedQuery = this.cleanupSearchQuery(query);
            HippoFacetNavigationBean facNavBean;
            if (!StringUtils.isBlank(resolvedContentPath) && !resolvedContentPath.startsWith("/")
                && context.getSiteContentBaseBean().getBean(resolvedContentPath, HippoFacetNavigationBean.class) != null) {
                facNavBean = ContentBeanUtils.getFacetNavigationBean(resolvedContentPath, parsedQuery);
            } else {
                facNavBean = ContentBeanUtils.getFacetNavigationBean(path, parsedQuery);
            }

            return facNavBean;
        }
    }

}
