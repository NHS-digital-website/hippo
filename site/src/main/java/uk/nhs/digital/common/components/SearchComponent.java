package uk.nhs.digital.common.components;

import org.apache.commons.lang.math.NumberUtils;
import org.hippoecm.hst.component.support.bean.BaseHstComponent;
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
import uk.nhs.digital.ps.beans.Publication;

import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.constraint;
import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.or;

@ParametersInfo(type = SearchComponentInfo.class)
public class SearchComponent extends BaseHstComponent {

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {

        HstRequestContext requestContext = request.getRequestContext();
        HippoBean scopeBean = requestContext.getContentBean();
        SearchComponentInfo info = getComponentParametersInfo(request);
        String query = SearchInputParsingUtils.parse(
            getPublicRequestParameter(request, "query"), false);
        int offset = NumberUtils.toInt(
            getPublicRequestParameter(request, "offset"), 0);
        HstQueryResult searchResult;

        try {
            searchResult = doSearch(scopeBean, query, offset, info.getLimit());
        } catch (QueryException e) {
            throw new HstComponentException(
                "Exception occurred during creation or execution of HstQuery.", e);
        }

        request.setAttribute("result", searchResult.getHippoBeans());
        request.setAttribute("totalSize", searchResult.getTotalSize());
        request.setAttribute("size", searchResult.getSize());
        request.setAttribute("limit", info.getLimit());
    }

    private HstQueryResult doSearch(HippoBean scope, String query, int offset, int limit) throws QueryException {
        HstQuery hstQuery = HstQueryBuilder.create(scope)
            .ofTypes(Publication.class)
            .where(or(
                constraint("publicationsystem:Title").contains(query),
                constraint("publicationsystem:Summary").contains(query),
                constraint(".").contains(query)
            ))
            .limit(limit)
            .offset(offset)
            .build();

        return hstQuery.execute();
    }
}
