package uk.nhs.digital.ps.test.acceptance.pages.site;

import uk.nhs.digital.ps.test.acceptance.pages.AbstractPage;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

public abstract class AbstractSitePage extends AbstractPage {

    public AbstractSitePage(WebDriverProvider webDriverProvider, String siteUrl) {
        super(webDriverProvider, siteUrl);
    }
}
