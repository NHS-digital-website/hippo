package uk.nhs.digital.ps.test.acceptance.pages.site.ps;

import uk.nhs.digital.ps.test.acceptance.models.Publication;
import uk.nhs.digital.ps.test.acceptance.pages.PageHelper;
import uk.nhs.digital.ps.test.acceptance.pages.site.AbstractSitePage;
import uk.nhs.digital.ps.test.acceptance.pages.site.PageElements;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;
import org.openqa.selenium.*;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class PublicationPage extends AbstractSitePage {

    private PageHelper helper;
    private PageElements pageElements;

    public PublicationPage(WebDriverProvider webDriverProvider, final PageHelper helper) {
        super(webDriverProvider);
        this.helper = helper;
        this.pageElements = new SeriesPageElements();
    }

    public void open(final Publication publication) {
        getWebDriver().get(URL + "/publications/" + publication.getPublicationUrlName());
    }

    public String getTitleText() {
        return pageElements.getElementByName("Publication Title", getWebDriver()).getText();
    }

    public String getSummaryText() {
        return pageElements.getElementByName("Publication Summary", getWebDriver()).getText();
    }

    public List<AttachmentElement> getAttachments() {
        return getWebDriver().findElements(By.cssSelector("li[class~='attachment']")).stream()
            .map(webElement -> new AttachmentElement(getWebDriver(), webElement))
            .collect(toList());
    }

    public AttachmentElement findAttachmentElementByName(final String fullName) {
        return getAttachments().stream()
            .filter(attachmentElement -> fullName.equals(attachmentElement.getFullName()))
            .findFirst()
            .orElse(null);
    }

    public String getGeographicCoverage() {
        return pageElements
            .getElementByName("Publication Geographic Coverage", getWebDriver())
            .getText();
    }

    public String getInformationType() {
        return helper.findElement(By.xpath("//*[@data-uipath='ps.publication.information-types']")).getText();
    }

    public String getGranularity() {
        return pageElements
            .getElementByName("Publication Granularity", getWebDriver())
            .getText();
    }

    public String getTaxonomy() {
        return helper.findElement(By.id("taxonomy")).getText();
    }

    public String getSeriesLinkTitle() {
        return helper.findElement(By.className("label--series")).getText();
    }

}
