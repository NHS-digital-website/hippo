package uk.nhs.digital.ps.test.acceptance.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

import java.util.Collections;
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

    public WebElement findElement(final Supplier<WebElement> findWebElementFunction) {
        return findElement(by -> Collections.singletonList(findWebElementFunction.get()), null, webElement -> true);
    }

    private WebElement findElement(final Function<By, List<WebElement>> findElements,
                                   final By bySelector,
                                   final Function<WebElement, Boolean> elementOkFilter) {

        try {
            return pollWithTimeout().until((WebDriver innerDriver) -> {
                List<WebElement> elements = findElements.apply(bySelector);
                if (elements.size() == 1) {
                    WebElement el = elements.get(0);
                    el.isEnabled();
                    return elementOkFilter.apply(el) ? el : null;
                } else {
                    return null;
                }
            });
        }
        catch (TimeoutException e){
            throw new RuntimeException("Failed to find element matching '" + bySelector + "'", e);
        }
    }

    public List<WebElement> findElements(By bySelector) {
        try{
            return pollWithTimeout().until((WebDriver innerDriver) -> {

                List<WebElement> elements = innerDriver.findElements(bySelector);
                if (elements.size() == 0) {
                    return null;
                } else {
                    for (WebElement el : elements) {
                        el.isEnabled();
                    }
                    return elements;
                }
            });
        }
        catch (TimeoutException e){
            throw new RuntimeException("Failed to find elements matching '" + bySelector + "'", e);
        }

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
        try {
            pollWithTimeout().until((WebDriver innerDriver) -> webElement.isDisplayed());
        }
        catch (TimeoutException e){
            throw new RuntimeException("Timeout while waiting for element to become visible: '" + webElement + "'", e);
        }
    }

    /**
     * This takes care of element being removed/refreshed from DOM
     */
    public <T> T waitForElementUntil(ExpectedCondition<T> condition) {
        return pollWithTimeout()
            .until(ExpectedConditions.refreshed(condition));
    }

    private FluentWait<WebDriver> pollWithTimeout() {
        return new WebDriverWait(getWebDriver(), 5)
            .ignoring(StaleElementReferenceException.class)
            .pollingEvery(100, TimeUnit.MILLISECONDS);
    }

    public boolean isElementPresent(By selector) {
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
