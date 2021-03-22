package uk.nhs.digital.apispecs;

import uk.nhs.digital.apispecs.model.OpenApiSpecification;

import java.util.List;

public interface OpenApiSpecificationRepository {
    
    List<OpenApiSpecification> apiSpecificationStatuses() throws OpenApiSpecificationRepositoryException;

    String apiSpecificationJsonForSpecId(String specificationId);
}
