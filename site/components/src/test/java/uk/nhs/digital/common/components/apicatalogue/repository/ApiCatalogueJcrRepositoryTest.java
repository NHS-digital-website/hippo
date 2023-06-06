package uk.nhs.digital.common.components.apicatalogue.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.onehippo.repository.mock.MockSession;
import uk.nhs.digital.common.beans.BaseDocument;
import uk.nhs.digital.test.util.JcrTestUtilsHst;

import java.io.IOException;
import java.util.Optional;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class ApiCatalogueJcrRepositoryTest {

    @Test
    public void taxonomyFiltersMapping_returnsConfigurationInYaml_whenAvailable() throws IOException, RepositoryException {

        // given
        final MockSession session = JcrTestUtilsHst.mockJcrRepo(
            "/test-data/ApiCatalogueJcrRepositoryTest/filters-taxonomy-mapping.yaml",
            ImmutableMap.of("website:taxonomymapping", BaseDocument.class),
            "//*["
                + "(@hippo:paths='cb7449f4-930c-4a51-9bfd-b0a562c8700e')"
                + " and (@hippo:availability='live')"
                + " and not(@jcr:primaryType='nt:frozenNode')"
                + " and (@hippostd:state = 'published')"
                + "]"
                + " order by @jcr:score descending ",
            "content/documents/administration/website/developer-hub/taxonomy-filters-mapping/taxonomy-filters-mapping[3]"
        );

        final String taxonomyPath = "/content/documents/administration/website/developer-hub/taxonomy-filters-mapping";

        final ApiCatalogueJcrRepository apiCatalogueJcrRepository = new ApiCatalogueJcrRepository(session, taxonomyPath);

        // when
        final String actualTaxonomyFiltersMappingYaml =
            apiCatalogueJcrRepository.taxonomyFiltersMapping().orElse("nothing was returned");

        // then
        assertThat("Taxonomy Filters Mapping Yaml was returned.",
            actualTaxonomyFiltersMappingYaml,
            is("some: 'yaml'")
        );
    }

    @Test
    public void taxonomyFiltersMapping_returnsEmpty_whenMappingDocumentNotAvailable() throws RepositoryException {

        // given
        final Session session = mock(Session.class);
        given(session.nodeExists(any())).willReturn(false);

        final ApiCatalogueJcrRepository apiCatalogueJcrRepository = new ApiCatalogueJcrRepository(session, "/content/documents/administration/website/developer-hub/taxonomy-filters-mapping");

        // when
        final Optional<String> actualTaxonomyFiltersMappingYaml = apiCatalogueJcrRepository.taxonomyFiltersMapping();

        // then
        assertThat("Empty optional is returned with no exception being thrown.",
            actualTaxonomyFiltersMappingYaml,
            is(Optional.empty())
        );
    }
}