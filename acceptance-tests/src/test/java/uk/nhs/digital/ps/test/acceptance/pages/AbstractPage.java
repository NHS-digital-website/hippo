package uk.nhs.digital.ps.test.acceptance.pages;

import org.openqa.selenium.WebDriver;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

/**
 * Base class for classes implementing 'Page Object' pattern; provides common properties (such as base URL)
 * and dependencies (such as web driver) to extending classes.
 */
abstract class AbstractPage {

    private final WebDriverProvider webDriverProvider;

    AbstractPage(final WebDriverProvider webDriverProvider) {
        this.webDriverProvider = webDriverProvider;
    }

    WebDriver getWebDriver() {
        return webDriverProvider.getWebDriver();
    }
}
