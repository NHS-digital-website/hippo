package uk.nhs.digital.apispecs.swagger.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
class RequestParamSchema {
    // subset of https://swagger.io/specification/#schema-object
    private String example;

    public Optional<String> getExample() {
        return Optional.ofNullable(example);
    }
}
