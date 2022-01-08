package uk.nhs.digital.common.components.apispecification.swagger.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

public class BodyWithMediaTypesExtractor {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
        .configure(MapperFeature.USE_ANNOTATIONS, true);

    public Optional<BodyWithMediaTypeObjects> bodyObjectFromJsonSchema(final String jsonSchema, final String elementType) {

        try {
            return extractBody(jsonSchema);

        } catch (final Exception e) {
            final String errorMessage = String.format("Failed to extract body model of %s", elementType);

            throw new RuntimeException(errorMessage, e);
        }
    }

    private Optional<BodyWithMediaTypeObjects> extractBody(final String jsonSchema) throws JsonProcessingException {
        return Optional.ofNullable(OBJECT_MAPPER.readValue(jsonSchema, BodyWithMediaTypeObjects.class));
    }
}
