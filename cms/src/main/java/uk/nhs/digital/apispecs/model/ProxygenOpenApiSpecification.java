package uk.nhs.digital.apispecs.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import uk.nhs.digital.apispecs.OpenApiSpecificationRepository;

import java.beans.ConstructorProperties;
import java.time.Instant;
import java.util.Optional;
import java.util.function.Supplier;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProxygenOpenApiSpecification {

    private final String specId;
    private final String lastModified;
    private String specJson;
    private Supplier<String> lazySpecJsonSupplier;

    @ConstructorProperties({"spec_id", "last_modified"})
    public ProxygenOpenApiSpecification(final String specId, final String lastModified) {
        this.specId = specId;
        this.lastModified = lastModified;
    }

    public String getSpecId() {
        return specId;
    }

    public Instant getLastModified() {
        return Instant.parse(lastModified);
    }

    public String getLastModifiedString() {
        return lastModified;
    }

    public void setService(final OpenApiSpecificationRepository openApiSpecificationRepository) {
        lazySpecJsonSupplier = () -> openApiSpecificationRepository.apiSpecificationJsonForSpecId(getSpecId());
    }

    public Optional<String> getSpecJson() {

        if (specJson == null) {
            specJson = lazySpecJsonSupplier.get();
        }

        return Optional.ofNullable(specJson);
    }
}
