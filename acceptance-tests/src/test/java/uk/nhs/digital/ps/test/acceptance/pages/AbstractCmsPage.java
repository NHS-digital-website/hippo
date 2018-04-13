package uk.nhs.digital.ps.test.acceptance.pages;

import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

public class AbstractCmsPage extends AbstractPage {

    AbstractCmsPage(final WebDriverProvider webDriverProvider, String cmsUrl) {
        super(webDriverProvider, cmsUrl);
    }

    public void openCms() {
        getWebDriver().get(getUrl());
    }
}
