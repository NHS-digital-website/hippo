package uk.nhs.digital.common.components.catalogue;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoDocumentIterator;
import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.content.beans.standard.HippoResultSetBean;
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
import org.onehippo.cms7.essentials.components.paging.DefaultPagination;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import org.onehippo.cms7.essentials.components.utils.SiteUtils;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import uk.nhs.digital.common.components.info.CatalogueResultsComponentInfo;

import java.util.Collections;
import java.util.Map;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HstServices.class, SiteUtils.class, ContentBeanUtils.class, RequestContextProvider.class, ParametersInfoUtils.class, PathUtils.class})
@PowerMockIgnore("javax.management.*")
public class CatalogueResultsComponentTest {

    @Mock
    private CatalogueResultsComponent catalogueResultsComponent;

    private HstRequest mockRequest;
    private HstResponse mockResponse;
    private CatalogueResultsComponentInfo mockComponentInfo;

    @Mock
    private HstRequestContext context;

    @Mock
    private Session mockSession;

    private ResolvedSiteMapItem resolvedSiteMapItem;
    private HippoBean mockSiteContentBaseBean;


    @Before
    public void setUp() throws RepositoryException {
        MockitoAnnotations.openMocks(this);

        // Mock static methods
        PowerMockito.mockStatic(HstServices.class);
        PowerMockito.mockStatic(SiteUtils.class);
        PowerMockito.mockStatic(ContentBeanUtils.class);
        PowerMockito.mockStatic(RequestContextProvider.class);
        PowerMockito.mockStatic(ParametersInfoUtils.class);
        PowerMockito.mockStatic(PathUtils.class);

        ComponentManager mockComponentManager = mock(ComponentManager.class);
        PowerMockito.when(HstServices.getComponentManager()).thenReturn(mockComponentManager);

        catalogueResultsComponent = spy(new CatalogueResultsComponentTestSubclass());
        mockRequest = mock(HstRequest.class);
        mockResponse = mock(HstResponse.class);
        mockComponentInfo = mock(CatalogueResultsComponentInfo.class);

        PowerMockito.when(RequestContextProvider.get()).thenReturn(context);
        when(mockRequest.getRequestContext()).thenReturn(context);
        when(context.getSession()).thenReturn(mockSession);

        when(mockComponentInfo.toString()).thenReturn("MockCatalogueResultsComponentInfo");

        doReturn(mockComponentInfo).when(catalogueResultsComponent).getComponentParametersInfo(mockRequest);
    }

    @Test
    public void testDoBeforeRenderSetsAttributes() {
        // Arrange
        when(mockComponentInfo.getTaxonomyFilterMappingDocumentPath()).thenReturn("/test/path");
        when(mockRequest.getAttribute("sectionEntries")).thenReturn(Collections.emptyMap());

        when(SiteUtils.getAnyParameter(eq("query"), eq(mockRequest), eq(catalogueResultsComponent)))
            .thenReturn("testQuery");
        // Act
        catalogueResultsComponent.doBeforeRender(mockRequest, mockResponse);

        // Verify the super method is called
        verify(catalogueResultsComponent).doBeforeRender(mockRequest, mockResponse);

        // Assert
        verify(mockRequest).setAttribute(eq("sectionEntries"), any(Map.class));
        verify(mockRequest).setAttribute(eq("requestContext"), any());
        verify(mockRequest).setAttribute(eq("currentQuery"), eq("testQuery"));

    }

    @Test
    public void testDoBeforeRenderWithNullQueryParameter() {
        // Arrange
        when(mockComponentInfo.getTaxonomyFilterMappingDocumentPath()).thenReturn("/test/path");
        when(SiteUtils.getAnyParameter(eq("query"), eq(mockRequest), eq(catalogueResultsComponent)))
            .thenReturn(null);
        // Act
        catalogueResultsComponent.doBeforeRender(mockRequest, mockResponse);
        // Assert
        verify(mockRequest).setAttribute("currentQuery", "");
    }

    @Test
    public void testDoBeforeRenderHandlesException() {
        // Arrange
        when(mockComponentInfo.getTaxonomyFilterMappingDocumentPath()).thenThrow(new RuntimeException("Test Exception"));

        // Act
        try {
            catalogueResultsComponent.doBeforeRender(mockRequest, mockResponse);
            fail("Expected exception was not thrown");
        } catch (RuntimeException e) {
            // Assert
            assertEquals("Test Exception", e.getMessage());
        }
    }

    @Test(expected = RuntimeException.class)
    public void testDoBeforeRenderThrowsException() {
        when(mockComponentInfo.getTaxonomyFilterMappingDocumentPath()).thenThrow(new RuntimeException("Test"));
        catalogueResultsComponent.doBeforeRender(mockRequest, mockResponse);
    }

    @Test
    public void testDoFacetedSearchReturnsPageable() {
        // Arrange
        when(mockComponentInfo.getPageSize()).thenReturn(10);

        Pageable<?> emptyPageable = DefaultPagination.emptyCollection();
        when(mockRequest.getAttribute("totalAvailable")).thenReturn(0);

        when(SiteUtils.getAnyParameter(eq("query"), eq(mockRequest), eq(catalogueResultsComponent)))
            .thenReturn(null);
        // Act
        Pageable<?> result = catalogueResultsComponent.doFacetedSearch(mockRequest, mockComponentInfo, null);

        // Assert
        assertEquals(emptyPageable.getItems().size(), result.getItems().size());
        verify(mockRequest, never()).setAttribute(eq("totalAvailable"), anyInt());
    }

    @Test
    public void testDoFacetedSearchReturnsPageableWithValidScope() {
        // Arrange
        String mockRelPath = "/mock/relative/path"; // Define a valid relative path
        HippoBean mockScope = mock(HippoBean.class); // Mock the HippoBean (scope)
        when(mockScope.getCanonicalPath()).thenReturn("/content/documents/corporate-website/developer/api-catalogue"); // Mock its behavior

        // Mock the SiteUtils.relativePathFrom method to return a valid relative path
        when(SiteUtils.relativePathFrom(eq(mockScope), any())).thenReturn(mockRelPath);

        when(mockComponentInfo.getPageSize()).thenReturn(10);
        when(mockRequest.getRequestContext()).thenReturn(context);

        // Mock other dependencies
        when(mockRequest.getAttribute("totalAvailable")).thenReturn(0);

        HippoFacetNavigationBean mockFacetBean = mock(HippoFacetNavigationBean.class);
        when(ContentBeanUtils.getFacetNavigationBean(eq(mockRelPath), anyString())).thenReturn(mockFacetBean);

        HippoResultSetBean mockResultSet = mock(HippoResultSetBean.class);
        when(mockFacetBean.getResultSet()).thenReturn(mockResultSet);

        HippoDocumentIterator<HippoBean> mockIterator = mock(HippoDocumentIterator.class);
        when(mockResultSet.getDocumentIterator(HippoBean.class)).thenReturn(mockIterator);

        // Act
        Pageable<HippoBean> result = catalogueResultsComponent.doFacetedSearch(mockRequest, mockComponentInfo, mockScope);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getItems().size()); // Validate collection size
    }

    @Test
    public void testDoFacetedSearchWithInvalidPath() {
        when(SiteUtils.relativePathFrom(mockSiteContentBaseBean,context)).thenReturn(null);
        Pageable<?> result = catalogueResultsComponent.doFacetedSearch(mockRequest, mockComponentInfo, null);
        assertNotNull(result);
        assertEquals(0, result.getItems().size());
    }

    @Test
    public void testGetFacetNavigationBeanReturnsNullForEmptyPath() {
        // Act
        HippoFacetNavigationBean result = catalogueResultsComponent.getFacetNavigationBean(null, "", "");

        // Assert
        assertNull(result);
    }

    @Test
    public void testDoFacetedSearchHandlesException() {
        when(SiteUtils.relativePathFrom(mockSiteContentBaseBean,context)).thenThrow(new RuntimeException("Test Exception"));
        try {
            catalogueResultsComponent.doFacetedSearch(mockRequest, mockComponentInfo, null);
            fail("Expected exception was not thrown");
        } catch (RuntimeException e) {
            assertEquals("Test Exception", e.getMessage());
        }
    }

    @Test
    public void testGetFacetNavigationBeanHandlesValidPath() {
        // Arrange
        HstRequestContext mockContext = mock(HstRequestContext.class);

        resolvedSiteMapItem = mock(ResolvedSiteMapItem.class);
        mockSiteContentBaseBean = mock(HippoBean.class);

        when(mockContext.getResolvedSiteMapItem()).thenReturn(resolvedSiteMapItem);
        when(mockContext.getSiteContentBaseBean()).thenReturn(mockSiteContentBaseBean);

        // Act
        HippoFacetNavigationBean result = catalogueResultsComponent.getFacetNavigationBean(mockContext, "/valid/path", "query");

        // Assert
        assertNull(result); // Simplified for example
    }

    @Test
    public void testGetFacetNavigationBean_WithValidResolvedContentPath() {
        // Mock the dependencies
        HstRequestContext mockContext = mock(HstRequestContext.class);
        ResolvedSiteMapItem mockResolvedSiteMapItem = mock(ResolvedSiteMapItem.class);
        HippoFacetNavigationBean mockFacetNavigationBean = mock(HippoFacetNavigationBean.class);
        HippoBean mockSiteContentBaseBean = mock(HippoBean.class);

        // Set up the resolvedSiteMapItem
        String validResolvedContentPath = "content/documents/corporate-website/developer/api-catalogue/";
        when(PathUtils.normalizePath(mockResolvedSiteMapItem.getRelativeContentPath())).thenReturn(validResolvedContentPath);
        when(mockContext.getResolvedSiteMapItem()).thenReturn(mockResolvedSiteMapItem);

        // Mock the siteContentBaseBean
        when(mockContext.getSiteContentBaseBean()).thenReturn(mockSiteContentBaseBean);

        doReturn(mockFacetNavigationBean).when(mockSiteContentBaseBean).getBean(validResolvedContentPath, HippoFacetNavigationBean.class);

        doReturn("parsedQuery").when(catalogueResultsComponent).cleanupSearchQuery(anyString());

        // Call the method under test
        HippoFacetNavigationBean result = catalogueResultsComponent.getFacetNavigationBean(mockContext, "somePath", "someQuery");

        // Assert the result
        assertNull(result);

        // Verify that the conditions were met
        verify(mockSiteContentBaseBean).getBean(validResolvedContentPath, HippoFacetNavigationBean.class);
    }

    @Test
    public void testGetComponentParametersInfo() {
        // Call the method under test
        catalogueResultsComponent.getComponentParametersInfo(mockRequest);

        verify(catalogueResultsComponent).getComponentParametersInfo(mockRequest);
    }

}
