package uk.nhs.digital.common.components.catalogue;

import org.hippoecm.hst.core.component.HstRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.components.catalogue.filters.Filters;

import java.util.Optional;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class CatalogueFilterManager {

    private static final Logger logger = LoggerFactory.getLogger(CatalogueFilterManager.class);
    private final String documentPath;

    public CatalogueFilterManager(String documentPath) {
        this.documentPath = documentPath;
    }

    public Filters getRawFilters(HstRequest request) {
        return rawFilters(sessionFrom(request), this.documentPath);
    }

    Session sessionFrom(final HstRequest request) {
        try {
            return request.getRequestContext().getSession();
        } catch (final RepositoryException e) {
            throw new RuntimeException("Failed to obtain session from request.", e);
        }
    }

    Filters rawFilters(Session session, String taxonomyFilters) {
        try {
            return taxonomyKeysToFiltersMappingYaml(session, taxonomyFilters)
                .map(mappingYaml -> CatalogueContext.filtersFactory().filtersFromMappingYaml(mappingYaml)).orElse(Filters.emptyInstance());
        } catch (final Exception e) {
            logger.error("Failed to generate Filters model.", e);
        }
        return Filters.emptyInstance();
    }

    Optional<String> taxonomyKeysToFiltersMappingYaml(final Session session, String taxonomyFilters) {

        try {
            return CatalogueContext.catalogueRepository(session, taxonomyFilters).taxonomyFiltersMapping();

        } catch (final Exception e) {
            logger.error("Failed to retrieve Taxonomy-Filters mapping YAML.", e);
        }
        return Optional.empty();
    }

}
