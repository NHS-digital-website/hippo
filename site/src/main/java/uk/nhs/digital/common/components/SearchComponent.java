package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.query.builder.Constraint;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.standard.*;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.util.ContentBeanUtils;
import org.hippoecm.hst.util.SearchInputParsingUtils;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsListComponentInfo;
import org.onehippo.cms7.essentials.components.paging.DefaultPagination;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.ps.beans.Publication;
import uk.nhs.digital.ps.beans.Series;
import uk.nhs.digital.ps.beans.Dataset;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.constraint;
import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.or;

/**
 * We are not extending "EssentialsSearchComponent" because we could not find a elegant way of using our own search
 * HstObject in the faceted search.
 */
@ParametersInfo(type = EssentialsListComponentInfo.class)
public class SearchComponent extends CommonComponent {

    private static Logger log = LoggerFactory.getLogger(SearchComponent.class);

    public void doBeforeRender(HstRequest request, HstResponse response) {
        Pageable<HippoBean> pageable = DefaultPagination.emptyCollection();
        EssentialsListComponentInfo paramInfo = getComponentInfo(request);
        HippoFacetNavigationBean facetNav = getFacetNavigationBean(request);

        if (facetNav != null) {
            HippoResultSetBean resultSet = facetNav.getResultSet();
            if (resultSet != null) {
                HippoDocumentIterator<HippoBean> iterator = resultSet.getDocumentIterator(HippoBean.class);
                pageable = getPageableFactory()
                    .createPageable(
                        iterator,
                        resultSet.getCount().intValue(),
                        paramInfo.getPageSize(),
                        getCurrentPage(request)
                    );
            }
            else {
                log.warn("Facet navigation result set is null!? No results for '{}'", getQueryParameter(request));
            }
        } else {
            log.warn("Facet navigation is null!? No results for '{}'", getQueryParameter(request));
        }

        request.setAttribute("query", getQueryParameter(request));
        request.setAttribute("pageable", pageable);
        request.setAttribute("cparam", paramInfo);
    }

    public HippoFacetNavigationBean getFacetNavigationBean(HstRequest request) {
        HippoBean scopeBean = request.getRequestContext().getContentBean();
        String facetRelativePath = scopeBean.getPath().replace(getContentRootPath(request) + "/", "");
        HstQuery query = buildQuery(request);

        return ContentBeanUtils.getFacetNavigationBean(query, facetRelativePath);
    }

    protected String getQueryParameter(HstRequest request) {
        return SearchInputParsingUtils.parse(getAnyParameter(request, REQUEST_PARAM_QUERY), false);
    }

    protected int getCurrentPage(HstRequest request) {
        return getAnyIntParameter(request, REQUEST_PARAM_PAGE, 1);
    }

    protected EssentialsListComponentInfo getComponentInfo(HstRequest request) {
        return getComponentParametersInfo(request);
    }

    private HstQuery buildQuery(HstRequest request) {
        String query = getQueryParameter(request);
        HstQueryBuilder queryBuilder = HstQueryBuilder.create(getSearchScope(request));

        // register content classes
        addPublicationSystemTypes(queryBuilder);

        return queryBuilder
            .where(or(
                publicationSystemConstraint(query),
                constraint(".").contains(query)
            ))
            .build();
    }

    private HippoBean getSearchScope(HstRequest request) {
        return request.getRequestContext().getSiteContentBaseBean();
    }

    private String getContentRootPath(HstRequest request) {
        return request.getRequestContext().getResolvedMount().getMount().getContentPath();
    }

    /**
     * Publication System content types that should be included in search
     */
    private void addPublicationSystemTypes(HstQueryBuilder query) {
        query.ofTypes(Publication.class, Series.class, Dataset.class);
    }

    /**
     * Publication System search constraint
     */
    private Constraint publicationSystemConstraint(String query) {
        return or(
            constraint("publicationsystem:Title").contains(query),
            constraint("publicationsystem:Summary").contains(query)
        );
    }
}
