package uk.nhs.digital.ps.test.acceptance.pages;

import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

abstract class AbstractConsumablePage extends AbstractPage {

    static final String URL = "http://localhost:8080";

    AbstractConsumablePage(final WebDriverProvider webDriverProvider) {
        super(webDriverProvider);
    }
}
