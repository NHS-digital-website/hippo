package uk.nhs.digital.common.components.catalogue.repository;

import java.util.Optional;

public interface CatalogueRepository {

    Optional<String> taxonomyFiltersMapping();
}
