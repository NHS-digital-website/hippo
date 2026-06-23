package uk.nhs.digital.ps.components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.openMocks;

import org.hippoecm.hst.mock.core.component.MockHstRequest;
import org.hippoecm.hst.mock.core.component.MockHstResponse;
import org.hippoecm.hst.mock.core.request.MockHstRequestContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import uk.nhs.digital.ps.beans.Dataset;
import uk.nhs.digital.ps.beans.Publication;
import uk.nhs.digital.ps.beans.navigation.CiBreadcrumb;
import uk.nhs.digital.ps.beans.navigation.CiBreadcrumbProvider;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CiBreadcrumbComponent.class)
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "javax.management.*"})
public class CiBreadcrumbComponentTest {

    private static final String STATISTICAL_PATH =
        "/content/documents/corporate-website/publication-system/statistical/example";

    private AutoCloseable mocks;
    private CiBreadcrumbComponent component;
    private MockHstRequest request;
    private MockHstResponse response;
    private MockHstRequestContext requestContext;

    @Mock private CiBreadcrumbProvider breadcrumbProvider;
    @Mock private CiBreadcrumb breadcrumb;
    @Mock private Dataset dataset;
    @Mock private Publication publication;

    @Before
    public void setUp() throws Exception {
        mocks = openMocks(this);

        component = new CiBreadcrumbComponent();
        request = new MockHstRequest();
        response = new MockHstResponse();
        requestContext = new MockHstRequestContext();
        request.setRequestContext(requestContext);

        PowerMockito.whenNew(CiBreadcrumbProvider.class)
            .withAnyArguments()
            .thenReturn(breadcrumbProvider);
        given(breadcrumbProvider.getBreadcrumb()).willReturn(breadcrumb);
    }

    @After
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void setsBreadcrumbAttributesForStatisticalPublication() throws Exception {
        requestContext.setContentBean(dataset);
        given(dataset.getPath()).willReturn(STATISTICAL_PATH);

        component.doBeforeRender(request, response);

        assertSame(breadcrumb, request.getAttribute("ciBreadcrumb"));
        assertEquals(Boolean.TRUE, request.getAttribute("isStatisticalPublication"));
    }

    @Test
    public void handlesMissingContentBean() throws Exception {
        requestContext.setContentBean(null);

        component.doBeforeRender(request, response);

        assertSame(breadcrumb, request.getAttribute("ciBreadcrumb"));
        assertEquals(Boolean.FALSE, request.getAttribute("isStatisticalPublication"));
    }

    @Test
    public void handlesContentBeanWithoutPath() throws Exception {
        requestContext.setContentBean(publication);
        given(publication.getPath()).willReturn(null);

        component.doBeforeRender(request, response);

        assertSame(breadcrumb, request.getAttribute("ciBreadcrumb"));
        assertEquals(Boolean.FALSE, request.getAttribute("isStatisticalPublication"));
    }
}
