package uk.nhs.digital.ps.test.acceptance.webdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Manages WebDriver (the client component of the WebDriver).
 * <p/>
 * Together with {@linkplain WebDriverServiceProvider} implements
 * <a href="https://sites.google.com/a/chromium.org/chromedriver/getting-started#Controlling_'s_lifetime">
 * recommendations for performant use of the ChromeDriver</a>, where the WebDriver service is started and stopped
 * only once per testing session (costly operation, should be performed as few times as possible) with only the
 * WebDriver client being cycled with each test (cheap operation, can be performed frequently).
 */
public class WebDriverProvider {

    private final WebDriverServiceProvider webDriverServiceProvider;

    private final boolean isHeadless;

    private WebDriver webDriver;

    public WebDriverProvider(final WebDriverServiceProvider webDriverServiceProvider, final boolean isHeadless) {
        this.webDriverServiceProvider = webDriverServiceProvider;
        this.isHeadless = isHeadless;
    }

    public WebDriver getWebDriver() {
        if (webDriver == null) {
            throw new IllegalStateException(
                "WebDriver hasn't been initialised, yet. Have you forgotten to call 'initialise()' first?");
        }

        return webDriver;
    }

    /**
     * Initialises the WebDriver (client).
     *
     * This method should be called once before each test to ensure that the session state doesn't bleed from one test
     * to another (such as user being logged in).
     */
    public void initialise() {

        final DesiredCapabilities desiredCapabilities = DesiredCapabilities.chrome();

        if (isHeadless) {
            final ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--headless");
            desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        }

        webDriver = new RemoteWebDriver(webDriverServiceProvider.getUrl(), desiredCapabilities);
    }

    /**
     * Stops the WebDriver client.
     *
     * This method should be called once after each test to ensure that the session state doesn't bleed from one test
     * to another (such as user being logged in).
     */
    public void dispose() {
        webDriver.quit();
        webDriver = null;
    }
}
