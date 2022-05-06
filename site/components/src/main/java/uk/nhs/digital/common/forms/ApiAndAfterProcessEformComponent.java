package uk.nhs.digital.common.forms;

import com.onehippo.cms7.eforms.hst.behaviors.AfterProcessBehavior;
import com.onehippo.cms7.eforms.hst.components.EformComponent;
import com.onehippo.cms7.eforms.hst.model.ErrorMessage;
import com.onehippo.cms7.eforms.hst.model.Form;
import org.hippoecm.hst.component.support.forms.FormMap;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.forms.behavior.SubscriptionBehavior;

import java.util.Map;
import javax.servlet.http.HttpServletResponse;

public class ApiAndAfterProcessEformComponent extends EformComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiAndAfterProcessEformComponent.class);

    @Override
    public boolean onValidationSuccess(HstRequest request, HstResponse response, Form form, FormMap map) {
        return true;
    }

    @Override
    public void onValidationError(HstRequest request, HstResponse response, Form form, FormMap map,
                                  Map<String, ErrorMessage> errors) {
        LOGGER.error("Form contains errors.");
        // Called on second pass (RENDER_PHASE)
        if ("RENDER_PHASE".equals(request.getLifecyclePhase()) && map.getFormMap().get(EformComponent.ATTRIBUTE_PROCESS_DONE) == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void addConfiguredBehaviors(HstRequest request) {
        super.addConfiguredBehaviors(request);
        addBehavior(new AfterProcessBehavior());
        addBehavior(new ReCaptchaValidationPlugin());
        addBehavior(new SubscriptionBehavior());
    }
}
