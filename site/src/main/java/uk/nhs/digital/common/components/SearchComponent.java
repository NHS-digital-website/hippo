package uk.nhs.digital.common.components;

import static java.lang.Math.*;
import static java.util.stream.Collectors.*;
import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.*;

import org.hippoecm.hst.container.*;
import org.hippoecm.hst.content.beans.query.*;
import org.hippoecm.hst.content.beans.query.builder.*;
import org.hippoecm.hst.content.beans.standard.*;
import org.hippoecm.hst.core.component.*;
import org.hippoecm.hst.core.parameters.*;
import org.hippoecm.hst.util.*;
import org.hippoecm.repository.*;
import org.onehippo.cms7.essentials.components.*;
import org.onehippo.cms7.essentials.components.info.*;
import org.onehippo.cms7.essentials.components.paging.*;
import org.slf4j.*;
import uk.nhs.digital.common.enums.*;
import uk.nhs.digital.nil.beans.*;
import uk.nhs.digital.ps.beans.*;
import uk.nhs.digital.website.beans.*;

import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

/**
 * We are not extending "EssentialsSearchComponent" because we could not find a elegant way of using our own search
 * HstObject in the faceted search.
 */
@ParametersInfo(type = EssentialsListComponentInfo.class)
public class SearchComponent extends CommonComponent {

    private static final String WILDCARD_IN_USE_CHAR = "*";
    private static final int WILDCARD_POSTFIX_MIN_LENGTH = 3;

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

    private static final Logger log = LoggerFactory.getLogger(SearchComponent.class);

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        EssentialsListComponentInfo paramInfo = getComponentInfo(request);
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

        request.setAttribute("query", getQueryParameter(request));
        request.setAttribute("facets", facetNavigationBean);
        request.setAttribute("sort", getSortOption(request));
        request.setAttribute("area", getAreaOption(request).toString());
        request.setAttribute("cparam", paramInfo);
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

    protected HippoFacetNavigationBean getFacetNavigationBean(HstRequest request) {
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

    protected String getQueryParameter(HstRequest request) {
        return getAnyParameter(request, REQUEST_PARAM_QUERY);
    }

    /**
     * Convert query to include wildcard (*) character after each search term.
     * If the term is a phrase, the search is for an exact match, so no wildcard is required,
     * but the escaped backslash must be removed for the results to be accurate.
     * If the term ends with fullstop or comma, do not apply wildcard
     * <p>
     * e.g. lorem ipsum                 =>      lorem* ipsum*
     * "dolor sit" lorem ipsum     =>      "dolor sit" lorem* ipsum*
     * lor ipsum                   =>      lor* ipsum*
     */
    protected String parseAndApplyWildcards(String query) {

        ArrayList<String> terms = new ArrayList<>();

        // Split terms by space, treat phrases (wrapped in double quotes) as a single term
        Matcher matcher = Pattern.compile("(\"[^\"]*\")|([^\\s\"]+)").matcher(query);
        while (matcher.find()) {
            String term = null;
            if (matcher.group(1) != null) {
                // phrase that was quoted so add without wild card
                term = matcher.group(1);
            } else if (matcher.group(2) != null) {
                term = matcher.group(2);

                if (term.length() >= WILDCARD_POSTFIX_MIN_LENGTH
                    && !term.endsWith(".")
                    && !term.endsWith(",")) {
                    // single unquoted word so add wildcard
                    term = matcher.group(2) + WILDCARD_IN_USE_CHAR;
                }
            }

            terms.add(term);
        }

        query = String.join(" ", terms);

        // Escape any special chars but not the quotes, we have already dealt with those
        // Do this last to properly sanitize any input
        return SearchInputParsingUtils.parse(query, true, new char[]{'"'});
    }


    protected int getCurrentPage(HstRequest request) {
        return getAnyIntParameter(request, REQUEST_PARAM_PAGE, 1);
    }

    protected EssentialsListComponentInfo getComponentInfo(HstRequest request) {
        return getComponentParametersInfo(request);
    }

    private HstQuery buildQuery(HstRequest request) {
        String queryParameter = getQueryParameter(request);
        Constraint searchStringConstraint = null;

        HstQueryBuilder queryBuilder = HstQueryBuilder.create(deriveScope(request));

        if (queryParameter != null) {
            String query = SearchInputParsingUtils.parse(queryParameter, true);
            String queryIncWildcards = parseAndApplyWildcards(queryParameter);

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
                log.error("Unknown sort mode: " + sortParam);
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

        return param.map(s -> SearchArea.valueOf(s.toUpperCase())).orElse(AREA_DEFAULT);
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
     * Adding the Hub document type
     */
    private void addHubTypes(HstQueryBuilder query) {
        query.ofTypes(
            Hub.class
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
    }

}
