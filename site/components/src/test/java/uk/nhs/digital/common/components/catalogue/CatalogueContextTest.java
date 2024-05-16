package uk.nhs.digital.common.components.catalogue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import uk.nhs.digital.common.components.catalogue.filters.FiltersFactory;
import uk.nhs.digital.common.components.catalogue.repository.CatalogueJcrRepository;
import uk.nhs.digital.common.components.catalogue.repository.CatalogueRepository;
import uk.nhs.digital.test.util.ReflectionTestUtils;

import javax.jcr.Session;

public class CatalogueContextTest {

    @Test
    public void filtersFactory_createsNewInstanceOfFiltersFactory() {

        // given
        // ApiCatalogueContext

        // when
        final FiltersFactory filtersFactory = CatalogueContext.filtersFactory();

        // then
        assertThat("Returns an instance of filters factory.",
            filtersFactory,
            is(notNullValue())
        );
    }

    @Test
    public void repository_createsNewApiCatalogueRepositoryInstance() {

        // given
        final Session expectedSession = mock(Session.class);
        final String taxonomyPath = "/content/documents/administration/website/developer-hub/taxonomy-filters-mapping";

        // when
        final CatalogueRepository actualRepository = CatalogueContext.catalogueRepository(expectedSession, taxonomyPath);

        // then
        assertThat("Repository is of expected type.",
            actualRepository,
            instanceOf(CatalogueJcrRepository.class)
        );

        final Session actualSession = ReflectionTestUtils.readField(actualRepository, "session");
        assertThat("Factory passes Session to the repository instance.",
            actualSession,
            sameInstance(expectedSession)
        );
    }
}