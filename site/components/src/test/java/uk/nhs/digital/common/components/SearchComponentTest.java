package uk.nhs.digital.common.components;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.core.request.ResolvedSiteMapItem;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.onehippo.cms7.essentials.components.info.EssentialsListComponentInfo;
import org.onehippo.cms7.essentials.components.paging.DefaultPagination;
import org.onehippo.cms7.essentials.components.paging.Pageable;

public class SearchComponentTest {

    private SearchComponent component;

    @Mock
    private HstRequest request;

    @Mock
    private HstRequestContext requestContext;

    @Mock
    private EssentialsListComponentInfo paramInfo;

    @Before
    public void setUp() {
        initMocks(this);
        component = new SearchComponent();
        when(request.getRequestContext()).thenReturn(requestContext);
    }

    /* ---------------------------------------------------------
     * doFacetedSearch – defensive guards
     * --------------------------------------------------------- */

    /**
     * Guard 1: scope == null
     */
    @Test
    public void shouldReturnEmptyPageableWhenScopeIsNull() {
        Pageable<HippoBean> result =
            component.doFacetedSearch(request, paramInfo, null);

        assertNotNull(result);
        assertSame(DefaultPagination.emptyCollection(), result);
    }

    /**
     * Guard 2: ResolvedSiteMapItem == null
     */
    @Test
    public void shouldReturnEmptyPageableWhenResolvedSiteMapItemIsNull() {
        HippoBean scope = mock(HippoBean.class);

        when(requestContext.getResolvedSiteMapItem()).thenReturn(null);

        Pageable<HippoBean> result =
            component.doFacetedSearch(request, paramInfo, scope);

        assertNotNull(result);
        assertSame(DefaultPagination.emptyCollection(), result);
    }

    /**
     * Guard 3: searchQuery == null or empty
     * (root cause of FacetedNavigationEngineImpl NPE)
     */
    @Test
    public void shouldReturnEmptyPageableWhenSearchQueryIsNull() {
        TestSearchComponent testComponent = new TestSearchComponent();

        when(request.getRequestContext()).thenReturn(requestContext);
        when(requestContext.getResolvedSiteMapItem())
            .thenReturn(mock(ResolvedSiteMapItem.class));

        testComponent.setSearchQuery(null);

        Pageable<HippoBean> result =
            testComponent.doFacetedSearch(
                request,
                paramInfo,
                mock(HippoBean.class)
            );

        assertNotNull(result);
        assertSame(DefaultPagination.emptyCollection(), result);
    }

    /* ---------------------------------------------------------
     * Test-only subclass
     * --------------------------------------------------------- */

    /**
     * Test-specific subclass used to safely override protected framework methods.
     * This avoids mocking Bloomreach internals while respecting Java access rules.
     */
    private static class TestSearchComponent extends SearchComponent {

        private String searchQuery;

        void setSearchQuery(String searchQuery) {
            this.searchQuery = searchQuery;
        }

        @Override
        protected String getSearchQuery(HstRequest request) {
            return searchQuery;
        }
    }
}