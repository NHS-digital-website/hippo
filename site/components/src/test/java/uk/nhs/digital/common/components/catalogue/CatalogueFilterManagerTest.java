package uk.nhs.digital.common.components.catalogue;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import uk.nhs.digital.common.components.catalogue.filters.Filters;

import java.util.Optional;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

@RunWith(MockitoJUnitRunner.class)
public class CatalogueFilterManagerTest {

    @Mock
    private HstRequest mockRequest;

    @Mock
    private HstRequestContext mockRequestContext;

    @Mock
    private Session mockSession;

    private CatalogueFilterManager catalogueFilterManager;

    private static final String DOCUMENT_PATH = "mocked/document/path";

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        catalogueFilterManager = spy(new CatalogueFilterManager(DOCUMENT_PATH));
        when(mockRequest.getRequestContext()).thenReturn(mockRequestContext);
    }

    @Test
    public void testGetRawFilters_successful() throws RepositoryException {
        // Arrange
        Filters mockFilters = mock(Filters.class);
        doReturn(mockSession).when(catalogueFilterManager).sessionFrom(mockRequest);
        doReturn(mockFilters).when(catalogueFilterManager).rawFilters(mockSession, DOCUMENT_PATH);

        // Act
        Filters result = catalogueFilterManager.getRawFilters(mockRequest);

        // Assert
        assertNotNull(result);
        assertEquals(mockFilters, result);
        verify(catalogueFilterManager).sessionFrom(mockRequest);
        verify(catalogueFilterManager).rawFilters(mockSession, DOCUMENT_PATH);
    }

    @Test
    public void testSessionFrom_successful() throws RepositoryException {
        // Arrange
        when(mockRequestContext.getSession()).thenReturn(mockSession);

        // Act
        Session result = catalogueFilterManager.sessionFrom(mockRequest);

        // Assert
        assertNotNull(result);
        assertEquals(mockSession, result);
        verify(mockRequestContext).getSession();
    }

    @Test(expected = RuntimeException.class)
    public void testSessionFrom_repositoryException() throws RepositoryException {
        // Arrange
        when(mockRequestContext.getSession()).thenThrow(new RepositoryException("Repository error"));

        // Act
        catalogueFilterManager.sessionFrom(mockRequest);
    }

    @Test
    public void testRawFilters_withoutMappingYaml() {
        // Arrange
        doReturn(Optional.empty())
            .when(catalogueFilterManager)
            .taxonomyKeysToFiltersMappingYaml(mockSession, DOCUMENT_PATH);

        // Act
        Filters result = catalogueFilterManager.rawFilters(mockSession, DOCUMENT_PATH);

        // Assert
        assertNotNull(result);
        assertEquals(Filters.emptyInstance(), result);
        verify(catalogueFilterManager).taxonomyKeysToFiltersMappingYaml(mockSession, DOCUMENT_PATH);
    }

}