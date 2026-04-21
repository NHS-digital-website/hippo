package uk.nhs.digital.common.components;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.doReturn;
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

    @Test
    public void shouldReturnEmptyPageableWhenScopeIsNull() {
        Pageable<HippoBean> result =
            component.doFacetedSearch(request, paramInfo, null);

        assertNotNull(result);
        assertSame(DefaultPagination.emptyCollection(), result);
    }

    @Test
    public void shouldReturnEmptyPageableWhenResolvedSiteMapItemIsNull() {
        HippoBean scope = mock(HippoBean.class);

        when(requestContext.getResolvedSiteMapItem()).thenReturn(null);

        Pageable<HippoBean> result =
            component.doFacetedSearch(request, paramInfo, scope);

        assertNotNull(result);
        assertSame(DefaultPagination.emptyCollection(), result);
    }

    @Test
    public void shouldReturnEmptyPageableWhenRelativePathIsEmpty() {
        HippoBean scope = mock(HippoBean.class);
        ResolvedSiteMapItem siteMapItem = mock(ResolvedSiteMapItem.class);

        when(requestContext.getResolvedSiteMapItem()).thenReturn(siteMapItem);

        SearchComponent spy = org.mockito.Mockito.spy(component);

        // Force relativePathFrom() to behave as empty
        doReturn("").when(spy).cleanupSearchQuery(org.mockito.Mockito.anyString());

        Pageable<HippoBean> result =
            spy.doFacetedSearch(request, paramInfo, scope);

        assertNotNull(result);
        assertSame(DefaultPagination.emptyCollection(), result);
    }

    /* ---------------------------------------------------------
     * getSearchScope – defensive behaviour
     * --------------------------------------------------------- */

    @Test
    public void shouldFallbackToDefaultScopeWhenResolvedSiteMapItemIsNull() {
        SearchComponent spy = org.mockito.Mockito.spy(component);

        when(requestContext.getResolvedSiteMapItem()).thenReturn(null);

        HippoBean defaultScope = mock(HippoBean.class);
        doReturn(defaultScope).when(spy).getSearchScope(request, null);

        HippoBean result = spy.getSearchScope(request, null);

        assertNotNull(result);
    }
}