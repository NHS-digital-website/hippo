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
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class ApiCatalogueComponent extends ContentRewriterComponent {

    private static final Logger log = LoggerFactory.getLogger(ApiCatalogueComponent.class);

    private static final Set<String> RETIRED_API_FILTER_KEYS = ImmutableSet.of("retired-api");

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);

        final List<ApiCatalogueLink> allApiCatalogueLinks = apiCatalogueLinksFrom(request);

        final boolean showRetired = shouldShowRetired(request);

        final List<ApiCatalogueLink> apiCatalogueLinksExcludingRetiredIfNeeded =
            eliminateRetiredIfNeeded(allApiCatalogueLinks, showRetired);

        final Set<String> userSelectedFilterKeys = userSelectedFilterKeysFrom(request);
        final Set<String> userEnteredKeywords = userEnteredKeywordsFrom(request);

        final List<ApiCatalogueLink> apiCatalogueLinksFiltered = applyUserSelectedFiltersAndUserEnteredKeywords(
            apiCatalogueLinksExcludingRetiredIfNeeded,
            userSelectedFilterKeys,
            userEnteredKeywords
        );

        final Filters filtersModel = filtersModel(
            apiCatalogueLinksExcludingRetiredIfNeeded,
            userSelectedFilterKeys,
            sessionFrom(request)
        );

        request.setAttribute(Param.showRetired.name(), showRetired);
        request.setAttribute(Param.apiCatalogueLinks.name(), apiCatalogueLinksFiltered.stream().map(ApiCatalogueLink::raw).collect(toList()));
        request.setAttribute(Param.filtersModel.name(), filtersModel);
        request.setAttribute(Param.keyword.name(), userEnteredKeywords);
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

    private List<ApiCatalogueLink> apiCatalogueLinksFrom(final HstRequest request) {
        return ApiCatalogueLink.linksFrom(((ComponentList) request.getRequestContext().getContentBean()).getBlocks());
    }

    private List<ApiCatalogueLink> eliminateRetiredIfNeeded(
        final List<ApiCatalogueLink> apiCatalogueLinks,
        final boolean showRetired
    ) {
        return apiCatalogueLinks.stream()
            .filter(link -> showRetired || link.notFilterable() || link.notTaggedWithAnyOf(RETIRED_API_FILTER_KEYS))
            .collect(toList());
    }

    private Set<String> userSelectedFilterKeysFrom(final HstRequest request) {
        return collectSetOfStringParam(request, Param.filter.name());
    }


    private Set<String> userEnteredKeywordsFrom(HstRequest request) {
        return collectSetOfStringParam(request, Param.keyword.name());
    }

    private Set<String> collectSetOfStringParam(HstRequest request, String paramName) {
        return Optional.ofNullable(request.getRequestContext())
            .map(HstRequestContext::getBaseURL)
            .map(HstContainerURL::getParameterMap)
            .map(parameterMap -> parameterMap.get(paramName))
            .map(Arrays::stream)
            .map(paramValues -> paramValues.collect(Collectors.toSet()))
            .orElse(Collections.emptySet());
    }

    private List<ApiCatalogueLink> applyUserSelectedFiltersAndUserEnteredKeywords(
        final List<ApiCatalogueLink> links,
        final Set<String> selectedTags,
        final Set<String> enteredKeywords
    ) {
        if (selectedTags.isEmpty() && enteredKeywords.isEmpty()) {
            return links;
        }

        List<ApiCatalogueLink> linksWithFiltersApplied = linksWithAllUserSelectedFilterKeys(links, selectedTags).collect(toList());
        return linksWithAllUserEnteredKeywords(linksWithFiltersApplied, enteredKeywords).collect(toList());
    }

    private Filters filtersModel(
        final List<ApiCatalogueLink> apiCatalogueLinks,
        final Set<String> userSelectedFilterKeys,
        final Session session
    ) {
        try {
            return taxonomyKeysToFiltersMappingYaml(session)
                .map(mappingYaml -> ApiCatalogueContext.filtersFactory().filtersFromMappingYaml(mappingYaml))
                .map(rawFilters -> rawFilters.initialisedWith(
                    allFilterKeysOfAllCatalogueDocsWhereEachDocTaggedWithAllUserSelectedKeys(userSelectedFilterKeys, apiCatalogueLinks), userSelectedFilterKeys)
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
        final List<ApiCatalogueLink> links
    ) {
        return linksWithAllUserSelectedFilterKeys(links, userSelectedFilterKeys)
            .flatMap(link -> link.allTaxonomyKeysOfReferencedDoc().stream())
            .collect(Collectors.toSet());
    }

    private Stream<ApiCatalogueLink> linksWithAllUserSelectedFilterKeys(final List<ApiCatalogueLink> links,
                                                                        final Set<String> userSelectedFilterKeys) {
        return links.stream()
            .filter(link -> userSelectedFilterKeys.isEmpty() || link.taggedWith(userSelectedFilterKeys));
    }

    private Stream<ApiCatalogueLink> linksWithAllUserEnteredKeywords(final List<ApiCatalogueLink> links,
                                                                        final Set<String> userEnteredKeywords) {
        return links.stream()
            .filter(link -> userEnteredKeywords.isEmpty() || linkContainsUserEnteredKeyword(link, userEnteredKeywords));
    }

    private boolean linkContainsUserEnteredKeyword(ApiCatalogueLink link, Set<String> userEnteredKeywords) {
        String title = link.getPropertyOrDefault("website:title", "").toString();
        String shortSummary = link.getPropertyOrDefault("website:shortsummary", "").toString();

        Set<String> searchFields = link.allTaxonomyKeysOfReferencedDoc();
        searchFields.addAll(
            ImmutableSet.of(title, shortSummary)
        );

        Predicate<String> linkContainsKeyword = keyword -> searchFields.stream().anyMatch(field -> field.toLowerCase().contains(keyword.toLowerCase()));

        return userEnteredKeywords.stream().anyMatch(linkContainsKeyword);
    }

    private static Optional<String> taxonomyKeysToFiltersMappingYaml(final Session session) {

        try {
            return ApiCatalogueContext.apiCatalogueRepository(session).taxonomyFiltersMapping();

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
        keyword,

        // Older parameter, deprecated in favour of showRetired,
        // retained in case it's been included in existing bookmarks.
        showDeprecatedAndRetired,
    }
}