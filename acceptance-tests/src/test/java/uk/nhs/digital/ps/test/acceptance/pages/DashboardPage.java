package uk.nhs.digital.ps.test.acceptance.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

public class DashboardPage extends AbstractPage {

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
}
