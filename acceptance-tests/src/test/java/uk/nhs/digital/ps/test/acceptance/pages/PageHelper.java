package uk.nhs.digital.ps.test.acceptance.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class PageHelper {

    private final WebDriverProvider webDriverProvider;

    public PageHelper(final WebDriverProvider webDriverProvider) {
        this.webDriverProvider = webDriverProvider;
    }

    private WebDriver getWebDriver() {
        return webDriverProvider.getWebDriver();
    }


    public WebElement findElementAfterWait(final By bySelector) {
        return findElementAfterWait(() -> getWebDriver().findElements(bySelector));
    }

    public WebElement findElementAfterWait(final WebElement webElement, final By bySelector) {
        return findElementAfterWait(() -> webElement.findElements(bySelector));
    }

    private WebElement findElementAfterWait(final Supplier<List<WebElement>> findElements){

        WebDriverWait wait = new WebDriverWait(getWebDriver(), 3);

        try{
            return wait.pollingEvery(100, MILLISECONDS).until((WebDriver innerDriver) -> {
                List<WebElement> elements = findElements.get();
                if (elements.size() == 1) {
                    WebElement el = elements.get(0);
                    try {
                        el.isEnabled();
                        return el;
                    }
                    catch (StaleElementReferenceException ex) {
                        return null;
                    }
                } else {
                    return null;
                }
            });
        }
        catch (TimeoutException e){
            throw new RuntimeException("Failed to find element matching '" + findElements + "'", e);
        }
    }


    public void waitUntilVisible(WebElement menuIcon) {
        try{
            WebDriverWait wait = new WebDriverWait(getWebDriver(), 2);
            wait.pollingEvery(100, MILLISECONDS).until((WebDriver innerDriver) -> {
                return menuIcon.isDisplayed();
            });
        }
        catch (TimeoutException e){
            throw new RuntimeException("Timeout while waiting for element to become visible: '" + menuIcon + "'", e);
        }
    }


    public boolean isElementPresent(By selector){
        return findElementAfterWait(selector) != null;
    }

}
