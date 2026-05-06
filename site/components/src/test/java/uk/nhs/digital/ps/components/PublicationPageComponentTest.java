package uk.nhs.digital.ps.components;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.hippoecm.hst.container.ModifiableRequestContextProvider;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.container.ComponentManager;
import org.hippoecm.hst.mock.core.component.MockHstRequest;
import org.hippoecm.hst.mock.core.component.MockHstResponse;
import org.hippoecm.hst.mock.core.container.MockComponentManager;
import org.hippoecm.hst.mock.core.request.MockComponentConfiguration;
import org.hippoecm.hst.mock.core.request.MockHstRequestContext;
import org.hippoecm.hst.site.HstServices;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.mock.web.MockServletContext;
import uk.nhs.digital.ps.beans.PublicationPage;
import uk.nhs.digital.test.mockito.MockitoSessionTestBase;

import java.util.List;

public class PublicationPageComponentTest extends MockitoSessionTestBase {

    @Mock private PublicationPage publicationPage;

    private PublicationPageComponent component;
    private MockHstRequest request;
    private MockHstResponse response;
    private MockHstRequestContext requestContext;
    private ComponentManager originalComponentManager;
    private MockComponentManager mockComponentManager;

    @Before
    public void setUp() throws Exception {
        originalComponentManager = HstServices.getComponentManager();
        mockComponentManager = new MockComponentManager();
        HstServices.setComponentManager(mockComponentManager);

        component = new PublicationPageComponent();
        component.init(new MockServletContext(), new MockComponentConfiguration());

        request = new MockHstRequest();
        response = new MockHstResponse();
        requestContext = new MockHstRequestContext();
        request.setRequestContext(requestContext);
        ModifiableRequestContextProvider.set(requestContext);
    }

    @After
    public void tearDown() {
        ModifiableRequestContextProvider.set(null);
        HstServices.setComponentManager(originalComponentManager);
    }

    @Test
    public void setsPageAndSectionsWhenContentBeanPresent() throws HstComponentException {
        List<HippoBean> sections = asList(mock(HippoBean.class), mock(HippoBean.class));
        when(publicationPage.getSections()).thenReturn(sections);
        requestContext.setContentBean(publicationPage);

        component.doBeforeRender(request, response);

        assertSame(publicationPage, request.getAttribute("page"));
        assertEquals(sections, request.getAttribute("pageSections"));
    }

    @Test
    public void doesNotSetAttributesWhenContentBeanIsNotPublicationPage() throws HstComponentException {
        requestContext.setContentBean(mock(HippoBean.class));

        component.doBeforeRender(request, response);

        assertNull(request.getAttribute("page"));
        assertNull(request.getAttribute("pageSections"));
    }
}
