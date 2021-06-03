package uk.nhs.digital.apispecs.jobs;

import static java.lang.System.getProperty;

import org.apache.commons.lang3.Validate;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.hst.module.CrispHstServices;
import org.onehippo.repository.scheduling.RepositoryJob;
import org.onehippo.repository.scheduling.RepositoryJobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.apispecs.ApiSpecificationPublicationService;
import uk.nhs.digital.apispecs.apigee.ApigeeService;
import uk.nhs.digital.apispecs.jcr.ApiSpecificationDocumentJcrRepository;
import uk.nhs.digital.apispecs.swagger.SwaggerCodeGenOpenApiSpecificationJsonToHtmlConverter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import javax.jcr.Session;

public class ApiSpecSyncFromApigeeJob implements RepositoryJob {

    private static final Logger log = LoggerFactory.getLogger(ApiSpecSyncFromApigeeJob.class);
    private static final Properties properties = new Properties();

    {
        try {
            properties.load(new FileInputStream(getProperty("secure.properties.location") + "/apigee-secrets.properties"));
        } catch (IOException e) {
            log.warn("The 'apigee-secrets.properties' file was not found.");
        }
    }

    // @formatter:off
    private static final String APIGEE_ALL_SPEC_URL    = "devzone.apigee.resources.specs.all.url";
    private static final String APIGEE_SINGLE_SPEC_URL = "devzone.apigee.resources.specs.individual.url";

    private static final String OAUTH_TOKEN_URL        = "devzone.apigee.oauth.token.url";

    private static final String USERNAME               = "DEVZONE_APIGEE_OAUTH_USERNAME";
    private static final String PASSWORD               = "DEVZONE_APIGEE_OAUTH_PASSWORD";
    private static final String BASIC_TOKEN            = "DEVZONE_APIGEE_OAUTH_BASICAUTHTOKEN";
    private static final String OTP_KEY                = "DEVZONE_APIGEE_OAUTH_OTPKEY";
    // @formatter:on

    @Override
    public void execute(final RepositoryJobExecutionContext context) {

        log.debug("API Specifications sync from Apigee: start.");

        // System properties for config - LOGGED ON SYSTEM START
        final String apigeeAllSpecUrl = getProperty(APIGEE_ALL_SPEC_URL);
        final String apigeeSingleSpecUrl = getProperty(APIGEE_SINGLE_SPEC_URL);
        final String oauthTokenUrl = getProperty(OAUTH_TOKEN_URL);

        // Environment variables FOR SECRETS - not logged on system start
        final String username = getSecret(USERNAME);
        final String password = getSecret(PASSWORD);
        final String basicToken = getSecret(BASIC_TOKEN);
        final String otpKey = getSecret(OTP_KEY);

        Session session = null;

        try {

            ensureRequiredArgProvided(APIGEE_ALL_SPEC_URL, apigeeAllSpecUrl);
            ensureRequiredArgProvided(APIGEE_SINGLE_SPEC_URL, apigeeSingleSpecUrl);
            ensureRequiredArgProvided(OAUTH_TOKEN_URL, oauthTokenUrl);
            ensureRequiredArgProvided(USERNAME, username);
            ensureRequiredArgProvided(PASSWORD, password);
            ensureRequiredArgProvided(BASIC_TOKEN, basicToken);
            ensureRequiredArgProvided(OTP_KEY, otpKey);

            final ResourceServiceBroker resourceServiceBroker = resourceServiceBroker();

            final ApigeeService apigeeService = new ApigeeService(
                resourceServiceBroker,
                apigeeAllSpecUrl,
                apigeeSingleSpecUrl
            );

            session = context.createSystemSession();

            final ApiSpecificationPublicationService apiSpecificationPublicationService =
                new ApiSpecificationPublicationService(
                    apigeeService,
                    new ApiSpecificationDocumentJcrRepository(session),
                    new SwaggerCodeGenOpenApiSpecificationJsonToHtmlConverter()
                );

            apiSpecificationPublicationService.syncEligibleSpecifications();

            log.debug("API Specifications sync from Apigee: done.");

        } catch (final Exception ex) {
            log.error("Failed to sync specifications from Apigee.", ex);
        } finally {
            Optional.ofNullable(session).ifPresent(Session::logout);
        }
    }

    private void ensureRequiredArgProvided(final String argName, final String argValue) {
        Validate.notBlank(argValue, "Required configuration argument is missing: %s", argName);
    }

    private ResourceServiceBroker resourceServiceBroker() {
        return Optional.ofNullable(
            CrispHstServices.getDefaultResourceServiceBroker(HstServices.getComponentManager())
        ).orElseThrow(() -> new RuntimeException(
            "ResourceServiceBroker not available. Ignore if this happens only once on app start as, in such a case it's justified as the env. is not fully initialised, yet."
        ));
    }

    private String getSecret(final String key) {
        String value = properties.getProperty(key, System.getenv(key));
        if (value == null) {
            log.warn("The key/value for '" + key + "' should be set as a Java Property or an Environment variable.");
        }
        return value;
    }

}
