package uk.nhs.digital.ps.test.acceptance.pages.site;

import uk.nhs.digital.ps.test.acceptance.pages.AbstractPage;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

public abstract class AbstractSitePage extends AbstractPage {

    protected static final String URL = "http://localhost:8080/site";

    public AbstractSitePage(WebDriverProvider webDriverProvider) {
        super(webDriverProvider);
    }
}
