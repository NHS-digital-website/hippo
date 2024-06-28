package uk.nhs.digital.common.components.catalogue;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.hippoecm.hst.core.component.HstRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.components.catalogue.filters.Filters;

import java.util.Optional;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class ApiCatalogueFilterManager {

    private static final Logger log = LoggerFactory.getLogger(ApiCatalogueComponent.class);
    private static final String TAXONOMY_FILTERS_MAPPING_DOCUMENT_PATH = "/content/documents/administration/website/developer-hub/taxonomy-filters-mapping";

    public Filters getRawFilters(HstRequest request) {
        CacheManager cacheManager = ApiCatalogueFilterCacheManager.loadFilterCache();
        Cache<String, Filters> cache = cacheManager.getCache("apiFilterCache", String.class, Filters.class);

        Filters rawFilters = null;

        if (null == cache.get("rawFiltersCache")) {
            rawFilters = rawFilters(sessionFrom(request), TAXONOMY_FILTERS_MAPPING_DOCUMENT_PATH, log);
            cache.put("rawFiltersCache", rawFilters);
            log.info("YAML coversion data has been fetched!!!");
        } else {
            rawFilters = cache.get("rawFiltersCache");
            log.info("Cache data has been fetched!!!");
        }

        return rawFilters;
    }

    protected Session sessionFrom(final HstRequest request) {
        try {
            return request.getRequestContext().getSession();
        } catch (final RepositoryException e) {
            throw new RuntimeException("Failed to obtain session from request.", e);
        }
    }

    protected Filters rawFilters(Session session, String taxonomyFilters, Logger logger) {
        try {
            return taxonomyKeysToFiltersMappingYaml(session, taxonomyFilters, logger)
                .map(mappingYaml -> CatalogueContext.filtersFactory().filtersFromMappingYaml(mappingYaml)).orElse(Filters.emptyInstance());
        } catch (final Exception e) {
            logger.error("Failed to generate Filters model.", e);
        }
        return Filters.emptyInstance();
    }

    protected Optional<String> taxonomyKeysToFiltersMappingYaml(final Session session, String taxonomyFilters, Logger logger) {

        try {
            return CatalogueContext.catalogueRepository(session, taxonomyFilters).taxonomyFiltersMapping();

        } catch (final Exception e) {
            logger.error("Failed to retrieve Taxonomy-Filters mapping YAML.", e);
        }

        return Optional.empty();
    }

}
