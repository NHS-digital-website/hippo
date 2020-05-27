package uk.nhs.digital.apispecs.jobs;

import org.onehippo.repository.scheduling.RepositoryJob;
import org.onehippo.repository.scheduling.RepositoryJobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import uk.nhs.digital.apispecs.ApiSpecificationDocumentPublicationService;
import uk.nhs.digital.apispecs.apigee.ApigeeClientConfig;
import uk.nhs.digital.apispecs.apigee.ApigeeService;
import uk.nhs.digital.apispecs.jcr.ApiSpecificationDocumentJcrRepository;
import uk.nhs.digital.apispecs.swagger.SwaggerCodeGenApiSpecificationHtmlProvider;

import java.time.Clock;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class ApiSpecSyncFromApigeeJob implements RepositoryJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiSpecSyncFromApigeeJob.class);

    private static final String APIGEE_ALL_SPEC_URL = "devzone.apigee.resources.specs.all.url";
    private static final String APIGEE_SINGLE_SPEC_URL = "devzone.apigee.resources.specs.individual.url";

    private static final String OAUTH_TOKEN_URL = "devzone.apigee.oauth.token.url";
    private static final String OAUTH_TOKEN_USERNAME = "devzone.apigee.oauth.username";
    private static final String OAUTH_TOKEN_PASSWORD = "devzone.apigee.oauth.password";
    private static final String BASIC_TOKEN = "devzone.apigee.oauth.basicauthtoken";
    private static final String OTP_KEY = "devzone.apigee.oauth.otpkey";

    @Override
    public void execute(RepositoryJobExecutionContext context) throws RepositoryException {

        LOGGER.debug("API Specifications sync from Apigee: start.");

        final Session session = context.createSystemSession();

        final String apigeeAllSpecUrl = System.getProperty(APIGEE_ALL_SPEC_URL);
        final String apigeeSingleSpecUrl = System.getProperty(APIGEE_SINGLE_SPEC_URL);
        final String tokenUrl = System.getProperty(OAUTH_TOKEN_URL);
        final String username = System.getProperty(OAUTH_TOKEN_USERNAME);
        final String password = System.getProperty(OAUTH_TOKEN_PASSWORD);
        final String basicToken = System.getProperty(BASIC_TOKEN);
        final String otpKey = System.getProperty(OTP_KEY);

        try {
            final RestTemplate restTemplate = new RestTemplate();

            final ApigeeClientConfig config = new ApigeeClientConfig(
                apigeeAllSpecUrl,
                apigeeSingleSpecUrl,
                tokenUrl,
                username,
                password,
                basicToken,
                otpKey
            );

            final Clock clock = Clock.systemDefaultZone();

            final ApigeeService apigeeService = new ApigeeService(restTemplate, config, clock);

            final ApiSpecificationDocumentPublicationService apiSpecificationDocumentPublicationService = new ApiSpecificationDocumentPublicationService(
                apigeeService,
                new ApiSpecificationDocumentJcrRepository(session),
                new SwaggerCodeGenApiSpecificationHtmlProvider(apigeeService)
            );

            apiSpecificationDocumentPublicationService.updateAndPublishEligibleSpecifications();

            LOGGER.debug("API Specifications sync from Apigee: done.");

        } catch (final Exception ex) {
            LOGGER.error("Failed to publish specifications.", ex);
        } finally {
            session.logout();
        }
    }
}
