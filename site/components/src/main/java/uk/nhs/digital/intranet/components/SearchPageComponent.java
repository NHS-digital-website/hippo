package uk.nhs.digital.intranet.components;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.essentials.components.CommonComponent;
import uk.nhs.digital.intranet.model.MockSearchResult;
import uk.nhs.digital.intranet.model.SearchResult;

import java.util.Collections;
import java.util.List;

public class SearchPageComponent extends CommonComponent {

    private static final String REQUEST_ATTR_RESULTS = "results";

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);
        populateRequest(request, Collections.singletonList(new MockSearchResult("dummy_title")));
    }

    private void populateRequest(final HstRequest request, final List<? extends SearchResult> results) {
        request.setAttribute(REQUEST_ATTR_QUERY, getSearchQuery(request));
        request.setModel(REQUEST_ATTR_RESULTS, results);
    }

    protected String getSearchQuery(HstRequest request) {
        return cleanupSearchQuery(getAnyParameter(request, REQUEST_PARAM_QUERY));
    }
}
