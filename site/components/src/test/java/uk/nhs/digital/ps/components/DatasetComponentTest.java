package uk.nhs.digital.ps.components;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.hippoecm.hst.container.ModifiableRequestContextProvider;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.mock.core.component.MockHstRequest;
import org.hippoecm.hst.mock.core.component.MockHstResponse;
import org.hippoecm.hst.mock.core.container.MockComponentManager;
import org.hippoecm.hst.mock.core.request.MockHstRequestContext;
import org.hippoecm.hst.site.HstServices;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import uk.nhs.digital.ps.beans.Dataset;

@RunWith(MockitoJUnitRunner.class)
public class DatasetComponentTest {

    private DatasetComponent component;
    private MockHstRequest request;
    private MockHstResponse response;
    private MockHstRequestContext requestContext;

    @Before
    public void setUp() {
        HstServices.setComponentManager(new MockComponentManager());
        request = new MockHstRequest();
        response = new MockHstResponse();
        requestContext = new MockHstRequestContext();
        request.setRequestContext(requestContext);
        ModifiableRequestContextProvider.set(requestContext);
        component = new DatasetComponent();
    }

    @After
    public void tearDown() {
        ModifiableRequestContextProvider.clear();
    }

    @Test
    public void setsDatasetAttributeWhenDatasetPubliclyAccessible() {
        Dataset dataset = Mockito.mock(Dataset.class);
        Mockito.when(dataset.isPubliclyAccessible()).thenReturn(true);
        requestContext.setContentBean(dataset);

        component.doBeforeRender(request, response);

        assertSame(dataset, request.getAttribute("dataset"));
        assertNull(response.getForwardPathInfo());
    }

    @Test
    public void forwardsTo404WhenDatasetIsRestricted() {
        Dataset dataset = Mockito.mock(Dataset.class);
        Mockito.when(dataset.isPubliclyAccessible()).thenReturn(false);
        requestContext.setContentBean(dataset);

        component.doBeforeRender(request, response);

        assertThat(response.getForwardPathInfo(), equalTo("/error/404"));
        assertThat(request.getAttribute("dataset"), nullValue());
    }

    @Test
    public void doesNotThrowWhenContentBeanIsNotDataset() {
        HippoBean nonDatasetBean = Mockito.mock(HippoBean.class);
        requestContext.setContentBean(nonDatasetBean);

        component.doBeforeRender(request, response);

        assertThat(request.getAttribute("dataset"), nullValue());
    }
}
