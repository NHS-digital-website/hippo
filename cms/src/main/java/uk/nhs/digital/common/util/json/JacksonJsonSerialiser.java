package uk.nhs.digital.common.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Uses Jackson library as the serialiser implementation.
 */
public class JacksonJsonSerialiser implements JsonSerialiser {

    private final ObjectMapper objectMapper;

    public JacksonJsonSerialiser(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String toJson(final Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (final JsonProcessingException ex) {
            throw new RuntimeException("Failed to serialise to JSON.", ex);
        }
    }
}
