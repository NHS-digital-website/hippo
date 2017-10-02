package uk.nhs.digital.ps.test.acceptance.config;

import org.junit.Before;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.nhs.digital.ps.test.acceptance.models.Publication;
import uk.nhs.digital.ps.test.acceptance.pages.DashboardPage;
import uk.nhs.digital.ps.test.acceptance.pages.ContentPage;
import uk.nhs.digital.ps.test.acceptance.pages.LoginPage;
import uk.nhs.digital.ps.test.acceptance.pages.ConsumablePublicationPage;
import uk.nhs.digital.ps.test.acceptance.pages.PageHelper;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverServiceProvider;

/**
 * Central configuration class, enabling acceptance tests to benefit from Spring-based dependency injection.
 */
@Configuration
public class AcceptanceTestConfiguration {

    @Bean
    public LoginPage loginPage(final WebDriverProvider webDriverProvider) {
        return new LoginPage(webDriverProvider);
    }

    @Bean
    public DashboardPage dashboardPage(final WebDriverProvider webDriverProvider) {
        return new DashboardPage(webDriverProvider);
    }

    @Bean
    public ContentPage contentPage(final WebDriverProvider webDriverProvider, final PageHelper pageHelper) {
        return new ContentPage(webDriverProvider, pageHelper);
    }

    @Bean
    public ConsumablePublicationPage consumablePublicationPage(final WebDriverProvider webDriverProvider, final PageHelper pageHelper) {
        return new ConsumablePublicationPage(webDriverProvider, pageHelper);
    }

    @Bean
    public PageHelper pageHelper(final WebDriverProvider webDriverProvider) {
        return new PageHelper(webDriverProvider);
    }

    @Bean
    public Publication publication() {
        return new Publication();
    }




    /**
     * @param isHeadless Determines whether the web driver will be operating in a headless mode. Default value is
     *                   {@code true}. Headless mode can be disabled by setting system property {@code -Dheadless=false}
     *                   in JVM command line.
     * @return New instance of WebDriver; at the moment of writing it's always {@linkplain ChromeDriver}.
     */
    @Bean
    public WebDriverProvider webDriverProvider(final WebDriverServiceProvider webDriverServiceProvider,
                                               @Value("${headless:true}") final boolean isHeadless) {
        return new WebDriverProvider(webDriverServiceProvider, isHeadless);
    }

    @Bean(initMethod = "initialise", destroyMethod = "dispose")
    public WebDriverServiceProvider webDriverServiceProvider() {
        return new WebDriverServiceProvider();
    }

}
