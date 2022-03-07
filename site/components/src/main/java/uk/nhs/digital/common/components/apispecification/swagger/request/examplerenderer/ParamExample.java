package uk.nhs.digital.common.components.apispecification.swagger.request.examplerenderer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import uk.nhs.digital.common.components.apispecification.swagger.request.bodyextractor.ToPrettyJsonStringDeserializer;

import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
class ParamExample {
    // subset of https://swagger.io/specification/#example-object

    private String summary;

    private String description;

    @JsonDeserialize(using = ToPrettyJsonStringDeserializer.class)
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
