package uk.nhs.digital.common.components.apicatalogue;

import static java.util.stream.Collectors.toList;

import com.google.common.collect.ImmutableSet;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.container.HstContainerURL;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.components.ContentRewriterComponent;
import uk.nhs.digital.common.components.apicatalogue.filters.Filters;
import uk.nhs.digital.website.beans.ComponentList;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class ApiCatalogueComponent extends ContentRewriterComponent {

    private static final Logger log = LoggerFactory.getLogger(ApiCatalogueComponent.class);

    private static final Set<String> RETIRED_API_FILTER_KEYS = ImmutableSet.of("retired-api");
    private static final String TAXONOMY_FILTERS_MAPPING_DOCUMENT_PATH = "/content/documents/administration/website/developer-hub/taxonomy-filters-mapping";

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);

        final List<CatalogueLink> allCatalogueLinks = apiCatalogueLinksFrom(request);

        final boolean showRetired = shouldShowRetired(request);

        final List<CatalogueLink> catalogueLinksExcludingRetiredIfNeeded =
            eliminateRetiredIfNeeded(allCatalogueLinks, showRetired);

        final Set<String> userSelectedFilterKeys = userSelectedFilterKeysFrom(request);

        final List<CatalogueLink> catalogueLinksFiltered = applyUserSelectedFilters(
            catalogueLinksExcludingRetiredIfNeeded,
            userSelectedFilterKeys
        );

        final Filters filtersModel = filtersModel(
            catalogueLinksExcludingRetiredIfNeeded,
            userSelectedFilterKeys,
            sessionFrom(request)
        );

        request.setAttribute(Param.showRetired.name(), showRetired);
        request.setAttribute(Param.apiCatalogueLinks.name(), catalogueLinksFiltered.stream().map(CatalogueLink::raw).collect(toList()));
        request.setAttribute(Param.filtersModel.name(), filtersModel);
    }

    private boolean queryStringContainsParameter(final HstRequest request, final Param queryStringParameter) {
        return Optional.ofNullable(request.getQueryString())
            .filter(queryString -> queryString.contains(queryStringParameter.name()))
            .isPresent();
    }

    private boolean shouldShowRetired(final HstRequest request) {
        return queryStringContainsParameter(request, Param.showDeprecatedAndRetired)
            || queryStringContainsParameter(request, Param.showRetired);
    }

    private Session sessionFrom(final HstRequest request) {
        try {
            return request.getRequestContext().getSession();
        } catch (final RepositoryException e) {
            throw new RuntimeException("Failed to obtain session from request.", e);
        }
    }

    private List<CatalogueLink> apiCatalogueLinksFrom(final HstRequest request) {
        return CatalogueLink.linksFrom(((ComponentList) request.getRequestContext().getContentBean()).getBlocks());
    }

    private List<CatalogueLink> eliminateRetiredIfNeeded(
        final List<CatalogueLink> catalogueLinks,
        final boolean showRetired
    ) {
        return catalogueLinks.stream()
            .filter(link -> showRetired || link.notFilterable() || link.notTaggedWithAnyOf(RETIRED_API_FILTER_KEYS))
            .collect(toList());
    }

    private static Set<String> userSelectedFilterKeysFrom(final HstRequest request) {

        return Optional.ofNullable(request.getRequestContext())
            .map(HstRequestContext::getBaseURL)
            .map(HstContainerURL::getParameterMap)
            .map(parameterMap -> parameterMap.get(Param.filter.name()))
            .map(Arrays::stream)
            .map(filterKeys -> filterKeys.collect(Collectors.toSet()))
            .orElse(Collections.emptySet());
    }

    private List<CatalogueLink> applyUserSelectedFilters(final List<CatalogueLink> links, final Set<String> selectedTags) {
        if (selectedTags.isEmpty()) {
            return links;
        }

        return linksWithAllUserSelectedFilterKeys(links, selectedTags).collect(toList());
    }

    private Filters filtersModel(
        final List<CatalogueLink> catalogueLinks,
        final Set<String> userSelectedFilterKeys,
        final Session session
    ) {
        try {
            return taxonomyKeysToFiltersMappingYaml(session)
                .map(mappingYaml -> ApiCatalogueContext.filtersFactory().filtersFromMappingYaml(mappingYaml))
                .map(rawFilters -> rawFilters.initialisedWith(
                    allFilterKeysOfAllCatalogueDocsWhereEachDocTaggedWithAllUserSelectedKeys(userSelectedFilterKeys, catalogueLinks), userSelectedFilterKeys)
                )
                .orElse(Filters.emptyInstance());
        } catch (final Exception e) {
            // We deliberately do not propagate the exception as it would break rendering of the page.
            // As it is, it's only the Filters section that won't be rendered but the content
            // will continue being displayed.
            log.error("Failed to generate Filters model.", e);
        }

        return Filters.emptyInstance();
    }

    private Set<String> allFilterKeysOfAllCatalogueDocsWhereEachDocTaggedWithAllUserSelectedKeys(
        final Set<String> userSelectedFilterKeys,
        final List<CatalogueLink> links
    ) {
        return linksWithAllUserSelectedFilterKeys(links, userSelectedFilterKeys)
            .flatMap(link -> link.allTaxonomyKeysOfReferencedDoc().stream())
            .collect(Collectors.toSet());
    }

    private Stream<CatalogueLink> linksWithAllUserSelectedFilterKeys(final List<CatalogueLink> links,
                                                                     final Set<String> userSelectedFilterKeys) {
        return links.stream()
            .filter(link -> userSelectedFilterKeys.isEmpty() || link.taggedWith(userSelectedFilterKeys));
    }

    private static Optional<String> taxonomyKeysToFiltersMappingYaml(final Session session) {

        try {
            return ApiCatalogueContext.apiCatalogueRepository(session, TAXONOMY_FILTERS_MAPPING_DOCUMENT_PATH).taxonomyFiltersMapping();

        } catch (final Exception e) {
            log.error("Failed to retrieve Taxonomy-Filters mapping YAML.", e);
        }

        return Optional.empty();
    }

    enum Param {
        showRetired,
        apiCatalogueLinks,
        filtersModel,
        filter,

        // Older parameter, deprecated in favour of showRetired,
        // retained in case it's been included in existing bookmarks.
        showDeprecatedAndRetired
    }
}