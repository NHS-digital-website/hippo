package uk.nhs.digital.ps.test.acceptance.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

public class AbstractCmsPage extends AbstractPage {

    static protected final String URL = "http://localhost:8080/cms";

    AbstractCmsPage(final WebDriverProvider webDriverProvider) {
        super(webDriverProvider);
    }

    public void openCms() {
        getWebDriver().get(URL);
    }

    public void logout() {

        new Actions(getWebDriver())
            .moveToElement(getWebDriver().findElement(By.className("hippo-user-menu")))
            .moveToElement(getWebDriver().findElement(By.className("hippo-logout")))
            .click()
            .perform();
    }
}
