package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.query.builder.Constraint;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.util.SearchInputParsingUtils;
import org.onehippo.cms7.essentials.components.EssentialsSearchComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsListComponentInfo;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import uk.nhs.digital.ps.beans.Publication;
import uk.nhs.digital.ps.beans.Series;
import uk.nhs.digital.ps.beans.Dataset;

import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.constraint;
import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.or;

@ParametersInfo(type = EssentialsListComponentInfo.class)
public class SearchComponent extends EssentialsSearchComponent {

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {

        HstRequestContext requestContext = request.getRequestContext();
        HippoBean scopeBean = requestContext.getContentBean();
        final EssentialsListComponentInfo paramInfo = getComponentParametersInfo(request);
        String query = SearchInputParsingUtils.parse(
            getPublicRequestParameter(request, "query"), false);
        final int pageSize = getPageSize(request, paramInfo);
        final int currentPageNumber = getCurrentPage(request);
        final int offset = (currentPageNumber - 1) * pageSize;

        HstQueryResult searchResult;

        try {
            searchResult = doSearch(scopeBean, query, offset, pageSize);
        } catch (QueryException e) {
            throw new HstComponentException(
                "Exception occurred during creation or execution of HstQuery.", e);
        }

        final Pageable<?extends HippoBean> pageable;

        pageable = getPageableFactory().createPageable(
            searchResult.getHippoBeans(),
            searchResult.getTotalSize(),
            pageSize,
            currentPageNumber);

        populateRequest(request, paramInfo, pageable);
    }

    private HstQueryResult doSearch(HippoBean scope, String query, int offset, int pageSize) throws QueryException {
        HstQueryBuilder queryBuilder = HstQueryBuilder.create(scope);

        // register content classes
        addPublicationSystemTypes(queryBuilder);

        HstQuery hstQuery = queryBuilder
            .where(or(
                publicationSystemConstraint(query),
                constraint(".").contains(query)
            ))
            .limit(pageSize)
            .offset(offset)
            .build();

        return hstQuery.execute();
    }

    private void addPublicationSystemTypes(HstQueryBuilder query) {
        query.ofTypes(Publication.class, Series.class, Dataset.class);
    }

    private Constraint publicationSystemConstraint(String query) {
        return or(
            constraint("publicationsystem:Title").contains(query),
            constraint("publicationsystem:Summary").contains(query)
        );
    }
}
