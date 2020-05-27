package uk.nhs.digital.apispecs.model;

import java.time.Instant;

public class OpenApiSpecificationStatus {

    private String id;
    private Instant modified;

    public void setId(final String id) {
        this.id = id;
    }

    public void setModified(final Instant modified) {
        this.modified = modified;
    }

    public String getId() {
        return id;
    }

    public Instant getModified() {
        return modified;
    }

}
