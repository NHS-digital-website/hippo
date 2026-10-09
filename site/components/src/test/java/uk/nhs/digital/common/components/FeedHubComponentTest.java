package uk.nhs.digital.common.components;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.hippoecm.hst.container.ModifiableRequestContextProvider;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.mock.core.container.MockComponentManager;
import org.hippoecm.hst.site.HstServices;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import uk.nhs.digital.common.components.info.FeedHubComponentInfo;
import uk.nhs.digital.website.beans.FeedHub;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FeedHubComponentTest {

    private static final String UNSAFE_FILTER = "abc') or ('1'='1";
    private static final String UNSAFE_QUERY = "test' or '1'='1";

    private HstRequest request;
    private HstResponse response;
    private HstRequestContext requestContext;
    private FeedHubComponentInfo componentInfo;
    private FeedHub feedHub;
    private TestableFeedHubComponent component;

    @Before
    public void setUp() {
        HstServices.setComponentManager(new MockComponentManager());

        request = mock(HstRequest.class);
        response = mock(HstResponse.class);
        requestContext = mock(HstRequestContext.class);
        componentInfo = mock(FeedHubComponentInfo.class);
        feedHub = mock(FeedHub.class);
        component = new TestableFeedHubComponent(componentInfo);

        when(request.getRequestContext()).thenReturn(requestContext);
        when(requestContext.getContentBean()).thenReturn(feedHub);
        when(feedHub.getFeedType()).thenReturn("News");
        when(componentInfo.getLimit()).thenReturn(20);
        ModifiableRequestContextProvider.set(requestContext);
    }

    @After
    public void tearDown() {
        ModifiableRequestContextProvider.clear();
    }

    @Test
    public void sanitisesFreeTextQueryBeforeBuildingFeedQuery() throws QueryException {
        component.setPublicRequestParameter("query", UNSAFE_QUERY);

        component.doBeforeRender(request, response);

        assertThat(component.capturedQueryText, equalTo("cleaned-test or 1=1"));
    }

    @Test
    public void sanitisesFilterValuesBeforeBuildingFeedQuery() throws QueryException {
        component.setPublicRequestParameters("type[]", UNSAFE_FILTER);
        component.setPublicRequestParameters("severity", UNSAFE_FILTER);
        component.setPublicRequestParameters("status", UNSAFE_FILTER);
        component.setPublicRequestParameters("granularity[]", UNSAFE_FILTER);
        component.setPublicRequestParameters("area[]", UNSAFE_FILTER);
        component.setPublicRequestParameters("information[]", UNSAFE_FILTER);
        component.setPublicRequestParameters("topic[]", UNSAFE_FILTER);
        component.setPublicRequestParameters("upcoming", UNSAFE_FILTER);

        component.doBeforeRender(request, response);

        assertThat(component.capturedFilterValues.get("type[]"), arrayContaining("cleaned-abc or 1=1"));
        assertThat(component.capturedFilterValues.get("severity"), arrayContaining("cleaned-abc or 1=1"));
        assertThat(component.capturedFilterValues.get("status"), arrayContaining("cleaned-abc or 1=1"));
        assertThat(component.capturedFilterValues.get("granularity[]"), arrayContaining("cleaned-abc or 1=1"));
        assertThat(component.capturedFilterValues.get("area[]"), arrayContaining("cleaned-abc or 1=1"));
        assertThat(component.capturedFilterValues.get("information[]"), arrayContaining("cleaned-abc or 1=1"));
        assertThat(component.capturedFilterValues.get("topic[]"), arrayContaining("cleaned-abc or 1=1"));
        assertThat(component.capturedFilterValues.get("upcoming"), arrayContaining("cleaned-abc or 1=1"));
    }

    @Test
    public void ignoresInvalidYearAndMonthBeforeBuildingFeedQuery() throws QueryException {
        component.setPublicRequestParameters("year", "abc");
        component.setPublicRequestParameters("month", "NotAMonth");

        component.doBeforeRender(request, response);

        assertThat(component.capturedFilterValues.get("year"), emptyArray());
        assertThat(component.capturedFilterValues.containsKey("month"), equalTo(false));
    }

    @Test
    public void defaultsInvalidSortBeforeBuildingFeedQuery() throws QueryException {
        component.setPublicRequestParameter("sort", "bad-value");

        component.doBeforeRender(request, response);

        assertThat(component.capturedSort, equalTo("date-desc"));
    }

    private static class TestableFeedHubComponent extends FeedHubComponent {

        private final FeedHubComponentInfo componentInfo;
        private final Map<String, String> publicRequestParameters = new HashMap<>();
        private final Map<String, String[]> publicRequestParameterArrays = new HashMap<>();
        private String capturedQueryText;
        private String capturedSort;
        private Map<String, String[]> capturedFilterValues;

        TestableFeedHubComponent(FeedHubComponentInfo componentInfo) {
            this.componentInfo = componentInfo;
        }

        void setPublicRequestParameter(String name, String value) {
            publicRequestParameters.put(name, value);
        }

        void setPublicRequestParameters(String name, String... values) {
            publicRequestParameterArrays.put(name, values);
        }

        @Override
        public String getPublicRequestParameter(HstRequest request, String parameterName) {
            return publicRequestParameters.get(parameterName);
        }

        @Override
        public String[] getPublicRequestParameters(HstRequest request, String parameterName) {
            return publicRequestParameterArrays.getOrDefault(parameterName, new String[0]);
        }

        @Override
        public String cleanupSearchQuery(String query) {
            if (query == null) {
                return null;
            }
            return "cleaned-" + query.replace("'", "").replace(")", "").replace("(", "");
        }

        @Override
        @SuppressWarnings("unchecked")
        protected <T> T getComponentParametersInfo(HstRequest request) {
            return (T) componentInfo;
        }

        @Override
        protected <T extends HippoBean> List<T> getFeed(
            HstRequest request,
            String queryText,
            String sort,
            int limit,
            Map<String, String[]> filterValues
        ) {
            capturedQueryText = queryText;
            capturedSort = sort;
            capturedFilterValues = new LinkedHashMap<>();
            filterValues.forEach((key, value) ->
                capturedFilterValues.put(key, Arrays.copyOf(value, value.length))
            );
            return Collections.emptyList();
        }

        @Override
        protected Pageable<HippoBean> pageResults(List<HippoBean> feed, HstRequest request) {
            return mock(Pageable.class);
        }
    }
}
