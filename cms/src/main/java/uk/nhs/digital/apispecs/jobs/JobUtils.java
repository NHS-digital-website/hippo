package uk.nhs.digital.apispecs.jobs;

import org.apache.commons.lang3.Validate;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.hst.module.CrispHstServices;

import java.util.Optional;

public class JobUtils {

    public static String requireParameter(final String argName, final String argValue) {
        return Validate.notBlank(argValue, "Required configuration argument is missing: %s", argName);
    }

    public static ResourceServiceBroker resourceServiceBroker() {
        return Optional.ofNullable(
            CrispHstServices.getDefaultResourceServiceBroker(HstServices.getComponentManager())
        ).orElseThrow(() -> new RuntimeException(
            "ResourceServiceBroker not available. Ignore if this happens only once on app start as, in such a case it's justified as the env. is not fully initialised, yet."
        ));
    }
}
