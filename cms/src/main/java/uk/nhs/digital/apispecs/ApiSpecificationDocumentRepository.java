package uk.nhs.digital.apispecs;

import uk.nhs.digital.apispecs.model.ApiSpecificationDocument;
import uk.nhs.digital.apispecs.model.SpecificationSyncData;

import java.util.List;

public interface ApiSpecificationDocumentRepository {

    /**
     * @return A data subset for all API specification documents required to
     * determine initial eligibility for spec publication without loading in the
     * full JSON spec.
     * Returns an empty collection if none found; never {@code null}.
     */
    List<SpecificationSyncData> findInitialSyncDataForAllApiSpecifications();

    /**
     * @return A single API Specification document found in the system using its
     * handle node JCR ID.
     * @throws RuntimeException API Specification for given handle node ID does not
     * exist
     */
    ApiSpecificationDocument findApiSpecification(String specHandleNodeId);
}
