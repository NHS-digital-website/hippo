package uk.nhs.digital.common.forms;

import com.onehippo.cms7.eforms.hst.beans.FormBean;
import com.onehippo.cms7.eforms.hst.behaviors.AfterProcessBehavior;
import com.onehippo.cms7.eforms.hst.behaviors.MailFormDataBehavior;
import com.onehippo.cms7.eforms.hst.components.EformComponent;
import com.onehippo.cms7.eforms.hst.model.ErrorMessage;
import com.onehippo.cms7.eforms.hst.model.Form;
import net.sf.json.JSONObject;
import org.hippoecm.hst.component.support.forms.FormMap;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.container.ComponentManager;
import org.hippoecm.hst.site.HstServices;
import org.hippoecm.repository.util.JcrUtils;
import org.onehippo.cms7.crisp.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.forms.behavior.SubscriptionBehavior;

import java.util.Map;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletResponse;

public class FormProcessingComponent extends EformComponent {

    private static Logger LOGGER = LoggerFactory.getLogger(FormProcessingComponent.class);

    @Override
    public void onProcessDone(final HstRequest request, final HstResponse response, final Form form,
                              final FormMap map) {
        request.setAttribute("processDone", "true");
    }

    @Override
    public void onProcessFail(HstRequest request, HstResponse response, Form form, FormMap map) {
        request.setAttribute("processFailed", "true");
    }

    @Override
    public boolean onValidationSuccess(HstRequest request, HstResponse response, Form form, FormMap map) {
        return true;
    }

    @Override
    public void onValidationError(HstRequest request, HstResponse response, Form form, FormMap map, Map<String, ErrorMessage> errors) {
        LOGGER.info("Form contains errors");
        // Called on second pass (RENDER_PHASE)
        if ("RENDER_PHASE".equals(request.getLifecyclePhase()) && map.getFormMap().get("eforms_process_done") == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    public void doBeforeServeResource(HstRequest request, HstResponse response) throws HstComponentException {
        super.doBeforeServeResource(request, response);

        Boolean scriptServiceApi = false;

        JSONObject json = (JSONObject) request.getAttribute("json");
        if (json.isEmpty()) {
            try {
                FormBean formBean = this.getFormBean(request);
                scriptServiceApi = Boolean.parseBoolean(JcrUtils.getStringProperty(formBean.getNode(), "eforms:govdeliveryScriptService", "false"));
            } catch (RepositoryException e) {
                LOGGER.error(e.getMessage(), e);
            }

            if (scriptServiceApi) {
                handleScriptServiceRequest(request, response, json);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void addConfiguredBehaviors(HstRequest request) {
        super.addConfiguredBehaviors(request);
        addBehavior(new MailFormDataBehavior());
        addBehavior(new AfterProcessBehavior());
        addBehavior(new ReCaptchaValidationPlugin());
        addBehavior(new SubscriptionBehavior());
    }

    private void handleScriptServiceRequest(HstRequest request, HstResponse response, JSONObject json) {
        ReCaptchaValidationPlugin captchaValidator = new ReCaptchaValidationPlugin();
        final String clientReCaptchaResponseString = request.getParameter("gRecaptchaResponse");
        Resource gRecaptchaResponse = captchaValidator.validateReCaptcha(clientReCaptchaResponseString);
        Boolean success = (boolean) gRecaptchaResponse.getValue("success");
        if (!success) {
            LOGGER.error("Recaptcha validation failed");

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        ComponentManager componentManager = HstServices.getComponentManager();
        String govDeliveryKey = componentManager.getContainerConfiguration().getString("govdelivery.api.key");
        String govDeliveryUrl = componentManager.getContainerConfiguration().getString("govdelivery.api.base.url");
        String scriptServiceUrl = componentManager.getContainerConfiguration().getString("govdelivery.api.scriptservice.url");

        json.put("apiKey", govDeliveryKey);
        json.put("apiUrl", govDeliveryUrl.concat(scriptServiceUrl));
        request.setAttribute("jsonString", json.toString());
    }
}
