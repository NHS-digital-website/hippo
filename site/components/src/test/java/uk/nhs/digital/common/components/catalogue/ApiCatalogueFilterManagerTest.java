package uk.nhs.digital.common.components.catalogue;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import uk.nhs.digital.common.components.catalogue.filters.Filters;
import uk.nhs.digital.common.components.catalogue.filters.FiltersFactory;
import uk.nhs.digital.common.components.catalogue.repository.CatalogueRepository;

import java.util.Optional;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * Test class for ApiCatalogueFilterManager.
 * <p>
 * This class uses PowerMockRunner to run tests with mocked dependencies to isolate and test
 * the behavior of ApiCatalogueFilterManager. The tests cover the following functionalities:
 * <p>
 * - Retrieve raw filters successfully using the given HstRequest.
 * - Obtain a valid session from the request context.
 * - Handle RepositoryException when obtaining a session from the request context.
 * - Verify interactions with the request context.
 * - Create filters successfully from a YAML mapping.
 * - Return empty filters when no YAML mapping is found.
 * - Handle exceptions during filters creation and log errors.
 * - Retrieve YAML mapping for taxonomy keys successfully.
 * - Handle exceptions during YAML mapping retrieval and log errors.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({CatalogueContext.class, ApiCatalogueFilterManager.class})
public class ApiCatalogueFilterManagerTest {

    private static final String TAXONOMY_FILTERS_MAPPING_PATH = "/content/documents/administration/website/developer-hub/taxonomy-filters-mapping";
    @InjectMocks
    private ApiCatalogueFilterManager apiCatalogueFilterManager;
    @Mock
    private CatalogueRepository catalogueRepository;
    @Mock
    private HstRequest hstRequest;
    @Mock
    private HstRequestContext hstRequestContext;
    @Mock
    private Session session;
    @Mock
    private Logger logger;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        PowerMockito.mockStatic(CatalogueContext.class);
    }

    @Test
    public void testGetRawFiltersSuccessfully() throws RepositoryException {
        // Arrange
        when(hstRequest.getRequestContext()).thenReturn(hstRequestContext);
        when(hstRequestContext.getSession()).thenReturn(session);

        Filters expectedFilters = Filters.emptyInstance();
        ApiCatalogueFilterManager manager = new ApiCatalogueFilterManager() {
            @Override
            protected Filters rawFilters(Session session, String taxonomyFilters, Logger logger) {
                return expectedFilters;
            }
        };

        // Act
        Filters filters = manager.getRawFilters(hstRequest);

        // Assert
        verify(hstRequest, times(1)).getRequestContext();
        verify(hstRequestContext, times(1)).getSession();

        assertNotNull(filters);
        assertEquals(expectedFilters, filters);
    }

    @Test
    public void sessionFrom_ValidRequest_ReturnsSession() throws RepositoryException {
        // Arrange
        Mockito.when(hstRequest.getRequestContext()).thenReturn(hstRequestContext);
        Mockito.when(hstRequestContext.getSession()).thenReturn(session);

        // Act
        Session resultSession = apiCatalogueFilterManager.sessionFrom(hstRequest);

        // Assert
        assertEquals("The session should be returned from the request context.", session, resultSession);
    }

    @Test
    public void sessionFrom_RequestContextThrowsRepositoryException_RuntimeExceptionThrown() throws RepositoryException {
        // Arrange
        RepositoryException exception = new RepositoryException("Test exception");
        Mockito.when(hstRequest.getRequestContext()).thenReturn(hstRequestContext);
        Mockito.when(hstRequestContext.getSession()).thenThrow(exception);

        // Act
        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
            apiCatalogueFilterManager.sessionFrom(hstRequest);
        });

        // Assert
        assertEquals("Failed to obtain session from request.", thrownException.getMessage());
        assertEquals(exception, thrownException.getCause());
    }

    @Test
    public void sessionFrom_RequestContextInteraction() throws RepositoryException {
        // Arrange
        Mockito.when(hstRequest.getRequestContext()).thenReturn(hstRequestContext);
        Mockito.when(hstRequestContext.getSession()).thenReturn(session);

        // Act
        apiCatalogueFilterManager.sessionFrom(hstRequest);

        // Assert
        Mockito.verify(hstRequest).getRequestContext();
        Mockito.verify(hstRequestContext).getSession();
    }

    @Test
    public void rawFilters_successfulFiltersCreation() {
        // Arrange
        Session session = mock(Session.class);
        Logger logger = mock(Logger.class);

        String yamlMapping = "taxonomy mapping in YAML format";
        Filters expectedFilters = mock(Filters.class);

        FiltersFactory filtersFactory = mock(FiltersFactory.class);
        Mockito.when(CatalogueContext.filtersFactory()).thenReturn(filtersFactory);
        Mockito.when(filtersFactory.filtersFromMappingYaml(yamlMapping)).thenReturn(expectedFilters);

        ApiCatalogueFilterManager apiCatalogueFilterManager = new ApiCatalogueFilterManager() {
            @Override
            protected Optional<String> taxonomyKeysToFiltersMappingYaml(Session session, String path, Logger logger) {
                return Optional.of(yamlMapping);
            }
        };

        // Act
        Filters result = apiCatalogueFilterManager.rawFilters(session, TAXONOMY_FILTERS_MAPPING_PATH, logger);

        // Assert
        assertEquals(expectedFilters, result);
    }

    @Test
    public void rawFilters_emptyFiltersWhenNoMappingFound() {
        // Arrange
        Session session = mock(Session.class);
        Logger logger = mock(Logger.class);

        Filters emptyFilters = new Filters();

        FiltersFactory filtersFactory = mock(FiltersFactory.class);
        Mockito.when(CatalogueContext.filtersFactory()).thenReturn(filtersFactory);
        Mockito.when(filtersFactory.filtersFromMappingYaml("")).thenReturn(emptyFilters);

        ApiCatalogueFilterManager apiCatalogueFilterManager = new ApiCatalogueFilterManager() {
            @Override
            protected Optional<String> taxonomyKeysToFiltersMappingYaml(Session session, String path, Logger logger) {
                return Optional.empty();
            }
        };

        // Act
        Filters result = apiCatalogueFilterManager.rawFilters(session, TAXONOMY_FILTERS_MAPPING_PATH, logger);

        // Assert
        assertEquals(emptyFilters, result);
    }

    @Test
    public void rawFilters_handlesExceptionsDuringFiltersCreation() {
        // Arrange
        Session session = mock(Session.class);
        Logger logger = mock(Logger.class);

        Filters emptyFilters = new Filters();

        FiltersFactory filtersFactory = mock(FiltersFactory.class);
        Mockito.when(CatalogueContext.filtersFactory()).thenReturn(filtersFactory);
        Mockito.when(filtersFactory.filtersFromMappingYaml(anyString())).thenThrow(RuntimeException.class);

        ApiCatalogueFilterManager apiCatalogueFilterManager = new ApiCatalogueFilterManager() {
            @Override
            protected Optional<String> taxonomyKeysToFiltersMappingYaml(Session session, String path, Logger logger) {
                return Optional.of("taxonomy mapping in YAML format");
            }
        };

        // Act
        Filters result = apiCatalogueFilterManager.rawFilters(session, TAXONOMY_FILTERS_MAPPING_PATH, logger);

        // Assert
        assertEquals(emptyFilters, result);
        verify(logger).error(anyString(), any(RuntimeException.class));
    }

    @Test
    public void taxonomyKeysToFilter_SuccessfulYamlMappingRetrieval() {
        // Arrange
        String taxonomyFilters = "someFilter";
        String expectedYaml = "someYaml";

        when(CatalogueContext.catalogueRepository(session, taxonomyFilters)).thenReturn(catalogueRepository);
        when(catalogueRepository.taxonomyFiltersMapping()).thenReturn(Optional.of(expectedYaml));

        ApiCatalogueFilterManager apiCatalogueFilterManager = new ApiCatalogueFilterManager();

        // Act
        Optional<String> result = apiCatalogueFilterManager.taxonomyKeysToFiltersMappingYaml(session, taxonomyFilters, logger);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedYaml, result.get());
        verify(logger, never()).error(anyString(), any(Exception.class));
    }

    @Test
    public void taxonomyKeysToFilter_YamlMappingRetrievalWithException() {
        // Arrange
        String taxonomyFilters = "someFilter";
        String expectedErrorMessage = "Failed to retrieve Taxonomy-Filters mapping YAML";

        when(CatalogueContext.catalogueRepository(session, taxonomyFilters)).thenThrow(new RuntimeException("Simulated exception"));

        // Act
        Optional<String> result = apiCatalogueFilterManager.taxonomyKeysToFiltersMappingYaml(session, taxonomyFilters, logger);

        // Assert
        assertFalse(result.isPresent());
        verify(logger).error(contains(expectedErrorMessage), any(RuntimeException.class));
    }
}
