package uk.nhs.digital.common.components.catalogue;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.container.ComponentManager;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.site.HstServices;
import org.hippoecm.hst.util.ContentBeanUtils;
import org.hippoecm.hst.util.ParametersInfoUtils;
import org.hippoecm.hst.util.PathUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onehippo.cms7.essentials.components.utils.SiteUtils;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import uk.nhs.digital.common.components.catalogue.filters.Filters;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * Unit tests for the ApiCatalogueEssentialsFacetsComponent.
 * This test class uses mock objects to verify the behavior of the ApiCatalogueEssentialsFacetsComponent.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({HstServices.class, SiteUtils.class, ContentBeanUtils.class, RequestContextProvider.class, ParametersInfoUtils.class, PathUtils.class})
@PowerMockIgnore("javax.management.*")
public class CatalogueEssentialsFacetsComponentTest {
    @Mock
    private HstRequest request;

    @Mock
    private HstResponse response;

    @Mock
    private HstRequestContext context;

    @Mock
    private CatalogueEssentialsFacetsComponent testComponent;

    @Mock
    private CatalogueFilterManager apiCatalogueFilterManager;

    @Mock
    private Session mockSession;

    @Mock
    private HippoFacetNavigationBean mockFacetBean;

    @Mock
    private HippoFacetNavigationBean mockFolder1;

    @Mock
    private HippoFacetNavigationBean mockFolder2;

    @Mock
    private HippoFacetNavigationBean mockSubFolder;

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
        PowerMockito.when(RequestContextProvider.get()).thenReturn(context);
        when(request.getRequestContext()).thenReturn(context);
        when(context.getSession()).thenReturn(mockSession);

        // Use the test subclass
        testComponent = spy(new CatalogueEssentialsFacetsComponentTestSubclass());
    }

    @Test
    public void testDoBeforeRender() throws Exception {
        // Arrange
        HippoFacetNavigationBean mockedBean = mock(HippoFacetNavigationBean.class);
        when(request.getModel("facets")).thenReturn(mockedBean);

        Filters mockedFilters = mock(Filters.class);
        when(apiCatalogueFilterManager.getRawFilters(request)).thenReturn(mockedFilters);

        // Use ArgumentCaptor
        ArgumentCaptor<Filters> filtersCaptor = ArgumentCaptor.forClass(Filters.class);

        // Act
        testComponent.doBeforeRender(request, response);

        // Assert
        verify(request, times(1)).getModel("facets");
        verify(request, times(1)).setAttribute(eq("filters"), filtersCaptor.capture());
        verify(request, times(1)).setModel(eq("facetFilterMap"), any(HashMap.class));

        // Validate captured Filters object
        Filters capturedFilters = filtersCaptor.getValue();
        assertNotNull(capturedFilters); // Example validation, add more checks as needed
    }


    @Test
    public void testGetFacetFilterMap_withNestedFolders() {
        // Arrange
        when(mockFacetBean.getFolders()).thenReturn(Collections.singletonList(mockFolder1));
        when(mockFolder1.getFolders()).thenReturn(Arrays.asList(mockFolder2, mockSubFolder));

        when(mockFolder2.getDisplayName()).thenReturn("Folder2");
        when(mockFolder2.isLeaf()).thenReturn(false);
        when(mockFolder2.getCount()).thenReturn(5L);

        when(mockSubFolder.getDisplayName()).thenReturn("SubFolder");
        when(mockSubFolder.isLeaf()).thenReturn(true);
        when(mockSubFolder.getCount()).thenReturn(10L);

        // Act
        HashMap<String, List<Object>> result = testComponent.getFacetFilterMap(mockFacetBean);

        // Assert
        assertEquals(2, result.size());

        List<Object> folder2Details = result.get("Folder2");
        assertEquals(mockFolder2, folder2Details.get(0));
        assertEquals(false, folder2Details.get(1));
        assertEquals(5L, folder2Details.get(2));

        List<Object> subFolderDetails = result.get("SubFolder");
        assertEquals(mockSubFolder, subFolderDetails.get(0));
        assertEquals(true, subFolderDetails.get(1));
        assertEquals(10L, subFolderDetails.get(2));

        // Verify interactions
        verify(mockFacetBean, atLeastOnce()).getFolders(); // Allow multiple invocations
        verify(mockFolder1).getFolders();
        verify(mockFolder2).getDisplayName();
        verify(mockFolder2).isLeaf();
        verify(mockFolder2).getCount();
        verify(mockSubFolder).getDisplayName();
        verify(mockSubFolder).isLeaf();
        verify(mockSubFolder).getCount();
    }


    @Test
    public void testGetFacetFilterMap_withEmptyFolders() {
        // Arrange
        when(mockFacetBean.getFolders()).thenReturn(Collections.emptyList());

        // Act
        HashMap<String, List<Object>> result = testComponent.getFacetFilterMap(mockFacetBean);

        // Assert
        assertEquals(0, result.size());

        // Verify interactions
        verify(mockFacetBean).getFolders();
    }

}
