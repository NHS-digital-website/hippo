package uk.nhs.digital.ps.test.acceptance.config;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import uk.nhs.digital.ps.test.acceptance.data.TestDataRepo;
import uk.nhs.digital.ps.test.acceptance.data.WebDriversRepo;
import uk.nhs.digital.ps.test.acceptance.pages.ContentPage;
import uk.nhs.digital.ps.test.acceptance.pages.DashboardPage;
import uk.nhs.digital.ps.test.acceptance.pages.LoginPage;
import uk.nhs.digital.ps.test.acceptance.pages.PageHelper;
import uk.nhs.digital.ps.test.acceptance.pages.site.HomePage;
import uk.nhs.digital.ps.test.acceptance.pages.site.ServicePage;
import uk.nhs.digital.ps.test.acceptance.pages.site.SitePage;
import uk.nhs.digital.ps.test.acceptance.pages.site.common.SearchPage;
import uk.nhs.digital.ps.test.acceptance.pages.site.ps.PublicationPage;
import uk.nhs.digital.ps.test.acceptance.util.TestContentUrls;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverServiceProvider;

import java.nio.file.Paths;

/**
 * Central configuration class, enabling acceptance tests to benefit from Spring-based dependency injection.
 */
@Configuration
public class AcceptanceTestConfiguration {

    private static final Logger log = getLogger(AcceptanceTestConfiguration.class);

    @Bean
    public LoginPage loginPage(final WebDriverProvider webDriverProvider,
                               final PageHelper pageHelper,
                               final AcceptanceTestProperties acceptanceTestProperties) {
        return new LoginPage(webDriverProvider, pageHelper, acceptanceTestProperties.getCmsUrl());
    }

    @Bean
    public DashboardPage dashboardPage(final WebDriverProvider webDriverProvider,
                                       final PageHelper pageHelper,
                                       final AcceptanceTestProperties acceptanceTestProperties) {
        return new DashboardPage(webDriverProvider, pageHelper, acceptanceTestProperties.getCmsUrl());
    }

    @Bean
    public ContentPage contentPage(final WebDriverProvider webDriverProvider,
                                   final PageHelper pageHelper,
                                   final AcceptanceTestProperties acceptanceTestProperties) {
        return new ContentPage(webDriverProvider, pageHelper, acceptanceTestProperties.getCmsUrl());
    }

    @Bean
    public PublicationPage publicationPage(final WebDriverProvider webDriverProvider,
                                           final PageHelper pageHelper,
                                           final AcceptanceTestProperties acceptanceTestProperties) {
        return new PublicationPage(webDriverProvider, pageHelper, acceptanceTestProperties.getSiteUrl());
    }

    @Bean
    public SearchPage searchResultsPage(final WebDriverProvider webDriverProvider,
                                        final PageHelper pageHelper,
                                        final AcceptanceTestProperties acceptanceTestProperties) {
        return new SearchPage(webDriverProvider, pageHelper, acceptanceTestProperties.getCmsUrl());
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
    public WebDriversRepo testUserRepo() {
        return new WebDriversRepo();
    }

    @Bean
    public TestContentUrls testContentUrls(final AcceptanceTestProperties acceptanceTestProperties) {
        return new TestContentUrls(
            acceptanceTestProperties.getCmsUrl(),
            acceptanceTestProperties.getSiteUrl()
        );
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
        // cmsUrl         - URL of the CMS application. Optional, if missing, defaults to http://localhost:8080/cms
        // siteUrl        - URL of the Site application. Optional, if missing, defaults to http://localhost:8080

        final AcceptanceTestProperties acceptanceTestProperties = new AcceptanceTestProperties(
            Boolean.parseBoolean(environment.getProperty("headless", "true")),
            Paths.get(buildDirectory, "download"),
            Paths.get(buildDirectory),
            getProperty(environment, "cmsUrl", "http://localhost:8080/cms"),
            getProperty(environment, "siteUrl", "http://localhost:8080")
        );

        log.info("Applying test properties: {}", acceptanceTestProperties);

        return acceptanceTestProperties;
    }

    @Bean
    public SitePage sitePage(final WebDriverProvider webDriverProvider,
                             final PageHelper pageHelper,
                             final AcceptanceTestProperties acceptanceTestProperties,
                             final TestContentUrls testContentUrls) {
        return new SitePage(webDriverProvider, pageHelper, acceptanceTestProperties.getCmsUrl(), testContentUrls);
    }

    @Bean
    public ServicePage servicePage(final WebDriverProvider webDriverProvider,
                                   final AcceptanceTestProperties acceptanceTestProperties) {
        return new ServicePage(webDriverProvider, acceptanceTestProperties.getCmsUrl());
    }

    @Bean
    public HomePage homePage(final WebDriverProvider webDriverProvider,
                             final AcceptanceTestProperties acceptanceTestProperties) {
        return new HomePage(webDriverProvider, acceptanceTestProperties.getCmsUrl());
    }

    private String getProperty(Environment environment, String propertyKey, String defaultPropertyValue) {
        final String resolvedPropertyValue = environment.getProperty(propertyKey);

        return isNotBlank(resolvedPropertyValue) ? resolvedPropertyValue : defaultPropertyValue;
    }
}
