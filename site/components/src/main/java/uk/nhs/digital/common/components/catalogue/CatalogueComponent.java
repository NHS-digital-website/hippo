package uk.nhs.digital.common.components.catalogue;

import static java.util.stream.Collectors.toList;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.container.HstContainerURL;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.slf4j.Logger;
import uk.nhs.digital.common.components.ContentRewriterComponent;
import uk.nhs.digital.common.components.catalogue.filters.Filters;
import uk.nhs.digital.website.beans.ComponentList;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
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

    protected List<CatalogueLink> applyUserSelectedFilters(final List<CatalogueLink> links, final Set<String> selectedTags) {
        if (selectedTags.isEmpty()) {
            return links;
        }

        return linksWithAnyUserSelectedFilterKeys(links, selectedTags).collect(toList());
    }

    protected Stream<CatalogueLink> linksWithAnyUserSelectedFilterKeys(final List<CatalogueLink> links,
                                                                       final Set<String> userSelectedFilterKeys) {
        return links.stream()
            .filter(link -> userSelectedFilterKeys.isEmpty() || link.taggedWith(userSelectedFilterKeys));
    }

    protected Filters filtersModel(
        final List<CatalogueLink> catalogueLinks,
        final Set<String> userSelectedFilterKeys,
        final Session session,
        String taxonomyFilters,
        Logger logger
    ) {
        try {
            return taxonomyKeysToFiltersMappingYaml(session, taxonomyFilters, logger)
                .map(mappingYaml -> CatalogueContext.filtersFactory().filtersFromMappingYaml(mappingYaml))
                .map(rawFilters -> {
                    Set<String> filters =
                            userSelectedFilterKeys.isEmpty()
                                    ? allFilterKeysOfAllCatalogueDocsWhereEachDocTaggedWithAllUserSelectedKeys(userSelectedFilterKeys, catalogueLinks)
                                    : filterSequence(userSelectedFilterKeys, catalogueLinks, rawFilters);
                    return rawFilters.initialisedWith(filters, userSelectedFilterKeys);
                })
                .orElse(Filters.emptyInstance());
        } catch (final Exception e) {
            // We deliberately do not propagate the exception as it would break rendering of the page.
            // As it is, it's only the Filters section that won't be rendered but the content
            // will continue being displayed.
            logger.error("Failed to generate Filters model.", e);
        }

        return Filters.emptyInstance();
    }

    protected Optional<Filters> rawFilters(Session session, String taxonomyFilters, Logger logger) {
        return taxonomyKeysToFiltersMappingYaml(session, taxonomyFilters, logger)
                .map(mappingYaml -> CatalogueContext.filtersFactory().filtersFromMappingYaml(mappingYaml));
    }

    protected Optional<String> taxonomyKeysToFiltersMappingYaml(final Session session, String taxonomyFilters, Logger logger) {

        try {
            return CatalogueContext.catalogueRepository(session, taxonomyFilters).taxonomyFiltersMapping();

        } catch (final Exception e) {
            logger.error("Failed to retrieve Taxonomy-Filters mapping YAML.", e);
        }

        return Optional.empty();
    }

    private Set<String> allKeysFromLinks(List<CatalogueLink> links) {
        return links.stream().flatMap(link -> link.allTaxonomyKeysOfReferencedDoc().stream()).collect(Collectors.toSet());
    }

    private Set<Set<String>> linkKeysByCategoryOrdered(Filters rawFilters, List<CatalogueLink> links, Set<String> userSelectedFilterKeys) {
        Set<Set<String>> linkFiltersByCategory = rawFilters.getSections()
                .stream()
                .map(section -> section.getKeysInSection()
                        .stream()
                        .filter(allKeysFromLinks(links)::contains)
                        .collect(Collectors.toSet()))
                .collect(Collectors.toSet());

        Set<Set<String>> orderedLinkFiltersByCategory = new HashSet<>();

        userSelectedFilterKeys.forEach(filterKey -> {
            Set<String> categoryForFilterKey = linkFiltersByCategory.stream().filter(filters -> filters.contains(filterKey)).findFirst().get();
            orderedLinkFiltersByCategory.add(categoryForFilterKey);
        });
        return orderedLinkFiltersByCategory;
    }

    protected List<CatalogueLink> filterLinks(final Set<String> userSelectedFilterKeys, final Filters rawFilters, final List<CatalogueLink> links) {
        Set<Set<String>> orderedLinkFiltersByCategory = linkKeysByCategoryOrdered(rawFilters, links, userSelectedFilterKeys);

        AtomicReference<List<CatalogueLink>> filteredLinks = new AtomicReference<>(links);
        orderedLinkFiltersByCategory.forEach(category -> {

            Set<String> userSelectedFiltersForCategory = userSelectedFilterKeys.stream().filter(filter -> category.contains(filter)).collect(Collectors.toSet());
            List<CatalogueLink> filteredLinksForCategory = linksWithAnyUserSelectedFilterKeys(filteredLinks.get(), userSelectedFiltersForCategory).collect(toList());
            filteredLinks.set(filteredLinksForCategory);
        });
        return filteredLinks.get();
    }

    protected Set<String> filterSequence(final Set<String> userSelectedFilterKeys, final List<CatalogueLink> links, final Filters rawFilters) {
        //Organise filters in order of selection in same category
        //Retain all filters from same category of most recent selected category applying any previous filtering to that category
        //Deselect any filters from other than recent category that are not applicable after last filter
        //Select any filters from other than recent category that are now applicable after last filter

        Set<Set<String>> orderedLinkFiltersByCategory = linkKeysByCategoryOrdered(rawFilters, links, userSelectedFilterKeys);

        AtomicReference<List<CatalogueLink>> filteredLinks = new AtomicReference<>(links);
        List<String> remainingFilters = new ArrayList<>();
        orderedLinkFiltersByCategory.forEach(category -> {
            Set<String> filtersForCategoryFromLinks = filteredLinks.get()
                    .stream()
                    .flatMap(link -> link.allTaxonomyKeysOfReferencedDoc().stream().filter(filter -> category.contains(filter)))
                    .collect(Collectors.toSet());
            Set<String> userSelectedFiltersForCategory = userSelectedFilterKeys.stream().filter(filter -> category.contains(filter)).collect(Collectors.toSet());
            List<CatalogueLink> filteredLinksForCategory = linksWithAnyUserSelectedFilterKeys(filteredLinks.get(), userSelectedFiltersForCategory).collect(toList());
            filteredLinks.set(filteredLinksForCategory);
            filtersForCategoryFromLinks.addAll(
                    filteredLinksForCategory
                            .stream()
                            .flatMap(link -> link.allTaxonomyKeysOfReferencedDoc().stream().filter(filter -> !filtersForCategoryFromLinks.contains(filter)))
                            .collect(Collectors.toSet())
            );
            remainingFilters.addAll(filtersForCategoryFromLinks);
            Set<String> allFilteredLinksKeys = filteredLinks.get().stream().flatMap(link -> link.allTaxonomyKeysOfReferencedDoc().stream()).collect(Collectors.toSet());
            remainingFilters.removeIf(filter -> !category.contains(filter) && !allFilteredLinksKeys.contains(filter));
        });
        return new HashSet<>(remainingFilters);

        /*
        * e.g.
        * Select Men from Gender category -> Other categories filtered
        * Select Women from Gender category -> Other categories filtered
        * Select C-cup from Size category -> Other Categories filtered and Men is deselected and hidden
        * Select Large from Size category -> Other categories filtered and Men is selected and visible
        * Result is Men, Women selected from Gender and C-cup and Large selected from Size
        * */

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
