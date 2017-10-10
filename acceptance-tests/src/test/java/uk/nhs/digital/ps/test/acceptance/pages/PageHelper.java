package uk.nhs.digital.ps.test.acceptance.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;


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

        try{
            return pollWithTimeout().until((WebDriver innerDriver) -> {

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

    public WebElement findWhenStable(final Supplier<WebElement> findWebElementFunction) {

        WebElement[] webElement = new WebElement[1];

        pollWithTimeout().until(webDriver -> {
                try {
                    webElement[0] = findWebElementFunction.get();
                    return true;
                } catch (final StaleElementReferenceException e) {
                    return false;
                }
            });

        return webElement[0];
    }

    public void waitUntilTrue(final Predicate predicate) {

        pollWithTimeout().until(webDriver -> {
                try {
                    return predicate.executeWithPredicate();
                } catch (final Exception e) {
                    return false;
                }
            });
    }

    public void executeWhenStable(final Task task) {

        pollWithTimeout().until(webDriver -> {
                try {
                    task.execute();
                    return true;
                } catch (final Exception e) {
                    return false;
                }
            });
    }

    public void waitUntilVisible(WebElement webElement) {
        try{
            pollWithTimeout().until((WebDriver innerDriver) -> webElement.isDisplayed());
        }
        catch (TimeoutException e){
            throw new RuntimeException("Timeout while waiting for element to become visible: '" + webElement + "'", e);
        }
    }

    private FluentWait<WebDriver> pollWithTimeout() {
        return new WebDriverWait(getWebDriver(), 3)
            .pollingEvery(100, TimeUnit.MILLISECONDS);
    }


    public boolean isElementPresent(By selector){
        return findElement(selector) != null;
    }




    @FunctionalInterface
    interface Task {
        void execute();
    }

    @FunctionalInterface
    interface Predicate {
        boolean executeWithPredicate();
    }
}
