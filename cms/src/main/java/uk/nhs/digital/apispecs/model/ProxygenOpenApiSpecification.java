package uk.nhs.digital.apispecs.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.beans.ConstructorProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProxygenOpenApiSpecification {

    private final String specId;
    private final String lastModified;

    @ConstructorProperties({"spec_id", "last_modified"})
    public ProxygenOpenApiSpecification(final String specId, final String lastModified) {
        this.specId = specId;
        this.lastModified = lastModified;
    }

    public String getSpecId() {
        return specId;
    }

    public String getLastModified() {
        return lastModified;
    }

}
