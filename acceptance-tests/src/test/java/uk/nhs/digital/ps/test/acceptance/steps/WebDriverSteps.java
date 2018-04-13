package uk.nhs.digital.ps.test.acceptance.steps;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import uk.nhs.digital.ps.test.acceptance.data.WebDriversRepo;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

/**
 * 'Infrastructure' class that allows propagating {@code cucumber-jvm} lifecycle events to the web driver.
 * This allows to re-set the web driver (and thus current user session) for each Cucumber scenario.
 */
public class WebDriverSteps extends AbstractSpringSteps {

    @Autowired
    private WebDriverProvider webDriverProvider;

    @Autowired
    private WebDriversRepo webDriversRepo;

    @Before
    public void initialise() {
        webDriverProvider.initialise();
    }

    // Low execution order ensures that the web driver is disposed after any other @After steps that may need it.
    @After(order = 0)
    public void dispose() {
        webDriverProvider.dispose();
        webDriversRepo.getAll().forEach(WebDriver::close);
    }
}
