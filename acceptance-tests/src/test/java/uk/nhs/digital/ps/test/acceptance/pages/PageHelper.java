package uk.nhs.digital.ps.test.acceptance.pages;

import static org.springframework.util.CollectionUtils.isEmpty;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class PageHelper {

    private static final int TIME_OUT = 5;

    private final WebDriverProvider webDriverProvider;

    public PageHelper(final WebDriverProvider webDriverProvider) {
        this.webDriverProvider = webDriverProvider;
    }

    private WebDriver getWebDriver() {
        return webDriverProvider.getWebDriver();
    }

    public WebElement findElement(final By bySelector) {
        return firstOrNull(findElements(bySelector));
    }

    public WebElement findChildElement(final WebElement parentElement, final By bySelector) {
        return firstOrNull(findChildElements(parentElement, bySelector));
    }

    public List<WebElement> findElements(By bySelector) {
        return findElements((by) -> getWebDriver().findElements(by), bySelector);
    }

    public List<WebElement> findChildElements(final WebElement parentElement, final By bySelector) {
        return findElements(parentElement::findElements, bySelector);
    }

    private List<WebElement> findElements(final Function<By, List<WebElement>> findElements,
                                          final By bySelector) {
        try {
            return pollWithTimeout().until((WebDriver driver) -> {
                List<WebElement> elements = findElements.apply(bySelector);
                return isEmpty(elements) ? null : elements;
            });
        } catch (TimeoutException e){
            throw new RuntimeException("Failed to find element matching '" + bySelector + "'", e);
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

        final AtomicReference<Exception> lastException = new AtomicReference<>();

        try {
            pollWithTimeout().until(webDriver -> {
                try {
                    task.execute();
                    return true;
                } catch (final Exception e) {
                    lastException.set(e);
                    return false;
                }
            });
        } catch (final TimeoutException e) { // ignore TimeoutException
            throw new RuntimeException("Failed to execute task within timeout.", lastException.get());
        }
    }

    public void waitUntilVisible(WebElement webElement) {
        try {
            pollWithTimeout().until((WebDriver innerDriver) -> webElement.isDisplayed());
        } catch (TimeoutException e){
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
        return new WebDriverWait(getWebDriver(), TIME_OUT)
            .ignoring(StaleElementReferenceException.class)
            .pollingEvery(Duration.ofMillis(500));
    }

    public boolean assertElementPresent(By selector) {
        // this will throw an exception if the element is not present
        return findElement(selector) != null;
    }

    public boolean isElementPresent(By selector) {
        return findOptionalElement(selector) != null;
    }

    public WebElement findOptionalElement(By by) {
        return firstOrNull(getWebDriver().findElements(by));
    }

    public WebElement findOptionalChildElement(WebElement menu, By by) {
        return firstOrNull(menu.findElements(by));
    }

    private WebElement firstOrNull(List<WebElement> elements) {
        if (elements.size() < 1) {
            return null;
        }

        return elements.get(0);
    }

    public void click(By by) {
        executeWhenStable(() -> findElement(by).click());
    }

    public List<WebElement> findOptionalElements(By by) {
        return getWebDriver().findElements(by);
    }

    @FunctionalInterface
    public interface Task {
        void execute();
    }

    @FunctionalInterface
    public interface Predicate {
        boolean executeWithPredicate();
    }
}
