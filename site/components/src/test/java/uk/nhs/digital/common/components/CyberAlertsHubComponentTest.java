package uk.nhs.digital.common.components;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.util.ContentBeanUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.onehippo.cms7.essentials.components.EssentialsListComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsListComponentInfo;
import org.onehippo.cms7.essentials.components.utils.SiteUtils;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import uk.nhs.digital.common.components.info.CyberAlertsHubComponentInfo;

import java.lang.reflect.Method;
import java.util.Arrays;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "javax.management.*"})
@PrepareForTest({ContentBeanUtils.class, SiteUtils.class, EssentialsListComponent.class, CommonComponent.class})
public class CyberAlertsHubComponentTest {

    @Mock private HstRequest request;
    @Mock private HstResponse response;
    @Mock private HstRequestContext requestContext;
    @Mock private CyberAlertsHubComponentInfo componentInfo;
    @Mock private HippoBean searchScope;
    @Mock private HippoFacetNavigationBean facetNavigationBean;

    private TestableCyberAlertsHubComponent component;

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(ContentBeanUtils.class);
        PowerMockito.mockStatic(SiteUtils.class);
        PowerMockito.suppress(PowerMockito.constructor(CommonComponent.class));
        Arrays.stream(EssentialsListComponent.class.getDeclaredMethods())
            .filter(method -> method.getName().equals("doBeforeRender"))
            .forEach(PowerMockito::suppress);

        when(request.getRequestContext()).thenReturn(requestContext);
        when(componentInfo.getPath()).thenReturn("/cyber-alerts");

        component = new TestableCyberAlertsHubComponent();
        component.setComponentParametersInfo(componentInfo);
        component.setSearchScope(searchScope);
        component.setSearchQuery("query");
    }

    @Test
    public void setsTotalAvailableWhenFacetNavigationExists() {
        when(SiteUtils.relativePathFrom(searchScope, requestContext)).thenReturn("facet/path");
        when(ContentBeanUtils.getFacetNavigationBean("facet/path", "query")).thenReturn(facetNavigationBean);
        when(facetNavigationBean.getCount()).thenReturn(42L);

        component.doBeforeRender(request, response);

        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(request).setAttribute(eq("totalAvailable"), captor.capture());
        assertThat(captor.getValue(), equalTo(42));
    }

    @Test
    public void failsWhenFacetNavigationMissing() {
        when(SiteUtils.relativePathFrom(searchScope, requestContext)).thenReturn("facet/path");
        when(ContentBeanUtils.getFacetNavigationBean("facet/path", "query")).thenReturn(null);

        component.doBeforeRender(request, response);
    }

    @Test
    public void failsWhenSearchScopeIsNull() {
        component.setSearchScope(null);
        when(SiteUtils.relativePathFrom((HippoBean) null, requestContext)).thenThrow(new NullPointerException("scope"));

        component.doBeforeRender(request, response);
    }

    private static class TestableCyberAlertsHubComponent extends CyberAlertsHubComponent {

        private EssentialsListComponentInfo componentInfo;
        private HippoBean scope;
        private String searchQuery;

        void setComponentParametersInfo(final EssentialsListComponentInfo componentInfo) {
            this.componentInfo = componentInfo;
        }

        void setSearchScope(final HippoBean scope) {
            this.scope = scope;
        }

        void setSearchQuery(final String searchQuery) {
            this.searchQuery = searchQuery;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected <T> T getComponentParametersInfo(final HstRequest request) {
            return (T) componentInfo;
        }

        @Override
        protected HippoBean getSearchScope(final HstRequest request, final String scopePath) {
            return scope;
        }

        @Override
        protected String getSearchQuery(final HstRequest request) {
            return searchQuery;
        }
    }
}
