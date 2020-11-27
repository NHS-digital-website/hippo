package uk.nhs.digital.common.components;

import com.google.common.base.Strings;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.hippoecm.hst.content.beans.query.*;
import org.hippoecm.hst.content.beans.query.exceptions.*;
import org.hippoecm.hst.content.beans.query.filter.*;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.*;
import org.hippoecm.hst.core.parameters.*;
import org.hippoecm.repository.util.DateTools;
import org.onehippo.cms7.essentials.components.*;
import org.onehippo.cms7.essentials.components.info.*;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import uk.nhs.digital.common.components.info.*;
import uk.nhs.digital.common.util.DocumentUtils;
import uk.nhs.digital.website.beans.News;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ParametersInfo(
    type = LatestNewsComponentInfo.class
)
public class LatestNewsComponent extends EssentialsNewsComponent {
    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);
        request.setAttribute("selectedTypes", request.getParameter("query"));
        request.setAttribute("selectedYear", DocumentUtils.findYearOrDefault(getSelectedYear(request), 0));
        String expandYearsTags = getPublicRequestParameter(request, "expandYearsTags");
        request.setAttribute("expandYearsTags", !Strings.isNullOrEmpty(expandYearsTags) && expandYearsTags.equalsIgnoreCase("true"));
        request.setAttribute("years", years());
    }

    private Map<String, Long> years() {
        try {
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(DocumentUtils.documentsQuery(News.class), Spliterator.ORDERED), true)
                .filter(e -> ((News) e).getDisplay())
                .map(e -> String.valueOf(((News) e).getPublisheddatetime().get(Calendar.YEAR)))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        } catch (QueryException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    @Override
    protected Filter createQueryFilter(final HstRequest request, final HstQuery query) throws FilterException {
        Filter queryFilter = null;

        final String queryParam = getSearchQuery(request);
        if (queryParam != null) {
            String querystring = ComponentUtils.parseAndApplyWildcards(queryParam);

            queryFilter = query.createFilter();
            queryFilter.addContains(".", querystring);
        }

        return queryFilter;
    }

    @Override
    protected int getPageSize(final HstRequest request, final EssentialsPageable paramInfo) {
        //getting the componentPageSize parameter value if defined in the component
        String componentPageSize = StringUtils.defaultIfEmpty(getComponentParameter("defaultPageSize"), "");
        //if the componentPageSize hasn't been defined, then use the component param info
        return NumberUtils.isCreatable(componentPageSize)
            ? Integer.parseInt(componentPageSize) : super.getPageSize(request, paramInfo);
    }

    @Override
    protected <T extends EssentialsListComponentInfo> Pageable<HippoBean> executeQuery(HstRequest request, T paramInfo, HstQuery query) throws QueryException {
        int pageSize = this.getPageSize(request, paramInfo);
        int curPage = this.getCurrentPage(request);
        query.setLimit(pageSize);
        query.setOffset((curPage - 1) * pageSize);
        this.applyExcludeScopes(request, query, paramInfo);
        this.buildAndApplyFilters(request, query);

        /* Set up filters */
        List<BaseFilter> filters = new ArrayList<>();

        final String dateField = ((EssentialsNewsComponentInfo) paramInfo).getDocumentDateField();
        final String selectedYearString = getSelectedYear(request);
        if (!Strings.isNullOrEmpty(dateField) && !Strings.isNullOrEmpty(selectedYearString) && DocumentUtils.isYear(selectedYearString)) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, Integer.parseInt(selectedYearString));

            final Filter yearFilter = query.createFilter();
            yearFilter.addBetween(dateField, calendar, calendar, DateTools.Resolution.YEAR);
            filters.add(yearFilter);
        }

        Filter queryFilter = createQueryFilter(request, query);
        if (queryFilter != null) {
            filters.add(queryFilter);
        }

        applyAndFilters(query, filters);

        query.addOrderByDescending(dateField);

        HstQueryResult queryResult = query.execute();

        List<HippoBean> news = IteratorUtils.toList(queryResult.getHippoBeans());

        return this.getPageableFactory().createPageable(news, curPage, pageSize);
    }

    protected String getSelectedYear(HstRequest request) {
        return getPublicRequestParameter(request, "year");
    }
}
