package uk.nhs.digital.common.components.catalogue;

import static org.junit.Assert.*;
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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onehippo.cms7.essentials.components.info.EssentialsFacetsComponentInfo;
import org.onehippo.cms7.essentials.components.info.EssentialsListComponentInfo;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import org.onehippo.cms7.essentials.components.utils.SiteUtils;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import uk.nhs.digital.common.components.catalogue.filters.Filters;
import uk.nhs.digital.common.components.catalogue.filters.Section;

import java.util.*;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * Unit tests for the {@link ApiCatalogueHubComponent} class.
 * This test class is responsible for verifying the behavior of the ApiCatalogueHubComponent,
 * particularly the methods {@link ApiCatalogueHubComponent#doBeforeRender(HstRequest, HstResponse)}
 * and {@link ApiCatalogueHubComponent#doFacetedSearch(HstRequest, EssentialsListComponentInfo, HippoBean)}.
 * The test class uses PowerMock and Mockito for mocking static methods and dependencies.
 * It ensures that the component's logic behaves correctly under various scenarios.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({HstServices.class, SiteUtils.class, ContentBeanUtils.class, RequestContextProvider.class, ParametersInfoUtils.class})
@PowerMockIgnore("javax.management.*")
public class ApiCatalogueHubComponentTest {

    private ApiCatalogueHubComponent component;

    @Mock
    private HstRequest request;

    @Mock
    private HstResponse response;

    @Mock
    private ApiCatalogueFilterManager apiCatalogueFilterManager;

    @Mock
    private HstRequestContext requestContext;

    @Mock
    private ResolvedSiteMapItem resolvedSiteMapItem;

    @Mock
    private HippoFacetNavigationBean facetBean;

    @Mock
    private HippoResultSetBean resultSetBean;

    @Mock
    private HippoDocumentIterator<HippoBean> documentIterator;

    @Mock
    private EssentialsFacetsComponentInfo paramInfo;

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

        ComponentManager mockComponentManager = mock(ComponentManager.class);
        PowerMockito.when(HstServices.getComponentManager()).thenReturn(mockComponentManager);
        PowerMockito.when(RequestContextProvider.get()).thenReturn(requestContext);

        component = new ApiCatalogueHubComponent();
        component.setApiCatalogueFilterManager(apiCatalogueFilterManager);

        when(request.getRequestContext()).thenReturn(requestContext);
        when(requestContext.getSession()).thenReturn(mockSession);
        when(requestContext.getResolvedSiteMapItem()).thenReturn(resolvedSiteMapItem);

    }

    @Test
    public void testDoBeforeRender() {
        // Arrange
        Section statusSection = mock(Section.class);
        when(statusSection.getDisplayName()).thenReturn("Status");
        when(statusSection.getEntries()).thenReturn(new ArrayList<>());

        Section nonStatusSection = mock(Section.class);
        when(nonStatusSection.getDisplayName()).thenReturn("NonStatus");

        List<Section> sections = Arrays.asList(statusSection, nonStatusSection);
        Filters mockedFilters = mock(Filters.class);
        when(apiCatalogueFilterManager.getRawFilters(request)).thenReturn(mockedFilters);
        when(mockedFilters.getSections()).thenReturn(sections);

        when(paramInfo.getFacetPath()).thenReturn("mockedFacetPath");

        Map<String, Object> apiStatusEntries = new HashMap<>();
        when(request.getModel("apiStatusEntries")).thenReturn(apiStatusEntries);

        // Act
        component.doBeforeRender(request, response);

        // Assert
        verify(request, atLeastOnce()).setAttribute(eq("apiStatusEntries"), any(Map.class));
        verify(request, atLeastOnce()).setAttribute(eq("nonStatusSections"), eq(sections.subList(1, sections.size())));
    }

    @Test
    public void testDoFacetedSearchValidPath() {
        // Arrange
        EssentialsListComponentInfo info = mock(EssentialsListComponentInfo.class);
        setupCommonMocks(info);

        String path = "validPath";
        HippoBean scope = mock(HippoBean.class);
        mockContentBeanUtils(path, true);

        // Act
        Pageable<HippoBean> result = component.doFacetedSearch(request, info, scope);

        // Assert
        assertValidPath(result);

        // Clean up
        reset(request);
    }

    @Test
    public void testDoFacetedSearchInvalidPath() {
        // Arrange
        EssentialsListComponentInfo info = mock(EssentialsListComponentInfo.class);
        setupCommonMocks(info);

        String path = "invalidPath";
        HippoBean scope = mock(HippoBean.class);
        mockContentBeanUtils(path, false);

        // Act
        Pageable<HippoBean> result = component.doFacetedSearch(request, info, scope);

        // Assert
        assertInvalidPath(result);

        // Clean up
        reset(request);
    }

    private void setupCommonMocks(EssentialsListComponentInfo info) {
        when(info.getPageSize()).thenReturn(10);
        when(request.getRequestContext()).thenReturn(requestContext);
    }

    private void mockContentBeanUtils(String path, boolean isValid) {
        if (isValid) {
            when(SiteUtils.relativePathFrom(any(HippoBean.class), eq(requestContext))).thenReturn(path);
            when(ContentBeanUtils.getFacetNavigationBean(eq(path), any())).thenReturn(facetBean);
            when(facetBean.getResultSet()).thenReturn(resultSetBean);
            when(resultSetBean.getDocumentIterator(HippoBean.class)).thenReturn(documentIterator);
            when(resultSetBean.getDocumentSize()).thenReturn(2);
        } else {
            when(SiteUtils.relativePathFrom(any(HippoBean.class), eq(requestContext))).thenReturn(path);
            when(ContentBeanUtils.getFacetNavigationBean(eq(path), any())).thenReturn(null);
        }
    }

    private void assertValidPath(Pageable<HippoBean> result) {
        assertNotNull("The pageable result should not be null for valid path", result);
        verify(request).setAttribute("totalAvailable", 2);
    }

    private void assertInvalidPath(Pageable<HippoBean> result) {
        assertTrue("The pageable result should be empty for invalid path", result.getItems().isEmpty());
    }
}