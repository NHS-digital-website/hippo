package uk.nhs.digital.ps.components;

import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.query.filter.Filter;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.util.SearchInputParsingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.ps.beans.Publication;
import java.util.Calendar;

@ParametersInfo(type = SearchComponentInfo.class)
public class SearchComponent extends BaseHstComponent {

    public static final Logger Log = LoggerFactory.getLogger(SearchComponent.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {

        HstRequestContext requestContext = request.getRequestContext();
        HippoBean scopeBean = requestContext.getContentBean();

        SearchComponentInfo info = getComponentParametersInfo(request);

        try {

            HstQuery query = requestContext.getQueryManager().createQuery(scopeBean, Publication.class);
            query.setLimit(info.getLimit());

            int offset = 0;

            try {
                String offsetString = request.getParameter("offset");
                offset = Integer.parseInt(offsetString);

            } catch (NumberFormatException e) {
                // No offset set in this request, so use default 0
            }

            query.setOffset(offset);

            Filter queryFilter = query.createFilter();
            String unparsedQuery = getPublicRequestParameter(request, "query");
            String parsedQuery = SearchInputParsingUtils.parse(unparsedQuery, false);

            if (StringUtils.isNotEmpty(parsedQuery)){
                queryFilter.addContains(".", parsedQuery); // search all fields "."
            } else {
                return;
            }

            // ONLY show publications already published (exact field TBC)
            Filter dateFilter = query.createFilter();
            dateFilter.addLessOrEqualThan("publicationsystem:NominalDate", Calendar.getInstance());
            queryFilter.addAndFilter(dateFilter);

            query.setFilter(queryFilter);
            HstQueryResult queryResult = query.execute();

            request.setAttribute("result", queryResult.getHippoBeans());
            request.setAttribute("totalSize", queryResult.getTotalSize());
            request.setAttribute("size", queryResult.getSize());
            request.setAttribute("limit", info.getLimit());

        } catch (QueryException e) {
            Log.error("Error searching for publications", e);
            request.setAttribute("error", "Search failed. The error has been logged.");
        }
    }
}
