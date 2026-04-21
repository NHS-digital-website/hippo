package uk.nhs.digital.common.components;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
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
    private HstResponse response;

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

    /**
     * Guard 1: should return an empty pageable when the scope is null.
     */
    @Test
    public void shouldReturnEmptyPageableWhenScopeIsNull() {
        Pageable<HippoBean> result = component.doFacetedSearch(request, paramInfo, null);

        assertNotNull(result);
        assertSame(DefaultPagination.emptyCollection(), result);
    }

    /**
     * Guard 2: should return an empty pageable when the resolved sitemap item is null.
     */
    @Test
    public void shouldReturnEmptyPageableWhenResolvedSiteMapItemIsNull() {
        HippoBean scope = mock(HippoBean.class);

        when(requestContext.getResolvedSiteMapItem()).thenReturn(null);

        Pageable<HippoBean> result = component.doFacetedSearch(request, paramInfo, scope);

        assertNotNull(result);
        assertSame(DefaultPagination.emptyCollection(), result);
    }

    /**
     * Guard 3: should return an empty pageable when the unsupported query flag is set.
     */
    @Test
    public void shouldReturnEmptyPageableWhenUnsupportedQueryFlagIsSet() {
        HippoBean scope = mock(HippoBean.class);
        ResolvedSiteMapItem resolvedSiteMapItem = mock(ResolvedSiteMapItem.class);

        when(requestContext.getResolvedSiteMapItem()).thenReturn(resolvedSiteMapItem);
        when(request.getAttribute("unsupportedSearchQuery")).thenReturn(Boolean.TRUE);

        Pageable<HippoBean> result = component.doFacetedSearch(request, paramInfo, scope);

        assertNotNull(result);
        assertSame(DefaultPagination.emptyCollection(), result);
        verify(request).setAttribute("totalResults", 0L);
    }

    /**
     * Should short-circuit doBeforeRender when a query has already been classified
     * as unsupported (for example, a UUID-like query).
     */
    @Test
    public void shouldShortCircuitDoBeforeRenderForUnsupportedQuery() {
        String uuidQuery = "4799a2eb-ed65-4635-ba0c-20df4d3dcfbd";

        when(request.getAttribute("sanitizedSearchQueryDone")).thenReturn(Boolean.TRUE);
        when(request.getAttribute("sanitizedSearchQuery")).thenReturn(uuidQuery);
        when(request.getAttribute("unsupportedSearchQuery")).thenReturn(Boolean.TRUE);

        component.doBeforeRender(request, response);

        verify(request).setAttribute("query", uuidQuery);
        verify(request).setAttribute("totalResults", 0L);
        verify(request).setAttribute("sort", "relevance");
        verify(request).setModel(eq("pageable"), any(Pageable.class));
        verify(request).setAttribute(eq("pageable"), any(Pageable.class));
    }
}