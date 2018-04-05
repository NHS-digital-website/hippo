package uk.nhs.digital.ps.test.acceptance.pages.site.ps;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static uk.nhs.digital.ps.test.acceptance.pages.site.ps.PublicationPageElements.FieldKeys.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import uk.nhs.digital.ps.test.acceptance.models.Publication;
import uk.nhs.digital.ps.test.acceptance.pages.PageHelper;
import uk.nhs.digital.ps.test.acceptance.pages.site.AbstractSitePage;
import uk.nhs.digital.ps.test.acceptance.pages.site.PageElements;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.AttachmentWidget;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.ImageSectionWidget;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.SectionWidget;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.TextSectionWidget;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.RelatedLinkSectionWidget;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PublicationPage extends AbstractSitePage {

    private PageHelper helper;
    private List<PageElements> pageElements;

    public PublicationPage(WebDriverProvider webDriverProvider, final PageHelper helper) {
        super(webDriverProvider);
        this.helper = helper;
        this.pageElements = asList(new SeriesPageElements(), new PublicationPageElements());
    }

    public void open(final Publication publication) {
        getWebDriver().get(URL + "/publications/acceptance-tests/" + publication.getPublicationUrlName());
    }

    public String getSummaryText() {
        return findPageElement(SUMMARY).getText();
    }

    public List<AttachmentWidget> getAttachments() {
        return getWebDriver().findElements(By.cssSelector("li[class~='attachment']")).stream()
            .map(webElement -> new AttachmentWidget(getWebDriver(), webElement))
            .collect(toList());
    }

    public AttachmentWidget findAttachmentElementByName(final String fullName) {
        return getAttachments().stream()
            .filter(attachmentWidget -> fullName.equals(attachmentWidget.getFullName()))
            .findFirst()
            .orElse(null);
    }

    public String getGeographicCoverage() {
        return findPageElement(GEOGRAPHIC_COVERAGE).getText();
    }

    public String getInformationType() {
        return findPageElement(INFORMATION_TYPES).getText();
    }

    public String getGranularity() {
        return findPageElement(GRANULARITY).getText();
    }

    public String getSeriesLinkTitle() {
        return helper.findElement(By.className("label--series")).getText();
    }

    public String getNominalPublicationDate() {
        return findPageElement(NOMINAL_PUBLICATION_DATE).getText();
    }

    public boolean hasDisclaimer(final String disclaimer) {
        return disclaimer.equals(findPageElement(UPCOMING_DISCLAIMER).getText());
    }

    public Collection<WebElement> getElementsHiddenWhenUpcoming() {

        final List<String> fieldsHiddenForUpcoming = asList(
            SUMMARY,
            GEOGRAPHIC_COVERAGE,
            GRANULARITY,
            DATE_RANGE,
            INFORMATION_TYPES,
            KEY_FACTS,
            RESOURCES,
            DATA_SETS,
            ADMINISTRATIVE_SOURCES,
            BODY
        );

        return fieldsHiddenForUpcoming.stream()
            .map(PublicationPageElements::getFieldSelector)
            // Using findElements().isEmpty() is recommended by Selenium to search of absence of elements:
            .flatMap(selector -> getWebDriver().findElements(selector).stream())
            .collect(toList());
    }

    private WebElement findPageElement(final String pageElementName) {
        return pageElements.stream()
            .filter(pageElements -> pageElements.contains(pageElementName))
            .findFirst()
            .map(pageElements -> pageElements.getElementByName(pageElementName, helper))
            .orElse(null);
    }

    public List<SectionWidget> getBodySections() {
        WebElement body = findPageElement(BODY);
        return body == null ? Collections.emptyList() :
            body.findElements(By.xpath("./*"))
                .stream()
                .map(this::createSectionWidget)
                .collect(toList());
    }

    private SectionWidget createSectionWidget(WebElement webElement) {
        String uipath = webElement.getAttribute("data-uipath");
        switch (uipath) {
            case ImageSectionWidget.UIPATH:
                return new ImageSectionWidget(webElement);
            case TextSectionWidget.UIPATH:
                return new TextSectionWidget(webElement);
            case RelatedLinkSectionWidget.UIPATH:
                return new RelatedLinkSectionWidget(webElement);
            default:
                throw new RuntimeException("Unknown uipath: " + uipath);
        }
    }
}
