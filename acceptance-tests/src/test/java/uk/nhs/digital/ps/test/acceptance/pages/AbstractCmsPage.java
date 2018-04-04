package uk.nhs.digital.ps.test.acceptance.pages;

import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

public class AbstractCmsPage extends AbstractPage {

    static protected final String URL = "http://localhost:8080/cms";

    AbstractCmsPage(final WebDriverProvider webDriverProvider) {
        super(webDriverProvider);
    }

    public void openCms() {
        getWebDriver().get(URL);
    }
}
