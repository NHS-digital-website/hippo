package uk.nhs.digital.ps.test.acceptance.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

public class DashboardPage extends AbstractCmsPage {

    public DashboardPage(final WebDriverProvider webDriverProvider) {
        super(webDriverProvider);
    }

    public boolean isOpen() {
        WebElement activeDashboardTab;

        try {
            activeDashboardTab = getWebDriver().findElement(By.className("tab0"));
        } catch (NoSuchElementException e) {
            activeDashboardTab = null;
        }

        return activeDashboardTab != null && activeDashboardTab.getAttribute("class").contains("selected");
    }

    public boolean open() {
        WebElement dashboardTab;
        WebDriverWait wait = new WebDriverWait(getWebDriver(), 3);

        dashboardTab = getWebDriver().findElement(By.className("tab0"));
        dashboardTab.click();

        wait.until(
            ExpectedConditions.refreshed(
                ExpectedConditions.attributeContains(By.className("tab0"), "class", "selected")
            )
        );

        return true;
    }
}
