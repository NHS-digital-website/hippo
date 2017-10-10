package uk.nhs.digital.ps.test.acceptance.pages;

import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;
import org.openqa.selenium.*;

import java.util.List;

import static java.util.stream.Collectors.toList;


public class ConsumablePublicationPage extends AbstractPage {

    private static final String URL = "http://localhost:8080";

    private PageHelper helper;

    public ConsumablePublicationPage(WebDriverProvider webDriverProvider, final PageHelper helper) {
        super(webDriverProvider);
        this.helper = helper;
    }

    public void open(final String publicationUrlName) {
        getWebDriver().get(URL + "/publications/" + publicationUrlName);
    }

    public String getTitleText() {
        return helper.findElement(By.id("title")).getText();
    }

    public String getSummaryText() {
        return helper.findElement(By.id("summary")).getText();
    }

    public List<ConsumableAttachmentElement> getAttachments() {
        return getWebDriver().findElements(By.cssSelector("li[class~='attachment']")).stream()
            .map(ConsumableAttachmentElement::new)
            .collect(toList());
    }

    public ConsumableAttachmentElement findAttachmentElementByName(final String fullName) {
        return getAttachments().stream()
            .filter(consumableAttachmentElement -> fullName.equals(consumableAttachmentElement.getFullName()))
            .findFirst()
            .orElse(null);
    }

    public String getGeographicCoverage() {
        return helper.findElement(By.id("geographic-coverage")).getText();
    }

    public String getInformationType() {
        return helper.findElement(By.id("information-types")).getText();
    }
    public String getGranularity() {
        return helper.findElement(By.id("granularity")).getText();
    }

}
