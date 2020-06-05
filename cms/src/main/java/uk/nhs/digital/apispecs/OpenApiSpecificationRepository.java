package uk.nhs.digital.apispecs;

import uk.nhs.digital.apispecs.model.OpenApiSpecificationStatus;

import java.util.List;

public interface OpenApiSpecificationRepository {
    
    List<OpenApiSpecificationStatus> apiSpecificationStatuses() throws OpenApiSpecificationRepositoryException;

    String apiSpecificationJsonForSpecId(String specificationId);
}
