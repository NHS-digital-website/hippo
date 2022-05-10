package uk.nhs.digital.common.forms.behavior;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.MockitoAnnotations.openMocks;

import com.onehippo.cms7.eforms.hst.beans.FormBean;
import com.onehippo.cms7.eforms.hst.model.Form;
import org.hippoecm.hst.component.support.forms.FormField;
import org.hippoecm.hst.component.support.forms.FormMap;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.ComponentConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class SubscriptionBehaviorTest {

    @Mock
    private FormBean formBean;
    @Mock
    private FormMap formMap;
    @Mock
    private Form form;
    @Mock
    private ComponentConfiguration configuration;
    @Mock
    private HstRequest hstRequest;
    @Mock
    private HstResponse hstResponse;

    private final SubscriptionBehavior behavior = new SubscriptionBehavior();

    @Before
    public void setUp() {
        openMocks(this);
    }

    @Test
    public void isEnabled_returns_false_if_form_bean_does_not_have_required_property() {
        assertFalse(behavior.isEnabled(formBean));
    }

    @Test
    public void isEnabled_returns_true_if_form_bean_has_required_property() {
        given(formBean.getSingleProperty("eforms:govdeliveryapi")).willReturn("true");

        assertTrue(behavior.isEnabled(formBean));
    }

    @Test
    public void onValidationSuccess_calls_api() {
        FormField emailField = mock(FormField.class);
        FormField topicsField = mock(FormField.class);
        given(emailField.getValue()).willReturn("user@domain.com");
        given(topicsField.getValue()).willReturn("TOPIC1,TOPIC2");

        behavior.onValidationSuccess(hstRequest, hstResponse, configuration, formBean, form, formMap);
    }
}
