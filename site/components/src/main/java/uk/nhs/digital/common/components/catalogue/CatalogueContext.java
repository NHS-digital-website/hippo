package uk.nhs.digital.common.components.catalogue;

import uk.nhs.digital.common.components.catalogue.filters.FiltersFactory;
import uk.nhs.digital.common.components.catalogue.filters.JacksonFiltersFactory;
import uk.nhs.digital.common.components.catalogue.repository.CatalogueJcrRepository;
import uk.nhs.digital.common.components.catalogue.repository.CatalogueRepository;

import java.util.function.Supplier;
import javax.jcr.Session;

public class CatalogueContext {

    private static Supplier<FiltersFactory> filtersFactorySupplier = JacksonFiltersFactory::new;

    public static CatalogueRepository catalogueRepository(final Session session, String taxonomyFiltersMappingDocumentPath) {
        return new CatalogueJcrRepository(session, taxonomyFiltersMappingDocumentPath);
    }

    public static FiltersFactory filtersFactory() {
        return filtersFactorySupplier.get();
    }
}
