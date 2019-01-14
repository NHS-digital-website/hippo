package uk.nhs.digital.common.components;

import com.google.common.base.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.*;
import org.hippoecm.hst.container.*;
import org.hippoecm.hst.content.beans.*;
import org.hippoecm.hst.content.beans.manager.*;
import org.hippoecm.hst.content.beans.query.*;
import org.hippoecm.hst.content.beans.query.builder.*;
import org.hippoecm.hst.content.beans.query.exceptions.*;
import org.hippoecm.hst.content.beans.query.filter.*;
import org.hippoecm.hst.content.beans.standard.*;
import org.hippoecm.hst.core.component.*;
import org.hippoecm.hst.core.parameters.*;
import org.hippoecm.hst.core.request.*;
import org.hippoecm.repository.util.*;
import org.onehippo.cms7.essentials.components.*;
import org.onehippo.cms7.essentials.components.info.*;
import org.onehippo.cms7.essentials.components.paging.*;
import org.onehippo.forge.selection.hst.contentbean.*;
import org.onehippo.forge.selection.hst.util.*;
import org.slf4j.*;

import uk.nhs.digital.common.components.info.*;

import java.util.*;
import javax.jcr.*;
import javax.jcr.Node;
import javax.jcr.query.*;

@ParametersInfo(
    type = EventsComponentInfo.class
)
public class EventsComponent extends EssentialsEventsComponent {

    private static Logger log = LoggerFactory.getLogger(EventsComponent.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);
        //sending the eventstype values
        final ValueList eventsTypeValueList =
            SelectionUtil.getValueListByIdentifier("eventstype", RequestContextProvider.get());
        if (eventsTypeValueList != null) {
            request.setAttribute("eventstype", SelectionUtil.valueListAsMap(eventsTypeValueList));
        }

        String[] selectedTypes = getPublicRequestParameters(request, "type");
        request.setAttribute("selectedTypes", Arrays.asList(selectedTypes));
    }

    @Override
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

    /**
     * This method is creating an xpath query that will be appended to the query produced by the EssentialListComponent.
     * The main goal of this component is to query the interval compound and NOT the event documents: for this reason
     * we need to customize the query manually to have this behavior
     *
     * @param request neeeded to build the new query
     * @return String containing the query for the interval compound
     */
    protected String addIntervalFilter(final HstRequest request) {
        final EssentialsEventsComponentInfo paramInfo = getComponentParametersInfo(request);

        final String dateField = paramInfo.getDocumentDateField();
        if (!Strings.isNullOrEmpty(dateField)) {
            //filter list containing dates contraints
            try {
                HstQueryBuilder hstQueryBuider = HstQueryBuilder.create(request.getRequestContext().getSiteContentBaseBean());
                hstQueryBuider.ofTypes("website:interval");
                HstQuery hstQuery = hstQueryBuider.build();

                List<BaseFilter> filters = new ArrayList<>();
                //adding the interval date range constraint
                addIntervalConstraint(filters, hstQuery, dateField, request);

                final Filter queryFilter = createQueryFilter(request, hstQuery);
                if (queryFilter != null) {
                    filters.add(queryFilter);
                }
                //appling the filters on the hstQuery object
                applyAndFilters(hstQuery, filters);

                // Apply sort
                if (paramInfo.getHidePastEvents()) {
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
                log.warn("Exceptions while adding event date range filter {} ", filterException);
            } catch (QueryException queryException) {
                log.warn("Exceptions while getting the string representation of the query {} ", queryException);
            }
        }

        return "";
    }

    /**
     * This method adds the website:display constraint in order to consider documents having that property set to true
     */
    @Override
    protected void contributeAndFilters(final List<BaseFilter> filters, final HstRequest request, final HstQuery query) {
        try {
            Filter filter = query.createFilter();
            filter.addEqualTo("website:display", true);
            filters.add(filter);
        } catch (FilterException var7) {
            log.error("An exception occurred while trying to create a query filter showing document with display field on : {}", var7);
        }
    }

    @Override
    protected int getPageSize(final HstRequest request, final EssentialsPageable paramInfo) {
        //getting the componentPageSize parameter value if defined in the component
        String compononentPageSize = StringUtils.defaultIfEmpty(getComponentLocalParameter("defaultPageSize"), "");
        //if the componentPageSize hasn't been defined, then use the component param info
        return NumberUtils.isCreatable(compononentPageSize)
            ? Integer.parseInt(compononentPageSize) : super.getPageSize(request, paramInfo);
    }

    protected void addIntervalConstraint(final List filters, final HstQuery hstQuery, final String dateField, final HstRequest request) throws FilterException {

        final EssentialsEventsComponentInfo paramInfo = getComponentParametersInfo(request);
        final Filter eventsFilter = hstQuery.createFilter();

        if (paramInfo.getHidePastEvents()) {
            //in case of latest event component, the constraints will be the upcoming and currently running events
            eventsFilter.addGreaterOrEqualThan(dateField, Calendar.getInstance(), DateTools.Resolution.HOUR);
            Calendar today = Calendar.getInstance();
            //including the currently running events
            final Filter runningEventsFilter = hstQuery.createFilter();
            runningEventsFilter.addLessOrEqualThan(dateField, today, DateTools.Resolution.HOUR);
            runningEventsFilter.addGreaterOrEqualThan("website:enddatetime", today, DateTools.Resolution.HOUR);
            eventsFilter.addOrFilter(runningEventsFilter);
            //adding the event to the filters list
            filters.add(eventsFilter);
        } else {
            //in case of past event component, the constraints will be the past events
            eventsFilter.addLessOrEqualThan(dateField, Calendar.getInstance(), DateTools.Resolution.HOUR);
            //adding the event to the filters list
            filters.add(eventsFilter);
        }
    }
}
