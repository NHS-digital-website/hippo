package uk.nhs.digital.ps.test.acceptance.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

public class DashboardPage extends AbstractCmsPage {

    private PageHelper helper;

    public DashboardPage(final WebDriverProvider webDriverProvider, final PageHelper helper) {
        super(webDriverProvider);
        this.helper = helper;
    }

    public boolean open() {
        helper.findElement(By.xpath(XpathSelectors.TABBED_PANEL + "//li[contains(@class, 'tab0')]"))
            .click();

        return helper.waitForElementUntil(
            ExpectedConditions.attributeContains(
                By.xpath(XpathSelectors.TABBED_PANEL + "//li[contains(@class, 'tab0')]"), "class", "selected"
            )
        );
    }
}
