package uk.nhs.digital.apispecs;

import uk.nhs.digital.apispecs.apigee.ApigeeServiceException;
import uk.nhs.digital.apispecs.model.OpenApiSpecificationStatus;

import java.util.List;

public interface OpenApiSpecificationRepository {
    
    List<OpenApiSpecificationStatus> getSpecsStatuses() throws ApigeeServiceException;

    String getSpecification(String specificationId);
}
