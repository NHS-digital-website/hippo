package uk.nhs.digital.common.components.apicatalogue;

import uk.nhs.digital.common.components.apicatalogue.filters.FiltersFactory;
import uk.nhs.digital.common.components.apicatalogue.filters.JacksonFiltersFactory;
import uk.nhs.digital.common.components.apicatalogue.repository.ApiCatalogueJcrRepository;
import uk.nhs.digital.common.components.apicatalogue.repository.ApiCatalogueRepository;
import java.util.function.Supplier;
import javax.jcr.Session;

public class ApiCatalogueContext {

    private static Supplier<FiltersFactory> filtersFactorySupplier = JacksonFiltersFactory::new;

    public static ApiCatalogueRepository apiCatalogueRepository(final Session session, String taxonomyFiltersMappingDocumentPath) {
        return new ApiCatalogueJcrRepository(session, taxonomyFiltersMappingDocumentPath);
    }

    public static FiltersFactory filtersFactory() {
        return filtersFactorySupplier.get();
    }
}
