package uk.nhs.digital.ps.test.acceptance.pages;

import uk.nhs.digital.ps.test.acceptance.models.Publication;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;
import org.openqa.selenium.*;

import java.util.List;

import static java.util.stream.Collectors.toList;


public class ConsumablePublicationPage extends AbstractConsumablePage {

    private PageHelper helper;

    public ConsumablePublicationPage(WebDriverProvider webDriverProvider, final PageHelper helper) {
        super(webDriverProvider);
        this.helper = helper;
    }

    public void open(final Publication publication) {
        getWebDriver().get(URL + "/publications/" + publication.getPublicationUrlName());
    }

    public String getTitleText() {
        return helper.findElement(By.id("title")).getText();
    }

    public String getSummaryText() {
        return helper.findElement(By.id("summary")).getText();
    }

    public List<ConsumableAttachmentElement> getAttachments() {
        return getWebDriver().findElements(By.cssSelector("li[class~='attachment']")).stream()
            .map(webElement -> new ConsumableAttachmentElement(getWebDriver(), webElement))
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

    public String getTaxonomy() {
        return helper.findElement(By.id("taxonomy")).getText();
    }

    public String getSeriesLinkTitle() {
        return helper.findElement(By.className("label--series")).getText();
    }

}
