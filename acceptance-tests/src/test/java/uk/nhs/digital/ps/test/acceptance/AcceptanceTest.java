package uk.nhs.digital.ps.test.acceptance;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Entry point required by {@code cucumber-jvm} to discover and run tests.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        plugin = {"pretty", "html:target/cucumber"}
        )
public class AcceptanceTest {
    // no-op, config class only
}
