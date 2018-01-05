package uk.nhs.digital.ps.test.acceptance.pages.site;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import uk.nhs.digital.ps.test.acceptance.pages.PageHelper;
import uk.nhs.digital.ps.test.acceptance.pages.site.ps.DatasetPageElements;
import uk.nhs.digital.ps.test.acceptance.pages.site.ps.PublicationPageElements;
import uk.nhs.digital.ps.test.acceptance.pages.site.ps.SeriesPageElements;
import uk.nhs.digital.ps.test.acceptance.util.TestContentUrls;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

import java.util.ArrayList;
import java.util.List;

public class SitePage extends AbstractSitePage {

    private PageHelper helper;
    private TestContentUrls urlLookup;
    private List<PageElements> pagesElements;

    public SitePage(WebDriverProvider webDriverProvider, final PageHelper helper) {
        super(webDriverProvider);
        this.helper = helper;
        this.urlLookup = new TestContentUrls();
        this.pagesElements = new ArrayList<>();

        // load pageElement
        pagesElements.add(new CommonPageElements());
        pagesElements.add(new PublicationPageElements());
        pagesElements.add(new SeriesPageElements());
        pagesElements.add(new DatasetPageElements());
    }

    public void openByPageName(final String pageName) {
        getWebDriver().get(URL + urlLookup.lookupUrl(pageName));
    }

    public void searchForTerm(final String queryTerm) {
        // type, click search
        findSearchField().sendKeys(queryTerm);
        findSearchButton().click();

        // ensure search results is present on page
        getWebDriver().findElement(By.xpath("//*[@data-uipath='searchResults']"));
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
        List<WebElement> elements = getWebDriver().findElements(By.xpath("//*[@title='" + title +"']"));

        if (elements.size() < 1) {
            return null;
        }

        return elements.get(0);
    }

    public String getDocumentTitle() {
        return helper.findElement(By.xpath("//*[@data-uipath='ps.document.title']")).getText();
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
                return pageElements.getElementByName(elementName, nth, getWebDriver());
            }
        }

        return null;
    }

    private WebElement findSearchField() {
        return helper.findElement(By.id("query"));
    }

    private WebElement findSearchButton() {
        return helper.findElement(By.id("btnSearch"));
    }

    public WebElement findFooter() {
        return helper.findElement(By.id("footer"));
    }

    public String getResultCount() {
        return helper.findElement(By.xpath("//*[@data-uipath='resultCount']")).getText();
    }
}
