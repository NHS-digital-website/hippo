package uk.nhs.digital.common.components;

import com.google.common.base.Strings;
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

@ParametersInfo(type = EssentialsListComponentInfo.class)
public class SearchComponent extends EssentialsListComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchComponent.class);

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        long startTime = System.currentTimeMillis();
        LOGGER.debug("SearchComponent2 - Start Time:" + startTime);

        super.doBeforeRender(request, response);
        request.setAttribute("query", SiteUtils.getAnyParameter("query", request, this));

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        LOGGER.info("End of method: doBeforeRender in SearchComponent2 at " + endTime + " ms. Duration: " + duration + " ms");
    }

    protected HippoBean getSearchScope(HstRequest request, String path) {
        String sort = this.cleanupSearchQuery(this.getAnyParameter(request, "sort"));

        // Carry on with the default if no sort is provided, or it's not 'date'
        if (Strings.isNullOrEmpty(sort) || !"date".equalsIgnoreCase(sort)) {
            return setRequestAttributes(request, super.getSearchScope(request, path));
        }

        String theCurrentPath = request.getRequestContext().getResolvedSiteMapItem().getPathInfo();
        if (theCurrentPath.startsWith("search")) {
            String facetPath = theCurrentPath.replaceFirst("search", "faceted-search-by-date");
            return setRequestAttributes(request, this.doGetScopeBean(facetPath), "date");
        }

        // Fallback to default if replacement doesn't work
        return setRequestAttributes(request, super.getSearchScope(request, path));

    }

    private HippoBean setRequestAttributes(HstRequest request, HippoBean scope) {
        return setRequestAttributes(request, scope, "relevance");
    }

    private HippoBean setRequestAttributes(HstRequest request, HippoBean scope, String sortType) {
        request.setAttribute("sort", sortType);
        return scope;
    }

    /*
     * Copy of BR 14.7.13 doFacetedSearch so that "totalResults" can be captured.
     */
    protected <T extends EssentialsListComponentInfo> Pageable<HippoBean> doFacetedSearch(HstRequest request, T paramInfo, HippoBean scope) {
        Pageable<HippoBean> pageable = DefaultPagination.emptyCollection();
        String relPath = SiteUtils.relativePathFrom(scope, request.getRequestContext());
        HippoFacetNavigationBean facetBean = ContentBeanUtils.getFacetNavigationBean(relPath, this.getSearchQuery(request));

        if (facetBean != null) {
            request.setAttribute("totalResults", facetBean.getCount());
            HippoResultSetBean resultSet = facetBean.getResultSet();
            if (resultSet != null) {
                HippoDocumentIterator<HippoBean> iterator = resultSet.getDocumentIterator(HippoBean.class);
                pageable = this.getPageableFactory().createPageable(iterator, resultSet.getCount().intValue(), paramInfo.getPageSize(), this.getCurrentPage(request));
            }
        }

        return pageable;
    }

}
