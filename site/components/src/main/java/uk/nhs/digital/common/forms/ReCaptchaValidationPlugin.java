package uk.nhs.digital.common.forms;

import static org.hippoecm.hst.site.HstServices.getComponentManager;

import com.onehippo.cms7.eforms.hst.api.ValidationBehavior;
import com.onehippo.cms7.eforms.hst.beans.FormBean;
import com.onehippo.cms7.eforms.hst.model.ErrorMessage;
import com.onehippo.cms7.eforms.hst.model.Form;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Objects;

public class ReCaptchaValidationPlugin implements ValidationBehavior {

    private static Logger log = LoggerFactory.getLogger(ReCaptchaValidationPlugin.class);

    @Override
    public Map<String, ErrorMessage> validate(HstRequest request, HstResponse response, ComponentConfiguration config, FormBean bean, Form form, FormMap map) {

        boolean success;
        final Map<String, ErrorMessage> errors = new HashMap<>();
        final String clientReCaptchaResponseString = request.getParameter("gRecaptchaResponse");

        if (map.getFormMap().size() == 0
            || clientReCaptchaResponseString == null
            && "RENDER_PHASE".equals(request.getLifecyclePhase())
            && map.getFormMap().get("eforms_process_done") != null) {
            return errors;
        }

        // Validate response with Google ReCaptcha API
        Resource gRecaptchaResponse = validateReCaptcha(clientReCaptchaResponseString);

        success = (boolean) gRecaptchaResponse.getValue("success");
        if (!success) {
            String errorList = getReCaptchaErrors(gRecaptchaResponse);

            log.debug("Google ReCaptcha failed:" + errorList);
            errors.put("ReCaptcha Validation", new ErrorMessage("ReCaptcha validation failed", errorList));
        } else {
            log.debug("Google ReCaptcha succeeded");
            log.debug("Challenge TTL " + gRecaptchaResponse.getValue("challenge_ts"));
            log.debug("Domain " + gRecaptchaResponse.getValue("hostname"));
        }

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

    private Resource validateReCaptcha(String gReCaptchaResponseCode) {
        return validateReCaptcha(
            gReCaptchaResponseCode,
            ((ApplicationSecrets) getComponentManager().getComponent("applicationSecrets")).getValue("GOOGLE_CAPTCHA_SECRET")
        );
    }

    private Resource validateReCaptcha(String gReCaptchaResponseCode, String recaptchaSecret) {
        Resource resource = null;

        final Map<String, Object> pathVars = new HashMap<>();
        pathVars.put("secret", recaptchaSecret);
        pathVars.put("response", gReCaptchaResponseCode);

        try {
            final ResourceServiceBroker resourceServiceBroker = CrispHstServices.getDefaultResourceServiceBroker(getComponentManager());

            if (Objects.isNull(resourceServiceBroker)) {
                log.warn("The The Resource Service Broker Service is null!");
            }

            // Note: request submitted via CRISP default of GET, since POST is not working as described
            // in the documentation: https://www.onehippo.org/library/concepts/crisp-api/getting-started.html
            resource = resourceServiceBroker.resolve("googleReCaptchaResourceResolver",
                "/recaptcha/api/siteverify?secret={secret}&response={response}",
                pathVars);

            //  resource = resourceServiceBroker.resolve("googleReCaptchaResourceResolver",
            //      "/recaptcha/api/siteverify",
            //      ExchangeHintBuilder.create()
            //          .methodName("POST")
            //          .requestBody("{\"secret\":\"[ADD_SECRET]\",\"response\":\"" + gReCaptchaResponseCode + "\"}")
            //          .build());

        } catch (Exception e) {
            log.debug(e.getMessage());
            log.debug("recaptchaSecret is set: " + StringUtils.isNotBlank(recaptchaSecret));
            log.debug("pathVars is" + pathVars.toString());
            log.warn("Failed to find resources from '{}' resource space for ReCaptcha validation, '{}'.",
                "googleReCaptchaResourceResolver", "/recaptcha/api/siteverify/", e);
        }

        return resource;
    }

}
