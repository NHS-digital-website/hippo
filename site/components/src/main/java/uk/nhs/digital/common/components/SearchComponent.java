package uk.nhs.digital.common.components;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.stream.Collectors.toList;
import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.*;

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
import org.onehippo.cms7.essentials.components.info.EssentialsListComponentInfo;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.enums.SearchArea;
import uk.nhs.digital.nil.beans.Indicator;
import uk.nhs.digital.ps.beans.Archive;
import uk.nhs.digital.ps.beans.Dataset;
import uk.nhs.digital.ps.beans.LegacyPublication;
import uk.nhs.digital.ps.beans.Publication;
import uk.nhs.digital.ps.beans.Series;
import uk.nhs.digital.website.beans.ApiEndpoint;
import uk.nhs.digital.website.beans.ApiMaster;
import uk.nhs.digital.website.beans.ApiSpecification;
import uk.nhs.digital.website.beans.Blog;
import uk.nhs.digital.website.beans.BlogHub;
import uk.nhs.digital.website.beans.BusinessUnit;
import uk.nhs.digital.website.beans.ComponentList;
import uk.nhs.digital.website.beans.CyberAlert;
import uk.nhs.digital.website.beans.Event;
import uk.nhs.digital.website.beans.Feature;
import uk.nhs.digital.website.beans.FeedHub;
import uk.nhs.digital.website.beans.Gdprsummary;
import uk.nhs.digital.website.beans.Gdprtransparency;
import uk.nhs.digital.website.beans.General;
import uk.nhs.digital.website.beans.GlossaryList;
import uk.nhs.digital.website.beans.Hub;
import uk.nhs.digital.website.beans.News;
import uk.nhs.digital.website.beans.OrgStructure;
import uk.nhs.digital.website.beans.Person;
import uk.nhs.digital.website.beans.Publishedwork;
import uk.nhs.digital.website.beans.Publishedworkchapter;
import uk.nhs.digital.website.beans.Roadmap;
import uk.nhs.digital.website.beans.RoadmapItem;
import uk.nhs.digital.website.beans.Service;
import uk.nhs.digital.website.beans.VisualHub;

import java.util.*;
import java.util.stream.IntStream;

/**
 * We are not extending "EssentialsSearchComponent" because we could not find an elegant way of using our own search
 * HstObject in the faceted search.
 */
@ParametersInfo(type = EssentialsListComponentInfo.class)
public class SearchComponent extends CommonComponent {

    private static final String REQUEST_PARAM_SORT = "sort";
    private static final String REQUEST_PARAM_AREA = "area";
    private static final String SORT_RELEVANCE = "relevance";
    private static final String SORT_DATE_KEY = "date";
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

        LOGGER.debug("SearchComponent doBeforeRender");

        doSearch(request, getComponentParametersInfo(request));
    }

    private void doSearch(HstRequest request, EssentialsListComponentInfo paramInfo) {
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

        request.setAttribute("query", getQueryParameter(request));
        request.getRequestContext().setAttribute("query", getQueryParameter(request));
        request.getRequestContext().setAttribute("sort", getSortOption(request));
        request.setAttribute("sort", getSortOption(request));
        request.setAttribute("cparam", paramInfo);
        request.setAttribute("area", getAreaOption(request).toString());
        request.setAttribute("facets", facetNavigationBean);
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

        // If we get no results without wildcards, we add wildcards in the search.
        HippoFacetNavigationBean result = ContentBeanUtils.getFacetNavigationBean(buildQuery(request, false));
        if (result.getCount() != 0) {
            return result;
        } else {
            return ContentBeanUtils.getFacetNavigationBean(buildQuery(request, true));
        }
    }

    String getQueryParameter(HstRequest request) {
        return getAnyParameter(request, REQUEST_PARAM_QUERY);
    }

    private int getCurrentPage(HstRequest request) {
        return getAnyIntParameter(request, REQUEST_PARAM_PAGE, 1);
    }

    private HstQuery buildQuery(HstRequest request, Boolean includeWildcards) {
        String queryParameter = getQueryParameter(request);
        Constraint searchStringConstraint = null;

        HstQueryBuilder queryBuilder = HstQueryBuilder.create(deriveScope(request));

        if (queryParameter != null) {
            String query = SearchInputParsingUtils.parse(queryParameter, true);

            if (includeWildcards) {
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
            } else {
                searchStringConstraint = or(
                    //forcing specific fields first: this will boost the weight of a hit fot those specific property
                    constraint(".").contains(query),
                    constraint("website:title").contains(query),
                    constraint("website:summary").contains(query),
                    constraint("publicationsystem:title").contains(query),
                    constraint("publicationsystem:summary").contains(query),
                    constraint("nationalindicatorlibrary:title").contains(query)
                );
            }

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

        return queryBuilder.where(and(constraints.toArray(new Constraint[0]))).limit(10).build();
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
