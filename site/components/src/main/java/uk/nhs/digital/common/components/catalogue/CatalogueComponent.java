package uk.nhs.digital.common.components.catalogue;

import static java.util.stream.Collectors.toList;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.container.HstContainerURL;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.slf4j.Logger;
import uk.nhs.digital.common.components.ContentRewriterComponent;
import uk.nhs.digital.common.components.catalogue.filters.Filters;
import uk.nhs.digital.common.components.catalogue.filters.FiltersAndLinks;
import uk.nhs.digital.website.beans.ComponentList;

import java.util.*;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class CatalogueComponent extends ContentRewriterComponent {

    public FacetNavHelper facetNavHelper;

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);
        if (facetNavHelper == null) {
            String queryParam = this.cleanupSearchQuery(this.getAnyParameter(request, "query"));
            facetNavHelper = new FacetNavHelperImpl(this.getComponentParametersInfo(request), queryParam);
        }
    }

    protected Session sessionFrom(final HstRequest request) {
        try {
            return request.getRequestContext().getSession();
        } catch (final RepositoryException e) {
            throw new RuntimeException("Failed to obtain session from request.", e);
        }
    }

    protected List<CatalogueLink> catalogueLinksFrom(final HstRequest request) {
        return CatalogueLink.linksFrom(((ComponentList) request.getRequestContext().getContentBean()).getBlocks())
                .stream().filter(CatalogueLink::contentIsPublished).collect(toList());
    }

    protected static List<String> userSelectedFilterKeysFrom(final HstRequest request) {

        return Optional.ofNullable(request.getRequestContext())
            .map(HstRequestContext::getBaseURL)
            .map(HstContainerURL::getParameterMap)
            .map(parameterMap -> parameterMap.get(ServiceCatalogueComponent.Param.filter.name()))
            .map(Arrays::stream)
            .map(filterKeys -> filterKeys.collect(toList()))
            .orElse(Collections.emptyList());
    }

    protected Filters filtersModel(
        final List<String> userSelectedFilterKeys,
        Filters rawFilters,
        FiltersAndLinks filtersAndLinks,
        Logger logger
    ) {
        try {
            return rawFilters.initialisedWith(filtersAndLinks.filters, userSelectedFilterKeys);
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
}
