package uk.nhs.digital.common.components.catalogue.filters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThrows;
import static uk.nhs.digital.common.components.catalogue.filters.FiltersTestUtils.*;

import org.junit.Before;
import org.junit.Test;

public class JacksonFiltersFactoryTest {

    private JacksonFiltersFactory filtersFactory;

    @Before
    public void setUp() throws Exception {
        filtersFactory = new JacksonFiltersFactory();
    }

    @Test
    public void filtersFrom_throwsException_whenNullYamlProvided() {

        // given
        final String nullYaml = null;

        // when
        final RuntimeException actualException = assertThrows(
            "Exception thrown on null YAML.",
            RuntimeException.class,
            () -> filtersFactory.filtersFromMappingYaml(nullYaml)
        );

        // then
        assertThat("Failure message is provided.",
            actualException.getMessage(),
            is("Failed to construct Filters model from YAML: " + nullYaml)
        );

        final Throwable actualCause = actualException.getCause();
        assertThat("Failure cause exception is included",
            actualCause,
            is(notNullValue())
        );

        assertThat("Failure cause exception clarifies the reason.",
            actualCause.getMessage(),
            is("YAML content is required but none was provided.")
        );
    }

    @Test
    public void filtersFrom_throwsException_whenInvalidYamlProvided() {

        // given
        final String invalidYaml = "invalid yaml";

        // when
        final RuntimeException actualException = assertThrows(
            "Exception thrown on invalid YAML.",
            RuntimeException.class,
            () -> filtersFactory.filtersFromMappingYaml(invalidYaml)
        );

        // then
        assertThat("Failure message is provided.",
            actualException.getMessage(),
            is("Failed to construct Filters model from YAML: " + invalidYaml)
        );

        final Throwable actualCause = actualException.getCause();
        assertThat("Failure cause exception is included",
            actualCause,
            is(notNullValue())
        );

        assertThat("Failure cause exception clarifies the reason.",
            actualCause.getMessage(),
            startsWith("Cannot construct instance of `" + Filters.class.getName() + "`")
        );
    }
}