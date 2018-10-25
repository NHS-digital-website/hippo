package uk.nhs.digital.ps.test.acceptance.pages.site;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import uk.nhs.digital.ps.test.acceptance.pages.PageHelper;
import uk.nhs.digital.ps.test.acceptance.pages.site.nil.IndicatorPageElements;
import uk.nhs.digital.ps.test.acceptance.pages.site.ps.ArchivePageElements;
import uk.nhs.digital.ps.test.acceptance.pages.site.ps.DatasetPageElements;
import uk.nhs.digital.ps.test.acceptance.pages.site.ps.PublicationPageElements;
import uk.nhs.digital.ps.test.acceptance.pages.site.ps.SeriesPageElements;
import uk.nhs.digital.ps.test.acceptance.pages.site.website.*;
import uk.nhs.digital.ps.test.acceptance.util.TestContentUrls;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

import java.util.ArrayList;
import java.util.List;

public class SitePage extends AbstractSitePage {

    private PageHelper helper;
    private TestContentUrls urlLookup;
    private List<PageElements> pagesElements;

    public SitePage(final WebDriverProvider webDriverProvider,
                    final PageHelper helper,
                    final String siteUrl,
                    final TestContentUrls testContentUrls) {
        super(webDriverProvider, siteUrl);

        this.helper = helper;
        this.pagesElements = new ArrayList<>();
        urlLookup = testContentUrls;

        // load pageElement
        pagesElements.add(new ArchivePageElements());
        pagesElements.add(new CommonPageElements());
        pagesElements.add(new PublicationPageElements());
        pagesElements.add(new SeriesPageElements());
        pagesElements.add(new DatasetPageElements());
        pagesElements.add(new IndicatorPageElements());
        pagesElements.add(new GeneralPageElements());
        pagesElements.add(new ServicePageElements());
        pagesElements.add(new HubPageElements());
        pagesElements.add(new PublishedWorkPageElements());
        pagesElements.add(new LinksListPageElements());
        pagesElements.add(new GdprPageElements());
    }

    public void openByPageName(final String pageName) {
        String lookupUrl = urlLookup.lookupSiteUrl(pageName);
        getWebDriver().get(lookupUrl);
    }

    public void clickOnElement(WebElement element) {
        // scroll to element to prevent errors like "Other element would receive the click"
        new Actions(getWebDriver())
            .moveToElement(element)
            .moveByOffset(0, 100)
            .perform();

        // click it
        element.click();
    }

    public WebElement findElementWithTitle(String title) {
        return helper.findElement(By.xpath("//*[@title='" + title + "']"));
    }

    public WebElement findOptionalElementWithTitle(String title) {
        return helper.findOptionalElement(By.xpath("//*[@title='" + title + "']"));
    }

    public WebElement findLinkWithinUiPath(String uiPath, String linkName) {
        return helper.findOptionalElement(By.xpath("//*[@data-uipath='" + uiPath + "']"))
            .findElement(By.xpath("//a[text()='" + linkName + "']"));
    }

    public WebElement findElementWithUiPath(String uiPath) {
        return helper.findElement(By.xpath("//*[@data-uipath='" + uiPath + "']"));
    }

    public WebElement findOptionalElementWithText(String text) {
        return helper.findOptionalElement(By.xpath("//*[text()='" + text + "']"));
    }

    public WebElement findElementWithText(String text) {
        return helper.findElement(By.xpath("//*[text()='" + text + "']"));
    }

    public String getDocumentTitle() {
        return helper.findElement(By.xpath("//*[@data-uipath='document.title']")).getText();
    }

    public String getDocumentContent() {
        return helper.findElement(By.xpath("//*[@data-uipath='ps.document.content']")).getText();
    }

    public WebElement findPageElement(String elementName) {
        return findPageElement(elementName, 0);
    }

    public WebElement findPageElement(String elementName, int nth) {
        for (PageElements pageElements : pagesElements) {
            if (pageElements.contains(elementName)) {
                return pageElements.getElementByName(elementName, nth, helper);
            }
        }

        return null;
    }

    public WebElement findFooter() {
        return helper.findElement(By.id("footer"));
    }

    public void clickCookieAcceptButton() {
        WebElement cookieAcceptButton = helper.findOptionalElement(By.id("CybotCookiebotDialogBodyButtonAccept"));
        if (cookieAcceptButton != null
            && cookieAcceptButton.isDisplayed()) {
            cookieAcceptButton.click();

            waitForCookiebotDialogToBeGone();
        }
    }

    private void waitForCookiebotDialogToBeGone() {
        helper.waitUntilTrue(() -> findCookiebotDialog() == null);
    }

    private WebElement findCookiebotDialog() {
        return helper.findOptionalElement(By.xpath(".//*[@id='CybotCookiebotDialog' and contains(@style,'display: block')]"));
    }
}
