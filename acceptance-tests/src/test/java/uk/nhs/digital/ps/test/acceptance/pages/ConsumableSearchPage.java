package uk.nhs.digital.ps.test.acceptance.pages;

import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;
import org.openqa.selenium.*;


public class ConsumableSearchPage extends AbstractConsumablePage {

    private PageHelper helper;

    public ConsumableSearchPage(WebDriverProvider webDriverProvider, final PageHelper helper) {
        super(webDriverProvider);
        this.helper = helper;
    }

    public void open() {
        getWebDriver().get(URL);
    }

    public boolean search(final String query) {
        findSearchField().sendKeys(query);
        findSearchButton().click();

        return (findResultsCount() > 0);
    }


    public boolean openFirstSearchResult() {
        WebElement resultsList = findResultsList();

        if (resultsList != null) {
            WebElement firstResultLink = resultsList.findElement(By.xpath("//following-sibling::li//following-sibling::a"));
            String firstResultTitle = firstResultLink.getText();
            firstResultLink.click();

            String openedDocumentTitle = helper.findElement(By.id("title")).getText();
            return firstResultTitle.equals(openedDocumentTitle);

        } else {
            return false;
        }
    }

    public String findSearchFieldValue() {
        return findSearchField().getAttribute("value");
    }

    private WebElement findSearchField() {
        return helper.findElement(By.id("query"));
    }

    private WebElement findSearchButton() {
        return helper.findElement(By.id("btnSearch"));
    }

    private WebElement findResultsList() {
        return helper.findElement(By.id("resultsList"));
    }

    private int findResultsCount() {
        return Integer.parseInt(helper.findElement(By.id("searchResults")).getAttribute("data-totalresults"));
    }
}

