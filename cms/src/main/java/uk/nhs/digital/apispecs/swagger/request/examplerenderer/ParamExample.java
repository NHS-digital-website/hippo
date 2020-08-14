package uk.nhs.digital.apispecs.swagger.request.examplerenderer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
class ParamExample {
    // subset of https://swagger.io/specification/#example-object

    private String summary;
    private String description;
    private String value;

    public Optional<String> getSummary() {
        return Optional.ofNullable(summary);
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public Optional<String> getValue() {
        return Optional.ofNullable(value);
    }
}
