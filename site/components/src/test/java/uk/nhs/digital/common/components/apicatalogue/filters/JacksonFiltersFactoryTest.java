package uk.nhs.digital.common.components.apicatalogue.filters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThrows;
import static uk.nhs.digital.common.components.apicatalogue.filters.FiltersTestUtils.*;

import org.junit.Before;
import org.junit.Test;

public class JacksonFiltersFactoryTest {

    private JacksonFiltersFactory filtersFactory;

    @Before
    public void setUp() throws Exception {
        filtersFactory = new JacksonFiltersFactory();
    }

    @Test
    public void filtersFrom_produceFiltersInstance_populatedFromYaml() {

        // given
        final String validFiltersYaml = String.join("\n",
            "sections:",
            "  - displayName: Section 5",
            "    entries:",
            "      - displayName: Subsection Z",
            "        taxonomyKey: key-z",
            "      - displayName: Subsection D",
            "        taxonomyKey: key-d",
            "        entries:",
            "          - displayName: Subsection S",
            "            taxonomyKey: key-s",
            "          - displayName: Subsection U",
            "            taxonomyKey: key-u",
            "  - displayName: Section 2",
            "    description: section multiline description",
            "      with <a href=\"https://digital.nhs.uk/developer\" class=\"nhsd-a-link\" target=\"_blank\">hyperlink</a>",
            "      more text",
            "    entries:",
            "      - displayName: Subsection 3",
            "        taxonomyKey: key-3",
            "      - displayName: Subsection I",
            "        taxonomyKey: key-i",
            "        entries:",
            "          - displayName: Subsection T",
            "            taxonomyKey: key-t",
            "            description: subsection multiline description",
            "              with <a href=\"https://digital.nhs.uk/developer\" class=\"nhsd-a-link\" target=\"_blank\">hyperlink</a>",
            "              more text",
            "          - displayName: Subsection E",
            "            taxonomyKey: key-e"
        );

        final Filters expectedFilters = filters(
            section("Section 5",
                subsection("Subsection Z", "key-z"),
                subsection("Subsection D", "key-d",
                    subsection("Subsection S", "key-s"),
                    subsection("Subsection U", "key-u")
                )
            ),
            section("Section 2",
                "section multiline description"
                    + " with <a href=\"https://digital.nhs.uk/developer\" class=\"nhsd-a-link\" target=\"_blank\">hyperlink</a>"
                    + " more text",
                subsection("Subsection 3", "key-3"),
                subsection("Subsection I", "key-i",
                    subsection("Subsection T", "key-t",
                        "subsection multiline description"
                            + " with <a href=\"https://digital.nhs.uk/developer\" class=\"nhsd-a-link\" target=\"_blank\">hyperlink</a>"
                            + " more text"),
                    subsection("Subsection E", "key-e")
                )
            )
        );

        // when
        final Filters actualFilters = filtersFactory.filtersFromMappingYaml(validFiltersYaml);

        // then
        assertThat("Produced filters match given YAML.",
            actualFilters,
            is(expectedFilters)
        );
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