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
import uk.nhs.digital.common.components.info.ApiCatalogueHubComponentInfo;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

        request.setAttribute("showRetired", showRetired);
        request.setAttribute("requestContext", request.getRequestContext());
        request.setAttribute("parameterMap",  request.getRequestContext().getBaseURL().getParameterMap());

        if (Boolean.valueOf(request.getAttribute("showRetired").toString())) {
            request.setAttribute("totalAvailable", facetNavigationBean.getCount().intValue());
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        log.info("End of method: doBeforeRender in ApiCatalogueHubComponent  at " + endTime + " ms. Duration: " + duration + " ms");
    }

    /*
    StreamSupport.stream(
    Spliterators.spliteratorUnknownSize(resultSet.getDocumentIterator(HippoBean.class), Spliterator.ORDERED), false)
    .collect(Collectors.toList()).get(1).getMultipleProperty("hippotaxonomy:keys")
     */

    @Override
    protected <T extends EssentialsListComponentInfo> Pageable<HippoBean> doFacetedSearch(HstRequest request, T paramInfo, HippoBean scope) {
        ApiCatalogueComponent apiCatalogueComponent = new ApiCatalogueComponent();
        if (apiCatalogueComponent.shouldShowRetired(request)) {
            return super.doFacetedSearch(request, paramInfo, scope);
        } else {
            Pageable<HippoBean> pageable = DefaultPagination.emptyCollection();
            String relPath = SiteUtils.relativePathFrom(scope, request.getRequestContext());
            HippoFacetNavigationBean facetBean = ContentBeanUtils.getFacetNavigationBean(relPath, this.getSearchQuery(request));
            if (facetBean != null) {
                HippoResultSetBean resultSet = facetBean.getResultSet();
                if (resultSet != null) {
                    List<HippoBean> filteredList = filterItems(resultSet.getDocumentIterator(HippoBean.class), hippoBean -> {
                        String[] keys = hippoBean.getMultipleProperty("hippotaxonomy:keys");
                        if (keys != null) {
                            return !Arrays.asList(keys).contains("retired-api");
                        }
                        return true;
                    });
                    HippoDocumentIterator<HippoBean> iterator = new CustomHippoDocumentIterator(filteredList.stream().iterator());
                    pageable = this.getPageableFactory().createPageable(iterator, filteredList.size() - 1, paramInfo.getPageSize(), this.getCurrentPage(request));
                    request.setAttribute("totalAvailable", filteredList.size() - 1);
                }
            }
            return (Pageable) pageable;
        }
    }

    private <T> List<T> filterItems(Iterator<T> iterator, Predicate<T> filter) {
        Spliterator<T> spliterator = Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED);
        return StreamSupport.stream(spliterator, false)
            .filter(filter)
            .collect(Collectors.toList());
    }

}
