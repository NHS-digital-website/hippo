package uk.nhs.digital.ps.test.acceptance.pages.site.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import uk.nhs.digital.ps.test.acceptance.pages.PageHelper;
import uk.nhs.digital.ps.test.acceptance.pages.site.AbstractSitePage;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.SearchResultWidget;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class SearchPage extends AbstractSitePage {

    private PageHelper helper;

    public SearchPage(final WebDriverProvider webDriverProvider, PageHelper helper) {
        super(webDriverProvider);
        this.helper = helper;
    }

    public List<SearchResultWidget> getSearchResultWidgets() {
        return getWebDriver()
            .findElements(By.xpath("//*[@data-uipath='ps.search-results.result']"))
            .stream()
            .map(SearchResultWidget::new)
            .collect(toList());
    }

    public String getResultCount() {
        return helper.findElement(By.xpath("//*[@data-uipath='ps.search-results.count']")).getText();
    }

    public WebElement findSearchResults() {
        return helper.findElement(By.xpath("//*[@data-uipath='ps.search-results']"));
    }

    private WebElement findSearchField() {
        return helper.findElement(By.id("query"));
    }

    private WebElement findSearchButton() {
        return helper.findElement(By.id("btnSearch"));
    }

    public String getSearchFieldValue() {
        return findSearchField().getAttribute("value");
    }

    public WebElement searchForTerm(final String queryTerm) {
        // type, click search
        findSearchField().sendKeys(queryTerm);
        findSearchButton().click();

        // ensure search results is present on page
        return findSearchResults();
    }
}
