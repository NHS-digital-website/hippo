package uk.nhs.digital.apispecs.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import uk.nhs.digital.apispecs.model.OpenApiSpecification;

import java.beans.ConstructorProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenApiSpecifications {

    private final List<OpenApiSpecification> contents;

    @ConstructorProperties({"contents"})
    public OpenApiSpecifications(final List<OpenApiSpecification> contents) {
        this.contents = contents;
    }

    public List<OpenApiSpecification> getContents() {
        return contents;
    }
}
