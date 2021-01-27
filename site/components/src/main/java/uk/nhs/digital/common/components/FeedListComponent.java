package uk.nhs.digital.common.components;

import com.google.common.base.Strings;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.manager.ObjectConverter;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.exceptions.FilterException;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.query.filter.BaseFilter;
import org.hippoecm.hst.content.beans.query.filter.Filter;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.repository.util.DateTools;
import org.onehippo.cms7.essentials.components.EssentialsListComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsListComponentInfo;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import org.onehippo.cms7.essentials.components.utils.ComponentsUtils;
import org.onehippo.forge.selection.hst.contentbean.ValueList;
import org.onehippo.forge.selection.hst.util.SelectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

// tell cpd to start ignoring code - CPD-OFF
@ParametersInfo(
    type = FeedListComponentInfo.class
)
public class FeedListComponent extends EssentialsListComponent {

    private static final Logger log = LoggerFactory.getLogger(FeedListComponent.class);

    /**
     * Fetch the values of type parameters from the URL query string
     *
     * @param request containing the type parameters
     * @return array of type parameters if at least one exists, otherwise empty
     */
    protected String[] getSelectedTypes(HstRequest request) {
        return getPublicRequestParameters(request, "type");
    }

    @Override
    protected void contributeAndFilters(final List<BaseFilter> filters, final HstRequest request, final HstQuery query) {
        FeedListComponentInfo info = getComponentParametersInfo(request);
        // Filter logic from individual List components

        // Filter for News
        if (info.getDocumentTypes().equals("website:news")) {
            if (info.getHideFutureItems()) {
                final String documentDateField = info.getDocumentDateField();
                if (!Strings.isNullOrEmpty(documentDateField)) {
                    try {
                        Filter filter = query.createFilter();
                        filter.addLessOrEqualThan(documentDateField, Calendar.getInstance(), DateTools.Resolution.DAY);
                        filters.add(filter);
                    } catch (FilterException e) {
                        log.error("An exception occurred while trying to create a query filter for hiding future items: {}", e, e);
                    }
                }
            }

            query.addOrderByDescending("website:publisheddatetime");
            //filtering on 'Display on news hub and homepage' property, if it has been selected
            try {
                Filter filter = query.createFilter();
                filter.addEqualTo("website:display", true);
                filters.add(filter);
            } catch (FilterException var7) {
                // Exception is entered twice - once as a parameter for the string, and once as the actual exception thrown
                // see: http://www.slf4j.org/faq.html#paramException
                log.error("An exception occurred while trying to create a query filter showing document with display field on : {}", var7, var7);
            }
        }

        // Filter for Blogs
        if (info.getDocumentTypes().equals("website:blog")) {
            query.addOrderByDescending("website:dateofpublication");
        }

        // Filter for Events
        if (info.getDocumentTypes().equals("website:event")) {
            if (info.getHidePastEvents()) {
                final String dateField = info.getDocumentDateField();
                if (!Strings.isNullOrEmpty(dateField)) {
                    try {
                        final Filter filter = query.createFilter();
                        filter.addGreaterOrEqualThan(dateField, Calendar.getInstance(), DateTools.Resolution.DAY);
                        filters.add(filter);
                    } catch (FilterException e) {
                        log.error("Error while creating query filter to hide past events using date field {}", dateField, e);
                    }
                }
            }

            // filter's documents such that the website:display is set to true
            try {
                Filter filter = query.createFilter();
                filter.addEqualTo("website:display", true);
                filters.add(filter);
            } catch (FilterException var7) {
                log.error("An exception occurred while trying to create a query filter showing document with display field on : {}", var7, var7);
            }

            //fetching the selected types from the request
            String[] selectedTypes = getSelectedTypes(request);
            if (selectedTypes.length > 0) {
                final Filter filter = query.createFilter();
                for (String type : selectedTypes) {
                    try {
                        filter.addEqualTo("@website:type", type);
                    } catch (FilterException filterException) {
                        log.warn("Errors while adding event type filter {}", filterException, filterException);
                    }
                }
                filters.add(filter);
            }
        }
    }

    /**
     * Fetch the value of year parameter from the URL query string
     * <p>
     * Copied from uk.nhs.digital.common.components.EventsComponent
     *
     * @param request containing the year parameter
     * @return the value of the first year parameter if exists, otherwise empty
     */
    protected String getSelectedYear(HstRequest request) {
        return getPublicRequestParameter(request, "year");
    }

    private Map<String, Long> years() {
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

    /**
     * This method is creating an xpath query that will be appended to the query produced by the EssentialListComponent.
     * The main goal of this component is to query the interval compound and NOT the event documents: for this reason
     * we need to customize the query manually to have this behavior
     * <p>
     * Copied from uk.nhs.digital.common.components.EventsComponent
     *
     * @param request needed to build the new query
     * @return String containing the query for the interval compound
     */
    protected String addIntervalFilter(final HstRequest request) {
        final FeedListComponentInfo paramInfo = getComponentParametersInfo(request);

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
                log.warn("Exceptions while adding event date range filter {} ", filterException, filterException);
            } catch (QueryException queryException) {
                log.warn("Exceptions while getting the string representation of the query {} ", queryException, queryException);
            }
        }

        return "";
    }

    /**
     * Copied from uk.nhs.digital.common.components.EventsComponent
     */
    protected void addIntervalConstraint(final List filters, final HstQuery hstQuery, final String dateField, final HstRequest request) throws FilterException {
        Calendar calendar = Calendar.getInstance();
        int year = Integer.parseInt(DocumentUtils.findYearOrDefault(getSelectedYear(request), calendar.get(Calendar.YEAR)));
        final Filter filter = hstQuery.createFilter();
        calendar.set(Calendar.YEAR, year);
        filter.addBetween(dateField, calendar, calendar, DateTools.Resolution.YEAR);
        filters.add(filter);
    }


    /**
     * Copied from uk.nhs.digital.common.components.EventsComponent
     * Added a check for the "website:event" doctype.
     * Runs super if not,
     * Super from org.onehippo.cms7.essentials.components.EssentialsListComponent;
     */
    @Override
    protected <T extends EssentialsListComponentInfo> Pageable<HippoBean> executeQuery(HstRequest request, T paramInfo, HstQuery query) throws QueryException {
        final FeedListComponentInfo info = getComponentParametersInfo(request);
        final String documentTypes = info.getDocumentTypes();
        if (documentTypes.equals("website:event")) {
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
        } else {
            return super.executeQuery(request, paramInfo, query);
        }
    }

    @Override
    public void doBeforeRender(HstRequest request,
                               HstResponse response) throws HstComponentException {

        FeedListComponentInfo info = getComponentParametersInfo(request);
        final String documentTypes = info.getDocumentTypes();
        ComponentsUtils.addCurrentDateStrings(request);
        if (Strings.isNullOrEmpty(documentTypes)) {
            setEditMode(request);
            return;
        }

        super.doBeforeRender(request, response);

        if (info.getDocumentTypes().equals("website:event")) {
            //sending the eventstype values
            final ValueList eventsTypeValueList =
                SelectionUtil.getValueListByIdentifier("eventstype", RequestContextProvider.get());
            if (eventsTypeValueList != null) {
                request.setAttribute("eventstype", SelectionUtil.valueListAsMap(eventsTypeValueList));
            }

            String[] selectedTypes = getSelectedTypes(request);
            request.setAttribute("selectedTypes", Arrays.asList(selectedTypes));
            request.setAttribute("selectedYear", DocumentUtils.findYearOrDefault(getSelectedYear(request), Calendar.getInstance().get(Calendar.YEAR)));
            request.setAttribute("years", years());
        }

        String titleText = info.getTitleText();
        String buttonText = info.getButtonText();
        String buttonDestination = info.getButtonDestination();
        String secondaryButtonText = info.getSecondaryButtonText();
        String secondaryButtonDestination = info.getSecondaryButtonDestination();

        request.setAttribute("titleText", titleText);
        request.setAttribute("buttonText", buttonText);
        request.setAttribute("buttonDestination", buttonDestination);
        request.setAttribute("secondaryButtonText", secondaryButtonText);
        request.setAttribute("secondaryButtonDestination", secondaryButtonDestination);
    }
}
// resume CPD analysis - CPD-ON
