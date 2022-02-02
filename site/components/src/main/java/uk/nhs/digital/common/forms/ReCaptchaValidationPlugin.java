package uk.nhs.digital.common.forms;

import static org.hippoecm.hst.site.HstServices.getComponentManager;

import com.onehippo.cms7.eforms.hst.api.ValidationBehavior;
import com.onehippo.cms7.eforms.hst.beans.FormBean;
import com.onehippo.cms7.eforms.hst.model.ErrorMessage;
import com.onehippo.cms7.eforms.hst.model.Form;
import org.hippoecm.hst.component.support.forms.FormMap;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.ComponentConfiguration;
import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.api.resource.Resource;
import org.onehippo.cms7.crisp.hst.module.CrispHstServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.nhs.digital.toolbox.secrets.ApplicationSecrets;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Objects;

public class ReCaptchaValidationPlugin implements ValidationBehavior {

    private static Logger log = LoggerFactory.getLogger(ReCaptchaValidationPlugin.class);

    @Override
    public Map<String, ErrorMessage> validate(HstRequest request, HstResponse response, ComponentConfiguration config, FormBean bean, Form form, FormMap map) {

        final String reCaptchaSiteKey = ((ApplicationSecrets) getComponentManager().getComponent("applicationSecrets")).getValue("GOOGLE_CAPTCHA_SITE_KEY");
        final String reCaptchaSecretKey = ((ApplicationSecrets) getComponentManager().getComponent("applicationSecrets")).getValue("GOOGLE_CAPTCHA_SECRET");
        final Map<String, ErrorMessage> errors = new HashMap<>();
        final String clientReCaptchaResponseString = request.getParameter("gRecaptchaResponse");

        if (map.getFormMap().size() == 0
            || clientReCaptchaResponseString == null
            && "RENDER_PHASE".equals(request.getLifecyclePhase())
            && map.getFormMap().get("eforms_process_done") != null) {
            return errors;
        }

        try { // to validate response with Google ReCaptcha API

            log.debug("***************************** Validate ReCaptcha *****************************");
            log.debug("Recaptcha Site Key: " + reCaptchaSiteKey);
            log.debug("Recaptcha Secret Key: " + ApplicationSecrets.mask(reCaptchaSecretKey));
            log.debug("Recaptcha Response: " + clientReCaptchaResponseString);

            Resource gRecaptchaResponse = validateReCaptcha(clientReCaptchaResponseString, reCaptchaSecretKey);

            if (gRecaptchaResponse != null) {
                if ((boolean) gRecaptchaResponse.getValue("success")) {
                    log.debug("ReCaptcha succeeded!");
                    log.debug("ReCaptcha Challenge TTL: " + gRecaptchaResponse.getValue("challenge_ts"));
                    log.debug("ReCaptcha Hostname: " + gRecaptchaResponse.getValue("hostname"));
                } else {
                    String errorList = getReCaptchaErrors(gRecaptchaResponse);
                    log.debug("ReCaptcha Failed:" + errorList);
                    errors.put("ReCaptcha Validation", new ErrorMessage("ReCaptcha validation failed", errorList));
                }
            }
        } catch (MissingResourceException e) {
            log.warn(e.getMessage(), e.getClassName(), e.getKey(), e);
        }

        log.debug("ReCaptcha Error count is: " + errors.size());
        log.debug("**************************** End Validate ReCaptcha ****************************");

        return errors;
    }

    private String getReCaptchaErrors(Resource responseResource) {

        final StringBuilder builder = new StringBuilder();

        if (responseResource != null && responseResource.isAnyChildContained()) {
            ((Resource) responseResource.getValueMap()
                .get("error-codes"))
                .getChildren()
                .forEach((val) ->
                    builder
                        .append(val.getDefaultValue())
                        .append(", ")
                );
        }
        return builder.toString();
    }

    private Resource validateReCaptcha(String gReCaptchaResponseCode, String recaptchaSecret) throws MissingResourceException {

        Resource resource = null;

        final Map<String, Object> pathVars = new HashMap<>();
        pathVars.put("secret", recaptchaSecret);
        pathVars.put("response", gReCaptchaResponseCode);

        final ResourceServiceBroker resourceServiceBroker = CrispHstServices.getDefaultResourceServiceBroker(getComponentManager());

        if (Objects.isNull(resourceServiceBroker)) {
            throw new MissingResourceException("The CRISP Resource Service Broker is null!", "ResourceServiceBroker", "GoogleReCaptchaResourceResolver");
        } else {
            try {
                resource = resourceServiceBroker.resolve("googleReCaptchaResourceResolver",
                    "/recaptcha/api/siteverify?secret={secret}&response={response}",
                    pathVars
                );

            } catch (Exception e) {
                log.warn("Failed to find resources from '{}' resource space for ReCaptcha validation, '{}'. The message '{}'",
                    "googleReCaptchaResourceResolver", "/recaptcha/api/siteverify/", e.getMessage(), e);
            }
        }

        return resource;
    }



}
