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

    public WebElement findElement(final By bySelector) {
        return findElement((by) -> getWebDriver().findElements(by), bySelector, (w) -> true);
    }

    public WebElement findNewElement(final WebElement oldElement, final By bySelector) {
        Function<WebElement, Boolean> elementIsNew = (newElement) -> !newElement.equals(oldElement);
        return findElement((by) -> getWebDriver().findElements(by), bySelector, elementIsNew);
    }

    public WebElement findChildElement(final WebElement parentElement, final By bySelector) {
        return findElement((by) -> parentElement.findElements(by), bySelector, (w) -> true);
    }

    private WebElement findElement(final Function<By, List<WebElement>> findElements,
                                   final By bySelector,
                                   final Function<WebElement, Boolean> elementOkFilter){

        WebDriverWait wait = new WebDriverWait(getWebDriver(), 3);

        try{
            return wait.pollingEvery(100, MILLISECONDS).until((WebDriver innerDriver) -> {

                List<WebElement> elements = findElements.apply(bySelector);
                if (elements.size() == 1) {
                    WebElement el = elements.get(0);
                    try {
                        el.isEnabled();
                        return elementOkFilter.apply(el) ? el : null;
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
            throw new RuntimeException("Failed to find element matching '" + bySelector + "'", e);
        }
    }


    public void waitUntilVisible(WebElement menuIcon) {
        try {
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
        return findElement(selector) != null;
    }

}
