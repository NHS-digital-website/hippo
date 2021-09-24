package uk.nhs.digital.common.components.apicatalogue.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.util.Objects;

public class JacksonFiltersFactory implements FiltersFactory {

    @Override public Filters filtersFromMappingYaml(final String yaml) {
        try {
            Objects.requireNonNull(yaml, "YAML content is required but none was provided.");

            final ObjectMapper objectMapper = new ObjectMapper(YAMLFactory.builder().build());

            return objectMapper.readValue(yaml, Filters.class);

        } catch (final Exception cause) {
            throw new RuntimeException("Failed to construct Filters model from YAML: " + yaml, cause);
        }
    }
}
