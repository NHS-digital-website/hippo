package uk.nhs.digital.apispecs.swagger.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.codegen.v3.CodegenParameter;
import io.swagger.codegen.v3.CodegenResponse;

import java.util.Optional;

public class BodyWithMediaTypesExtractor {

    private final ObjectMapper jsonObjectMapper = new ObjectMapper()
        .configure(MapperFeature.USE_ANNOTATIONS, true);

    public Optional<BodyWithMediaTypeObjects> extractBody(final CodegenParameter requestParameter) {
        if (!requestParameter.getIsBodyParam()) {
            return Optional.empty();
        }

        return getBodyWithMediaTypeObjects(
            requestParameter.getJsonSchema(),
            "request parameter " + requestParameter
        );
    }

    public Optional<BodyWithMediaTypeObjects> extractBody(final CodegenResponse codegenResponse) {

        return getBodyWithMediaTypeObjects(
            codegenResponse.getJsonSchema(),
            "response " + codegenResponse
        );
    }

    private Optional<BodyWithMediaTypeObjects> extractBody(final String jsonSchema) throws JsonProcessingException {
        return Optional.ofNullable(jsonObjectMapper.readValue(jsonSchema, BodyWithMediaTypeObjects.class));
    }

    private Optional<BodyWithMediaTypeObjects> getBodyWithMediaTypeObjects(
        final String jsonSchema,
        final String errorMessageObjectDescription
    ) {

        try {
            return extractBody(jsonSchema);

        } catch (final Exception e) {
            final String errorMessage = String.format("Failed to extract body model of %s", errorMessageObjectDescription);

            throw new RuntimeException(errorMessage, e);
        }
    }
}
