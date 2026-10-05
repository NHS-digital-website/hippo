package uk.nhs.digital.apispecs.jobs;

import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.repository.scheduling.RepositoryJob;
import org.onehippo.repository.scheduling.RepositoryJobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.apispecs.ApiSpecificationPublicationService;
import uk.nhs.digital.apispecs.jcr.ApiSpecificationDocumentJcrRepository;
import uk.nhs.digital.apispecs.jcr.ApiSpecificationImportImportMetadataJcrRepository;
import uk.nhs.digital.apispecs.services.ApigeeService;

import java.net.URI;
import java.util.Optional;
import javax.jcr.Session;

public class ApiSpecSyncFromApigeeJob implements RepositoryJob {

    private static final Logger log = LoggerFactory.getLogger(ApiSpecSyncFromApigeeJob.class);

    // @formatter:off
    private static final String APIGEE_ALL_SPEC_URL    = "devzone.apigee.resources.specs.all.url";
    private static final String APIGEE_SINGLE_SPEC_URL = "devzone.apigee.resources.specs.individual.url";
    // @formatter:on

    @Override
    public void execute(final RepositoryJobExecutionContext context) {

        log.info("API Specifications sync from Apigee: job entered.");

        Session session = null;

        try {
            log.debug("API Specifications sync from Apigee: reading required configuration.");
            final String apigeeAllSpecUrl = JobUtils.requireParameter(APIGEE_ALL_SPEC_URL, System.getProperty(APIGEE_ALL_SPEC_URL));
            final String apigeeSingleSpecUrl = JobUtils.requireParameter(APIGEE_SINGLE_SPEC_URL, System.getProperty(APIGEE_SINGLE_SPEC_URL));
            log.info(
                "API Specifications sync from Apigee: configuration present; all specs endpoint: {}, single spec endpoint template: {}.",
                safeUri(apigeeAllSpecUrl),
                safeUri(apigeeSingleSpecUrl)
            );

            log.debug("API Specifications sync from Apigee: retrieving CRISP ResourceServiceBroker.");
            final ResourceServiceBroker resourceServiceBroker = JobUtils.resourceServiceBroker();
            log.debug("API Specifications sync from Apigee: CRISP ResourceServiceBroker retrieved: {}.", resourceServiceBroker.getClass().getName());

            final ApigeeService apigeeService = new ApigeeService(
                resourceServiceBroker,
                apigeeAllSpecUrl,
                apigeeSingleSpecUrl
            );

            log.debug("API Specifications sync from Apigee: creating system session.");
            session = context.createSystemSession();
            log.debug("API Specifications sync from Apigee: system session created.");

            final ApiSpecificationPublicationService apiSpecificationPublicationService =
                new ApiSpecificationPublicationService(
                    apigeeService,
                    new ApiSpecificationDocumentJcrRepository(session),
                    new ApiSpecificationImportImportMetadataJcrRepository(session)
                );

            log.debug("API Specifications sync from Apigee: invoking publication service.");
            apiSpecificationPublicationService.syncEligibleSpecifications();

            log.info("API Specifications sync from Apigee: job completed.");

        } catch (final Exception ex) {
            log.error("Failed to sync specifications from Apigee.", ex);
        } finally {
            Optional.ofNullable(session).ifPresent(openSession -> {
                log.debug("API Specifications sync from Apigee: logging out system session.");
                openSession.logout();
            });
        }
    }

    private String safeUri(final String url) {
        try {
            final URI uri = URI.create(url);
            return uri.getScheme() + "://" + uri.getHost() + safePort(uri) + uri.getPath();
        } catch (final Exception e) {
            return "<invalid-url>";
        }
    }

    private String safePort(final URI uri) {
        return uri.getPort() == -1 ? "" : ":" + uri.getPort();
    }
}
