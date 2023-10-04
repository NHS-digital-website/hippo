package uk.nhs.digital.common.components.catalogue;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.container.HstContainerURL;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.slf4j.Logger;
import uk.nhs.digital.common.components.ContentRewriterComponent;
import uk.nhs.digital.common.components.catalogue.filters.Filters;
import uk.nhs.digital.common.components.catalogue.filters.FiltersAndLinks;
import uk.nhs.digital.website.beans.ComponentList;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class CatalogueComponent extends ContentRewriterComponent {

    protected Session sessionFrom(final HstRequest request) {
        try {
            return request.getRequestContext().getSession();
        } catch (final RepositoryException e) {
            throw new RuntimeException("Failed to obtain session from request.", e);
        }
    }

    protected List<CatalogueLink> catalogueLinksFrom(final HstRequest request) {
        return CatalogueLink.linksFrom(((ComponentList) request.getRequestContext().getContentBean()).getBlocks());
    }

    protected static Set<String> userSelectedFilterKeysFrom(final HstRequest request) {

        return Optional.ofNullable(request.getRequestContext())
            .map(HstRequestContext::getBaseURL)
            .map(HstContainerURL::getParameterMap)
            .map(parameterMap -> parameterMap.get(ServiceCatalogueComponent.Param.filter.name()))
            .map(Arrays::stream)
            .map(filterKeys -> filterKeys.collect(Collectors.toSet()))
            .orElse(Collections.emptySet());
    }

    protected Stream<CatalogueLink> linksWithAnyUserSelectedFilterKeys(final List<CatalogueLink> links,
                                                                       final Set<String> userSelectedFilterKeys) {
        return links.stream()
            .filter(link -> userSelectedFilterKeys.isEmpty() || link.taggedWith(userSelectedFilterKeys));
    }

    protected Filters filtersModel(
        final List<CatalogueLink> catalogueLinks,
        final Set<String> userSelectedFilterKeys,
        Filters rawFilters,
        FiltersAndLinks filtersAndLinks,
        Logger logger
    ) {
        try {
            Set<String> filters =
                userSelectedFilterKeys.isEmpty()
                    ? allFilterKeysOfAllCatalogueDocsWhereEachDocTaggedWithAllUserSelectedKeys(userSelectedFilterKeys, catalogueLinks)
                    : filtersAndLinks.filters;
            return rawFilters.initialisedWith(filters, userSelectedFilterKeys);
        } catch (final Exception e) {
            // We deliberately do not propagate the exception as it would break rendering of the page.
            // As it is, it's only the Filters section that won't be rendered but the content
            // will continue being displayed.
            logger.error("Failed to generate Filters model.", e);
        }

        return Filters.emptyInstance();
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

    protected Set<String> allFilterKeysOfAllCatalogueDocsWhereEachDocTaggedWithAllUserSelectedKeys(
        final Set<String> userSelectedFilterKeys,
        final List<CatalogueLink> links
    ) {
        return linksWithAnyUserSelectedFilterKeys(links, userSelectedFilterKeys)
            .flatMap(link -> link.allTaxonomyKeysOfReferencedDoc().stream())
            .collect(Collectors.toSet());
    }
}
