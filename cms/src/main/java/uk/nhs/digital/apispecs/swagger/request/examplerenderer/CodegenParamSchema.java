package uk.nhs.digital.apispecs.swagger.request.examplerenderer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import uk.nhs.digital.apispecs.swagger.request.bodyextractor.ToPrettyJsonStringDeserializer;

import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
class CodegenParamSchema {
    // subset of https://swagger.io/specification/#schema-object

    @JsonDeserialize(using = ToPrettyJsonStringDeserializer.class)
    private String example;

    public Optional<String> getExample() {
        return Optional.ofNullable(example);
    }
}
