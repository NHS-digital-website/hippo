package uk.nhs.digital.common.components;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

import org.hippoecm.hst.container.ModifiableRequestContextProvider;
import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.core.component.HstParameterInfoProxyFactoryImpl;
import org.hippoecm.hst.core.container.ComponentManager;
import org.hippoecm.hst.mock.core.component.MockHstRequest;
import org.hippoecm.hst.mock.core.component.MockHstResponse;
import org.hippoecm.hst.mock.core.request.MockHstRequestContext;
import org.hippoecm.hst.site.HstServices;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.onehippo.cms7.essentials.components.EssentialsFacetsComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsFacetsComponentInfo;
import org.onehippo.taxonomy.api.Taxonomies;
import org.onehippo.taxonomy.api.Taxonomy;
import org.onehippo.taxonomy.api.TaxonomyManager;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import uk.nhs.digital.ps.beans.HippoBeanHelper;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SearchEssentialsFacetsComponent.class, EssentialsFacetsComponent.class})
@PowerMockIgnore({"javax.management.*", "javax.net.ssl.*", "javax.xml.*", "org.xml.*", "com.sun.org.apache.xerces.*"})
public class SearchEssentialsFacetsComponentTest {

    private SearchEssentialsFacetsComponent component;
    private MockHstRequest request;
    private MockHstResponse response;
    private MockHstRequestContext requestContext;
    private ComponentManager originalComponentManager;

    @Mock private ComponentManager componentManager;
    @Mock private TaxonomyManager taxonomyManager;
    @Mock private Taxonomies taxonomies;
    @Mock private Taxonomy taxonomy;
    @Mock private TaxonomyFacetWrapper taxonomyFacetWrapper;
    @Mock private HippoFacetNavigationBean facetNavigationBean;

    @Before
    public void setUp() {
        originalComponentManager = HstServices.getComponentManager();
        HstServices.setComponentManager(componentManager);

        component = new TestSearchEssentialsFacetsComponent(new StubEssentialsFacetsComponentInfo());
        request = new MockHstRequest();
        response = new MockHstResponse();
        requestContext = new MockHstRequestContext();
        requestContext.setParameterInfoProxyFactory(new HstParameterInfoProxyFactoryImpl());
        request.setRequestContext(requestContext);
        ModifiableRequestContextProvider.set(requestContext);

        when(componentManager.getComponent(TaxonomyManager.class.getName())).thenReturn(taxonomyManager);
        when(taxonomyManager.getTaxonomies()).thenReturn(taxonomies);

        PowerMockito.suppress(PowerMockito.methodsDeclaredIn(EssentialsFacetsComponent.class));
    }

    @After
    public void tearDown() {
        ModifiableRequestContextProvider.clear();
        HstServices.setComponentManager(originalComponentManager);
    }

    @Test
    public void setsTaxonomyAttributeWhenFacetDataAvailable() throws Exception {
        request.setModel("facets", facetNavigationBean);
        when(taxonomies.getTaxonomy(HippoBeanHelper.PUBLICATION_TAXONOMY)).thenReturn(taxonomy);
        PowerMockito.whenNew(TaxonomyFacetWrapper.class)
            .withArguments(taxonomy, facetNavigationBean)
            .thenReturn(taxonomyFacetWrapper);

        component.doBeforeRender(request, response);

        assertSame(taxonomyFacetWrapper, request.getAttribute("taxonomy"));
    }

    @Test
    public void handlesMissingTaxonomyGracefully() throws Exception {
        request.setModel("facets", facetNavigationBean);
        when(taxonomies.getTaxonomy(HippoBeanHelper.PUBLICATION_TAXONOMY)).thenReturn(null);

        component.doBeforeRender(request, response);

        assertNull(request.getAttribute("taxonomy"));
    }

    @Test
    public void handlesMissingTaxonomyManagerGracefully() throws Exception {
        request.setModel("facets", facetNavigationBean);
        when(componentManager.getComponent(TaxonomyManager.class.getName())).thenReturn(null);

        component.doBeforeRender(request, response);

        assertNull(request.getAttribute("taxonomy"));
    }

    private static class TestSearchEssentialsFacetsComponent extends SearchEssentialsFacetsComponent {

        private final EssentialsFacetsComponentInfo componentInfo;

        TestSearchEssentialsFacetsComponent(EssentialsFacetsComponentInfo componentInfo) {
            this.componentInfo = componentInfo;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected <T> T getComponentParametersInfo(org.hippoecm.hst.core.component.HstRequest request) {
            return (T) componentInfo;
        }
    }

    private static class StubEssentialsFacetsComponentInfo implements EssentialsFacetsComponentInfo {

        @Override
        public String getFacetPath() {
            return "test/facet/path";
        }
    }
}
