package uk.nhs.digital.common.components.apicatalogue;

import static java.util.stream.Collectors.toList;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.components.BaseGaContentComponent;
import uk.nhs.digital.common.components.apicatalogue.filters.Filters;
import uk.nhs.digital.common.components.apicatalogue.repository.ApiCatalogueRepository;
import uk.nhs.digital.website.beans.ComponentList;
import uk.nhs.digital.website.beans.Internallink;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class ApiCatalogueComponent extends BaseGaContentComponent {

    private static final Logger log = LoggerFactory.getLogger(ApiCatalogueComponent.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);

        final Set<String> selectedTags = userSelectedTaxonomyKeysFrom(request);

        final List<?> apiCatalogueLinksAll = apiCatalogueLinksFrom(request);

        final List<?> apiCatalogueLinksFiltered =
            apiCatalogueLinksFilteredBySelectedTags(apiCatalogueLinksAll, selectedTags);

        request.setAttribute("apiCatalogueLinks", apiCatalogueLinksFiltered);

        final Filters filtersModel = filtersModel(apiCatalogueLinksAll, selectedTags, sessionFrom(request));

        request.setAttribute("filtersModel", filtersModel);
    }

    private Session sessionFrom(final HstRequest request) {
        try {
            return request.getRequestContext().getSession();
        } catch (final RepositoryException e) {
            throw new RuntimeException("Failed to obtain session from request.", e);
        }
    }

    private static Set<String> userSelectedTaxonomyKeysFrom(final HstRequest request) {
        return Optional.ofNullable(request.getParameter("filters"))
            .map(commaDelimitedKeys -> commaDelimitedKeys.split(","))
            .map(Arrays::stream)
            .map(taxonomyKeys -> taxonomyKeys.collect(Collectors.toSet()))
            .orElse(Collections.emptySet());
    }

    private List<?> apiCatalogueLinksFrom(final HstRequest request) {
        return ((ComponentList) request.getRequestContext().getContentBean()).getBlocks();
    }

    private List<?> apiCatalogueLinksFilteredBySelectedTags(final List<?> links, final Set<String> selectedTags) {

        if (selectedTags.isEmpty()) {
            return links;
        }

        return internalLinksWithSelectedTaxonomyKeys(links, selectedTags).collect(toList());
    }

    private Filters filtersModel(final List<?> apiCatalogueLinks, final Set<String> selectedTags, final Session session) {

        try {
            return getMappingYaml(session)
                .map(mappingYaml -> {
                    final Set<String> filteredTaxonomyTags = filteredTaxonomyKeys(selectedTags, apiCatalogueLinks);

                    return ApiCatalogueContext.filtersFactory().filtersFromYaml(mappingYaml)
                        .initialisedWith(filteredTaxonomyTags, selectedTags);
                })
                .orElse(Filters.emptyInstance());
        } catch (final Exception e) {
            // We deliberately not propagate the exception as it would break rendering of the page.
            // As it is, it's only the Filters section that won't be rendered but the content
            // will continue being displayed.
            log.error("Failed to generate Filters model.", e);
        }

        return Filters.emptyInstance();
    }

    private Set<String> filteredTaxonomyKeys(final Set<String> selectedTaxonomyKeys, final List<?> links) {
        return internalLinksWithSelectedTaxonomyKeys(links, selectedTaxonomyKeys)
            .flatMap(internallink -> allTaxonomyKeysOfDocumentReferencedBy(internallink).stream())
            .collect(Collectors.toSet());
    }

    private Stream<Internallink> internalLinksWithSelectedTaxonomyKeys(final List<?> links, final Set<String> selectedTags) {

        return links.stream()
            .filter(Internallink.class::isInstance)
            .map(Internallink.class::cast)
            .filter(link -> linkHasSelectedTaxonomyKeys(link, selectedTags));
    }

    private boolean linkHasSelectedTaxonomyKeys(final Internallink link, final Set<String> selectedTags) {
        final Set<String> taxonomyKeysOfLinkedDoc = allTaxonomyKeysOfDocumentReferencedBy(link);
        return selectedTags.isEmpty() || taxonomyKeysOfLinkedDoc.containsAll(selectedTags);
    }

    private static Set<String> allTaxonomyKeysOfDocumentReferencedBy(final Internallink link) {
        return new HashSet<>(Arrays.asList((String[]) link
            .getLink()
            .getProperties()
            .getOrDefault("hippotaxonomy:keys", new String[0])));
    }

    private static Optional<String> getMappingYaml(final Session session) {

        try {
            final ApiCatalogueRepository apiCatalogueRepository = ApiCatalogueContext.repository(session);

            return apiCatalogueRepository.taxonomyFiltersMapping();

        } catch (final Exception e) {
            log.error("Failed to retrieve Taxonomy-Filters mapping YAML.", e);
        }

        return Optional.empty();
    }
}
