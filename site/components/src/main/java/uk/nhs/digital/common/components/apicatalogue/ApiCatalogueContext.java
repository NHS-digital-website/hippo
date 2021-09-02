package uk.nhs.digital.common.components.apicatalogue;

import uk.nhs.digital.common.components.apicatalogue.filters.FiltersFactory;
import uk.nhs.digital.common.components.apicatalogue.filters.JacksonFiltersFactory;
import uk.nhs.digital.common.components.apicatalogue.repository.ApiCatalogueJcrRepository;
import uk.nhs.digital.common.components.apicatalogue.repository.ApiCatalogueRepository;

import java.util.function.Function;
import java.util.function.Supplier;
import javax.jcr.Session;

public class ApiCatalogueContext {

    private static Function<Session, ApiCatalogueRepository> apiCatalogueRepositorySupplier
        = ApiCatalogueJcrRepository::new;

    private static Supplier<FiltersFactory> filtersFactorySupplier = JacksonFiltersFactory::new;

    public static ApiCatalogueRepository apiCatalogueRepository(final Session session) {
        return apiCatalogueRepositorySupplier.apply(session);
    }

    public static FiltersFactory filtersFactory() {
        return filtersFactorySupplier.get();
    }
}
