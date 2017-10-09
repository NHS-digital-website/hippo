package uk.nhs.digital.ps.test.acceptance.pages;

import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;
import org.openqa.selenium.*;


public class ConsumablePublicationPage extends AbstractPage {

    private static final String URL = "http://localhost:8080/site";

    private PageHelper helper;

    public ConsumablePublicationPage(WebDriverProvider webDriverProvider, final PageHelper helper) {
        super(webDriverProvider);
        this.helper = helper;
    }

    public void open(String publicationPath) {
        getWebDriver().get(URL + publicationPath);
    }

    public String getTitleText() {
        return helper.findElement(By.id("title")).getText();
    }

    public String getSummaryText() {
        return helper.findElement(By.id("summary")).getText();
    }

    public WebElement getAttachmentElement() {
        return helper.findElement(By.className("attachment-hyperlink"));
    }

    public String getAttachmentName() {
        return getAttachmentElement().getText();
    }

    public String getAttachmentSizeText() {
        return helper.findElement(By.className("fileSize")).getText();
    }

    public String getGeographicCoverage() {
        return helper.findElement(By.id("geographic-coverage")).getText();
    }

}
