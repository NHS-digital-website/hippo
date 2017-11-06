package uk.nhs.digital.ps.test.acceptance.steps;

import org.springframework.test.context.ContextConfiguration;
import uk.nhs.digital.ps.test.acceptance.config.AcceptanceTestConfiguration;

/**
 * Base class for all {@code cucumber-jvm} steps classes that want to benefit from Spring-based
 * dependency injection.
 */
@ContextConfiguration(classes = {AcceptanceTestConfiguration.class})
public abstract class AbstractSpringSteps {
    // no-op, this class only serves as a bridge to Spring test context configuration
}
