package uk.nhs.digital.apispecs.swagger.request.bodyextractor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.codegen.v3.CodegenParameter;

import java.util.Optional;

public class ParameterBodyComponentsExtractor {

    private final ObjectMapper jsonObjectMapper = new ObjectMapper()
        .configure(MapperFeature.USE_ANNOTATIONS, true);

    public Optional<RequestBody> extractBody(final CodegenParameter requestParameter) {

        try {
            if (requestParameter.getIsBodyParam()) {
                final RequestBody requestBody = from(requestParameter.getJsonSchema());
                return Optional.ofNullable(requestBody);
            }

            return Optional.empty();

        } catch (final Exception e) {
            throw new RuntimeException("Failed to extract request body model of parameter " + requestParameter, e);
        }
    }

    private RequestBody from(final String parameterJsonDefinition) throws JsonProcessingException {
        return jsonObjectMapper.readValue(parameterJsonDefinition, RequestBody.class);
    }
}
