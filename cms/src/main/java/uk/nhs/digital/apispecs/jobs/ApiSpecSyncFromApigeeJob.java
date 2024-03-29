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

        log.debug("API Specifications sync from Apigee: start.");

        Session session = null;

        try {
            final String apigeeAllSpecUrl = JobUtils.requireParameter(APIGEE_ALL_SPEC_URL, System.getProperty(APIGEE_ALL_SPEC_URL));
            final String apigeeSingleSpecUrl = JobUtils.requireParameter(APIGEE_SINGLE_SPEC_URL, System.getProperty(APIGEE_SINGLE_SPEC_URL));

            final ResourceServiceBroker resourceServiceBroker = JobUtils.resourceServiceBroker();

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
                    new ApiSpecificationImportImportMetadataJcrRepository(session)
                );

            apiSpecificationPublicationService.syncEligibleSpecifications();

            log.debug("API Specifications sync from Apigee: done.");

        } catch (final Exception ex) {
            log.error("Failed to sync specifications from Apigee.", ex);
        } finally {
            Optional.ofNullable(session).ifPresent(Session::logout);
        }
    }
}
