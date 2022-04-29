package uk.nhs.digital.common.components;

import static org.apache.commons.collections.IteratorUtils.toList;
import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.and;
import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.constraint;
import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.or;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.query.builder.Constraint;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.repository.util.DateTools;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import uk.nhs.digital.website.beans.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FeedHubComponent extends ContentRewriterComponent {

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);

        List<Map> filters = new ArrayList<>();
        Map<String, String[]> filterValues = new HashMap<>();

        String[] yearParams = getPublicRequestParameters(request, "year");
        filterValues.put("year", yearParams);
        if (yearParams.length > 0) {
            filterValues.put("month", getPublicRequestParameters(request, "month"));
        }
        filterValues.put("type[]", getPublicRequestParameters(request, "type[]"));
        filterValues.put("severity", getPublicRequestParameters(request, "severity"));
        request.setAttribute("filterValues", filterValues);
        long filterCount = filterValues.values()
            .stream()
            .filter(value -> value.length > 0).count();
        request.setAttribute("filterCount", filterCount);

        String queryText = getPublicRequestParameter(request, "query");
        if (queryText == null) {
            queryText = "";
        }
        request.setAttribute("query", queryText);

        String sort = getPublicRequestParameter(request, "sort");
        if (sort == null) {
            sort = "date-desc";
        }
        request.setAttribute("sort", sort);

        String activeFiltersString = getPublicRequestParameter(request, "active-filters");
        if (activeFiltersString != null) {
            String[] activeFilters = activeFiltersString.split(",");
            request.setAttribute("activeFilters", activeFilters);
        }

        try {
            List<HippoBean> feed = getFeed(request, filterValues, queryText, sort);
            request.setAttribute("feed", feed);
            Pageable<HippoBean> pagedFeed = pageResults(feed, request);
            request.setAttribute("pageable", pagedFeed);

            final HstRequestContext context = request.getRequestContext();
            FeedHub feedHub = (FeedHub) context.getContentBean();

            switch (feedHub.getFeedType()) {
                case "News":
                    getNewsFilters(feed, filters);
                    break;
                case "Supplementary information":
                    getSupInfoFilters(feed, filters, filterValues);
                    break;
                case "Events":
                    getEventFilters(feed, filters, filterValues);
                    break;
                case "Cyber Alerts":
                    getCyberAlertFilters(feed, filters, filterValues);
                    break;
                default:
            }
            request.setAttribute("filters", filters);
        } catch (QueryException e) {
            e.printStackTrace();
        }
    }

    private void getNewsFilters(List<HippoBean> newsFeed, List<Map> filters) throws QueryException {
        Map<String, Long> yearFilters = newsFeed.stream()
            .map(e -> (News) e)
            .map(News::getPublisheddatetime)
            .map(e -> String.valueOf(e.get(Calendar.YEAR)))
            .collect(Collectors.groupingBy(
                Function.identity(), Collectors.counting()
            ));

        addFilter("Year", "year", yearFilters, filters);
    }

    private void getSupInfoFilters(List<HippoBean> supInfoFeed, List<Map> filters, Map<String, String[]> filterValues) throws QueryException {
        Map<String, Long> yearFilters = supInfoFeed.stream()
            .map(e -> (SupplementaryInformation) e)
            .map(SupplementaryInformation::getPublishedDate)
            .map(e -> e != null ? String.valueOf(e.get(Calendar.YEAR)) : "Unknown")
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        addFilter("Year", "year", yearFilters, filters);

        if (yearFilters.size() > 0 && filterValues.get("year").length > 0) {
            Map<String, Long> monthFilters = supInfoFeed.stream()
                .map(e -> (SupplementaryInformation) e)
                .map(SupplementaryInformation::getPublishedDate)
                .filter(Objects::nonNull)
                .map(e -> new SimpleDateFormat("MMMMM").format(e.getTime()))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            addFilter("Month", "month", monthFilters, filters);
        }
    }

    private void getEventFilters(List<HippoBean> eventFeed, List<Map> filters, Map<String, String[]> filterValues) throws QueryException {
        Map<String, Long> yearFilters = eventFeed.stream()
            .map(e -> (Event) e)
            .flatMap(e -> e.getEvents().parallelStream()
                .map(Interval::getStartdatetime)
                .map(date -> String.valueOf(date.get(Calendar.YEAR)))
                .distinct())
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        addFilter("Year", "year", yearFilters, filters);

        if (yearFilters.size() > 0 && filterValues.get("year").length > 0) {
            Map<String, Long> monthFilters = eventFeed.stream()
                .map(e -> (Event) e)
                .flatMap(e -> e.getEvents().parallelStream()
                    .map(Interval::getStartdatetime)
                    .map(date -> new SimpleDateFormat("MMMMM").format(date.getTime()))
                    .distinct())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            addFilter("Month", "month", monthFilters, filters);
        }

        Map<String, Long> typeFilters = eventFeed.stream()
            .map(e -> (Event) e)
            .flatMap(e -> Arrays.stream(e.getType()))
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        addFilter("Type", "type[]", typeFilters, filters);
    }

    private void getCyberAlertFilters(List<HippoBean> eventFeed, List<Map> filters, Map<String, String[]> filterValues) throws QueryException {
        Map<String, Long> yearFilters = eventFeed.stream()
            .map(e -> (CyberAlert) e)
            .map(CyberAlert::getPublishedDate)
            .map(e -> String.valueOf(e.get(Calendar.YEAR)))
            .collect(Collectors.groupingBy(
                Function.identity(), Collectors.counting()
            ));

        addFilter("Year", "year", yearFilters, filters);

        if (yearFilters.size() > 0 && filterValues.get("year").length > 0) {
            Map<String, Long> monthFilters = eventFeed.stream()
                .map(e -> (CyberAlert) e)
                .map(CyberAlert::getPublishedDate)
                .map(date -> new SimpleDateFormat("MMMMM").format(date.getTime()))
                .distinct()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            addFilter("Month", "month", monthFilters, filters);
        }

        Map<String, Long> typeFilters = eventFeed.stream()
            .map(e -> (CyberAlert) e)
            .map(CyberAlert::getThreatType)
            .collect(Collectors.groupingBy(
                Function.identity(), Collectors.counting()
            ));

        addFilter("Type", "type[]", typeFilters, filters);

        Map<String, Long> severityFilters = eventFeed.stream()
            .map(e -> (CyberAlert) e)
            .map(CyberAlert::getSeverity)
            .collect(Collectors.groupingBy(
                Function.identity(), Collectors.counting()
            ));

        addFilter("Severity", "severity", severityFilters, filters);
    }

    private <T extends HippoBean> List<T> getFeed(HstRequest request, Map<String, String[]> filterValues, String queryText, String sort) throws QueryException {
        final HstRequestContext context = request.getRequestContext();
        FeedHub feedHub = (FeedHub) context.getContentBean();

        HippoBean folder = feedHub.getParentBean();
        ArrayList<Constraint> constraints = new ArrayList<>();

        if ("Site-wide documents".equalsIgnoreCase(feedHub.getHubType())) {
            folder = RequestContextProvider.get().getSiteContentBaseBean();

            if (feedHub.getFeedType().equals("Cyber Alerts")) {
                constraints.add(constraint("website:publicallyaccessible").equalTo(true));
            } else if (!feedHub.getFeedType().equals("Supplementary information")) {
                constraints.add(constraint("website:display").equalTo(true));
            }
        }

        String dateField = "website:publisheddatetime";

        Class feedClass = null;
        switch (feedHub.getFeedType()) {
            case "News":
                feedClass = News.class;

                if (filterValues.get("year").length > 0) {
                    Calendar newsDateFilter = Calendar.getInstance();
                    newsDateFilter.set(Calendar.YEAR, Integer.parseInt(filterValues.get("year")[0]));
                    newsDateFilter.set(Calendar.DAY_OF_YEAR, 1);

                    constraints.add(constraint(dateField).equalTo(newsDateFilter, DateTools.Resolution.YEAR));
                }

                break;
            case "Events":
                feedClass = Event.class;
                dateField = "website:events/@website:startdatetime";

                if (filterValues.get("year").length > 0) {
                    Calendar eventsDateFilter = Calendar.getInstance();
                    eventsDateFilter.set(Calendar.YEAR, Integer.parseInt(filterValues.get("year")[0]));
                    eventsDateFilter.set(Calendar.DAY_OF_YEAR, 1);
                    constraints.add(constraint("website:events/website:startdatetime").equalTo(eventsDateFilter, DateTools.Resolution.YEAR));

                    if (filterValues.get("month").length > 0) {
                        Integer month = getMonth(filterValues.get("month")[0]);
                        if (month != null) {
                            eventsDateFilter.set(Calendar.MONTH, month);
                            eventsDateFilter.set(Calendar.DAY_OF_MONTH, 1);
                            constraints.add(constraint("website:events/website:startdatetime").equalTo(eventsDateFilter, DateTools.Resolution.MONTH));
                        }
                    }
                }

                if (filterValues.get("type[]").length > 0) {
                    String[] types = filterValues.get("type[]");
                    for (String type : types) {
                        constraints.add(constraint("website:type").equalTo(type));
                    }
                }

                break;
            case "Cyber Alerts":
                feedClass = CyberAlert.class;
                dateField = "publicationsystem:NominalDate";

                if (filterValues.get("year").length > 0) {
                    Calendar cyberAlertsDateFilter = Calendar.getInstance();
                    cyberAlertsDateFilter.set(Calendar.YEAR, Integer.parseInt(filterValues.get("year")[0]));
                    cyberAlertsDateFilter.set(Calendar.DAY_OF_YEAR, 1);

                    constraints.add(constraint(dateField).equalTo(cyberAlertsDateFilter, DateTools.Resolution.YEAR));
                    if (filterValues.get("month").length > 0) {
                        Integer month = getMonth(filterValues.get("month")[0]);
                        if (month != null) {
                            cyberAlertsDateFilter.set(Calendar.MONTH, month);
                            cyberAlertsDateFilter.set(Calendar.DAY_OF_MONTH, 1);
                            constraints.add(constraint(dateField).equalTo(cyberAlertsDateFilter, DateTools.Resolution.MONTH));
                        }
                    }
                }

                if (filterValues.get("type[]").length > 0) {
                    String[] types = filterValues.get("type[]");
                    for (String type : types) {
                        constraints.add(constraint("website:threattype").equalTo(type));
                    }
                }

                if (filterValues.get("severity").length > 0) {
                    constraints.add(or(
                        constraint("website:severity").equalTo(filterValues.get("severity")[0]),
                        constraint("website:severitystatuschanges/website:severity").equalTo(filterValues.get("severity")[0])
                    ));
                }

                break;
            case "Supplementary information":
                feedClass = SupplementaryInformation.class;
                dateField = "publicationsystem:NominalDate";

                if (filterValues.get("year").length > 0) {
                    Calendar supplimentaryInfoDateFilter = Calendar.getInstance();

                    String year = filterValues.get("year")[0];
                    if (year.equals("Unknown")) {
                        constraints.add(constraint(dateField).notExists());
                    } else {
                        supplimentaryInfoDateFilter.set(Calendar.YEAR, Integer.parseInt(year));
                        supplimentaryInfoDateFilter.set(Calendar.DAY_OF_YEAR, 1);
                        DateTools.Resolution dateResolution = DateTools.Resolution.YEAR;

                        if (filterValues.get("month").length > 0) {
                            Integer month = getMonth(filterValues.get("month")[0]);
                            if (month != null) {
                                supplimentaryInfoDateFilter.set(Calendar.MONTH, month);
                                supplimentaryInfoDateFilter.set(Calendar.DAY_OF_MONTH, 1);
                                dateResolution = DateTools.Resolution.MONTH;
                            }
                        }

                        constraints.add(constraint(dateField).equalTo(supplimentaryInfoDateFilter, dateResolution));
                    }
                }

                break;
            default:
        }

        if (queryText != null && !queryText.isEmpty()) {
            constraints.add(or(
                constraint("website:title").contains(queryText),
                constraint("website:shortsummary").contains(queryText)
            ));
        }

        HstQueryBuilder query = HstQueryBuilder.create(folder);
        query.where(and(constraints.toArray(new Constraint[0])))
            .ofTypes(feedClass);

        if (sort.equals("date-asc")) {
            query.orderByAscending(dateField);
        } else {
            query.orderByDescending(dateField);
        }

        HippoBeanIterator beanIterator = query
            .build()
            .execute()
            .getHippoBeans();

        return toList(beanIterator);
    }

    private Integer getMonth(String monthName) {
        try {
            if (monthName != null) {
                Date date = new SimpleDateFormat("MMM").parse(monthName);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                return cal.get(Calendar.MONTH);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void addFilter(String filterName, String filterKey, Map<String, Long> filterValues, List<Map> filters) {
        if (filterValues.size() == 0) {
            return;
        }

        filterValues = filterValues.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        Map<String, Object> filterData = new LinkedHashMap<>();
        filterData.put("values", filterValues);
        filterData.put("key", filterKey);
        filterData.put("name", filterName);
        filters.add(filterData);
    }

    private Pageable<HippoBean> pageResults(List<HippoBean> feed, HstRequest request) {
        int pageSize = 20;
        int page = getAnyIntParameter(request, "page", 1);

        return getPageableFactory().createPageable(
            feed,
            page,
            pageSize
        );
    }
}