package uk.nhs.digital.ps.test.acceptance.pages;

import static org.springframework.util.CollectionUtils.isEmpty;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class PageHelper {

    private static Logger log = LoggerFactory.getLogger(PageHelper.class);

    private static final int TIME_OUT = 15;

    private final WebDriverProvider webDriverProvider;

    public PageHelper(final WebDriverProvider webDriverProvider) {
        this.webDriverProvider = webDriverProvider;
    }

    private WebDriver getWebDriver() {
        return webDriverProvider.getWebDriver();
    }

    public WebElement findElement(final WebDriver webDriver, final By selector) {
        return firstOrNull(findElements(webDriver, selector));
    }

    public WebElement findElement(final By selector) {
        return findElement(getWebDriver(), selector);
    }

    public List<WebElement> findElements(final WebDriver webDriver, final By bySelector) {
        return findElements(webDriver, webDriver::findElements, bySelector);
    }

    public List<WebElement> findElements(final By bySelector) {
        return findElements((by) -> getWebDriver().findElements(by), bySelector);
    }

    private List<WebElement> findElements(final Function<By, List<WebElement>> findElements,
                                          final By bySelector
    ) {
        return findElements(getWebDriver(), findElements, bySelector);
    }

    private List<WebElement> findElements(final WebDriver webDriver,
                                          final Function<By, List<WebElement>> findElements,
                                          final By bySelector
    ) {
        try {
            return pollWithTimeout(webDriver).until((WebDriver driver) -> {
                List<WebElement> elements = findElements.apply(bySelector);
                return isEmpty(elements) ? null : elements;
            });
        } catch (TimeoutException exception) {
            throw new RuntimeException("Failed to find element matching '" + bySelector + "'", exception);
        }
    }

    public WebElement findChildElement(final WebElement parentElement, final By bySelector) {
        return firstOrNull(findChildElements(parentElement, bySelector));
    }

    public List<WebElement> findChildElements(final WebElement parentElement, final By bySelector) {
        return findElements(parentElement::findElements, bySelector);
    }

    public void waitUntilTrue(final Predicate predicate) {

        pollWithTimeout().until(webDriver -> {
            try {
                return predicate.executeWithPredicate();
            } catch (final Exception exception) {
                return false;
            }
        });
    }

    public void waitUntilTrue(final WebDriver webDriver, final Predicate predicate) {

        pollWithTimeout(webDriver).until(driver -> {
            try {
                return predicate.executeWithPredicate();
            } catch (final Exception exception) {
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
                } catch (final Exception exception) {
                    lastException.set(exception);
                    return false;
                }
            });
        } catch (final TimeoutException exception) { // ignore TimeoutException
            throw new RuntimeException("Failed to execute task within timeout.", lastException.get());
        }
    }

    public void waitUntilVisible(WebElement webElement) {
        try {
            pollWithTimeout().until((WebDriver innerDriver) -> webElement.isDisplayed());
        } catch (TimeoutException exception) {
            throw new RuntimeException("Timeout while waiting for element to become visible: '" + webElement + "'", exception);
        }
    }

    /**
     * This takes care of element being removed/refreshed from DOM
     */
    public <T> T waitForElementUntil(ExpectedCondition<T> condition) {
        return pollWithTimeout()
            .until(ExpectedConditions.refreshed(condition));
    }

    private FluentWait<WebDriver> pollWithTimeout(WebDriver webDriver) {
        return new WebDriverWait(webDriver, TIME_OUT)
            .ignoring(StaleElementReferenceException.class)
            .pollingEvery(Duration.ofMillis(500));
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

    public WebElement findOptionalElement(WebDriver webDriver, By by) {
        return firstOrNull(webDriver.findElements(by));
    }

    public WebElement findOptionalElement(By by) {
        return findOptionalElement(getWebDriver(), by);
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
        try {
            executeWhenStable(() -> findElement(by).click());
        } catch (RuntimeException re) {
            log.error("Failed to click an element, will try once more: " + re.toString());
            executeWhenStable(() -> findElement(by).click());
        }
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
