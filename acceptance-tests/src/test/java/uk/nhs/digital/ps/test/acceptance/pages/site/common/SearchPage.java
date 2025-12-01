package uk.nhs.digital.ps.test.acceptance.pages.site.common;

import static java.util.stream.Collectors.toList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import uk.nhs.digital.ps.test.acceptance.pages.PageHelper;
import uk.nhs.digital.ps.test.acceptance.pages.site.AbstractSitePage;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.SearchResultWidget;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

import java.util.List;

public class SearchPage extends AbstractSitePage {

    private PageHelper helper;

    public SearchPage(final WebDriverProvider webDriverProvider, PageHelper helper, String siteUrl) {
        super(webDriverProvider, siteUrl);
        this.helper = helper;
    }

    public List<SearchResultWidget> getSearchResultWidgets() {
        return helper
            .findElements(By.xpath("//*[@data-uipath='ps.search-results.result']"))
            .stream()
            .map(SearchResultWidget::new)
            .collect(toList());
    }

    public String getResultCount() {
        return helper.findElement(By.xpath("//*[@data-uipath='ps.search-results.count']")).getText();
    }

    public String getResultDescription() {
        By descriptionSelector = By.xpath("//*[@data-uipath='ps.search-results.description']");
        return helper.waitForElementUntil(
            ExpectedConditions.visibilityOfElementLocated(descriptionSelector)
        ).getText();
    }

    public WebElement findSearchResults() {
        return helper.findElement(By.xpath("//*[@data-uipath='ps.search-results']"));
    }

    private WebElement findSearchField() {
        return helper.findElement(By.id("query"));
    }

    private WebElement findSearchButton() {
        return helper.findElement(By.xpath("//*[@data-uipath='search.button']"));
    }

    public String getSortSelection() {
        return new Select(helper.findElement(By.xpath("//*[@data-uipath='ps.search-results.sort-selector']")))
            .getFirstSelectedOption()
            .getText();
    }

    public String getSearchFieldValue() {
        return findSearchField().getAttribute("value");
    }

    public WebElement searchForTerm(final String queryTerm) {
        if (helper.isElementPresent(By.id("nhsd-global-header__search-button"))) {
            clickSearchButton();
        }

        // type, click search
        WebElement searchField = findSearchField();
        searchField.clear();
        searchField.sendKeys(queryTerm);
        findSearchButton().click();

        // ensure search results is present on page
        return findSearchResults();
    }

    private void clickSearchButton() {
        helper.findElement(By.id("nhsd-global-header__search-button")).click();
    }
}
