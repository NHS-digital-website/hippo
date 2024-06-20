package uk.nhs.digital.common.components.catalogue;

import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryManager;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.exceptions.FilterException;
import org.hippoecm.hst.content.beans.query.filter.BaseFilter;
import org.hippoecm.hst.content.beans.query.filter.Filter;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
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
import uk.nhs.digital.common.components.info.ApiCatalogueHubComponentInfo;

import java.util.List;

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

        super.doBeforeRender(request, response);

        ApiCatalogueComponent apiCatalogueComponent = new ApiCatalogueComponent();
        showRetired = apiCatalogueComponent.shouldShowRetired(request);
        log.debug("showRetired:" + showRetired);

        // Get the total results before the facet limit is applied.
        EssentialsListComponentInfo paramInfo = this.getComponentParametersInfo(request);
        HippoBean scope = this.getSearchScope(request, paramInfo.getPath());
        String relPath = SiteUtils.relativePathFrom(scope, request.getRequestContext());
        HippoFacetNavigationBean facetNavigationBean = ContentBeanUtils.getFacetNavigationBean(relPath, this.getSearchQuery(request));
        request.setAttribute("totalAvailable", facetNavigationBean.getCount().intValue());
        request.setAttribute("showRetired", showRetired);
        request.setAttribute("requestContext", request.getRequestContext());
        request.setAttribute("parameterMap",  request.getRequestContext().getBaseURL().getParameterMap());

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        log.info("End of method: doBeforeRender in ApiCatalogueHubComponent  at " + endTime + " ms. Duration: " + duration + " ms");
    }


    @Override
    protected void contributeAndFilters(final List<BaseFilter> filters, final HstRequest request, final HstQuery query) {
        // filter's documents such that the website:showRetired is set to true
        try {
            Filter filter = query.createFilter();
            filter.addEqualTo("website:showRetired", showRetired);
            filters.add(filter);
        } catch (FilterException var7) {
            log.error("An exception occurred while trying to create a query filter showing document with display field on : {}", var7);
        }
    }

    @Override
    protected <T extends EssentialsListComponentInfo> Pageable<HippoBean> doFacetedSearch(HstRequest request, T paramInfo, HippoBean scope) {
        Pageable<HippoBean> pageable = DefaultPagination.emptyCollection();

        try {
            HstQueryManager queryManager = request.getRequestContext().getQueryManager();
            HstQuery query = queryManager.createQuery(scope);

            // Add filter to exclude documents with 'excludeProperty' set to false
            Filter filter = query.createFilter();
            filter.addEqualTo("common:retired", showRetired); // Replace with your property name
            query.setFilter(filter);

            // Execute the query
            HstQueryResult result = query.execute();
            log.info("result" + result);

            // Convert the query result to a pageable
            pageable = null;
        } catch (Exception e) {
            log.error("Error executing faceted search with exclusion filter", e);
        }

        return pageable;
    }
}
