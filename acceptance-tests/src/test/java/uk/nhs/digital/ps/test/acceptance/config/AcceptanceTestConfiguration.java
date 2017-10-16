package uk.nhs.digital.ps.test.acceptance.config;

import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import uk.nhs.digital.ps.test.acceptance.data.TestDataLoader;
import uk.nhs.digital.ps.test.acceptance.data.TestDataRepo;
import uk.nhs.digital.ps.test.acceptance.pages.*;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverServiceProvider;

import java.nio.file.Paths;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Central configuration class, enabling acceptance tests to benefit from Spring-based dependency injection.
 */
@Configuration
public class AcceptanceTestConfiguration {

    private final static Logger log = getLogger(AcceptanceTestConfiguration.class);

    @Bean
    public LoginPage loginPage(final WebDriverProvider webDriverProvider) {
        return new LoginPage(webDriverProvider);
    }

    @Bean
    public DashboardPage dashboardPage(final WebDriverProvider webDriverProvider, final PageHelper pageHelper) {
        return new DashboardPage(webDriverProvider, pageHelper);
    }

    @Bean
    public ContentPage contentPage(final WebDriverProvider webDriverProvider, final PageHelper pageHelper) {
        return new ContentPage(webDriverProvider, pageHelper);
    }

    @Bean
    public ConsumablePublicationPage consumablePublicationPage(final WebDriverProvider webDriverProvider,
                                                               final PageHelper pageHelper) {
        return new ConsumablePublicationPage(webDriverProvider, pageHelper);
    }

    @Bean
    public ConsumablePublicationSeriesPage consumablePublicationSeriesPage(final WebDriverProvider webDriverProvider,
                                                                           final PageHelper pageHelper) {
        return new ConsumablePublicationSeriesPage(webDriverProvider, pageHelper);
    }

    @Bean
    public PageHelper pageHelper(final WebDriverProvider webDriverProvider) {
        return new PageHelper(webDriverProvider);
    }

    @Bean
    public TestDataRepo testDataRepo() {
        return new TestDataRepo();
    }

    @Bean
    public WebDriverProvider webDriverProvider(final WebDriverServiceProvider webDriverServiceProvider,
                                               final AcceptanceTestProperties acceptanceTestProperties) {

        return new WebDriverProvider(webDriverServiceProvider,
            acceptanceTestProperties.isHeadlessMode(),
            acceptanceTestProperties.getDownloadDir()
        );
    }

    @Bean(initMethod = "initialise", destroyMethod = "dispose")
    public WebDriverServiceProvider webDriverServiceProvider() {
        return new WebDriverServiceProvider();
    }

    @Bean
    public AcceptanceTestProperties acceptanceTestProperties(final Environment environment) {

        final String buildDirectory = environment.getRequiredProperty("buildDirectory");

        // Supported custom JVM system properties:
        //
        // headless       - values 'true' or 'false'; optional. Determines whether the tests will be run in a headless
        //                  mode.
        // buildDirectory - full path to a directory that the build process generates artefacts into, one that the
        //                  tests can safely write temporary content to and expected that it'll be gone on clean build.
        //                  In Maven this would typically be 'target' directory of the current module.

        final AcceptanceTestProperties acceptanceTestProperties = new AcceptanceTestProperties(
            Boolean.parseBoolean(environment.getProperty("headless", "true")),
            Paths.get(buildDirectory, "download"),
            Paths.get(buildDirectory));

        log.info("Applying test properties: {}", acceptanceTestProperties);

        return acceptanceTestProperties;
    }

}
