package uk.nhs.digital.apispecs.swagger.request.examplerenderer;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
class RequestParamDefinition {

    // subset of https://swagger.io/specification/#parameter-object

    private String example;
    private Map<String, ParamExample> examples;

    private RequestParamSchema schema;

    public Optional<String> getExample() {
        return Optional.ofNullable(example);
    }

    public Map<String, ParamExample> getExamples() {
        return examples == null ? emptyMap() : unmodifiableMap(examples);
    }

    public Optional<RequestParamSchema> getSchema() {
        return Optional.ofNullable(schema);
    }
}
