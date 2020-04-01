package uk.nhs.digital.intranet.components;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.util.SearchInputParsingUtils;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsListComponentInfo;
import uk.nhs.digital.intranet.provider.BloomreachSearchProvider;
import uk.nhs.digital.intranet.provider.GraphProvider;

@ParametersInfo(type = EssentialsListComponentInfo.class)
public class SearchPageComponent extends CommonComponent {

    private static final String REQUEST_ATTR_PAGEABLE = "pageable";
    private static final String REQUEST_ATTR_PEOPLE_RESULTS = "peopleResults";

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
        request.setAttribute(REQUEST_ATTR_QUERY, getSearchQuery(request, true));
        request.setAttribute(REQUEST_ATTR_PAGEABLE, bloomreachSearchProvider
            .getBloomreachResults(getSearchQuery(request, true),
                getComponentInfo(request).getPageSize(), getCurrentPage(request)));
        request.setAttribute(REQUEST_ATTR_PEOPLE_RESULTS, graphProvider.getPeople(getSearchQuery(request, false)));
    }

    private String getSearchQuery(HstRequest request, boolean allowWildcards) {
        return SearchInputParsingUtils.parse(getAnyParameter(request, REQUEST_PARAM_QUERY), allowWildcards);
    }

    private int getCurrentPage(HstRequest request) {
        return getAnyIntParameter(request, REQUEST_PARAM_PAGE, 1);
    }

    private EssentialsListComponentInfo getComponentInfo(HstRequest request) {
        return getComponentParametersInfo(request);
    }
}
