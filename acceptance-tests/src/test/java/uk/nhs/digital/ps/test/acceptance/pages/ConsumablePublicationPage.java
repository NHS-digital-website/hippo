package uk.nhs.digital.ps.test.acceptance.pages;

import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;
import org.openqa.selenium.*;


public class ConsumablePublicationPage extends AbstractPage{

    static final String URL = "http://localhost:8080/site";

    private PageHelper helper;

    public ConsumablePublicationPage(WebDriverProvider webDriverProvider, final PageHelper helper) {
        super(webDriverProvider);
        this.helper = helper;
    }

    public void open(String publicationPath) {
        getWebDriver().get(URL + publicationPath);
    }

    public boolean isPageDisplayed() {
        return helper.isElementPresent(By.id("header"));
    }

    public String getTitle() {
        return helper.findElement(By.id("title")).getText();
    }

    public String getSummary() {
        return helper.findElement(By.id("summary")).getText();
    }

}
