package uk.nhs.digital.ps.test.acceptance.webdriver;

import org.openqa.selenium.chrome.ChromeDriverService;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;

/**
 * Manages WebDriver service (the server component of WebDriver).
 * <p/>
 * Together with {@linkplain WebDriverProvider} implements
 * <a href="https://sites.google.com/a/chromium.org/chromedriver/getting-started#Controlling_'s_lifetime">
 * recommendations for performant use of the ChromeDriver</a>, where the WebDriver service is started and stopped
 * only once per testing session (costly operation, should be performed as few times as possible) with only the
 * WebDriver client being cycled with each test (cheap operation, can be performed frequently).
 */
public class WebDriverServiceProvider {

    private ChromeDriverService chromeDriverService;

    /**
     * Starts WebDriver service.
     * <p>
     * For perfomance reasons, this method should be called once before the entire testing session rather than
     * before each test.
     */
    public void initialise() {
        chromeDriverService = new ChromeDriverService.Builder()
            .usingAnyFreePort()
            .build();

        try {
            chromeDriverService.start();
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    /**
     * Stops WebDriver service.
     * <p>
     * For perfomance reasons, this method should be called once at the end of the entire testing session rather than
     * after each test.
     */
    public void dispose() {
        chromeDriverService.stop();
    }

    /**
     * @return URL at which the WebDriver service is available to the WebDriver client.
     */
    URL getUrl() {
        return getChromeDriverService().getUrl();
    }

    private ChromeDriverService getChromeDriverService() {
        if (chromeDriverService == null) {
            throw new IllegalStateException(
                "WebDriverService hasn't been initialised, yet. Have you forgotten to call 'initialise()' first?");
        }

        return chromeDriverService;
    }
}
