package uk.nhs.digital.apispecs;

import uk.nhs.digital.apispecs.model.ApiSpecificationDocument;

import java.util.List;

public interface ApiSpecificationDocumentRepository {

    /**
     * @return All documents of type API Specifications found in the system or empty collection if none found; never
     * {@code null}.
     */
    List<ApiSpecificationDocument> findAllApiSpecifications();
}
