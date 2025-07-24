package uk.nhs.digital.common.components.catalogue;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.container.ComponentManager;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.core.request.ResolvedSiteMapItem;
import org.hippoecm.hst.site.HstServices;
import org.hippoecm.hst.util.ContentBeanUtils;
import org.hippoecm.hst.util.ParametersInfoUtils;
import org.hippoecm.hst.util.PathUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onehippo.cms7.essentials.components.utils.SiteUtils;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.jcr.RepositoryException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HstServices.class, SiteUtils.class, ContentBeanUtils.class, RequestContextProvider.class, ParametersInfoUtils.class, PathUtils.class})
@PowerMockIgnore({"javax.management.*", "com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*"})
public class FilteredEssentialsFacetsComponentTest {

    private FilteredEssentialsFacetsComponent filteredEssentialsFacetsComponent;

    @Mock
    private HstRequestContext mockContext;

    @Mock
    private ResolvedSiteMapItem mockResolvedSiteMapItem;

    @Mock
    private HippoFacetNavigationBean mockFacetNavigationBean;

    @Before
    public void setUp() throws RepositoryException {
        MockitoAnnotations.openMocks(this);

        // Mock static methods using PowerMockito
        PowerMockito.mockStatic(HstServices.class);
        PowerMockito.mockStatic(SiteUtils.class);
        PowerMockito.mockStatic(ContentBeanUtils.class);
        PowerMockito.mockStatic(RequestContextProvider.class);
        PowerMockito.mockStatic(ParametersInfoUtils.class);
        PowerMockito.mockStatic(PathUtils.class);

        ComponentManager mockComponentManager = mock(ComponentManager.class);
        PowerMockito.when(HstServices.getComponentManager()).thenReturn(mockComponentManager);

        filteredEssentialsFacetsComponent = new FilteredEssentialsFacetsComponent();
    }

    @Test
    public void testGetFacetNavigationBean_NullOrEmptyPath() {
        // Arrange
        String path = "";
        String query = "search query";

        // Act
        HippoFacetNavigationBean result = filteredEssentialsFacetsComponent.getFacetNavigationBean(mockContext, path, query);

        // Assert
        assertNull(result);
        verify(mockContext, never()).getResolvedSiteMapItem();
    }

    @Test
    public void testGetFacetNavigationBean_ValidPathWithSearchQuery() {
        // Arrange
        String path = "valid/path";
        String query = "search query";

        when(mockContext.getResolvedSiteMapItem()).thenReturn(mockResolvedSiteMapItem);
        when(ContentBeanUtils.getFacetNavigationBean("valid/path", "xpath(//*[jcr:contains(@website:title,'search query') or jcr:contains(@website:shortsummary,'search query')])"))
            .thenReturn(mockFacetNavigationBean);

        // Act
        HippoFacetNavigationBean result = filteredEssentialsFacetsComponent.getFacetNavigationBean(mockContext, path, query);

        // Assert
        assertNotNull(result);
        assertEquals(mockFacetNavigationBean, result);
    }

    @Test
    public void testGetFacetNavigationBean_BlankResolvedContentPath() {
        // Arrange
        String path = "valid/path";
        String query = null;

        when(mockContext.getResolvedSiteMapItem()).thenReturn(mockResolvedSiteMapItem);
        when(mockResolvedSiteMapItem.getRelativeContentPath()).thenReturn("   "); // Simulate a blank path
        when(ContentBeanUtils.getFacetNavigationBean("valid/path", null))
            .thenReturn(mockFacetNavigationBean);

        // Act
        HippoFacetNavigationBean result = filteredEssentialsFacetsComponent.getFacetNavigationBean(mockContext, path, query);

        // Assert
        assertNotNull(result);
        assertEquals(mockFacetNavigationBean, result);
    }

    @Test
    public void testGetFacetNavigationBean_InvalidResolvedContentPath() {
        // Arrange
        String path = "valid/path";
        String query = "search query";

        when(mockContext.getResolvedSiteMapItem()).thenReturn(mockResolvedSiteMapItem);
        when(mockResolvedSiteMapItem.getRelativeContentPath()).thenReturn("/invalid/path");
        when(ContentBeanUtils.getFacetNavigationBean("valid/path", "xpath(//*[jcr:contains(@website:title,'search query') or jcr:contains(@website:shortsummary,'search query')])"))
            .thenReturn(mockFacetNavigationBean);

        // Act
        HippoFacetNavigationBean result = filteredEssentialsFacetsComponent.getFacetNavigationBean(mockContext, path, query);

        // Assert
        assertNotNull(result);
        assertEquals(mockFacetNavigationBean, result);
    }

}
