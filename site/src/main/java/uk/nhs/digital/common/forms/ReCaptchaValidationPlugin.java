package uk.nhs.digital.common.forms;

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

import java.util.HashMap;
import java.util.Map;

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

            log.warn("Google ReCaptcha failed:" + errorList);
            errors.put("ReCaptcha Validation", new ErrorMessage("ReCaptcha validation failed", errorList));
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
        Resource resource = null;

        try {
            final ResourceServiceBroker resourceServiceBroker = CrispHstServices.getDefaultResourceServiceBroker();

            final Map<String, Object> pathVars = new HashMap<>();
            pathVars.put("secret", System.getenv("GOOGLE_CAPTCHA_SECRET"));
            pathVars.put("response", gReCaptchaResponseCode);

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
            log.warn("Failed to find resources from '{}' resource space for ReCaptcha validation, '{}'.",
                "googleReCaptchaResourceResolver", "/recaptcha/api/siteverify/", e);
        }

        return resource;
    }

}
