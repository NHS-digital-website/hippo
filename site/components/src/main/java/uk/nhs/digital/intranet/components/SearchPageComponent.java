package uk.nhs.digital.intranet.components;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.util.SearchInputParsingUtils;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;
import uk.nhs.digital.intranet.enums.SearchArea;
import uk.nhs.digital.intranet.model.Person;
import uk.nhs.digital.intranet.model.SearchResultTab;
import uk.nhs.digital.intranet.model.exception.ProviderCommunicationException;
import uk.nhs.digital.intranet.provider.BloomreachSearchProvider;
import uk.nhs.digital.intranet.provider.GraphProvider;
import uk.nhs.digital.intranet.utils.Constants;

import java.util.*;

@ParametersInfo(type = SearchPageComponentInfo.class)
public class SearchPageComponent extends CommonComponent {

    static final String REQUEST_ATTR_PAGEABLE = "pageable";
    static final String REQUEST_ATTR_PEOPLE_RESULTS = "peopleResults";
    private static final String REQUEST_ATTR_ACCESS_TOKEN_REQUIRED = "accessTokenRequired";
    private static final String REQUEST_ATTR_API_ERROR_MESSAGE = "apiErrorMessage";
    private static final String REQUEST_ATTR_MISSING_TERM_ERROR_MESSAGE = "searchTermErrorMessage";
    private static final String REQUEST_ATTR_MORE_PEOPLE = "morePeople";
    static final String REQUEST_ATTR_AREA = "area";
    static final String REQUEST_ATTR_SEARCH_TABS = "searchTabs";

    private final GraphProvider graphProvider;
    private final BloomreachSearchProvider bloomreachSearchProvider;
    private final String applicationId;
    private final String redirectUri;
    private final String baseUri;

    public SearchPageComponent(final GraphProvider graphProvider,
                               final BloomreachSearchProvider bloomreachSearchProvider,
                               final String applicationId,
                               final String redirectUri,
                               final String baseUri) {
        this.graphProvider = graphProvider;
        this.bloomreachSearchProvider = bloomreachSearchProvider;
        this.applicationId = applicationId;
        this.redirectUri = redirectUri;
        this.baseUri = baseUri;
    }

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);
        HstRequestContext requestContext = RequestContextProvider.get();
        List<SearchResultTab> searchResultTabs = new ArrayList<>();
        try {
            searchContent(request, searchResultTabs);
            searchPeople(request, searchResultTabs);
        } finally {
            request.setAttribute(REQUEST_ATTR_AREA, getAreaOption(request).getDisplayName());
            request.setAttribute(REQUEST_ATTR_QUERY, getSearchQuery(request, true));
            searchResultTabs.add(new SearchResultTab(SearchArea.ALL,
                searchResultTabs.stream()
                    .reduce(0, (sum, tab) -> sum + tab.getNumberOfResults(),
                        Integer::sum)));
            searchResultTabs.sort(Comparator.comparing(SearchResultTab::getTabName));
            request.setAttribute(REQUEST_ATTR_SEARCH_TABS, searchResultTabs);
            request.setAttribute("authorizationUrl", getAuthorizationUrl());
            request.setAttribute("loginRequired", !hasValidAccessToken(requestContext));
        }
    }

    private String getAuthorizationUrl() {
        return UriComponentsBuilder.fromUriString(baseUri)
            .pathSegment("authorize")
            .queryParam("client_id", applicationId)
            .queryParam("response_type", "code")
            .queryParam("redirect_uri", redirectUri)
            .queryParam("response_mode", "query")
            .queryParam("scope", "offline_access user.read.all")
            .queryParam("state", UUID.randomUUID().toString())
            .toUriString();
    }

    private boolean hasValidAccessToken(HstRequestContext requestContext) {
        return StringUtils.hasText((String) requestContext.getAttribute(Constants.ACCESS_TOKEN_PROPERTY_NAME));
    }

    private void searchContent(HstRequest request,
                               List<SearchResultTab> searchResultTabs) {
        final SearchArea searchArea = getAreaOption(request);
        final String searchQuery = getSearchQuery(request, true);

        if (searchArea != SearchArea.PEOPLE) {
            final Pageable<HippoBean> bloomreachResults = bloomreachSearchProvider
                .getBloomreachResults(searchQuery,
                    getComponentInfo(request).getPageSize(),
                    getCurrentPage(request), searchArea);
            request.setAttribute(REQUEST_ATTR_PAGEABLE, bloomreachResults);
            if (searchArea != SearchArea.ALL) {
                searchResultTabs.add(new SearchResultTab(searchArea,
                    Math.toIntExact(bloomreachResults.getTotal())));
            }
        }
        SearchArea.getDocumentSearchAreasWithout(searchArea).forEach(
            unselectedSearchArea -> searchResultTabs
                .add(new SearchResultTab(unselectedSearchArea,
                    bloomreachSearchProvider.getBloomreachResultsCount(searchQuery,
                        unselectedSearchArea)))
        );
    }

    private void searchPeople(HstRequest request, List<SearchResultTab> searchResultTabs) {
        final String accessToken = (String) RequestContextProvider.get()
            .getAttribute(Constants.ACCESS_TOKEN_PROPERTY_NAME);
        final SearchArea area = getAreaOption(request);
        final String searchQuery = getSearchQuery(request, false);
        final int peopleLimit = getComponentInfo(request).getPeopleLimit();
        int numberOfResults;
        if (!StringUtils.hasText(accessToken)) {
            numberOfResults = 0;
            request.setAttribute(REQUEST_ATTR_ACCESS_TOKEN_REQUIRED, true);
        } else if (!StringUtils.hasText(searchQuery)) {
            numberOfResults = 0;
            request.setAttribute(REQUEST_ATTR_MISSING_TERM_ERROR_MESSAGE, true);
        } else {
            try {
                List<Person> people = graphProvider.getPeople(searchQuery);
                int peopleCount = people.size();
                if (SearchArea.ALL.equals(area) && peopleCount > peopleLimit) {
                    people = people.subList(0, peopleLimit);
                    request.setAttribute(REQUEST_ATTR_MORE_PEOPLE, true);
                }
                numberOfResults = peopleCount;
                request.setAttribute(REQUEST_ATTR_PEOPLE_RESULTS, people);
            } catch (ProviderCommunicationException e) {
                numberOfResults = 0;
                request.setAttribute(REQUEST_ATTR_API_ERROR_MESSAGE, true);
            }
        }
        searchResultTabs.add(new SearchResultTab(SearchArea.PEOPLE, numberOfResults));
    }

    private String getSearchQuery(HstRequest request, boolean allowWildcards) {
        return SearchInputParsingUtils.parse(getAnyParameter(request, REQUEST_PARAM_QUERY), allowWildcards);
    }

    private int getCurrentPage(HstRequest request) {
        return getAnyIntParameter(request, REQUEST_PARAM_PAGE, 1);
    }

    private SearchArea getAreaOption(HstRequest request) {
        Optional<String> param = Optional.ofNullable(getAnyParameter(request, REQUEST_ATTR_AREA));
        return param.map(SearchArea::fromQueryString).orElse(SearchArea.ALL);
    }

    private SearchPageComponentInfo getComponentInfo(HstRequest request) {
        return getComponentParametersInfo(request);
    }
}
