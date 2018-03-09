package uk.nhs.digital.ps.test.acceptance.pages.site;

import uk.nhs.digital.ps.test.acceptance.pages.AbstractPage;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

public abstract class AbstractSitePage extends AbstractPage {

    public static final String URL = "http://localhost:8080";

    public AbstractSitePage(WebDriverProvider webDriverProvider) {
        super(webDriverProvider);
    }
}
