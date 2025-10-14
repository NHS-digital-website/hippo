package uk.nhs.digital.common.components.catalogue;

import static org.mockito.Mockito.*;

import org.hippoecm.hst.container.RequestContextProvider;
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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onehippo.cms7.essentials.components.utils.SiteUtils;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import uk.nhs.digital.common.components.catalogue.filters.Filters;
import uk.nhs.digital.common.components.catalogue.filters.Section;
import uk.nhs.digital.common.components.catalogue.filters.Subsection;
import uk.nhs.digital.common.components.info.CatalogueSearchComponentInfo;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HstServices.class, SiteUtils.class, ContentBeanUtils.class, RequestContextProvider.class, ParametersInfoUtils.class, PathUtils.class})
@PowerMockIgnore({"javax.management.*", "com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*"})
public class CatalogueSearchComponentTest {

    @Mock
    private HstRequest request;

    @Mock
    private HstResponse response;

    @Mock
    private Section section;

    @Mock
    private Subsection subsection;

    @Mock
    private HstRequestContext context;

    private CatalogueSearchComponent catalogueSearchComponent;

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

        catalogueSearchComponent = new CatalogueSearchComponent();
    }

    @Test
    public void testDoBeforeRenderSetsQueryAttribute() {
        // Arrange
        String mockQueryValue = "testQuery";

        when(SiteUtils.getAnyParameter(eq("query"), eq(request), any())).thenReturn(mockQueryValue);

        CatalogueSearchComponentInfo mockInfo = mock(CatalogueSearchComponentInfo.class);
        when(mockInfo.getCatalogueSitemapRefId()).thenReturn("someRefId");

        CatalogueSearchComponentTest1 testComponent = new CatalogueSearchComponentTest1();
        testComponent.setMockInfo(mockInfo);

        // Act
        testComponent.doBeforeRender(request, response);

        // Assert
        verify(request).setAttribute("query", mockQueryValue);

        verify(request).setAttribute("catalogueSitemapRefId", "someRefId");

        // Clean up static mocking
        Mockito.clearAllCaches();
    }
}

