package uk.nhs.digital.common.components;

import com.google.common.base.Strings;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.manager.ObjectConverter;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.exceptions.FilterException;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.query.filter.BaseFilter;
import org.hippoecm.hst.content.beans.query.filter.Filter;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.repository.util.DateTools;
import org.onehippo.cms7.essentials.components.EssentialsListComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsListComponentInfo;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.components.info.EventsComponentInfo;
import uk.nhs.digital.common.components.info.FeedListComponentInfo;
import uk.nhs.digital.common.util.DocumentUtils;
import uk.nhs.digital.website.beans.Event;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;


public class ComponentHelper extends EssentialsListComponent {
    private static final Logger log = LoggerFactory.getLogger(FeedListComponent.class);

    protected <T extends EssentialsListComponentInfo> Pageable<HippoBean> executeQuery(HstRequest request, T paramInfo, HstQuery query) throws QueryException {
        int pageSize = this.getPageSize(request, paramInfo);
        int page = this.getCurrentPage(request);
        query.setLimit(pageSize);
        query.setOffset((page - 1) * pageSize);
        this.applyExcludeScopes(request, query, paramInfo);
        this.buildAndApplyFilters(request, query);

        try {
            // the query coming from the component is manually extended since it needs to consider intervals
            String eventQueryString = query.getQueryAsString(true);
            // appending the query containing filters the on the interval compound
            String queryString = eventQueryString + addIntervalFilter(request);

            HstRequestContext requestContext = request.getRequestContext();
            QueryManager jcrQueryManager = requestContext.getSession().getWorkspace().getQueryManager();
            Query jcrQuery = jcrQueryManager.createQuery(queryString, "xpath");
            QueryResult queryResult = jcrQuery.execute();

            ObjectConverter objectConverter = requestContext.getContentBeansTool().getObjectConverter();

            NodeIterator it = queryResult.getNodes();
            List parentNodes = new ArrayList();
            List<String> parentPath = new ArrayList();
            //the result of the query contains the interval compound, but the actual result should contain events.
            //For this reason this component needs to fetch the parent node
            while (it.hasNext() && parentPath.size() < pageSize) {
                Node interval = it.nextNode();
                Node eventNode = interval.getParent();
                if (eventNode.getPrimaryNodeType().isNodeType("website:event")
                    && !parentPath.contains(eventNode.getPath())) {
                    parentPath.add(eventNode.getPath());
                    parentNodes.add(objectConverter.getObject(eventNode));
                }
            }

            return this.getPageableFactory().createPageable(parentNodes, page, pageSize);
        } catch (RepositoryException repositoryEx) {
            throw new QueryException(repositoryEx.getMessage());
        } catch (ObjectBeanManagerException converterEx) {
            throw new QueryException(converterEx.getMessage());
        }
    }

    protected String addIntervalFilter(final HstRequest request) {

        Object paramInfoType = getComponentParametersInfo(request);
        String dateField = null;
        boolean hidePastEvents = false;
        if (paramInfoType instanceof FeedListComponentInfo) {
            FeedListComponentInfo paramInfo = getComponentParametersInfo(request);
            dateField = paramInfo.getDocumentDateField();
            hidePastEvents = paramInfo.getHidePastEvents();
        } else if (paramInfoType instanceof EventsComponentInfo) {
            EventsComponentInfo paramInfo = getComponentParametersInfo(request);
            dateField = paramInfo.getDocumentDateField();
            hidePastEvents = paramInfo.getHidePastEvents();
        }

        if (!Strings.isNullOrEmpty(dateField)) {
            //filter list containing dates contraints
            try {
                HstQueryBuilder hstQueryBuider = HstQueryBuilder.create(request.getRequestContext().getSiteContentBaseBean());
                hstQueryBuider.ofTypes("website:interval");
                HstQuery hstQuery = hstQueryBuider.build();

                List<BaseFilter> filters = new ArrayList<>();
                //adding the interval date range constraint
                addIntervalConstraint(filters, hstQuery, dateField, request);

                if (hidePastEvents && paramInfoType instanceof FeedListComponentInfo) {
                    try {
                        final Filter filter = hstQuery.createFilter();
                        filter.addGreaterOrEqualThan(dateField, Calendar.getInstance(), DateTools.Resolution.DAY);
                        filters.add(filter);
                    } catch (FilterException e) {
                        log.error("Error while creating query filter to hide past events using date field {}", dateField, e);
                    }
                }

                final Filter queryFilter = createQueryFilter(request, hstQuery);
                if (queryFilter != null) {
                    filters.add(queryFilter);
                }
                //appling the filters on the hstQuery object
                applyAndFilters(hstQuery, filters);

                // Apply sort
                if (hidePastEvents) {
                    hstQuery.addOrderByAscending(dateField);
                } else {
                    // past events sorted descending order by date, most recent first
                    hstQuery.addOrderByDescending(dateField);
                }

                //removing existing filters the query, since it shouldn't include path, availability and query constraint
                String intervalQueryString = hstQuery.getQueryAsString(true).replaceAll("\\(@hippo:paths='[^']*'\\)( and)?", "");
                intervalQueryString = intervalQueryString.replaceAll("\\(@hippo:availability='[^']*'\\)( and)?", "");
                intervalQueryString = intervalQueryString.replaceAll("not\\(@jcr:primaryType='nt:frozenNode'\\)( and)?", "");
                //removing the first slash, since the string must be appended to an existing xpath query
                return intervalQueryString.substring(1, intervalQueryString.length());
            } catch (FilterException filterException) {
                log.warn("Exceptions while adding event date range filter {} ", filterException, filterException);
            } catch (QueryException queryException) {
                log.warn("Exceptions while getting the string representation of the query {} ", queryException, queryException);
            }
        }

        return "";
    }

    protected void addIntervalConstraint(final List filters, final HstQuery hstQuery, final String dateField, final HstRequest request) throws FilterException {
        Calendar calendar = Calendar.getInstance();
        int year = Integer.parseInt(DocumentUtils.findYearOrDefault(getSelectedYear(request), calendar.get(Calendar.YEAR)));
        final Filter filter = hstQuery.createFilter();
        calendar.set(Calendar.YEAR, year);
        filter.addBetween(dateField, calendar, calendar, DateTools.Resolution.YEAR);
        filters.add(filter);
    }

    protected String getSelectedYear(HstRequest request) {
        return getPublicRequestParameter(request, "year");
    }

    public Map<String, Long> years() {
        try {
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(DocumentUtils.documentsQuery(Event.class), Spliterator.ORDERED), true)
                .filter(e -> ((Event) e).getDisplay())
                .flatMap(e -> ((Event) e).getEvents().parallelStream())
                .map(e -> String.valueOf(e.getStartdatetime().get(Calendar.YEAR)))
                .collect(Collectors.groupingBy(
                    Function.identity(), Collectors.counting()
                ));
        } catch (QueryException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }
}
