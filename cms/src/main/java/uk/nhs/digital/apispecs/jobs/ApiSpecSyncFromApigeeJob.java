package uk.nhs.digital.apispecs.jobs;

import org.apache.commons.lang3.Validate;
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

        LOGGER.debug("API Specifications sync from Apigee: start.");

        final Session session = context.createSystemSession();

        // System properties for config - LOGGED ON SYSTEM START
        final String oauthAapigeeAllSpecUrl = System.getProperty(APIGEE_ALL_SPEC_URL);
        final String apigeeSingleSpecUrl = System.getProperty(APIGEE_SINGLE_SPEC_URL);
        final String oauthTokenUrl = System.getProperty(OAUTH_TOKEN_URL);

        // Environment variables FOR SECRETS - not logged on system start
        final String username = System.getenv(USERNAME);
        final String password = System.getenv(PASSWORD);
        final String basicToken = System.getenv(BASIC_TOKEN);
        final String otpKey = System.getenv(OTP_KEY);

        try {
            ensureRequiredArgProvided(APIGEE_ALL_SPEC_URL, oauthAapigeeAllSpecUrl);
            ensureRequiredArgProvided(APIGEE_SINGLE_SPEC_URL, apigeeSingleSpecUrl);
            ensureRequiredArgProvided(OAUTH_TOKEN_URL, oauthTokenUrl);
            ensureRequiredArgProvided(USERNAME, username);
            ensureRequiredArgProvided(PASSWORD, password);
            ensureRequiredArgProvided(BASIC_TOKEN, basicToken);
            ensureRequiredArgProvided(OTP_KEY, otpKey);

            final RestTemplate restTemplate = new RestTemplate();

            final ApigeeClientConfig config = new ApigeeClientConfig(
                oauthAapigeeAllSpecUrl,
                apigeeSingleSpecUrl,
                oauthTokenUrl,
                username,
                password,
                basicToken,
                otpKey
            );

            final Clock clock = Clock.systemDefaultZone();

            final ApigeeService apigeeService = new ApigeeService(restTemplate, config, clock);

            final ApiSpecificationDocumentPublicationService apiSpecificationDocumentPublicationService =
                new ApiSpecificationDocumentPublicationService(
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

    private void ensureRequiredArgProvided(final String argName, final String argValue) {
        Validate.notBlank(argValue, "Required configuration argument is missing: %s", argName);
    }
}
