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
import uk.nhs.digital.common.enums.Region;
import uk.nhs.digital.ps.beans.HippoBeanHelper;
import uk.nhs.digital.ps.beans.RestrictableDate;
import uk.nhs.digital.ps.beans.Series;
import uk.nhs.digital.website.beans.CyberAlert;
import uk.nhs.digital.website.beans.Event;
import uk.nhs.digital.website.beans.FeedHub;
import uk.nhs.digital.website.beans.Interval;
import uk.nhs.digital.website.beans.News;
import uk.nhs.digital.website.beans.SupplementaryInformation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FeedHubComponent extends ContentRewriterComponent {
    List<Map> filters;
    Map<String, String[]> filterValues;
    Map<String, String> topicMap;

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);

        filters = new ArrayList<>();
        filterValues = new HashMap<>();
        topicMap = new HashMap<>();

        String[] yearParams = getPublicRequestParameters(request, "year");
        filterValues.put("year", yearParams);
        if (yearParams.length > 0) {
            filterValues.put("month", getPublicRequestParameters(request, "month"));
        }
        filterValues.put("type[]", getPublicRequestParameters(request, "type[]"));
        filterValues.put("severity", getPublicRequestParameters(request, "severity"));

        filterValues.put("status", getPublicRequestParameters(request, "status"));
        filterValues.put("granularity[]", getPublicRequestParameters(request, "granularity[]"));
        filterValues.put("area[]", getPublicRequestParameters(request, "area[]"));
        filterValues.put("information[]", getPublicRequestParameters(request, "information[]"));
        filterValues.put("topic[]", getPublicRequestParameters(request, "topic[]"));
        filterValues.put("upcoming", getPublicRequestParameters(request, "upcoming"));

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
            List<HippoBean> feed = getFeed(request, queryText, sort);
            request.setAttribute("feed", feed);
            Pageable<HippoBean> pagedFeed = pageResults(feed, request);
            request.setAttribute("pageable", pagedFeed);

            final HstRequestContext context = request.getRequestContext();
            FeedHub feedHub = (FeedHub) context.getContentBean();

            switch (feedHub.getFeedType()) {
                case "News":
                    getNewsFilters(feed);
                    break;
                case "Supplementary information":
                    getSupInfoFilters(feed, filterValues);
                    break;
                case "Events":
                    getEventFilters(feed, filterValues);
                    break;
                case "Cyber Alerts":
                    getCyberAlertFilters(feed, filterValues);
                    break;
                case "Series":
                    getSeriesFilters(feed);
                    request.setAttribute("topicMap", topicMap);
                    break;
                default:
            }
            request.setAttribute("filters", filters);
        } catch (QueryException e) {
            e.printStackTrace();
        }
    }

    private void getNewsFilters(List<HippoBean> newsFeed) throws QueryException {
        Map<String, Long> yearFilters = newsFeed.stream()
            .map(e -> (News) e)
            .map(News::getPublisheddatetime)
            .map(e -> String.valueOf(e.get(Calendar.YEAR)))
            .collect(Collectors.groupingBy(
                Function.identity(), Collectors.counting()
            ));

        addFilter("Year", "year", yearFilters);
    }

    private void getSupInfoFilters(List<HippoBean> supInfoFeed, Map<String, String[]> filterValues) throws QueryException {
        Map<String, Long> yearFilters = supInfoFeed.stream()
            .map(e -> (SupplementaryInformation) e)
            .map(SupplementaryInformation::getPublishedDate)
            .map(e -> e != null ? String.valueOf(e.get(Calendar.YEAR)) : "Unknown")
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        addFilter("Year", "year", yearFilters);

        if (yearFilters.size() > 0 && filterValues.get("year").length > 0) {
            Map<String, Long> monthFilters = supInfoFeed.stream()
                .map(e -> (SupplementaryInformation) e)
                .map(SupplementaryInformation::getPublishedDate)
                .filter(Objects::nonNull)
                .map(e -> new SimpleDateFormat("MMMMM").format(e.getTime()))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            addFilter("Month", "month", monthFilters);
        }
    }

    private void getEventFilters(List<HippoBean> eventFeed, Map<String, String[]> filterValues) throws QueryException {
        Map<String, Long> yearFilters = eventFeed.stream()
            .map(e -> (Event) e)
            .flatMap(e -> e.getEvents().parallelStream()
                .map(Interval::getStartdatetime)
                .map(date -> String.valueOf(date.get(Calendar.YEAR)))
                .distinct())
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        addFilter("Year", "year", yearFilters);

        if (yearFilters.size() > 0 && filterValues.get("year").length > 0) {
            Map<String, Long> monthFilters = eventFeed.stream()
                .map(e -> (Event) e)
                .flatMap(e -> e.getEvents().parallelStream()
                    .map(Interval::getStartdatetime)
                    .map(date -> new SimpleDateFormat("MMMMM").format(date.getTime()))
                    .distinct())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            addFilter("Month", "month", monthFilters);
        }

        Map<String, Long> typeFilters = eventFeed.stream()
            .map(e -> (Event) e)
            .flatMap(e -> Arrays.stream(e.getType()))
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        addFilter("Type", "type[]", typeFilters);
    }

    private void getCyberAlertFilters(List<HippoBean> cyberFeed, Map<String, String[]> filterValues) throws QueryException {
        Map<String, Long> severityFilters = cyberFeed.stream()
            .map(e -> (CyberAlert) e)
            .map(CyberAlert::getSeverity)
            .collect(Collectors.groupingBy(
                Function.identity(), Collectors.counting()
            ));

        addFilter("Severity", "severity", severityFilters);

        Map<String, Long> yearFilters = cyberFeed.stream()
            .map(e -> (CyberAlert) e)
            .map(CyberAlert::getPublishedDate)
            .map(e -> String.valueOf(e.get(Calendar.YEAR)))
            .collect(Collectors.groupingBy(
                Function.identity(), Collectors.counting()
            ));

        addFilter("Year", "year", yearFilters);

        if (yearFilters.size() > 0 && filterValues.get("year").length > 0) {
            Map<String, Long> monthFilters = cyberFeed.stream()
                .map(e -> (CyberAlert) e)
                .map(CyberAlert::getPublishedDate)
                .map(date -> new SimpleDateFormat("MMMMM").format(date.getTime()))
                .distinct()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            addFilter("Month", "month", monthFilters);
        }

        Map<String, Long> typeFilters = cyberFeed.stream()
            .map(e -> (CyberAlert) e)
            .map(CyberAlert::getThreatType)
            .collect(Collectors.groupingBy(
                Function.identity(), Collectors.counting()
            ));

        addFilter("Type", "type[]", typeFilters);

        Map<String, Long> cyberSeverityFilters = cyberFeed.stream()
            .map(e -> (CyberAlert) e)
            .map(CyberAlert::getSeverity)
            .collect(Collectors.groupingBy(
                Function.identity(), Collectors.counting()
            ));

        addFilter("Severity", "severity", cyberSeverityFilters);
    }

    private void getSeriesFilters(List<HippoBean> seriesFeed) throws QueryException {
        Map<String, Long> seriesYearFilters = seriesFeed.stream()
            .filter(x -> x instanceof Series && ((Series) x).getPublicationDates() != null && ((Series) x).getPublicationDates().size() > 0)
            .map(e -> (Series) e)
            .map(Series::getPublicationDates)
            .map(date -> String.valueOf(date.get(0).getYear()))
            .collect(Collectors.groupingBy(
                Function.identity(), Collectors.counting()
            ));

        addFilter("Year", "year", seriesYearFilters);

        if (seriesYearFilters.size() > 0 && filterValues.get("year").length > 0) {
            Map<String, Long> monthFilters = seriesFeed.stream()
                .filter(x -> x instanceof Series && ((Series) x).getPublicationDates() != null && ((Series) x).getPublicationDates().size() > 0)
                .map(e -> (Series) e)
                .map(Series::getPublicationDates)
                .map(date -> date.get(0).getMonth().toString())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            addFilter("Month", "month", monthFilters);
        }

        Map<String, Long> seriesTaxFilters = seriesFeed.stream()
            .filter(x -> x instanceof Series && ((Series) x).getKeys() != null)
            .map(e -> (Series) e)
            .flatMap(e -> Arrays.stream(e.getKeys()))
            .collect(Collectors.groupingBy(
                Function.identity(), Collectors.counting()
            ));

        addFilter("Topic", "topic[]", seriesTaxFilters);

        topicMap = HippoBeanHelper.getTaxonomyKeysAndNames(seriesTaxFilters.keySet().toArray(new String[seriesTaxFilters.size()]));

        Map<String, Long> seriesInfoFilters = seriesFeed.stream()
            .filter(e -> e instanceof Series && ((Series) e).getInformationType() != null)
            .map(e -> (Series) e)
            .flatMap(e -> Arrays.stream(e.getInformationType()))
            .collect(Collectors.groupingBy(
                Function.identity(), Collectors.counting()
            ));

        addFilter("Information type", "information[]", seriesInfoFilters);

        Map<String, Long> seriesAreaFilters = seriesFeed.stream()
            .filter(e -> e instanceof Series && ((Series) e).getGeographicCoverage() != null)
            .map(e -> (Series) e)
            .flatMap(e -> Arrays.stream(e.getGeographicCoverage()))
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        addFilter("Geographical area", "area[]", seriesAreaFilters);

        Map<String, Long> seriesGranularityFilters = seriesFeed.stream()
            .filter(e -> e instanceof Series && ((Series) e).getGranularity() != null)
            .map(e -> (Series) e)
            .flatMap(e -> Arrays.stream(e.getGranularity()))
            .collect(Collectors.groupingBy(
                Function.identity(), Collectors.counting()
            ));

        addFilter("Geographical granularity", "granularity[]", seriesGranularityFilters);

        Map<String, Long> upcomingFilters = new HashMap<>();
        upcomingFilters.put("Upcomming publications", -1L);

        addFilter("Upcoming publications", "upcoming", upcomingFilters);
    }

    private <T extends HippoBean> List<T> getFeed(HstRequest request, String queryText, String sort) throws QueryException {
        final HstRequestContext context = request.getRequestContext();
        FeedHub feedHub = (FeedHub) context.getContentBean();
        HippoBean folder = feedHub.getParentBean();
        ArrayList<Constraint> constraints = new ArrayList<>();

        if ("Site-wide documents".equalsIgnoreCase(feedHub.getHubType())) {
            folder = RequestContextProvider.get().getSiteContentBaseBean();

            if (feedHub.getFeedType().equals("Cyber Alerts")) {
                constraints.add(constraint("website:publicallyaccessible").equalTo(true));
            } else if (!feedHub.getFeedType().equals("Supplementary information") && !feedHub.getFeedType().equals("Series")) {
                constraints.add(constraint("website:display").equalTo(true));
            }
        }

        String dateField = "website:publisheddatetime";
        String titleField = "";
        String tierField = "";
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

            case "Series":
                feedClass = Series.class;
                dateField = "hippostdpubwf:lastModificationDate";
                titleField = "publicationsystem:Title";
                tierField = "publicationsystem:publicationTier";

                if (filterValues.get("status").length > 0) {
                    String[] status = filterValues.get("status");
                    constraints.add(constraint("publicationsystem:PubliclyAccessible").equalTo(status[0]));
                }

                if (filterValues.get("granularity[]").length > 0) {
                    String[]geoGran = filterValues.get("granularity[]");
                    for (String granularity : geoGran) {
                        constraints.add(constraint("publicationsystem:Granularity").equalTo(granularity));
                    }
                }

                if (filterValues.get("area[]").length > 0) {
                    String[] areas = Region.convertGeographicCoverageToValues(filterValues.get("area[]"));
                    for (String area : areas) {
                        constraints.add(or(
                            constraint("publicationsystem:GeographicCoverage").equalTo(area),
                            constraint("publicationsystem:properties/publicationsystem:GeographicCoverage").equalTo(area)
                        ));
                    }
                    if (filterValues.get("area[]")[0].equals("Great Britain")) {
                        constraints.add(and(
                            constraint("publicationsystem:GeographicCoverage").notContains("Northern Ireland"),
                            constraint("publicationsystem:properties/publicationsystem:GeographicCoverage").notContains("Northern Ireland")
                        ));
                    }
                    if (filterValues.get("area[]")[0].equals("Great Britain") || filterValues.get("area[]")[0].equals("United Kingdom")) {
                        constraints.add(and(
                            constraint("publicationsystem:GeographicCoverage").notContains("Republic of Ireland"),
                            constraint("publicationsystem:properties/publicationsystem:GeographicCoverage").notContains("Republic of Ireland")
                        ));
                    }
                }

                if (filterValues.get("information[]").length > 0) {
                    String[] information = filterValues.get("information[]");
                    for (String info : information) {
                        constraints.add(constraint("publicationsystem:InformationType").equalTo(info));
                    }
                }

                if (filterValues.get("topic[]").length > 0) {
                    String[] topics = filterValues.get("topic[]");
                    for (String topic : topics) {
                        constraints.add(constraint("hippotaxonomy:keys").equalTo(topic));
                    }
                }
                break;

            default:
        }

        if (queryText != null && !queryText.isEmpty()) {
            if (feedHub.getFeedType().equals("Series")) {
                constraints.add(or(
                    constraint("publicationsystem:Title").contains(queryText),
                    constraint("publicationsystem:Summary").contains(queryText)
                ));
            } else {
                constraints.add(or(
                    constraint("website:title").contains(queryText),
                    constraint("website:shortsummary").contains(queryText)
                ));
            }
        }

        HstQueryBuilder query = HstQueryBuilder.create(folder);

        query.where(and(constraints.toArray(new Constraint[0]))).ofTypes(feedClass);

        if (feedHub.getFeedType().equals("Series")) {
            switch (sort) {
                case "title-desc":
                    query.orderByDescending(titleField);
                    break;
                case "title-asc":
                    query.orderByAscending(titleField);
                    break;
                case "date-asc":
                    query.orderByAscending(dateField)
                        .orderByAscending(tierField)
                        .orderByAscending(titleField);
                    break;
                case "date-desc":
                    query.orderByDescending(dateField)
                        .orderByAscending(tierField)
                        .orderByAscending(titleField);
                    break;
                default:
                    break;
            }
        } else {
            if (sort.equals("date-asc")) {
                query.orderByAscending(dateField);
            } else {
                query.orderByDescending(dateField);
            }
        }

        HippoBeanIterator beanIterator = query
            .build()
            .execute()
            .getHippoBeans();

        if (feedHub.getFeedType().equals("Series")) {
            List<T> beanList = new ArrayList<T>();
            boolean inRange = false;

            for (Object bean : toList(beanIterator)) {
                inRange = false;
                if (bean instanceof Series) {
                    if (filterValues.get("year").length > 0) {
                        ArrayList<RestrictableDate> dateList = ((Series) bean).getPublicationDates();
                        for (RestrictableDate date : dateList) {
                            if (date.getYear() >= Integer.parseInt(filterValues.get("year")[0])
                                && date.getYear() <= Integer.parseInt(filterValues.get("year")[filterValues.get("year").length - 1])) {
                                if (filterValues.get("month").length > 0) {
                                    if (date.getMonth().toString().equals(filterValues.get("month")[0])) {
                                        inRange = true;
                                        break;
                                    }
                                } else {
                                    inRange = true;
                                    break;
                                }
                            }
                        }
                    } else {
                        inRange = true;
                    }

                    if (filterValues.get("upcoming").length > 0) {
                        if (((Series) bean).getHasUpcomming()) {
                            inRange = true;
                        } else {
                            inRange = false;
                        }
                    }

                    if (inRange) {
                        beanList.add((T) bean);
                    }
                }
            }
            return beanList;
        }

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

    private void addFilter(String filterName, String filterKey, Map<String, Long> filterValues) {
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