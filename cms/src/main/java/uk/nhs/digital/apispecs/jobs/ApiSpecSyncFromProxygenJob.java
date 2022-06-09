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
import uk.nhs.digital.apispecs.jcr.ApiSpecificationImportImportMetadataJcrRepository;
import uk.nhs.digital.apispecs.services.ProxygenService;

import java.util.Optional;
import javax.jcr.Session;


public class ApiSpecSyncFromProxygenJob implements RepositoryJob {

    private static final Logger log = LoggerFactory.getLogger(ApiSpecSyncFromProxygenJob.class);

    // @formatter:off
    private static final String PROXYGEN_ALL_SPEC_URL        = "devzone.proxygen.resources.specs.all.url";
    private static final String PROXYGEN_SINGLE_SPEC_URL     = "devzone.proxygen.resources.specs.individual.url";
    // @formatter:on

    @Override
    public void execute(final RepositoryJobExecutionContext context) {

        log.debug("API Specifications sync from Proxygen: start.");

        Session session = null;

        try {
            final String proxygenAllSpecUrl = requireParameter(PROXYGEN_ALL_SPEC_URL, System.getProperty(PROXYGEN_ALL_SPEC_URL));
            final String proxygenSingleSpecUrl = requireParameter(PROXYGEN_SINGLE_SPEC_URL, System.getProperty(PROXYGEN_SINGLE_SPEC_URL));

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
                    new ApiSpecificationImportImportMetadataJcrRepository(session)
                );

            apiSpecificationPublicationService.syncEligibleSpecifications();

            log.debug("API Specifications sync from proxygen: done.");

        } catch (final Exception ex) {
            log.error("Failed to sync specifications from proxygen.", ex);
        } finally {
            Optional.ofNullable(session).ifPresent(Session::logout);
        }
    }

    private String requireParameter(final String argName, final String argValue) {
        return Validate.notBlank(argValue, "Required configuration argument is missing: %s", argName);
    }

    private ResourceServiceBroker resourceServiceBroker() {
        return Optional.ofNullable(
            CrispHstServices.getDefaultResourceServiceBroker(HstServices.getComponentManager())
        ).orElseThrow(() -> new RuntimeException(
            "ResourceServiceBroker not available. Ignore if this happens only once on app start as, in such a case it's justified as the env. is not fully initialised, yet."
        ));
    }
}
