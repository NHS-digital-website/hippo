package uk.nhs.digital.common.components.apicatalogue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import uk.nhs.digital.common.components.apicatalogue.filters.FiltersFactory;
import uk.nhs.digital.common.components.apicatalogue.repository.ApiCatalogueJcrRepository;
import uk.nhs.digital.common.components.apicatalogue.repository.ApiCatalogueRepository;
import uk.nhs.digital.test.util.ReflectionTestUtils;

import javax.jcr.Session;

public class ApiCatalogueContextTest {

    @Test
    public void filtersFactory_createsNewInstanceOfFiltersFactory() {

        // given
        // ApiCatalogueContext

        // when
        final FiltersFactory filtersFactory = ApiCatalogueContext.filtersFactory();

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

        // when
        final ApiCatalogueRepository actualRepository = ApiCatalogueContext.apiCatalogueRepository(expectedSession);

        // then
        assertThat("Repository is of expected type.",
            actualRepository,
            instanceOf(ApiCatalogueJcrRepository.class)
        );

        final Session actualSession = ReflectionTestUtils.readField(actualRepository, "session");
        assertThat("Factory passes Session to the repository instance.",
            actualSession,
            sameInstance(expectedSession)
        );
    }
}