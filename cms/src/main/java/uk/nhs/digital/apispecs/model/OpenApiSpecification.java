package uk.nhs.digital.apispecs.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import uk.nhs.digital.apispecs.OpenApiSpecificationRepository;

import java.beans.ConstructorProperties;
import java.time.Instant;
import java.util.Optional;
import java.util.function.Supplier;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenApiSpecification {

    private final String id;
    private final String modified;
    private String specJson;
    private Supplier<String> specJsonSupplier;

    @ConstructorProperties({"id", "modified"})
    public OpenApiSpecification(final String id, final String modified) {
        this.id = id;
        this.modified = modified;
    }

    public String getId() {
        return id;
    }

    public Instant getModified() {
        return Instant.parse(modified);
    }

    public void setApigeeService(final OpenApiSpecificationRepository openApiSpecificationRepository) {
        specJsonSupplier = () -> openApiSpecificationRepository.apiSpecificationJsonForSpecId(getId());
    }

    public Optional<String> getSpecJson() {

        if (specJson == null) {
            specJson = specJsonSupplier.get();
        }

        return Optional.ofNullable(specJson);
    }
}
