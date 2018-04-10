package uk.nhs.digital.ps.test.acceptance.webdriver;

import static java.nio.file.Files.isDirectory;

import org.openqa.selenium.chrome.ChromeDriverService;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    /**
     * Location where the WebDriver is installed by {@code webdriverextensions-maven-plugin},
     * relative to the directory of the current working directory (i.e. that of current Maven module).
     */
    private static final String WEB_DRIVER_LOCATION = "drivers";

    private ChromeDriverService chromeDriverService;

    /**
     * Starts WebDriver service.
     *
     * For perfomance reasons, this method should be called once before the entire testing session rather than
     * before each test.
     */
    public void initialise() {
        chromeDriverService = new ChromeDriverService.Builder()
            .usingAnyFreePort()
            .usingDriverExecutable(getChromedriverFileLocation())
            .build();

        try {
            chromeDriverService.start();
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    /**
     * Stops WebDriver service.
     *
     * For perfomance reasons, this method should be called once at the end of the entire testing session rather than
     * after each test.
     */
    public void dispose() {
        chromeDriverService.stop();
    }

    /**
     * Resolves actual name of the web driver file downloaded. {@code webdriverextensions-maven-plugin}
     * downloads the driver binary files and saves them in files named after specific driver type,
     * and OS. Since there are many OSs used within the team, we cannot be sure what the actual file
     * name will end up to be, and so, we need to resolve it dynamically.
     *
     * @return Actual name of the downloaded WebDriver binary file.
     */
    private File getChromedriverFileLocation() {

        final Path webDriverLocationPath = Paths.get(WEB_DRIVER_LOCATION).toAbsolutePath();

        if (!isDirectory(webDriverLocationPath)) {
            throw new IllegalStateException("Expected to find a directory with downloaded web driver binaries at "
                + webDriverLocationPath);
        }

        final File[] candidateFiles = webDriverLocationPath.toFile().listFiles((dir, name) -> !name.endsWith(".version"));

        if (candidateFiles == null || candidateFiles.length != 1) {
            throw new IllegalStateException("Expected exactly one web driver binary file to be available in "
                + webDriverLocationPath);
        }

        return candidateFiles[0];
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
