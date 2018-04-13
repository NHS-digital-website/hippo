package uk.nhs.digital.ps.test.acceptance.pages;

import org.openqa.selenium.WebDriver;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

/**
 * Base class for classes implementing 'Page Object' pattern; provides common properties (such as base URL)
 * and dependencies (such as web driver) to extending classes.
 */
public abstract class AbstractPage {

    private final WebDriverProvider webDriverProvider;
    private final String applicationUrl;

    public AbstractPage(final WebDriverProvider webDriverProvider, final String applicationUrl) {
        this.webDriverProvider = webDriverProvider;
        this.applicationUrl = applicationUrl;
    }

    protected WebDriver getWebDriver() {
        return webDriverProvider.getWebDriver();
    }

    /**
     * See {@linkplain WebDriverProvider#newWebDriver(String)} for details.
     */
    protected WebDriver getNewWebDriver(String userName) {
        return webDriverProvider.newWebDriver(userName);
    }

    protected String getUrl() {
        return applicationUrl;
    }
}
