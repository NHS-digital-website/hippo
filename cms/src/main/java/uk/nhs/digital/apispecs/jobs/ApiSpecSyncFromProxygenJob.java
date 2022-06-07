package uk.nhs.digital.apispecs.jobs;

import org.apache.commons.lang3.Validate;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.hst.module.CrispHstServices;
import org.onehippo.repository.scheduling.RepositoryJob;
import org.onehippo.repository.scheduling.RepositoryJobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.apispecs.ApiSpecificationPublicationService;
import uk.nhs.digital.apispecs.jcr.ApiSpecificationDocumentJcrRepository;
import uk.nhs.digital.apispecs.services.ProxygenService;
import uk.nhs.digital.apispecs.swagger.SwaggerCodeGenOpenApiSpecificationJsonToHtmlConverter;

import java.util.Optional;
import javax.jcr.Session;


public class ApiSpecSyncFromProxygenJob implements RepositoryJob {

    private static final Logger log = LoggerFactory.getLogger(ApiSpecSyncFromProxygenJob.class);

    // @formatter:off
    private static final String PROXYGEN_ALL_SPEC_URL        = "devzone.proxygen.resources.specs.all.url";
    private static final String PROXYGEN_SINGLE_SPEC_URL     = "devzone.proxygen.resources.specs.individual.url";

    private static final String OAUTH_TOKEN_URL              = "devzone.proxygen.oauth.token.url";

    private static final String USERNAME                     = "DEVZONE_PROXYGEN_OAUTH_USERNAME";
    private static final String PASSWORD                     = "DEVZONE_PROXYGEN_OAUTH_PASSWORD";
    private static final String CLIENT_ID                    = "DEVZONE_PROXYGEN_OAUTH_CLIENT_ID";
    private static final String CLIENT_SECRET                = "DEVZONE_PROXYGEN_OAUTH_CLIENT_SECRET";


    // @formatter:on

    @Override
    public void execute(final RepositoryJobExecutionContext context) {

        log.debug("API Specifications sync from proxygen: start.");

        // System properties for config - LOGGED ON SYSTEM START
        final String proxygenAllSpecUrl = System.getProperty(PROXYGEN_ALL_SPEC_URL);
        final String proxygenSingleSpecUrl = System.getProperty(PROXYGEN_SINGLE_SPEC_URL);
        final String oauthTokenUrl = System.getProperty(OAUTH_TOKEN_URL);

        // Environment variables FOR SECRETS - not logged on system start
        final String username = System.getenv(USERNAME);
        final String password = System.getenv(PASSWORD);
        final String clientId = System.getenv(CLIENT_ID);
        final String clientSecret = System.getenv(CLIENT_SECRET);

        Session session = null;

        try {
            ensureRequiredArgProvided(PROXYGEN_ALL_SPEC_URL, proxygenAllSpecUrl);
            ensureRequiredArgProvided(PROXYGEN_SINGLE_SPEC_URL, proxygenSingleSpecUrl);
            ensureRequiredArgProvided(OAUTH_TOKEN_URL, oauthTokenUrl);
            ensureRequiredArgProvided(USERNAME, username);
            ensureRequiredArgProvided(PASSWORD, password);
            ensureRequiredArgProvided(CLIENT_ID, clientId);
            ensureRequiredArgProvided(CLIENT_SECRET, clientSecret);

            final ResourceServiceBroker resourceServiceBroker = resourceServiceBroker();

            final ProxygenService proxygenService = new ProxygenService(
                resourceServiceBroker,
                proxygenAllSpecUrl,
                proxygenSingleSpecUrl
            );

            session = context.createSystemSession();

            final ApiSpecificationPublicationService apiSpecificationPublicationService =
                new ApiSpecificationPublicationService(
                    proxygenService,
                    new ApiSpecificationDocumentJcrRepository(session),
                    new SwaggerCodeGenOpenApiSpecificationJsonToHtmlConverter()
                );

            apiSpecificationPublicationService.syncEligibleSpecifications();

            log.debug("API Specifications sync from proxygen: done.");

        } catch (final Exception ex) {
            log.error("Failed to sync specifications from proxygen.", ex);
        } finally {
            Optional.ofNullable(session).ifPresent(Session::logout);
        }
    }

    // TODO duplicated methods
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
}
