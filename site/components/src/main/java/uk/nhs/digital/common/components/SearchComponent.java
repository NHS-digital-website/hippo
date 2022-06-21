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
import org.hippoecm.hst.site.HstServices;
import org.hippoecm.hst.util.ContentBeanUtils;
import org.hippoecm.hst.util.SearchInputParsingUtils;
import org.hippoecm.repository.HippoStdPubWfNodeType;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import org.onehippo.cms7.services.HippoServiceRegistry;
import org.onehippo.taxonomy.api.Category;
import org.onehippo.taxonomy.api.Taxonomy;
import org.onehippo.taxonomy.api.TaxonomyManager;
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
    private static final String TAXONOMY_CLASSIFICATION_FIELD = "taxonomyClassificationField_taxonomyAllValues_keyPath";
    private static final String TAXONOMY_TOPIC = "topic";
    private static final String PUBLISHED_BY = "publishedBy";
    private static final String REPORTING_LEVEL = "reportingLevel";
    private static final String PUBLICLY_ACCESSIBLE = "publiclyAccessible";
    private static final String ASSURED_STATUS = "assuredStatus";
    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String SEARCH_TAB = "searchTab";

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

    private static final String FACET_ATTRIBUTE_NAME = "name";
    private static final String FACET_ATTRIBUTE_DEPTH = "depth";
    private static final String FACET_ATTRIBUTE_URL = "facetUrl";
    private static final String FACET_ATTRIBUTE_COUNT = "count";
    private static final String FACET_ATTRIBUTE_LABEL = "label";

    private static final int PAGEABLE_SIZE = 5;

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchComponent.class);

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {

        super.doBeforeRender(request, response);
        SearchComponentInfo paramInfo = getComponentInfo(request);

        final boolean contentSearchEnabled = paramInfo.isContentSearchEnabled();
        final long contentSearchTimeOut = paramInfo.getContentSearchTimeOut();
        final boolean contentSearchOverride = getAnyBooleanParam(request, "contentSearch", true);

        boolean mountEnabled = false;
        if (request.getRequestContext().getResolvedMount().getMount().getProperty("contentSearchEnabled") != null) {
            mountEnabled = Boolean.parseBoolean(request.getRequestContext().getResolvedMount().getMount().getProperty("contentSearchEnabled"));
        }

        if (mountEnabled && contentSearchOverride && contentSearchEnabled) {
            Future<QueryResponse> queryResponseFuture = null;
            QueryResponse queryResponse;

            try {
                final int pageSize = paramInfo.getPageSize();
                final int currentPage = getCurrentPage(request);
                String query = getQueryParameter(request);

                if (query != null && query.contains("&")) {
                    query = query.replace("&", "");
                }

                queryResponseFuture = buildAndExecuteContentSearch(request, pageSize, currentPage, query);
                queryResponse = queryResponseFuture.get(contentSearchTimeOut, TimeUnit.MILLISECONDS);

                if (queryResponse.isOk()) {
                    final long totalResults = queryResponse.getSearchResult().getNumFound();
                    final int pageCount = (int) Math.ceil((double) totalResults / pageSize);
                    final List<Integer> pageNumbers = getPageNumbers(currentPage, pageCount);
                    Map<String, Object> facetFields = queryResponse.getFacetCountResult().getFields();
                    configureFacets(facetFields, request, totalResults);

                    Pageable<Document> pageable = new ContentSearchPageable<>(totalResults, currentPage, pageSize);
                    request.setAttribute("totalResults", totalResults);
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
            } catch (InterruptedException | ExecutionException | TimeoutException | RuntimeException ex) {
                boolean interrupted = false;
                if (queryResponseFuture != null) {
                    queryResponseFuture.cancel(true);
                }
                if (ex instanceof InterruptedException || ex instanceof ExecutionException || ex instanceof TimeoutException) {
                    if (ex instanceof InterruptedException) {
                        interrupted = true;
                    }
                    LOGGER.error("Content Search response timed out with a timeout of " + contentSearchTimeOut + " ms, falling back to HST search");
                } else {
                    LOGGER.error("Content Search runtime exception occurred, falling back to HST search ", ex);
                }
                buildAndExecuteHstSearch(request, paramInfo);

                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
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
            .facetField(TAXONOMY_CLASSIFICATION_FIELD)
            .facetField(YEAR)
            .facetField(SEARCH_TAB);

        /* Only add the below facets if the search tab query is present with a valid value */
        if (getAnyParameter(request, SEARCH_TAB) != null && (getAnyParameter(request, SEARCH_TAB).equals("data")
            || getAnyParameter(request, SEARCH_TAB).equals("services") || getAnyParameter(request, SEARCH_TAB).equals("news"))) {
            queryBuilder.facetField(GEOGRAPHIC_COVERAGE)
                .facetField(INFORMATION_TYPE)
                .facetField(GEOGRAPHIC_GRANULARITY)
                .facetField(PUBLISHED_BY)
                .facetField(REPORTING_LEVEL)
                .facetField(ASSURED_STATUS)
                .facetField(PUBLICLY_ACCESSIBLE);
        }

        if (getSortOption(request).equals(SORT_DATE_KEY)) {
            queryBuilder.sortBy(SORT_PUBLICATION_DATE, QueryBuilder.SortType.DESC);
        }

        //Only retrieve Month facets if year is selected.
        if (getAnyParameter(request, YEAR) != null) {
            queryBuilder.facetField(MONTH);
        }

        //If Taxonomy is selected, determine prefix. Otherwise retrieve root Taxonomy.
        if (getAnyParameter(request, TAXONOMY_TOPIC) != null && !getAnyParameter(request, TAXONOMY_TOPIC).equals("root")) {
            final String taxonomyFacet = getAnyParameter(request, TAXONOMY_TOPIC);
            if (taxonomyFacet.contains("/")) {
                final String[] split = taxonomyFacet.split("/", 2);
                String facetIndex = split[0];
                String facetUrl = split[1];
                if (facetIndex != null && facetUrl != null) {
                    int prefixIndex = Integer.parseInt(facetIndex) + 1;
                    String facetPrefix = prefixIndex + "/" + facetUrl;
                    queryBuilder.facetFieldPrefix(TAXONOMY_CLASSIFICATION_FIELD, facetPrefix);
                }
            }

        } else {
            queryBuilder.facetFieldPrefix(TAXONOMY_CLASSIFICATION_FIELD, "0/");
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
        queryBuilder = getQueryBuilder(request, queryBuilder, GEOGRAPHIC_COVERAGE);
        queryBuilder = getQueryBuilder(request, queryBuilder, INFORMATION_TYPE);
        queryBuilder = getQueryBuilder(request, queryBuilder, GEOGRAPHIC_GRANULARITY);
        queryBuilder = getQueryBuilder(request, queryBuilder, PUBLISHED_BY);
        queryBuilder = getQueryBuilder(request, queryBuilder, REPORTING_LEVEL);
        queryBuilder = getQueryBuilder(request, queryBuilder, ASSURED_STATUS);
        queryBuilder = getQueryBuilder(request, queryBuilder, PUBLICLY_ACCESSIBLE);
        queryBuilder = getQueryBuilder(request, queryBuilder, YEAR);
        queryBuilder = getQueryBuilder(request, queryBuilder, SEARCH_TAB);

        if (getAnyParameter(request, MONTH) != null && getAnyParameter(request, YEAR) != null) {
            queryBuilder = appendFilter(queryBuilder, MONTH, getAnyParameter(request, MONTH));
        }

        if (getAnyParameter(request, TAXONOMY_TOPIC) != null && !getAnyParameter(request, TAXONOMY_TOPIC).equals("root")) {
            queryBuilder = appendFilter(queryBuilder, TAXONOMY_CLASSIFICATION_FIELD, getAnyParameter(request, TAXONOMY_TOPIC));
        }

        return queryBuilder;
    }

    private QueryBuilder getQueryBuilder(HstRequest request, QueryBuilder queryBuilder, String geographicCoverage) {
        if (getAnyParameter(request, geographicCoverage) != null) {
            queryBuilder = appendFilter(queryBuilder, geographicCoverage, getAnyParameter(request, geographicCoverage));
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
    private void configureFacets(Map<String, Object> facetFields, HstRequest request, long totalResults) {
        addTaxonomyFacets(facetFields, request, totalResults);
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
            removeNullFacets(fields);

            if (key.equals("xmPrimaryDocType")) {
                configureDocTypeFacets(fields);
            }
            if (key.equals(TAXONOMY_CLASSIFICATION_FIELD)) {
                key = TAXONOMY_TOPIC;
            }
            for (Object field : fields) {
                LinkedHashMap<String, Object> facetField = (LinkedHashMap) field;
                StringBuilder facetUrlBuilder = new StringBuilder().append(currentUrl);
                String facetUrl;
                if (hasQueryString) {
                    if (request.getRequestContext().getServletRequest().getParameter(key) != null) {
                        if (request.getRequestContext().getServletRequest().getParameter(key).equals(facetField.get(FACET_ATTRIBUTE_NAME))) {
                            facetUrl = UriComponentsBuilder.fromHttpUrl(currentUrl)
                                .replaceQueryParam(key).build().toUriString();
                        } else {
                            facetUrl = UriComponentsBuilder.fromHttpUrl(currentUrl)
                                .replaceQueryParam(key, facetField.get(FACET_ATTRIBUTE_NAME)).build().toUriString();
                        }
                    } else {
                        facetUrl = facetUrlBuilder.append("&")
                            .append(key).append("=").append(facetField.get(FACET_ATTRIBUTE_NAME)).toString();
                    }
                } else {
                    facetUrl = facetUrlBuilder.append("?")
                        .append(key).append("=").append(facetField.get(FACET_ATTRIBUTE_NAME)).toString();
                }
                facetField.put(FACET_ATTRIBUTE_URL, facetUrl);
            }

            if (key.equals("searchTab")) {
                configureSearchTabFacets(fields, request, totalResults);
            }

            if (key.equals(YEAR)) {
                final ArrayList<Object> yearFacetList = configureYearFacets(fields);
                ((ArrayList<Object>) entry.getValue()).clear();
                entry.setValue(yearFacetList);
            }
        }
        if (facetFields.get(TAXONOMY_CLASSIFICATION_FIELD) != null && ((ArrayList) facetFields.get(TAXONOMY_CLASSIFICATION_FIELD)).size() > 0) {
            configureTaxonomyFacet((ArrayList) facetFields.get(TAXONOMY_CLASSIFICATION_FIELD), request);
        }
        reorderFacets(facetFields);
    }

    private void addTaxonomyFacets(Map<String, Object> facetFields, HstRequest request, long totalResults) {
        //if attribute set & results > 0, construct facet hierarchy from request parameter
        if (facetFields.get(TAXONOMY_CLASSIFICATION_FIELD) != null && ((ArrayList) facetFields.get(TAXONOMY_CLASSIFICATION_FIELD)).size() == 0
            && totalResults > 0) {
            LinkedHashMap<String, Object> facetField = new LinkedHashMap<>();
            facetField.put(FACET_ATTRIBUTE_COUNT, totalResults);
            facetField.put(FACET_ATTRIBUTE_NAME, getAnyParameter(request, TAXONOMY_TOPIC));
            ArrayList<Map<String, Object>> facetList = new ArrayList<>();

            if (facetField.get("name") != null) {
                facetList.add(facetField);
                facetFields.put(TAXONOMY_CLASSIFICATION_FIELD, facetList);
            }
        }

        //Retrieve and add Parent taxonomy facets
        if (getAnyParameter(request, TAXONOMY_TOPIC) != null
            && !getAnyParameter(request, TAXONOMY_TOPIC).equals("root") && totalResults > 0) {
            ArrayList<Map<String, Object>> taxonomyArray = (ArrayList<Map<String, Object>>) facetFields.get(TAXONOMY_CLASSIFICATION_FIELD);
            if (taxonomyArray.size() > 0 && taxonomyArray.get(0) != null
                && taxonomyArray.get(0).get(FACET_ATTRIBUTE_NAME) != null) {
                String facetKeyPath = (String) taxonomyArray.get(0).get(FACET_ATTRIBUTE_NAME);
                final List<String> taxonomyKeys = Arrays.asList(facetKeyPath.split("/"));
                if (taxonomyKeys.size() > 0) {
                    final int facetIndex = Integer.parseInt(taxonomyKeys.get(0));
                    for (int i = 0; i < facetIndex; i++) {
                        StringBuilder facetName = new StringBuilder();
                        facetName.append(i);
                        boolean isValid = true;
                        for (int k = 0; k < i + 1; k++) {
                            facetName.append("/");
                            if (taxonomyKeys.size() >= k + 1) {
                                facetName.append(taxonomyKeys.get(k + 1));
                            } else {
                                isValid = false;
                            }
                        }
                        facetName.append("/");
                        if (isValid) {
                            LinkedHashMap<String, Object> taxonomyEntry = new LinkedHashMap<>();
                            taxonomyEntry.put(FACET_ATTRIBUTE_NAME, facetName.toString());
                            taxonomyEntry.put(FACET_ATTRIBUTE_DEPTH, i + 1);
                            taxonomyArray.add(taxonomyEntry);
                        }
                    }
                }

                //Add root taxonomy entry
                LinkedHashMap<String, Object> rootTaxonomyEntry = new LinkedHashMap<>();
                rootTaxonomyEntry.put(FACET_ATTRIBUTE_NAME, "root");
                rootTaxonomyEntry.put(FACET_ATTRIBUTE_LABEL, "Reset Taxonomy");
                rootTaxonomyEntry.put(FACET_ATTRIBUTE_DEPTH, 0);
                taxonomyArray.add(rootTaxonomyEntry);
                facetFields.put(TAXONOMY_CLASSIFICATION_FIELD, taxonomyArray);
            }
        }
    }


    private void configureTaxonomyFacet(ArrayList taxonomyFacet, HstRequest request) {
        final String taxonomyDepth = calculateTotalDepth(taxonomyFacet);
        final Map<String, List<Map<String, Object>>> taxonomyHierarchy = createTaxonomyHierarchy(taxonomyFacet, taxonomyDepth);
        addTaxonomyLabels(taxonomyHierarchy);
        request.setAttribute("taxonomyTotalDepth", taxonomyDepth);
        request.setAttribute("taxonomyHierarchy", taxonomyHierarchy);
        request.getRequestContext().setAttribute("taxonomyTotalDepth", taxonomyDepth);
        request.getRequestContext().setAttribute("taxonomyHierarchy", taxonomyHierarchy);
    }

    private void addTaxonomyLabels(Map<String, List<Map<String, Object>>> taxonomyHierarchy) {
        for (String key : taxonomyHierarchy.keySet()) {
            final List<Map<String, Object>> facetsAtDepth = taxonomyHierarchy.get(key);
            for (Map<String, Object> facet : facetsAtDepth) {
                final String facetKeyPath = (String) facet.get(FACET_ATTRIBUTE_NAME);
                if (facetKeyPath != null) {
                    List<String> facetKeys = Arrays.asList(facetKeyPath.split("/"));
                    if (facetKeys.size() >= (Integer.valueOf(key) + 1) && facet.get(FACET_ATTRIBUTE_LABEL) == null) {
                        String facetLabel = calculateTaxonomyLabel(facetKeys);
                        facet.put(FACET_ATTRIBUTE_LABEL, facetLabel);
                    }
                }
            }
        }
    }

    private String calculateTaxonomyLabel(List<String> taxonomyKeys) {
        TaxonomyManager taxonomyManager = HstServices.getComponentManager().getComponent(TaxonomyManager.class.getName());
        Taxonomy taxonomy = taxonomyManager.getTaxonomies().getTaxonomy(HippoBeanHelper.PUBLICATION_TAXONOMY);
        List<String> pathLabels = new ArrayList<>();
        boolean isValid = true;
        for (int i = 1; i < taxonomyKeys.size(); i++) {
            final Category categoryByKey = taxonomy.getCategoryByKey(taxonomyKeys.get(i));
            if (categoryByKey != null && categoryByKey.getInfo(Locale.getDefault()) != null
                && categoryByKey.getInfo(Locale.getDefault()).getName() != null) {
                pathLabels.add(taxonomy.getCategoryByKey(taxonomyKeys.get(i)).getInfo(Locale.getDefault()).getName());
            } else {
                isValid = false;
            }
        }

        if (pathLabels.size() > 0 && isValid) {
            return pathLabels.get(pathLabels.size() - 1);
        } else {
            return null;
        }
    }

    private Map<String, List<Map<String, Object>>> createTaxonomyHierarchy(ArrayList taxonomyFacets, String taxonomyDepth) {

        Map<String, List<Map<String, Object>>> taxonomyHierarchy = new HashMap<>();
        for (Map<String, Object> taxonomyEntry : (ArrayList<Map<String, Object>>) taxonomyFacets) {
            if (taxonomyEntry.get(FACET_ATTRIBUTE_DEPTH) != null) {
                String currentDepth = String.valueOf(taxonomyEntry.get(FACET_ATTRIBUTE_DEPTH));
                if (taxonomyHierarchy.get(currentDepth) == null) {
                    ArrayList<Map<String, Object>> facets = new ArrayList<>();
                    facets.add(taxonomyEntry);
                    taxonomyHierarchy.put(String.valueOf(taxonomyEntry.get(FACET_ATTRIBUTE_DEPTH)), facets);
                } else {
                    List<Map<String, Object>> facets = taxonomyHierarchy.get(currentDepth);
                    facets.add(taxonomyEntry);
                    taxonomyHierarchy.put(currentDepth, facets);
                }
            } else if (taxonomyHierarchy.get(taxonomyDepth) == null) {
                ArrayList<Map<String, Object>> facets = new ArrayList<>();
                facets.add(taxonomyEntry);
                taxonomyHierarchy.put(taxonomyDepth, facets);
            } else {
                final List<Map<String, Object>> returnedFacets = taxonomyHierarchy.get(taxonomyDepth);
                returnedFacets.add(taxonomyEntry);
                taxonomyHierarchy.put(taxonomyDepth, returnedFacets);
            }
        }
        return taxonomyHierarchy;

    }

    private String calculateTotalDepth(ArrayList facets) {
        int maxDepth = 0;
        for (Map<String, Object> facetEntry : (ArrayList<Map<String, Object>>) facets) {
            if (facetEntry.get(FACET_ATTRIBUTE_DEPTH) != null) {
                int currentDepth = (int) facetEntry.get(FACET_ATTRIBUTE_DEPTH);
                maxDepth = max(currentDepth, maxDepth);
            }
        }
        return String.valueOf(maxDepth + 1);
    }

    private void reorderFacets(Map<String, Object> facetFields) {
        LinkedHashMap<String, Object> orderedFacets = new LinkedHashMap<>();

        addFacetFields(facetFields, orderedFacets, XM_PRIMARY_DOC_TYPE);
        addFacetFields(facetFields, orderedFacets, YEAR);
        addFacetFields(facetFields, orderedFacets, MONTH);
        addFacetFields(facetFields, orderedFacets, TAXONOMY_CLASSIFICATION_FIELD);

        //Add remaining facets
        for (Map.Entry<String, Object> facetEntry : facetFields.entrySet()) {
            if (!orderedFacets.containsKey(facetEntry.getKey())) {
                orderedFacets.put(facetEntry.getKey(), facetEntry.getValue());
            }
        }
        facetFields.clear();
        facetFields.putAll(orderedFacets);
    }

    private void addFacetFields(Map<String, Object> facetFields, LinkedHashMap<String, Object> orderedFacets, String fieldName) {
        if (facetFields.get(fieldName) != null) {
            orderedFacets.put(fieldName, facetFields.get(fieldName));
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

    private void removeNullFacets(ArrayList<Object> entry) {
        Iterator<Object> iterable = entry.iterator();
        while (iterable.hasNext()) {
            LinkedHashMap<String, Object> facetField = (LinkedHashMap) iterable.next();
            if (facetField.get(FACET_ATTRIBUTE_NAME) != null && facetField.get(FACET_ATTRIBUTE_NAME).equals("null")) {
                iterable.remove();
            }
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

    private void configureSearchTabFacets(ArrayList<Object> entry, HstRequest request, long totalResults) {
        if (entry.size() < 3) {
            addEmptySearchTabFacets(entry);
        }

        String allTabUrl = request.getAttribute("currentUrl").toString();
        allTabUrl = UriComponentsBuilder.fromHttpUrl(allTabUrl).replaceQueryParam("searchTab").build().toUriString();

        LinkedHashMap<String, Object> allTabFacet = new LinkedHashMap<>();
        allTabFacet.put(FACET_ATTRIBUTE_NAME, "All");
        allTabFacet.put(FACET_ATTRIBUTE_URL, allTabUrl);
        allTabFacet.put(FACET_ATTRIBUTE_COUNT, totalResults);
        entry.add(0, allTabFacet);
    }

    private void addEmptySearchTabFacets(ArrayList<Object> entry) {
        ArrayList<String> configuredSearchTabs = new ArrayList<>();
        for (Object field : entry) {
            LinkedHashMap<String, Object> facetField = (LinkedHashMap) field;
            configuredSearchTabs.add((String) facetField.get(FACET_ATTRIBUTE_NAME));
        }
        if (!configuredSearchTabs.contains("data")) {
            addSearchTab(entry, "data", 0);
        }
        if (!configuredSearchTabs.contains("services")) {
            addSearchTab(entry, "services", 1);
        }
        if (!configuredSearchTabs.contains("news")) {
            addSearchTab(entry, "news", 2);
        }
    }

    private ArrayList<Object> configureYearFacets(ArrayList<Object> entry) {
        List<String> yearArray = new ArrayList<>();
        Map<String, Map<String, String>> yearMap = new HashMap<>();
        for (Object fields : entry) {
            LinkedHashMap<String, Object> facetField = (LinkedHashMap) fields;
            Map<String, String> facetAttributes = new HashMap<>();
            facetAttributes.put(FACET_ATTRIBUTE_URL, (String) facetField.get(FACET_ATTRIBUTE_URL));
            facetAttributes.put(FACET_ATTRIBUTE_COUNT, String.valueOf(facetField.get(FACET_ATTRIBUTE_COUNT)));
            yearMap.put((String) facetField.get(FACET_ATTRIBUTE_NAME), facetAttributes);
            yearArray.add((String) facetField.get(FACET_ATTRIBUTE_NAME));
        }
        Collections.sort(yearArray);
        Collections.reverse(yearArray);

        ArrayList<Object> yearEntry = new ArrayList<>();
        for (String year : yearArray) {
            LinkedHashMap<String, Object> facetFields = new LinkedHashMap<>();
            facetFields.put(FACET_ATTRIBUTE_NAME, year);
            facetFields.put(FACET_ATTRIBUTE_COUNT, Integer.valueOf(yearMap.get(year).get(FACET_ATTRIBUTE_COUNT)));
            facetFields.put(FACET_ATTRIBUTE_URL, yearMap.get(year).get(FACET_ATTRIBUTE_URL));
            yearEntry.add(facetFields);
        }
        return yearEntry;
    }

    private void addSearchTab(ArrayList<Object> entry, String tabName, int tabIndex) {
        LinkedHashMap<String, Object> tabEntry = new LinkedHashMap<>();
        tabEntry.put(FACET_ATTRIBUTE_NAME, tabName);
        tabEntry.put(FACET_ATTRIBUTE_COUNT, 0);
        entry.add(tabIndex, tabEntry);
    }

    /* Method for grouping, website:hub & website:visualhub into one facet 'homepage' */
    private void groupHomePageFacets(ArrayList<Object> entry) {
        int groupCount = 0;
        List<Integer> countArray = new ArrayList<>();
        for (Object fields : entry) {
            LinkedHashMap<String, Object> facetField = (LinkedHashMap) fields;
            String docType = (String) facetField.get(FACET_ATTRIBUTE_NAME);
            int currentCount = (int) facetField.get(FACET_ATTRIBUTE_COUNT);
            countArray.add(currentCount);
            if (docType.equals(WEBSITE_HUB) || docType.equals(WEBSITE_VISUAL_HUB)) {
                groupCount += (int) facetField.get(FACET_ATTRIBUTE_COUNT);
            }
        }

        addEntry(groupCount, countArray, "homepage", entry);
    }

    private void addEntry(int groupCount, List<Integer> countArray, String homepage, ArrayList<Object> entry) {
        if (groupCount > 0) {
            countArray.add(groupCount);
            Collections.sort(countArray);
            Collections.reverse(countArray);
            LinkedHashMap<String, Object> groupedEntry = new LinkedHashMap<>();
            groupedEntry.put(FACET_ATTRIBUTE_COUNT, groupCount);
            groupedEntry.put(FACET_ATTRIBUTE_NAME, homepage);
            entry.add(countArray.indexOf(groupCount), groupedEntry);
        }
    }

    /* Method for grouping, publicationsystem:publication & publication:archive into one facet 'publication' */
    private void groupPublicationFacets(ArrayList<Object> entry) {
        int groupCount = 0;
        List<Integer> countArray = new ArrayList<>();
        for (Object fields : entry) {
            LinkedHashMap<String, Object> facetField = (LinkedHashMap) fields;
            String docType = (String) facetField.get(FACET_ATTRIBUTE_NAME);
            countArray.add((int) facetField.get(FACET_ATTRIBUTE_COUNT));
            if (docType.equals(PUBLICATION_SYSTEM_LEGACY_PUBLICATION) || docType.equals(PUBLICATION_SYSTEM_PUBLICATION) || docType.equals(PUBLICATION_SYSTEM_ARCHIVE)) {
                groupCount += (int) facetField.get(FACET_ATTRIBUTE_COUNT);
            }
        }

        addEntry(groupCount, countArray, "publication", entry);
    }

    private boolean removeDocType(LinkedHashMap<String, Object> facetField) {
        String docType = (String) facetField.get(FACET_ATTRIBUTE_NAME);
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
            }
        }

        setCommonSearchRequestAttributes(request, paramInfo);
        request.setAttribute("area", getAreaOption(request).toString());
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

    SearchArea getAreaOption(HstRequest request) {
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
     * Adding the Blog document type
     */
    private void addFeatureTypes(HstQueryBuilder query) {
        query.ofTypes(
            Feature.class
        );
    }

    private void addFeedHubTypes(HstQueryBuilder query) {
        query.ofTypes(
            FeedHub.class
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
        addFeatureTypes(query);
        addFeedHubTypes(query);
    }

}
