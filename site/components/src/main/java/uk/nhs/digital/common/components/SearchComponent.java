package uk.nhs.digital.common.components;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.stream.Collectors.toList;
import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.*;

import com.onehippo.search.integration.api.Document;
import com.onehippo.search.integration.api.ExternalSearchService;
import com.onehippo.search.integration.api.QueryBuilder;
import com.onehippo.search.integration.api.QueryResponse;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.builder.Constraint;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.content.beans.standard.HippoResultSetBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.util.ContentBeanUtils;
import org.hippoecm.hst.util.SearchInputParsingUtils;
import org.hippoecm.repository.HippoStdPubWfNodeType;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import org.onehippo.cms7.services.HippoServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import uk.nhs.digital.common.components.info.SearchComponentInfo;
import uk.nhs.digital.common.enums.SearchArea;
import uk.nhs.digital.nil.beans.Indicator;
import uk.nhs.digital.ps.beans.*;
import uk.nhs.digital.website.beans.*;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

/**
 * We are not extending "EssentialsSearchComponent" because we could not find a elegant way of using our own search
 * HstObject in the faceted search.
 */
@ParametersInfo(type = SearchComponentInfo.class)
public class SearchComponent extends CommonComponent {

    private static final String REQUEST_PARAM_SORT = "sort";
    private static final String REQUEST_PARAM_AREA = "area";
    private static final String SORT_RELEVANCE = "relevance";
    private static final String SORT_DATE_KEY = "date";
    private static final String SORT_PUBLICATION_DATE = "publicationDate";
    private static final String SORT_DEFAULT = SORT_RELEVANCE;
    private static final SearchArea AREA_DEFAULT = SearchArea.ALL;

    private static final String XM_PRIMARY_DOC_TYPE = "xmPrimaryDocType";
    private static final String GEOGRAPHIC_COVERAGE = "geographicCoverage";
    private static final String INFORMATION_TYPE = "informationType";
    private static final String GEOGRAPHIC_GRANULARITY = "geographicGranularity";
    private static final String PUBLISHED_BY = "publishedBy";
    private static final String REPORTING_LEVEL = "reportingLevel";
    private static final String PUBLICLY_ACCESSIBLE = "publiclyAccessible";
    private static final String ASSURED_STATUS = "assuredStatus";
    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String SEARCHTAB = "searchTab";

    private static final String PUBLICATION_SYSTEM_LEGACY_PUBLICATION = "publicationsystem:legacypublication";
    private static final String PUBLICATION_SYSTEM_PUBLICATION = "publicationsystem:publication";
    private static final String PUBLICATION_SYSTEM_ARCHIVE = "publicationsystem:archive";
    private static final String WEBSITE_VISUAL_HUB = "website:visualhub";
    private static final String WEBSITE_HUB = "website:hub";

    private static final String FOLDER_NEWS_AND_EVENTS = "news-and-events";
    private static final String FOLDER_PUBLICATIONS = "publication-system";
    private static final String FOLDER_SERVICES = "services";
    private static final String FOLDER_DATAANDINFORMATION = "data-and-information";
    private static final String FOLDER_NIL = "national-indicator-library";

    private static final String PROPERTY_SEARCH_RANK = "common:searchRank";
    private static final String PROPERTY_ORDERED_SEARCH_DATE = "common:orderedSearchDate";

    private static final int PAGEABLE_SIZE = 5;

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchComponent.class);

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {

        super.doBeforeRender(request, response);
        SearchComponentInfo paramInfo = getComponentInfo(request);

        final boolean contentSearchEnabled = paramInfo.isContentSearchEnabled();
        final long contentSearchTimeOut = paramInfo.getContentSearchTimeOut();
        final boolean contentSearchOverride = getAnyBooleanParam(request, "contentSearch", false);

        Future<QueryResponse> queryResponseFuture = null;
        QueryResponse queryResponse;

        if (contentSearchEnabled || contentSearchOverride) {
            try {
                final int pageSize = paramInfo.getPageSize();
                final int currentPage = getCurrentPage(request);
                final String query = getQueryParameter(request);

                queryResponseFuture = buildAndExecuteContentSearch(request, pageSize, currentPage, query);
                queryResponse = queryResponseFuture.get(contentSearchTimeOut, TimeUnit.MILLISECONDS);

                if (queryResponse.isOk()) {
                    final long totalResults = queryResponse.getSearchResult().getNumFound();
                    final int pageCount = (int) Math.ceil((double) totalResults / pageSize);
                    final List<Integer> pageNumbers = getPageNumbers(currentPage, pageCount);
                    Map<String, Object> facetFields = queryResponse.getFacetCountResult().getFields();
                    request.setAttribute("totalResults", totalResults);
                    configureFacets(facetFields, request);

                    Pageable<Document> pageable = new ContentSearchPageable<>(totalResults, currentPage, pageSize);
                    request.setAttribute("pageCount", pageCount);
                    request.setAttribute("pageable", pageable);
                    request.setAttribute("pageNumbers", pageNumbers);
                    request.setAttribute("isContentSearch", true);
                    request.setAttribute("searchTabs", facetFields.get("searchTab"));
                    request.setAttribute("queryResponse", queryResponse);
                    request.getRequestContext().setAttribute("facetFields", facetFields);
                    request.getRequestContext().setAttribute("searchTabs", facetFields.get("searchTab"));
                    request.getRequestContext().setAttribute("isContentSearch", true);
                    setCommonSearchRequestAttributes(request, paramInfo);
                } else {
                    LOGGER.error("Content Search returned a failure, falling back to HST search");
                    buildAndExecuteHstSearch(request, paramInfo);
                }
            } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                queryResponseFuture.cancel(true);
                LOGGER.error("Content Search response timed out with a timeout of " + contentSearchTimeOut + " ms, falling back to HST search");
                buildAndExecuteHstSearch(request, paramInfo);
            }
        } else {
            buildAndExecuteHstSearch(request, paramInfo);
        }
    }

    private Future<QueryResponse> buildAndExecuteContentSearch(HstRequest request, int pageSize, int currentPage, String query) {
        ExternalSearchService searchService = HippoServiceRegistry.getService(ExternalSearchService.class);
        QueryBuilder queryBuilder = searchService.builder()
            .catalog("content_en")
            .query(query)
            .view("common")
            .limit(pageSize)
            .offset((currentPage - 1) * pageSize)
            .retrieveField("title")
            .retrieveField("shortsummary")
            .retrieveField("xmUrl")
            .retrieveField(XM_PRIMARY_DOC_TYPE)
            .retrieveField(INFORMATION_TYPE)
            .retrieveField(PUBLISHED_BY)
            .facetField(XM_PRIMARY_DOC_TYPE)
            .facetField(GEOGRAPHIC_COVERAGE)
            .facetField(INFORMATION_TYPE)
            .facetField(GEOGRAPHIC_GRANULARITY)
            .facetField(PUBLISHED_BY)
            .facetField(REPORTING_LEVEL)
            .facetField(ASSURED_STATUS)
            .facetField(PUBLICLY_ACCESSIBLE)
            .facetField(YEAR)
            .facetField(SEARCHTAB);

        if (getSortOption(request).equals(SORT_DATE_KEY)) {
            queryBuilder.sortBy(SORT_PUBLICATION_DATE, QueryBuilder.SortType.DESC);
        }

        //Only retrieve Month facets if year is selected.
        if (getAnyParameter(request, YEAR) != null) {
            queryBuilder.facetField(MONTH);
        }

        queryBuilder = appendFacets(request, queryBuilder);

        return queryBuilder.build().execute();
    }

    private QueryBuilder appendFacets(HstRequest request, QueryBuilder queryBuilder) {
        if (getAnyParameter(request, XM_PRIMARY_DOC_TYPE) != null) {
            if (getAnyParameter(request, XM_PRIMARY_DOC_TYPE).equals("publication")) {
                queryBuilder = appendFilter(queryBuilder, XM_PRIMARY_DOC_TYPE, PUBLICATION_SYSTEM_PUBLICATION);
                queryBuilder = appendFilter(queryBuilder, XM_PRIMARY_DOC_TYPE, PUBLICATION_SYSTEM_LEGACY_PUBLICATION);
                queryBuilder = appendFilter(queryBuilder, XM_PRIMARY_DOC_TYPE, PUBLICATION_SYSTEM_ARCHIVE);
            } else if (getAnyParameter(request, XM_PRIMARY_DOC_TYPE).equals("homepage")) {
                queryBuilder = appendFilter(queryBuilder, XM_PRIMARY_DOC_TYPE, WEBSITE_HUB);
                queryBuilder = appendFilter(queryBuilder, XM_PRIMARY_DOC_TYPE, WEBSITE_VISUAL_HUB);
            } else {
                queryBuilder = appendFilter(queryBuilder, XM_PRIMARY_DOC_TYPE, getAnyParameter(request, XM_PRIMARY_DOC_TYPE));
            }
        }
        if (getAnyParameter(request, GEOGRAPHIC_COVERAGE) != null) {
            queryBuilder = appendFilter(queryBuilder, GEOGRAPHIC_COVERAGE, getAnyParameter(request, GEOGRAPHIC_COVERAGE));
        }
        if (getAnyParameter(request, INFORMATION_TYPE) != null) {
            queryBuilder = appendFilter(queryBuilder, INFORMATION_TYPE, getAnyParameter(request, INFORMATION_TYPE));
        }
        if (getAnyParameter(request, GEOGRAPHIC_GRANULARITY) != null) {
            queryBuilder = appendFilter(queryBuilder, GEOGRAPHIC_GRANULARITY, getAnyParameter(request, INFORMATION_TYPE));
        }
        if (getAnyParameter(request, PUBLISHED_BY) != null) {
            queryBuilder = appendFilter(queryBuilder, PUBLISHED_BY, getAnyParameter(request, PUBLISHED_BY));
        }
        if (getAnyParameter(request, REPORTING_LEVEL) != null) {
            queryBuilder = appendFilter(queryBuilder, REPORTING_LEVEL, getAnyParameter(request, REPORTING_LEVEL));
        }
        if (getAnyParameter(request, ASSURED_STATUS) != null) {
            queryBuilder = appendFilter(queryBuilder, ASSURED_STATUS, getAnyParameter(request, ASSURED_STATUS));
        }
        if (getAnyParameter(request, PUBLICLY_ACCESSIBLE) != null) {
            queryBuilder = appendFilter(queryBuilder, PUBLICLY_ACCESSIBLE, getAnyParameter(request, PUBLICLY_ACCESSIBLE));
        }
        if (getAnyParameter(request, MONTH) != null && getAnyParameter(request, YEAR) != null) {
            queryBuilder = appendFilter(queryBuilder, MONTH, getAnyParameter(request, MONTH));
        }
        if (getAnyParameter(request, YEAR) != null) {
            queryBuilder = appendFilter(queryBuilder, YEAR, getAnyParameter(request, YEAR));
        }
        if (getAnyParameter(request, SEARCHTAB) != null) {
            queryBuilder = appendFilter(queryBuilder, SEARCHTAB, getAnyParameter(request, SEARCHTAB));
        }

        return queryBuilder;
    }

    private QueryBuilder appendFilter(QueryBuilder queryBuilder, String facetField, String facetValue) {
        return queryBuilder.filterQuery(facetField, facetValue);
    }

    private List<Integer> getPageNumbers(int currentPage, int pageCount) {
        int buffer = (PAGEABLE_SIZE - 1) / 2;
        int end = min(pageCount, max(currentPage + buffer, PAGEABLE_SIZE));
        int start = max(1, end - PAGEABLE_SIZE + 1);

        return IntStream.rangeClosed(start, end)
            .boxed()
            .collect(toList());
    }

    /* Method for configuring Facets. Sets URL for all facets, groups docType facets
     */
    private void configureFacets(Map<String, Object> facetFields, HstRequest request) {
        final String queryString = request.getRequestContext().getServletRequest().getQueryString();

        StringBuffer baseUrlBuilder = new StringBuffer();
        if (!request.getRequestContext().getResolvedMount().getMount().getVirtualHost().getHostName().equals("localhost")) {
            baseUrlBuilder.append(request.getRequestContext().getServletRequest().getScheme())
                .append("://")
                .append(request.getRequestContext().getBaseURL().getHostName())
                .append(request.getRequestContext().getBaseURL().getRequestPath());
        } else {
            baseUrlBuilder = request.getRequestContext().getServletRequest().getRequestURL();
        }
        StringBuffer baseUrl = new StringBuffer(baseUrlBuilder);
        configureFacetResetUrl(request, baseUrl, queryString);

        if (queryString != null) {
            baseUrlBuilder.append("?")
                .append(queryString);
        }

        String currentUrl = baseUrlBuilder.toString();
        if (queryString != null && queryString.contains("page")) {
            currentUrl = UriComponentsBuilder.fromHttpUrl(baseUrlBuilder.toString())
                .replaceQueryParam(request.getReferenceNamespace() + ":page")
                .replaceQueryParam(request.getReferenceNamespace() + ":pageSize").build().toUriString();
        }
        request.setAttribute("currentUrl", currentUrl);

        boolean hasQueryString = false;
        if (UriComponentsBuilder.fromHttpUrl(currentUrl).build().getQueryParams().size() > 0) {
            hasQueryString = true;
        }

        for (Map.Entry<String, Object> entry : facetFields.entrySet()) {
            ArrayList<Object> fields = (ArrayList<Object>) entry.getValue();
            String key = entry.getKey();

            if (key.equals("xmPrimaryDocType")) {
                configureDocTypeFacets(fields);
            }

            for (Object field : fields) {
                LinkedHashMap<String, Object> facetField = (LinkedHashMap) field;
                StringBuilder facetUrlBuilder = new StringBuilder().append(currentUrl);
                String facetUrl;
                if (hasQueryString) {
                    if (request.getRequestContext().getServletRequest().getParameter(key) != null) {
                        if (request.getRequestContext().getServletRequest().getParameter(key).equals(facetField.get("name"))) {
                            facetUrl = UriComponentsBuilder.fromHttpUrl(currentUrl)
                                .replaceQueryParam(key).build().toUriString();
                        } else {
                            facetUrl = UriComponentsBuilder.fromHttpUrl(currentUrl)
                                .replaceQueryParam(key, facetField.get("name")).build().toUriString();
                        }
                    } else {
                        facetUrl = facetUrlBuilder.append("&")
                            .append(key).append("=").append(facetField.get("name")).toString();
                    }
                } else {
                    facetUrl = facetUrlBuilder.append("?")
                        .append(key).append("=").append(facetField.get("name")).toString();
                }
                facetField.put("facetUrl", facetUrl);
            }
            if (key.equals("searchTab")) {
                configureSearchTabFacets(fields, request);
            }
        }
    }

    private void configureFacetResetUrl(HstRequest request, StringBuffer resetBaseUrl, String queryString) {
        if (queryString != null) {
            StringBuilder fullUrl = new StringBuilder(resetBaseUrl);
            final MultiValueMap<String, String> queryParams =
                UriComponentsBuilder.fromHttpUrl(fullUrl.append("?").append(queryString).toString()).build().getQueryParams();

            boolean firstParamSet = false;
            if (queryParams.containsKey("searchTab")) {
                resetBaseUrl.append("?").append("searchTab=").append(queryParams.get("searchTab").get(0));
                firstParamSet = true;
            }
            if (queryParams.containsKey("query")) {
                if (firstParamSet) {
                    resetBaseUrl.append("&");
                } else {
                    resetBaseUrl.append("?");
                }
                resetBaseUrl.append("query=").append(queryParams.get("query").get(0));
            }
            request.getRequestContext().setAttribute("resetUrl", resetBaseUrl.toString());
        } else {
            request.getRequestContext().setAttribute("resetUrl", resetBaseUrl.toString());
        }
    }

    /* Method for configuring the doctype facets, grouping + removing specific docTypes */
    private void configureDocTypeFacets(ArrayList<Object> entry) {
        groupPublicationFacets(entry);
        groupHomePageFacets(entry);
        Iterator<Object> iterable = entry.iterator();
        while (iterable.hasNext()) {
            LinkedHashMap<String, Object> facetField = (LinkedHashMap) iterable.next();
            if (removeDocType(facetField)) {
                iterable.remove();
            }
        }
    }

    private void configureSearchTabFacets(ArrayList<Object> entry, HstRequest request) {
        Iterator<Object> iterable = entry.iterator();
        while (iterable.hasNext()) {
            LinkedHashMap<String, Object> facetField = (LinkedHashMap) iterable.next();
            if (facetField.get("name").equals("null")) {
                iterable.remove();
            }
        }

        if (entry.size() < 3) {
            addMissingSearchTabs(entry);
        }

        String allTabUrl = request.getAttribute("currentUrl").toString();
        allTabUrl = UriComponentsBuilder.fromHttpUrl(allTabUrl).replaceQueryParam("searchTab").build().toUriString();

        LinkedHashMap<String, Object> allTabEntry = new LinkedHashMap<>();
        allTabEntry.put("name", "All");
        allTabEntry.put("facetUrl", allTabUrl);
        allTabEntry.put("count", request.getAttribute("totalResults"));
        entry.add(0, allTabEntry);
    }

    private void addMissingSearchTabs(ArrayList<Object> entry) {
        ArrayList<String> configuredSearchTabs = new ArrayList<>();
        for (Object o : entry) {
            LinkedHashMap<String, Object> facetField = (LinkedHashMap) o;
            configuredSearchTabs.add((String) facetField.get("name"));
        }

        if (!configuredSearchTabs.contains("data")) {
            LinkedHashMap<String, Object> dataEntry = new LinkedHashMap<>();
            dataEntry.put("name", "data");
            dataEntry.put("count", 0);
            entry.add(0, dataEntry);
        }
        if (!configuredSearchTabs.contains("services")) {
            LinkedHashMap<String, Object> servicesEntry = new LinkedHashMap<>();
            servicesEntry.put("name", "services");
            servicesEntry.put("count", 0);
            entry.add(1, servicesEntry);
        }
        if (!configuredSearchTabs.contains("news")) {
            LinkedHashMap<String, Object> newsEntry = new LinkedHashMap<>();
            newsEntry.put("name", "news");
            newsEntry.put("count", 0);
            entry.add(2, newsEntry);
        }
    }

    /* Method for grouping, website:hub & website:visualhub into one facet 'homepage' */
    private void groupHomePageFacets(ArrayList<Object> entry) {
        int groupCount = 0;
        List<Integer> countArray = new ArrayList<>();
        for (Object fields : entry) {
            LinkedHashMap<String, Object> facetField = (LinkedHashMap) fields;
            String docType = (String) facetField.get("name");
            int currentCount = (int) facetField.get("count");
            countArray.add(currentCount);
            if (docType.equals(WEBSITE_HUB) || docType.equals(WEBSITE_VISUAL_HUB)) {
                int count = (int) facetField.get("count");
                groupCount += count;
            }
        }

        if (groupCount > 0) {
            countArray.add(groupCount);
            Collections.sort(countArray);
            Collections.reverse(countArray);
            LinkedHashMap<String, Object> groupedEntry = new LinkedHashMap<>();
            groupedEntry.put("count", groupCount);
            groupedEntry.put("name", "homepage");
            int groupCountIndex = countArray.indexOf(groupCount);
            entry.add(groupCountIndex, groupedEntry);
        }
    }

    /* Method for grouping, publicationsystem:publication & publication:archive into one facet 'publication' */
    private void groupPublicationFacets(ArrayList<Object> entry) {
        int groupCount = 0;
        List<Integer> countArray = new ArrayList<>();
        for (Object fields : entry) {
            LinkedHashMap<String, Object> facetField = (LinkedHashMap) fields;
            String docType = (String) facetField.get("name");
            int currentCount = (int) facetField.get("count");
            countArray.add(currentCount);
            if (docType.equals(PUBLICATION_SYSTEM_LEGACY_PUBLICATION) || docType.equals(PUBLICATION_SYSTEM_PUBLICATION) || docType.equals(PUBLICATION_SYSTEM_ARCHIVE)) {
                int count = (int) facetField.get("count");
                groupCount += count;
            }
        }

        if (groupCount > 0) {
            countArray.add(groupCount);
            Collections.sort(countArray);
            Collections.reverse(countArray);
            LinkedHashMap<String, Object> groupedEntry = new LinkedHashMap<>();
            groupedEntry.put("count", groupCount);
            groupedEntry.put("name", "publication");
            int groupCountIndex = countArray.indexOf(groupCount);
            entry.add(groupCountIndex, groupedEntry);
        }
    }

    private boolean removeDocType(LinkedHashMap<String, Object> facetField) {
        String docType = (String) facetField.get("name");
        return docType.equals("website:general") || docType.equals("website:componentlist") || docType.equals("website:roadmapitem") || docType.equals("website:apimaster")
            || docType.equals("website:bloghub") || docType.equals("website:apiendpoint") || docType.equals("website:gdprsummary") || docType.equals("website:orgstructure")
            || docType.equals(PUBLICATION_SYSTEM_LEGACY_PUBLICATION) || docType.equals(PUBLICATION_SYSTEM_PUBLICATION) || docType.equals(PUBLICATION_SYSTEM_ARCHIVE)
            || docType.equals(WEBSITE_HUB) || docType.equals(WEBSITE_VISUAL_HUB);
    }

    private void buildAndExecuteHstSearch(HstRequest request, SearchComponentInfo paramInfo) {
        HippoFacetNavigationBean facetNavigationBean = getFacetNavigationBean(request);

        if (facetNavigationBean != null) {
            HippoResultSetBean resultSet = facetNavigationBean.getResultSet();
            if (resultSet != null) {
                Pageable<HippoBean> pageable = getPageableFactory()
                    .createPageable(
                        resultSet.getDocumentIterator(HippoBean.class),
                        facetNavigationBean.getCount().intValue(),
                        paramInfo.getPageSize(),
                        getCurrentPage(request)
                    );

                request.setAttribute("pageable", pageable);
                request.setAttribute("pageNumbers", getHstPageNumbers(pageable));
                request.setAttribute("area", getAreaOption(request).toString());
            }
        }

        setCommonSearchRequestAttributes(request, paramInfo);
        request.setAttribute("facets", facetNavigationBean);
    }

    private void setCommonSearchRequestAttributes(HstRequest request, SearchComponentInfo paramInfo) {
        request.setAttribute("query", getQueryParameter(request));
        request.getRequestContext().setAttribute("query", getQueryParameter(request));
        request.getRequestContext().setAttribute("sort", getSortOption(request));
        request.setAttribute("sort", getSortOption(request));
        request.setAttribute("cparam", paramInfo);
    }

    protected List<Integer> getHstPageNumbers(Pageable<HippoBean> pageable) {
        int currentPage = pageable.getCurrentPage();
        int buffer = (PAGEABLE_SIZE - 1) / 2;
        int end = min((int) pageable.getTotalPages(), max(currentPage + buffer, PAGEABLE_SIZE));
        int start = max(1, end - PAGEABLE_SIZE + 1);

        return IntStream.rangeClosed(start, end)
            .boxed()
            .collect(toList());
    }

    HippoFacetNavigationBean getFacetNavigationBean(HstRequest request) {
        // If we went to a random URL and are on the 404 error page we won't have
        // a relative content path to get the facets from so return null
        String relativeContentPath = request.getRequestContext()
            .getResolvedSiteMapItem()
            .getRelativeContentPath();
        if (relativeContentPath == null
            || !relativeContentPath.startsWith("facet")) {
            return null;
        }

        return ContentBeanUtils.getFacetNavigationBean(buildQuery(request));
    }

    String getQueryParameter(HstRequest request) {
        return getAnyParameter(request, REQUEST_PARAM_QUERY);
    }

    private int getCurrentPage(HstRequest request) {
        return getAnyIntParameter(request, REQUEST_PARAM_PAGE, 1);
    }

    SearchComponentInfo getComponentInfo(HstRequest request) {
        return getComponentParametersInfo(request);
    }

    private HstQuery buildQuery(HstRequest request) {
        String queryParameter = getQueryParameter(request);
        Constraint searchStringConstraint = null;

        HstQueryBuilder queryBuilder = HstQueryBuilder.create(deriveScope(request));

        if (queryParameter != null) {
            String query = SearchInputParsingUtils.parse(queryParameter, true);
            String queryIncWildcards = ComponentUtils.parseAndApplyWildcards(queryParameter);

            searchStringConstraint = or(
                //forcing specific fields first: this will boost the weight of a hit fot those specific property
                constraint(".").contains(query),
                constraint(".").contains(queryIncWildcards),
                constraint("website:title").contains(query),
                constraint("website:summary").contains(query),
                constraint("publicationsystem:title").contains(query),
                constraint("publicationsystem:summary").contains(query),
                constraint("nationalindicatorlibrary:title").contains(query)
            );
        }

        // register content classes
        deriveTypes(queryBuilder, request);

        String sortParam = getSortOption(request);
        switch (sortParam) {
            case SORT_DATE_KEY:
                queryBuilder.orderByDescending(
                    PROPERTY_ORDERED_SEARCH_DATE,
                    "nationalindicatorlibrary:assuranceDate",
                    PROPERTY_SEARCH_RANK,
                    HippoStdPubWfNodeType.HIPPOSTDPUBWF_LAST_MODIFIED_DATE);
                break;
            case SORT_RELEVANCE:
                // This is what we want for data and info - when we have tabs this will need to be made specific
                queryBuilder.orderByDescending(
                    PROPERTY_SEARCH_RANK,
                    "jcr:score",
                    PROPERTY_ORDERED_SEARCH_DATE,
                    "nationalindicatorlibrary:assuranceDate",
                    HippoStdPubWfNodeType.HIPPOSTDPUBWF_LAST_MODIFIED_DATE);
                break;
            default:
                LOGGER.error("Unknown sort mode: " + sortParam);
                break;
        }

        return constructQuery(queryBuilder, searchStringConstraint);
    }

    private HippoBean[] deriveScope(HstRequest request) {
        SearchArea areaParam = getAreaOption(request);

        final HippoBean base = RequestContextProvider.get().getSiteContentBaseBean();
        final HippoBean newsAndEventsFolder = base.getBean(FOLDER_NEWS_AND_EVENTS);
        final HippoBean publicationsFolder = base.getBean(FOLDER_PUBLICATIONS);
        final HippoBean servicesFolder = base.getBean(FOLDER_SERVICES);
        final HippoBean dataAndInformationFolder = base.getBean(FOLDER_DATAANDINFORMATION);
        final HippoBean nationalIndicatorLibraryFolder = base.getBean(FOLDER_NIL);

        List<HippoBean> scopeBeans = new ArrayList<HippoBean>();

        switch (areaParam) {
            case NEWS_AND_EVENTS:
                scopeBeans.add(newsAndEventsFolder);
                break;
            case DATA:
                scopeBeans.add(publicationsFolder);
                scopeBeans.add(nationalIndicatorLibraryFolder);
                scopeBeans.add(dataAndInformationFolder);
                break;
            case SERVICES:
                scopeBeans.add(servicesFolder);
                break;
            case ALL:
                scopeBeans.add(request.getRequestContext().getSiteContentBaseBean());
                break;
            default:
                scopeBeans.add(request.getRequestContext().getSiteContentBaseBean());
        }

        return scopeBeans.toArray(new HippoBean[0]);
    }

    String getSortOption(HstRequest request) {
        return Optional.ofNullable(getAnyParameter(request, REQUEST_PARAM_SORT)).orElse(SORT_DEFAULT);
    }

    private SearchArea getAreaOption(HstRequest request) {
        Optional<String> param = Optional.ofNullable(getAnyParameter(request, REQUEST_PARAM_AREA));

        return param.map(s -> {
            try {
                return SearchArea.valueOf(s.toUpperCase());
            } catch (Exception ex) {
                return AREA_DEFAULT;
            }
        }).orElse(AREA_DEFAULT);
    }

    private HstQuery constructQuery(HstQueryBuilder queryBuilder, Constraint searchStringConstraint) {
        ArrayList<Constraint> constraints = new ArrayList<>(2);
        constraints.add(constraint("common:searchable").equalTo(true));

        if (searchStringConstraint != null) {
            constraints.add(searchStringConstraint);
        }

        return queryBuilder.where(and(constraints.toArray(new Constraint[0]))).build();
    }

    /**
     * Publication System content types that should be included in search
     */
    private void deriveTypes(final HstQueryBuilder query, final HstRequest request) {

        SearchArea areaParam = getAreaOption(request);

        switch (areaParam) {
            case NEWS_AND_EVENTS:
                addNewsAndEventsTypes(query);
                break;
            case DATA:
                addDataAndInfoTypes(query);
                break;
            case SERVICES:
                addServiceTypes(query);
                break;
            default:
                addAllTypes(query);

        }
    }

    /**
     * Adding document type classes related to the Data and information domain
     */
    private void addDataAndInfoTypes(HstQueryBuilder query) {
        query.ofTypes(
            Archive.class,
            Dataset.class,
            LegacyPublication.class,
            Publication.class,
            Series.class,
            Indicator.class
        );
    }

    /**
     * Adding the news and event document types for the News and Events documents.
     */
    private void addNewsAndEventsTypes(HstQueryBuilder query) {
        query.ofTypes(
            News.class,
            Event.class
        );
    }

    /**
     * Adding the Service document type
     */
    private void addServiceTypes(HstQueryBuilder query) {
        query.ofTypes(
            Service.class
        );
    }

    /**
     * Adding the General document type
     */
    private void addGeneralTypes(HstQueryBuilder query) {
        query.ofTypes(
            General.class
        );
    }

    /**
     * Adding the Hub and Visual Hub document type
     */
    private void addHubTypes(HstQueryBuilder query) {
        query.ofTypes(
            Hub.class,
            VisualHub.class
        );
    }

    private void addGdprTypes(HstQueryBuilder query) {
        query.ofTypes(
            Gdprsummary.class,
            Gdprtransparency.class
        );
    }

    /**
     * Adding the Publishedwork and publishedwork chapter document type
     */
    private void addPublishedWorkTypes(HstQueryBuilder query) {
        query.ofTypes(
            Publishedwork.class,
            Publishedworkchapter.class
        );
    }

    /**
     * Adding the Roadmap document type
     */
    private void addRoadmapTypes(HstQueryBuilder query) {
        query.ofTypes(
            Roadmap.class,
            RoadmapItem.class
        );
    }

    /**
     * Adding the Link List (ComponentList) document type
     */
    private void addComponentList(HstQueryBuilder query) {
        query.ofTypes(
            ComponentList.class
        );
    }

    /**
     * Adding the GlossaryList document type
     */
    private void addGlossaryList(HstQueryBuilder query) {
        query.ofTypes(
            GlossaryList.class
        );
    }

    /**
     * Add Cyber alerts document type
     */
    private void addCyberAlert(HstQueryBuilder query) {
        query.ofTypes(
            CyberAlert.class
        );
    }

    /**
     * Adding the Blog document type
     */
    private void addBlogTypes(HstQueryBuilder query) {
        query.ofTypes(
            Blog.class,
            BlogHub.class
        );
    }

    /**
     * Adding the API document type
     */
    private void addApis(HstQueryBuilder query) {
        query.ofTypes(
            ApiMaster.class,
            ApiEndpoint.class,
            ApiSpecification.class
        );
    }

    /**
     * Adding the API document type
     */
    private void addPerson(HstQueryBuilder query) {
        query.ofTypes(
            Person.class
        );
    }

    /**
     * Adding BusinessUnit-based document types
     */
    private void addBusinessUnitRelatedTypes(HstQueryBuilder query) {
        query.ofTypes(
            BusinessUnit.class,
            OrgStructure.class
        );
    }

    /**
     * Adding all the document types supported by the SearchComponent
     */
    private void addAllTypes(HstQueryBuilder query) {
        addDataAndInfoTypes(query);
        addGeneralTypes(query);
        addHubTypes(query);
        addNewsAndEventsTypes(query);
        addServiceTypes(query);
        addGdprTypes(query);
        addPublishedWorkTypes(query);
        addRoadmapTypes(query);
        addComponentList(query);
        addGlossaryList(query);
        addCyberAlert(query);
        addBlogTypes(query);
        addApis(query);
        addPerson(query);
        addBusinessUnitRelatedTypes(query);
    }

}
