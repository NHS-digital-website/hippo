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
import org.onehippo.cms7.essentials.components.info.EssentialsFacetsComponentInfo;
import org.onehippo.cms7.essentials.components.utils.SiteUtils;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import uk.nhs.digital.common.components.catalogue.filters.Filters;
import uk.nhs.digital.common.components.catalogue.filters.Section;
import uk.nhs.digital.common.components.catalogue.filters.Subsection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * Unit tests for the ApiCatalogueEssentialsFacetsComponent.
 * This test class uses mock objects to verify the behavior of the ApiCatalogueEssentialsFacetsComponent.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({HstServices.class, SiteUtils.class, ContentBeanUtils.class, RequestContextProvider.class, ParametersInfoUtils.class, PathUtils.class})
@PowerMockIgnore("javax.management.*")
public class ApiCatalogueEssentialsFacetsComponentTest {

    @Mock
    private HstRequest request;

    @Mock
    private HstResponse response;

    @Mock
    private Filters rawFilters;

    @Mock
    private Section section;

    @Mock
    private Subsection subsection;

    @Mock
    private HstRequestContext context;

    @Mock
    private ApiCatalogueEssentialsFacetsComponentTestHelper testComponent;

    @Mock
    private ResolvedSiteMapItem resolvedSiteMapItem;

    @Mock
    private ApiCatalogueFilterManager apiCatalogueFilterManager;

    @Mock
    private Session mockSession;

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

        resolvedSiteMapItem = mock(ResolvedSiteMapItem.class);

        when(resolvedSiteMapItem.getRelativeContentPath()).thenReturn("faceted-api-specifications");
        when(PathUtils.normalizePath("mocked/path")).thenReturn("faceted-api-specifications");
        when(context.getResolvedSiteMapItem()).thenReturn(resolvedSiteMapItem);

        // Set up the mock behavior for the protected method
        EssentialsFacetsComponentInfo paramInfo = mock(EssentialsFacetsComponentInfo.class);
        when(paramInfo.getFacetPath()).thenReturn("some/facet/path");

        testComponent = spy(new ApiCatalogueEssentialsFacetsComponentTestHelper(paramInfo));

    }

    @Test
    public void testDoBeforeRender() throws Exception {

        // Arrange
        HippoFacetNavigationBean mockedBean = mock(HippoFacetNavigationBean.class);
        when(request.getModel("facets")).thenReturn(mockedBean);

        HashMap<String, List<Object>> facetBeanMap = new HashMap<>();
        facetBeanMap.put("exampleKey", Arrays.asList(new Object(), new Object()));

        Filters mockedFilters = mock(Filters.class);
        when(apiCatalogueFilterManager.getRawFilters(request)).thenReturn(mockedFilters);
        List<Section> mockSections = Arrays.asList(mock(Section.class), mock(Section.class));
        when(mockedFilters.getSections()).thenReturn(mockSections);

        PowerMockito.doReturn(facetBeanMap).when(testComponent, "getFacetFilterMap", mockedBean);

        // Act
        testComponent.doBeforeRender(request, response);

        // Assert
        verify(request, times(1)).setAttribute(eq("filtersModel"), any(Filters.class));
        verify(request, times(1)).getModel("facets");
        verify(request, times(1)).setModel("facets1", facetBeanMap);
        assertNotNull(mockedBean);

    }

    @Test
    public void testDoBeforeRenderHandlesNullValues() throws Exception {

        when(apiCatalogueFilterManager.getRawFilters(request)).thenReturn(null);

        HippoFacetNavigationBean mockedBean = mock(HippoFacetNavigationBean.class);
        when(request.getModel("facets")).thenReturn(mockedBean);

        HashMap<String, List<Object>> facetBeanMap = new HashMap<>();
        facetBeanMap.put("exampleKey", Arrays.asList(new Object(), new Object()));

        PowerMockito.doReturn(facetBeanMap).when(testComponent, "getFacetFilterMap", mockedBean);

        // Act
        try {
            testComponent.doBeforeRender(request, response);
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            // Assert
            fail("IndexOutOfBoundsException or NullPointerException should not occur in doBeforeRender: " + e.getMessage());
        }
    }

    @Test
    public void testGetFiltersBasedOnFacetResults() {
        // Arrange
        List<Section> sections = new ArrayList<>();
        sections.add(section);
        when(rawFilters.getSections()).thenReturn(sections);

        List<Subsection> entries = new ArrayList<>();
        entries.add(subsection);
        when(section.getEntries()).thenReturn(entries);

        doNothing().when(subsection).display();
        doNothing().when(section).display();
        doNothing().when(section).setShowMoreIndc(false);
        when(subsection.getTaxonomyKey()).thenReturn("someKey");

        HashMap<String, List<Object>> facetBeanMap = new HashMap<>();
        facetBeanMap.put("someKey", Arrays.asList("someValue", true));

        // Act
        Filters result = testComponent.getFiltersBasedOnFacetResults(rawFilters, facetBeanMap);

        assertEquals(rawFilters, result);

        // Assert
        verify(section, atLeastOnce()).display();
        verify(subsection, atLeastOnce()).display();
        verify(section).setShowMoreIndc(false);
    }

    @Test
    public void testGetFiltersBasedOnFacetResultsHandlesNullValues() {
        // Arrange
        Filters rawFilters = new Filters();
        HashMap<String, List<Object>> facetBeanMap = new HashMap<>();

        // Act
        try {
            Filters result = testComponent.getFiltersBasedOnFacetResults(rawFilters, facetBeanMap);
            // Assert
            assertNotNull(result);
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            // Assert
            fail("IndexOutOfBoundsException or NullPointerException should not occur in getFiltersBasedOnFacetResults: " + e.getMessage());
        }

        // ActfacetBeanMap
        try {
            Filters result = testComponent.getFiltersBasedOnFacetResults(null, null);
            // Assert
            assertNull(result);
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            // Assert
            fail("IndexOutOfBoundsException or NullPointerException should not occur in getFiltersBasedOnFacetResults: " + e.getMessage());
        }
    }

    @Test
    public void testDisplayShowMoreButton_ShouldShowMore() {
        // Arrange
        AtomicInteger subSectionCounter = new AtomicInteger(5);
        Section section = mock(Section.class);

        when(section.getAmountChildrenToShow()).thenReturn(3);
        when(section.getHideChildren()).thenReturn(true);

        // Act
        testComponent.displayShowMoreButton(subSectionCounter, section);

        // Assert
        verify(section).setShowMoreIndc(true);
    }

    @Test
    public void testDisplayShowMoreButton_ShouldNotShowMoreDueToChildrenCount() {
        // Arrange
        AtomicInteger subSectionCounter = new AtomicInteger(2);
        Section section = mock(Section.class);

        when(section.getAmountChildrenToShow()).thenReturn(3);
        when(section.getHideChildren()).thenReturn(true);

        // Act
        testComponent.displayShowMoreButton(subSectionCounter, section);

        // Assert
        verify(section, never()).setShowMoreIndc(true);
    }

    @Test
    public void testDisplayShowMoreButton_ShouldNotShowMoreDueToHideChildrenFlag() {
        // Arrange
        AtomicInteger subSectionCounter = new AtomicInteger(5);
        Section section = mock(Section.class);

        when(section.getAmountChildrenToShow()).thenReturn(3);
        when(section.getHideChildren()).thenReturn(false);

        // Act
        testComponent.displayShowMoreButton(subSectionCounter, section);

        // Assert
        verify(section, never()).setShowMoreIndc(true);
    }

    @Test
    public void testDisplayFirstLevelParentFilter() {
        // Arrange
        final AtomicInteger subSectionCounter = new AtomicInteger(1);
        Section section = mock(Section.class);
        Subsection subsection = mock(Subsection.class);
        final HashMap<String, List<Object>> facetBeanMap = new HashMap<>();

        when(subsection.getTaxonomyKey()).thenReturn("someKey");
        facetBeanMap.put("someKey", Arrays.asList("someValue", true));

        when(section.getAmountChildrenToShow()).thenReturn(3);
        when(section.getHideChildren()).thenReturn(false);

        ApiCatalogueEssentialsFacetsComponent testedComponent = new ApiCatalogueEssentialsFacetsComponent() {
            @Override
            public boolean isTaxonomyKeyPresentInFacet(Subsection subsection, HashMap<String, List<Object>> facetBeanMap) {
                return true;
            }
        };

        // Act
        testedComponent.displayFirstLevelParentFilter(subSectionCounter, section, subsection, facetBeanMap);

        // Assert
        verify(subsection).display();
        verify(section).expand();
    }

    @Test
    public void testDisplaySecondLevelChildFilter() {
        // Arrange
        final AtomicInteger subSectionCounter = new AtomicInteger(1);
        Section section = mock(Section.class);
        Subsection subsection = mock(Subsection.class);
        Subsection subsectionEntry = mock(Subsection.class);
        final HashMap<String, List<Object>> facetBeanMap = new HashMap<>();
        final List<Runnable> deferredOperations = new ArrayList<>();
        final AtomicBoolean display = new AtomicBoolean(false);

        when(subsection.getEntries()).thenReturn(Arrays.asList(subsectionEntry));
        when(subsectionEntry.getAmountChildrenToShow()).thenReturn(0);
        when(subsectionEntry.getHideChildren()).thenReturn(false);
        when(subsectionEntry.getTaxonomyKey()).thenReturn("someKey");
        when(section.getAmountChildrenToShow()).thenReturn(3);

        facetBeanMap.put("someKey", Arrays.asList("someValue", true));

        ApiCatalogueEssentialsFacetsComponent testedComponent = new ApiCatalogueEssentialsFacetsComponent() {
            @Override
            public boolean isTaxonomyKeyPresentInFacet(Subsection subsection, HashMap<String, List<Object>> facetBeanMap) {
                return true;
            }

            @Override
            public boolean isGrayedOutFilter(Subsection subsection) {
                return true;
            }

            @Override
            public boolean isApisFilter(Subsection subsection) {
                return true;
            }
        };

        // Act
        testedComponent.displaySecondLevelChildFilter(subsection, facetBeanMap, subSectionCounter, section, display, deferredOperations);

        int expectedCount = subSectionCounter.get() + subsectionEntry.getAmountChildrenToShow();

        // Assert
        verify(subsectionEntry).display();
        verify(subsectionEntry).setCount(expectedCount);
        verify(section).expand();
        assertTrue(display.get());
        assertEquals(1, deferredOperations.size());
    }

    @Test
    public void testHandleIndexOutOfBoundsAndNullPointer() {
        try {
            HippoFacetNavigationBean mockedBean = mock(HippoFacetNavigationBean.class);
            when(request.getModel("facets")).thenReturn(mockedBean);

            // Act
            testComponent.doBeforeRender(request, mock(HstResponse.class));
            testComponent.getFiltersBasedOnFacetResults(null, null);
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            // Assert
            fail("IndexOutOfBoundsException or NullPointerException should not occur: " + e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testDoBeforeRender_ThrowsNullPointerException() throws Exception {
        // Arrange
        // Mocking necessary objects
        HippoFacetNavigationBean mockedBean = mock(HippoFacetNavigationBean.class);
        when(request.getModel("facets")).thenReturn(null); // Intentionally returning null to trigger the exception

        // Act
        testComponent.doBeforeRender(request, response);
    }

    @Test
    public void getFiltersBasedOnFacetResults_ShouldReturnNull_WhenRawFiltersAndFacetBeanMapAreNull() {
        // Act: Call the method with null inputs
        Filters result = testComponent.getFiltersBasedOnFacetResults(null, null);

        // Assert: Verify that the method returns null
        assertNull("Expected null result when both inputs are null", result);
    }

}