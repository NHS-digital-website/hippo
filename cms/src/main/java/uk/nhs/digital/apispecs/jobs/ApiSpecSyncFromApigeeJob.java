package uk.nhs.digital.apispecs.jobs;

import org.apache.commons.lang3.Validate;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.hst.module.CrispHstServices;
import org.onehippo.repository.scheduling.RepositoryJob;
import org.onehippo.repository.scheduling.RepositoryJobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.apispecs.ApiSpecificationDocumentPublicationService;
import uk.nhs.digital.apispecs.apigee.ApigeeClientConfig;
import uk.nhs.digital.apispecs.apigee.ApigeeService;
import uk.nhs.digital.apispecs.jcr.ApiSpecificationDocumentJcrRepository;
import uk.nhs.digital.apispecs.swagger.SwaggerCodeGenApiSpecificationHtmlProvider;

import java.util.Optional;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class ApiSpecSyncFromApigeeJob implements RepositoryJob {

    private static final Logger log = LoggerFactory.getLogger(ApiSpecSyncFromApigeeJob.class);

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
    public void execute(RepositoryJobExecutionContext context) throws RepositoryException {

        log.debug("API Specifications sync from Apigee: start.");

        // System properties for config - LOGGED ON SYSTEM START
        final String oauthAapigeeAllSpecUrl = System.getProperty(APIGEE_ALL_SPEC_URL);
        final String apigeeSingleSpecUrl = System.getProperty(APIGEE_SINGLE_SPEC_URL);
        final String oauthTokenUrl = System.getProperty(OAUTH_TOKEN_URL);

        // Environment variables FOR SECRETS - not logged on system start
        final String username = System.getenv(USERNAME);
        final String password = System.getenv(PASSWORD);
        final String basicToken = System.getenv(BASIC_TOKEN);
        final String otpKey = System.getenv(OTP_KEY);

        Session session = null;

        try {

            ensureRequiredArgProvided(APIGEE_ALL_SPEC_URL, oauthAapigeeAllSpecUrl);
            ensureRequiredArgProvided(APIGEE_SINGLE_SPEC_URL, apigeeSingleSpecUrl);
            ensureRequiredArgProvided(OAUTH_TOKEN_URL, oauthTokenUrl);
            ensureRequiredArgProvided(USERNAME, username);
            ensureRequiredArgProvided(PASSWORD, password);
            ensureRequiredArgProvided(BASIC_TOKEN, basicToken);
            ensureRequiredArgProvided(OTP_KEY, otpKey);

            final ResourceServiceBroker resourceServiceBroker = resourceServiceBroker();

            final ApigeeClientConfig config = new ApigeeClientConfig(
                oauthAapigeeAllSpecUrl,
                apigeeSingleSpecUrl,
                oauthTokenUrl,
                username,
                password,
                basicToken,
                otpKey
            );

            final ApigeeService apigeeService = new ApigeeService(
                config,
                resourceServiceBroker
            );

            session = context.createSystemSession();

            final ApiSpecificationDocumentPublicationService apiSpecificationDocumentPublicationService =
                new ApiSpecificationDocumentPublicationService(
                    apigeeService,
                    new ApiSpecificationDocumentJcrRepository(session),
                    new SwaggerCodeGenApiSpecificationHtmlProvider(apigeeService)
                );

            apiSpecificationDocumentPublicationService.updateAndPublishEligibleSpecifications();

            log.debug("API Specifications sync from Apigee: done.");

        } catch (final Exception ex) {
            log.error("Failed to publish specifications.", ex);
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
        ).orElseThrow(() -> new RuntimeException("ResourceServiceBroker not available."));
    }
}
