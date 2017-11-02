package uk.nhs.digital.ps.test.acceptance.pages;

import uk.nhs.digital.ps.test.acceptance.models.Publication;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;
import org.openqa.selenium.*;

import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;


public class ConsumablePublicationPage extends AbstractConsumablePage {

    private static final By TITLE_SELECTOR = By.id("title");
    private static final By SERIES_LINK_SELECTOR = By.className("label--series");

    private static final By SUMMARY_SELECTOR = By.id("summary");
    private static final By ATTACHMENTS_SELECTOR = By.cssSelector("li[class~='attachment']");
    private static final By GEOGRAPHIC_COVERAGE_SELECTOR = By.id("geographic-coverage");
    private static final By INFORMATION_TYPE_SELECTOR = By.id("information-types");
    private static final By GRANULARITY_SELECTOR = By.id("granularity");
    private static final By TAXONOMY_SELECTOR = By.id("taxonomy");
    private static final By NOMINAL_PUBLICATION_DATE_SELECTOR = By.id("nominal-date");
    private static final By NOMINAL_PUBLICATION_DATE_VALUE_SELECTOR = By.id("nominal-date-value");
    private static final By COVERAGE_START_DATE_SELECTOR = By.id("coverage-start");
    private static final By COVERAGE_END_DATE_SELECTOR = By.id("coverage-end");
    private static final By RELATED_LINKS_SELECTOR = By.id("related-links");
    private static final By ADMINISTRATIVE_SOURCES_SELECTOR = By.id("administrative-sources");

    private PageHelper helper;

    public ConsumablePublicationPage(WebDriverProvider webDriverProvider, final PageHelper helper) {
        super(webDriverProvider);
        this.helper = helper;
    }

    public void open(final Publication publication) {
        getWebDriver().get(URL + "/publications/" + publication.getPublicationUrlName());
    }

    public String getTitleText() {
        return helper.findElement(TITLE_SELECTOR).getText();
    }

    public String getSummaryText() {
        return helper.findElement(SUMMARY_SELECTOR).getText();
    }

    public List<ConsumableAttachmentElement> getAttachments() {
        return getWebDriver().findElements(ATTACHMENTS_SELECTOR).stream()
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
        return helper.findElement(GEOGRAPHIC_COVERAGE_SELECTOR).getText();
    }

    public String getInformationType() {
        return helper.findElement(INFORMATION_TYPE_SELECTOR).getText();
    }

    public String getGranularity() {
        return helper.findElement(GRANULARITY_SELECTOR).getText();
    }

    public String getTaxonomy() {
        return helper.findElement(TAXONOMY_SELECTOR).getText();
    }

    public String getSeriesLinkTitle() {
        return helper.findElement(SERIES_LINK_SELECTOR).getText();
    }

    public boolean hasDisclaimer(final String disclaimer) {
        return helper.findElement(
            By.xpath("//*[@class='empty-field-disclaimer' and text()='" + disclaimer + "']")
        ) != null;
    }

    public Collection<WebElement> getElementsHiddenWhenUpcoming() {

        final List<By> selectorsOfFieldsHiddenForUpcoming = asList(
            SUMMARY_SELECTOR,
            ATTACHMENTS_SELECTOR,
            GEOGRAPHIC_COVERAGE_SELECTOR,
            INFORMATION_TYPE_SELECTOR,
            GRANULARITY_SELECTOR,
            TAXONOMY_SELECTOR,
            COVERAGE_START_DATE_SELECTOR,
            COVERAGE_END_DATE_SELECTOR,
            RELATED_LINKS_SELECTOR,
            ADMINISTRATIVE_SOURCES_SELECTOR
        );

        return selectorsOfFieldsHiddenForUpcoming.stream()
            .flatMap(selector -> getWebDriver().findElements(selector).stream())
            .collect(toList());
    }

    public String getNominalDate() {
        return helper.findElement(NOMINAL_PUBLICATION_DATE_VALUE_SELECTOR).getText();
    }
}
