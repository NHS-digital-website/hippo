package uk.nhs.digital.common.components;

import com.google.common.base.Strings;
import org.hippoecm.hst.content.beans.standard.HippoBean;
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

@ParametersInfo(type = EssentialsListComponentInfo.class)
public class SearchComponent extends EssentialsListComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchComponent.class);

    private static final String QUERY_PARAM = "query";
    private static final String SORT_PARAM = "sort";
    private static final String SORT_ATTR = "sort";
    private static final String TOTAL_RESULTS_ATTR = "totalResults";
    private static final String PAGEABLE_MODEL = "pageable";

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        long startTime = System.currentTimeMillis();
        LOGGER.debug("SearchComponent - Start Time: {}", startTime);

        final String query = SearchQuerySanitizer.getSanitizedSearchQuery(request, this);
        request.setAttribute(QUERY_PARAM, query);

        // Short-circuit unsupported queries BEFORE inherited search logic runs.
        if (Boolean.TRUE.equals(request.getAttribute(SearchQuerySanitizer.UNSUPPORTED_QUERY_ATTR))) {
            LOGGER.warn("Skipping faceted search for unsupported query '{}'", query);
            applyEmptyState(request);
            logEnd(startTime);
            return;
        }

        super.doBeforeRender(request, response);
        logEnd(startTime);
    }

    private void logEnd(long startTime) {
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        LOGGER.info("End of method: doBeforeRender in SearchComponent at {} ms. Duration: {} ms", endTime, duration);
    }

    @Override
    protected String getSearchQuery(HstRequest request) {
        return SearchQuerySanitizer.getSanitizedSearchQuery(request, this);
    }

    /**
     * Preserve the existing behaviour:
     * - default sort = relevance
     * - sort=date switches to the date-based faceted search path
     */
    @Override
    protected HippoBean getSearchScope(HstRequest request, String path) {
        String sort = getSanitizedSort(request);

        // Default behaviour = relevance
        if (Strings.isNullOrEmpty(sort) || !"date".equalsIgnoreCase(sort)) {
            return setRequestAttributes(request, super.getSearchScope(request, path), "relevance");
        }

        if (request.getRequestContext().getResolvedSiteMapItem() == null) {
            return setRequestAttributes(request, super.getSearchScope(request, path), "relevance");
        }

        String currentPath = request.getRequestContext().getResolvedSiteMapItem().getPathInfo();
        if (currentPath != null && currentPath.startsWith("search")) {
            String facetPath = currentPath.replaceFirst("search", "faceted-search-by-date");
            return setRequestAttributes(request, this.doGetScopeBean(facetPath), "date");
        }

        return setRequestAttributes(request, super.getSearchScope(request, path), "relevance");
    }

    /**
     * Only whitelist supported sort values.
     */
    private String getSanitizedSort(HstRequest request) {
        String sort = this.getAnyParameter(request, SORT_PARAM);
        if ("date".equalsIgnoreCase(sort)) {
            return "date";
        }
        return null;
    }

    private HippoBean setRequestAttributes(HstRequest request, HippoBean scope, String sortType) {
        request.setAttribute(SORT_ATTR, sortType);
        return scope;
    }

    private void applyEmptyState(HstRequest request) {
        Pageable<HippoBean> empty = DefaultPagination.emptyCollection();
        request.setModel(PAGEABLE_MODEL, empty);
        request.setAttribute(PAGEABLE_MODEL, empty);
        request.setAttribute(TOTAL_RESULTS_ATTR, 0L);
        request.setAttribute(SORT_ATTR, "relevance");
    }

    /*
     * Copy of BR 14.7.13 doFacetedSearch so that "totalResults" can be captured.
     */
    @Override
    protected <T extends EssentialsListComponentInfo> Pageable<HippoBean> doFacetedSearch(
        HstRequest request, T paramInfo, HippoBean scope) {

        Pageable<HippoBean> pageable = DefaultPagination.emptyCollection();

        if (scope == null) {
            LOGGER.warn("Search scope is null, skipping faceted search");
            request.setAttribute(TOTAL_RESULTS_ATTR, 0L);
            return pageable;
        }

        if (request.getRequestContext().getResolvedSiteMapItem() == null) {
            LOGGER.warn("ResolvedSiteMapItem is null, skipping faceted search");
            request.setAttribute(TOTAL_RESULTS_ATTR, 0L);
            return pageable;
        }

        if (Boolean.TRUE.equals(request.getAttribute(SearchQuerySanitizer.UNSUPPORTED_QUERY_ATTR))) {
            LOGGER.warn("Unsupported free-text query for faceted navigation. Returning empty result set.");
            request.setAttribute(TOTAL_RESULTS_ATTR, 0L);
            return pageable;
        }

        try {
            String relPath = SiteUtils.relativePathFrom(scope, request.getRequestContext());
            HippoFacetNavigationBean facetBean =
                ContentBeanUtils.getFacetNavigationBean(relPath, getSearchQuery(request));

            if (facetBean != null) {
                request.setAttribute(TOTAL_RESULTS_ATTR, facetBean.getCount());

                HippoResultSetBean resultSet = facetBean.getResultSet();
                if (resultSet != null) {
                    pageable = getPageableFactory().createPageable(
                        resultSet.getDocumentIterator(HippoBean.class),
                        resultSet.getCount().intValue(),
                        paramInfo.getPageSize(),
                        getCurrentPage(request)
                    );
                }
            } else {
                request.setAttribute(TOTAL_RESULTS_ATTR, 0L);
            }

        } catch (RuntimeException e) {
            LOGGER.warn("Faceted navigation failed. Returning empty results.");
            request.setAttribute(TOTAL_RESULTS_ATTR, 0L);
            return pageable;
        }

        return pageable;
    }
}