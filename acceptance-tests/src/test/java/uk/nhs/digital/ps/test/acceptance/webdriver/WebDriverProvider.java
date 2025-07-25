package uk.nhs.digital.ps.test.acceptance.webdriver;

import static org.slf4j.LoggerFactory.getLogger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;

import java.time.Duration;

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

    private static final Logger log = getLogger(WebDriverProvider.class);

    private final WebDriverServiceProvider webDriverServiceProvider;

    /**
     * When 'true', the WebDriver will be operating in a headless mode, i.e. not displaying web browser window.
     */
    private final boolean isHeadlessMode;

    private WebDriver webDriver;


    public WebDriverProvider(final WebDriverServiceProvider webDriverServiceProvider,
                             final boolean isHeadlessMode) {
        this.webDriverServiceProvider = webDriverServiceProvider;
        this.isHeadlessMode = isHeadlessMode;
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

        final ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("window-size=1920,1080");
        //workaround for "DevToolsActivePort not found" error
        chromeOptions.addArguments("--remote-debugging-pipe");

        log.info("Configuring WebDriver to run in {} mode.", isHeadlessMode ? "headless" : "full, graphical");

        if (isHeadlessMode) {
            chromeOptions.addArguments("--headless");
        }

        chromeOptions.addArguments("--host-resolver-rules=MAP consent.cookiebot.com 127.0.0.1");

        log.info("Using WebDriver url '{}'.", webDriverServiceProvider.getUrl());
        webDriver = new RemoteWebDriver(webDriverServiceProvider.getUrl(), chromeOptions);
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

    /**
     * <p>
     * Creates and initialises a new instance of WebDriver leaving it to the caller to manage its
     * lifecycle.
     * </p><p>
     * Used in performance tests where there is a need to simulate concurrent users as each user
     * requires their own WebDriver instance.
     * </p><p>
     * The returned instance is configured with:
     * </p>
     * <ul>
     * <li>implicit wait of 5 minutes, which helps reducing amount of state-probing
     * code in performance tests,</li>
     * <li>download directory unique to the user, with name given by provided argument,
     * named with the provided user name and located under test download directory
     * configured via acceptance tests' config parameters.</li>
     * </ul>
     */
    public WebDriver newWebDriver(String sessionSpecificDownloadDirectoryName) {

        final ChromeOptions chromeOptions = new ChromeOptions();

        final ChromeDriver chromeDriver = new ChromeDriver(chromeOptions);
        chromeDriver.manage().timeouts().implicitlyWait(Duration.ofMinutes(5));

        return chromeDriver;
    }
}
