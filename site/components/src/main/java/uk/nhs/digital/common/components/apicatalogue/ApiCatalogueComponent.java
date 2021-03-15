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
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class ApiCatalogueComponent extends BaseGaContentComponent {

    private static final Logger log = LoggerFactory.getLogger(ApiCatalogueComponent.class);

    private static final Predicate<Internallink> INTERNAL_LINKS = link -> link.getLinkType().equals("internal");

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);

        final Set<String> selectedTags = userSelectedTagsFrom(request);

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

    private static Set<String> userSelectedTagsFrom(final HstRequest request) {
        // We only support (and expect) a single value passed as filters.
        // Support for multiple values is planned to be added later.

        return Optional.ofNullable(request.getParameter("filters"))
            .map(filter -> new HashSet<>(Collections.singleton(filter)))
            .orElse(new HashSet<>());
    }

    private List<?> apiCatalogueLinksFrom(final HstRequest request) {
        return ((ComponentList) request.getRequestContext().getContentBean()).getBlocks();
    }

    private List<?> apiCatalogueLinksFilteredBySelectedTags(final List<?> links, final Set<String> selectedTags) {

        if (selectedTags.isEmpty()) {
            return links;
        }

        return internalLinksWithSelectedTags(links, selectedTags).collect(toList());
    }

    private Filters filtersModel(final List<?> apiCatalogueLinks, final Set<String> selectedTags, final Session session) {

        return getMappingYaml(session)
            .map(mappingYaml -> {
                final Set<String> filteredTaxonomyTags = filteredTaxonomyTags(selectedTags, apiCatalogueLinks);

                return ApiCatalogueContext
                    .filtersFactory()
                    .filtersFromYaml(mappingYaml)
                    .initialisedWith(filteredTaxonomyTags, selectedTags);
            })
            .orElse(Filters.emptyInstance());
    }

    private Set<String> filteredTaxonomyTags(final Set<String> selectedTags, final List<?> links) {
        return internalLinksWithSelectedTags(links, selectedTags)
            .flatMap(internallink -> getTagsFromLink(internallink).stream())
            .collect(Collectors.toSet());
    }

    private Stream<Internallink> internalLinksWithSelectedTags(final List<?> links, final Set<String> selectedTags) {

        return links.stream()
            .filter(Internallink.class::isInstance)
            .map(Internallink.class::cast)
            .filter(INTERNAL_LINKS)
            .filter(link -> linkHasSelectedTags(link, selectedTags));
    }

    private boolean linkHasSelectedTags(final Internallink link, final Set<String> selectedTags) {
        final Set<String> taxonomyKeys = getTagsFromLink(link);
        return selectedTags.isEmpty() || taxonomyKeys.stream().anyMatch(selectedTags::contains);
    }

    private static Set<String> getTagsFromLink(final Internallink link) {
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
