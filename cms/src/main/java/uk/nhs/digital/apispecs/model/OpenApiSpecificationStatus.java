package uk.nhs.digital.apispecs.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.beans.ConstructorProperties;
import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenApiSpecificationStatus {

    private final String id;
    private final String modified;

    @ConstructorProperties({"id", "modified"})
    public OpenApiSpecificationStatus(final String id, final String modified) {
        this.id = id;
        this.modified = modified;
    }

    public String getId() {
        return id;
    }

    public Instant getModified() {
        return Instant.parse(modified);
    }

}
