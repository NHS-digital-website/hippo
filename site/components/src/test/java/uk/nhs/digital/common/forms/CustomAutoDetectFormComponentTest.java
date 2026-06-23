package uk.nhs.digital.common.forms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.onehippo.cms7.eforms.hst.beans.FormBean;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.nhs.digital.common.forms.behavior.SubscriptionBehavior;
import uk.nhs.digital.website.beans.Signup;

public class CustomAutoDetectFormComponentTest {

    private CustomAutoDetectFormComponent component;

    @Mock
    private HstRequest request;

    @Mock
    private HstRequestContext requestContext;

    @Mock
    private Signup signup;

    @Mock
    private FormBean formBean;

    @Before
    public void setUp() {
        initMocks(this);
        component = new CustomAutoDetectFormComponent();
        when(request.getRequestContext()).thenReturn(requestContext);
    }

    /* -----------------------------
     * Auto-detect behaviour tests
     * ----------------------------- */

    @Test
    public void shouldIncludeSubscriptionBehaviour() {
        Class[] behaviours = component.getAutoDetectBehaviors();

        boolean found = false;
        for (Class behaviour : behaviours) {
            if (behaviour.equals(SubscriptionBehavior.class)) {
                found = true;
                break;
            }
        }

        assertTrue(found);
    }

    @Test
    public void shouldIncludeRecaptchaBehaviour() {
        Class[] behaviours = component.getAutoDetectBehaviors();

        boolean found = false;
        for (Class behaviour : behaviours) {
            if (behaviour.equals(ReCaptchaValidationPlugin.class)) {
                found = true;
                break;
            }
        }

        assertTrue(found);
    }

    /* -----------------------------
     * getFormBean behaviour
     * ----------------------------- */

    @Test
    public void shouldReturnFormBeanFromSignupDocument() {
        CustomAutoDetectFormComponent spy =
            org.mockito.Mockito.spy(component);

        when(requestContext.getContentBean()).thenReturn(signup);
        doReturn(formBean).when(spy).getFormBeanFromSignupDocument(request);

        FormBean result = spy.getFormBean(request);

        assertNotNull(result);
        assertEquals(formBean, result);
    }

    @Test
    public void shouldReturnNullWhenSignupHasNoFormLink() {
        CustomAutoDetectFormComponent spy =
            org.mockito.Mockito.spy(component);

        when(request.getRequestContext()).thenReturn(requestContext);
        when(requestContext.getContentBean()).thenReturn(signup);
        when(signup.getFormLink()).thenReturn(null);

        // ✅ Prevent framework picker logic from executing
        doReturn(null).when(spy).getFormBeanFromPicker(request);

        FormBean result = spy.getFormBean(request);

        assertNull(result);

    }

    @Test
    public void shouldFallbackToPickerWhenNotSignupDocument() {
        HippoBean otherDocument = mock(HippoBean.class);
        when(requestContext.getContentBean()).thenReturn(otherDocument);

        CustomAutoDetectFormComponent spy =
            org.mockito.Mockito.spy(component);

        doReturn(formBean).when(spy).getFormBeanFromPicker(request);

        FormBean result = spy.getFormBean(request);

        assertNotNull(result);
        assertEquals(formBean, result);
    }
}