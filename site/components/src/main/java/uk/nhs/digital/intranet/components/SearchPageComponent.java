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
import uk.nhs.digital.intranet.enums.SearchArea;
import uk.nhs.digital.intranet.model.Person;
import uk.nhs.digital.intranet.model.exception.ProviderCommunicationException;
import uk.nhs.digital.intranet.provider.BloomreachSearchProvider;
import uk.nhs.digital.intranet.provider.GraphProvider;
import uk.nhs.digital.intranet.utils.Constants;

import java.util.List;
import java.util.Optional;

@ParametersInfo(type = SearchPageComponentInfo.class)
public class SearchPageComponent extends CommonComponent {

    private static final String REQUEST_ATTR_PAGEABLE = "pageable";
    private static final String REQUEST_ATTR_PEOPLE_RESULTS = "peopleResults";
    private static final String REQUEST_ATTR_ACCESS_TOKEN = "accessTokenRequired";
    private static final String REQUEST_ATTR_ERROR_MESSAGE = "errorMessage";
    private static final String REQUEST_ATTR_MORE_PEOPLE = "morePeople";
    private static final String REQUEST_ATTR_AREA = "area";
    private static final String REQUEST_ATTR_PEOPLE_COUNT = "peopleCount";

    private final GraphProvider graphProvider;
    private final BloomreachSearchProvider bloomreachSearchProvider;

    public SearchPageComponent(final GraphProvider graphProvider,
                               final BloomreachSearchProvider bloomreachSearchProvider) {
        this.graphProvider = graphProvider;
        this.bloomreachSearchProvider = bloomreachSearchProvider;
    }

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);
        try {
            searchPeople(request);
            searchContent(request);
        } finally {
            request.setAttribute(REQUEST_ATTR_AREA, getAreaOption(request));
            request.setAttribute(REQUEST_ATTR_QUERY, getSearchQuery(request, true));
        }
    }

    private void searchContent(HstRequest request) {
        Pageable<HippoBean> bloomreachResults = bloomreachSearchProvider
            .getBloomreachResults(getSearchQuery(request, true),
                getComponentInfo(request).getPageSize(), getCurrentPage(request));

        request.setAttribute(REQUEST_ATTR_PAGEABLE, bloomreachResults);
    }

    private void searchPeople(HstRequest request) {
        HstRequestContext requestContext = RequestContextProvider.get();
        String accessToken = (String) requestContext.getAttribute(Constants.ACCESS_TOKEN_PROPERTY_NAME);
        SearchArea area = getAreaOption(request);
        int peopleLimit = getComponentInfo(request).getPeopleLimit();
        if (!StringUtils.hasText(accessToken)) {
            request.setAttribute(REQUEST_ATTR_ACCESS_TOKEN, true);
        } else {
            try {
                List<Person> people = graphProvider.getPeople(getSearchQuery(request, false));
                int peopleCount = people.size();
                if (SearchArea.ALL.equals(area) && peopleCount > peopleLimit) {
                    people = people.subList(0, peopleLimit);
                    request.setAttribute(REQUEST_ATTR_MORE_PEOPLE, true);
                }
                request.setAttribute(REQUEST_ATTR_PEOPLE_COUNT, peopleCount);
                request.setAttribute(REQUEST_ATTR_PEOPLE_RESULTS, people);
            } catch (ProviderCommunicationException e) {
                request.setAttribute(REQUEST_ATTR_ERROR_MESSAGE, "Unable to search for people.");
            }
        }
    }

    private String getSearchQuery(HstRequest request, boolean allowWildcards) {
        return SearchInputParsingUtils.parse(getAnyParameter(request, REQUEST_PARAM_QUERY), allowWildcards);
    }

    private int getCurrentPage(HstRequest request) {
        return getAnyIntParameter(request, REQUEST_PARAM_PAGE, 1);
    }

    private SearchArea getAreaOption(HstRequest request) {
        Optional<String> param = Optional.ofNullable(getAnyParameter(request, REQUEST_ATTR_AREA));
        return param.map(s -> SearchArea.valueOf(s.toUpperCase())).orElse(SearchArea.ALL);
    }

    private SearchPageComponentInfo getComponentInfo(HstRequest request) {
        return getComponentParametersInfo(request);
    }
}
