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
import uk.nhs.digital.common.components.info.SearchComponentInfo;
import uk.nhs.digital.common.enums.SearchArea;
import uk.nhs.digital.nil.beans.Indicator;
import uk.nhs.digital.ps.beans.*;
import uk.nhs.digital.website.beans.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private static final String SORT_DATE = "date";
    private static final String SORT_DEFAULT = SORT_RELEVANCE;
    private static final SearchArea AREA_DEFAULT = SearchArea.ALL;

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
        final boolean fallbackEnabled = paramInfo.isFallbackEnabled();
        final boolean contentSearchOverride = getAnyBooleanParam(request, "contentSearch", false);

        //todo: remove after testing
        final String docType = paramInfo.getDoctype();

        Future<QueryResponse> queryResponseFuture = null;
        QueryResponse queryResponse;

        if (contentSearchEnabled || contentSearchOverride) {
            try {
                final int pageSize = paramInfo.getPageSize();
                final int currentPage = getCurrentPage(request);
                final String query = getQueryParameter(request);

                queryResponseFuture = buildAndExecuteContentSearch(pageSize, currentPage, query, docType);
                queryResponse = queryResponseFuture.get(contentSearchTimeOut, TimeUnit.MILLISECONDS);

                if (queryResponse.isOk()) {
                    final long totalResults = queryResponse.getSearchResult().getNumFound();
                    final List<Integer> pageNumbers = getPageNumbers(totalResults, currentPage);
                    Pageable<Document> pageable = new ContentSearchPageable<>(totalResults, currentPage, pageSize);
                    request.setAttribute("pageCount", (int) Math.ceil((double) totalResults / pageSize));
                    request.setAttribute("pageable", pageable);
                    request.setAttribute("pageNumbers", pageNumbers);
                    request.setAttribute("isContentSearch", true);
                    request.setAttribute("queryResponse", queryResponse);
                    request.setAttribute("totalResults", totalResults);
                    request.getRequestContext().setAttribute("isContentSearch", true);
                    setCommonSearchRequestAttributes(request, paramInfo);
                } else {
                    if (fallbackEnabled) {
                        LOGGER.warn("Content Search returned a failure, falling back to HST search");
                        buildAndExecuteHstSearch(request, paramInfo);
                    } else {
                        request.setAttribute("totalResults", 0);
                        request.setAttribute("success", false);
                    }
                }
            } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                queryResponseFuture.cancel(true);

                if (fallbackEnabled) {
                    LOGGER.warn("Content Search response timed out with a timeout of " + contentSearchTimeOut + " ms, falling back to HST search");
                    buildAndExecuteHstSearch(request, paramInfo);
                } else {
                    LOGGER.warn("Content Search response timed out with a timeout of " + contentSearchTimeOut + " ms, HST search fallback disabled");
                    request.setAttribute("totalResults", 0);
                    request.setAttribute("success", false);
                }
            }
        } else {
            buildAndExecuteHstSearch(request, paramInfo);
        }
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
                request.setAttribute("pageNumbers", getPageNumbers(pageable));
            }
        }

        setCommonSearchRequestAttributes(request, paramInfo);
        request.setAttribute("facets", facetNavigationBean);
    }

    private void setCommonSearchRequestAttributes(HstRequest request, SearchComponentInfo paramInfo) {
        request.setAttribute("query", getQueryParameter(request));
        request.setAttribute("sort", getSortOption(request));
        request.setAttribute("area", getAreaOption(request).toString());
        request.setAttribute("cparam", paramInfo);
    }


    private Future<QueryResponse> buildAndExecuteContentSearch(int pageSize, int currentPage, String query, String doctype) {
        ExternalSearchService searchService = HippoServiceRegistry.getService(ExternalSearchService.class);

        QueryBuilder queryBuilder = searchService.builder()
            .catalog("content_en")
            .query(query)
            .limit(pageSize)
            .offset((currentPage - 1) * pageSize)
            .retrieveField("title")
            .retrieveField("summary_content")
            .retrieveField("shortsummary")
            .retrieveField("publicationDate")
            .retrieveField("xmUrl")
            .retrieveField("xmPrimaryDocType")
            .retrieveField("informationType")
            .retrieveField("assuranceDate")
            .retrieveField("publishedBy")
            .retrieveField("details_briefDescription")
            .retrieveField("nominalDate")
            .retrieveField("summary")
            .retrieveField("showLatest");

        if (doctype != null) {
            queryBuilder = appendFilters(queryBuilder, doctype);
        }

        return queryBuilder.build().execute();
    }

    private QueryBuilder appendFilters(QueryBuilder queryBuilder, String doctype) {
        return queryBuilder.filterQuery("xmPrimaryDocType", doctype);
    }

    protected List<Integer> getPageNumbers(Pageable<HippoBean> pageable) {
        int currentPage = pageable.getCurrentPage();
        int buffer = (PAGEABLE_SIZE - 1) / 2;
        int end = min((int) pageable.getTotalPages(), max(currentPage + buffer, PAGEABLE_SIZE));
        int start = max(1, end - PAGEABLE_SIZE + 1);

        return IntStream.rangeClosed(start, end)
            .boxed()
            .collect(toList());
    }

    private List<Integer> getPageNumbers(long totalResults, int currentPage) {

        int buffer = (PAGEABLE_SIZE - 1) / 2;
        int end = min((int) totalResults, max(currentPage + buffer, PAGEABLE_SIZE));
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
            case SORT_DATE:
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

    private String getSortOption(HstRequest request) {
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
