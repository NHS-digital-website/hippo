package uk.nhs.digital.common.components.catalogue.filters;

import static java.util.stream.Collectors.toList;

import uk.nhs.digital.common.components.catalogue.CatalogueLink;
import uk.nhs.digital.common.components.catalogue.FacetNavHelper;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
* Responsibilities of this class are:
* hold the collections of filtered catalogue links and Navigation menu Filters
* hold selected filters collection
* perform filtering on the catalogue links and also Navigation menu filters
* */
public class FiltersAndLinks {
    public Set<NavFilter> filters = new HashSet<>();
    public List<CatalogueLink> links;
    public List<String> selectedFilterKeys;
    private final FacetNavHelper facetNavHelper;

    public FiltersAndLinks(final List<String> userSelectedFilterKeys, final List<CatalogueLink> links, final Filters rawFilters, final FacetNavHelper facetNavHelper) {
        this.facetNavHelper = facetNavHelper;
        List<Set<String>> orderedFiltersKeysByCategory = filterKeysSortedByCategory(rawFilters, userSelectedFilterKeys);
        this.links = filterLinks(userSelectedFilterKeys, orderedFiltersKeysByCategory, links);
        applyFiltersToNavKeys(userSelectedFilterKeys, links, orderedFiltersKeysByCategory);
        updateUserSelectedFilters(userSelectedFilterKeys);
        updateCountsForFilters(orderedFiltersKeysByCategory, userSelectedFilterKeys, links);
    }

    //Update the counts for the NavFilters with the OR Logic within the same category applied
    private void updateCountsForFilters(List<Set<String>> orderedFilterKeysByCategory, final List<String> userSelectedFilterKeys, List<CatalogueLink> rawLinks) {
        filters.forEach(navFilter -> navFilter.count = countOfLinksWithKey(navFilter.filterKey, this.links));
        if (!orderedFilterKeysByCategory.isEmpty()) {
            Set<String> lastCategory = orderedFilterKeysByCategory.get(orderedFilterKeysByCategory.size() - 1);
            AtomicReference<List<CatalogueLink>> filteredLinks = new AtomicReference<>(rawLinks);
            orderedFilterKeysByCategory.remove(lastCategory);
            filteredLinks.set(filterLinks(userSelectedFilterKeys, orderedFilterKeysByCategory, rawLinks));
            filters.forEach(navFilter -> {
                if (lastCategory.contains(navFilter.filterKey)) {
                    navFilter.count = countOfLinksWithKey(navFilter.filterKey, filteredLinks.get());
                }
            });
        }
    }

    //Filter the catalogue links with the currently selected filter keys using all filter keys ordered by selection and sorted into their categories.
    private List<CatalogueLink> filterLinks(final List<String> userSelectedFilterKeys, List<Set<String>> orderedFilterKeysByCategory, List<CatalogueLink> links) {
        if (!userSelectedFilterKeys.isEmpty()) {
            AtomicReference<List<CatalogueLink>> filteredLinks = new AtomicReference<>(links);
            orderedFilterKeysByCategory.forEach(category -> {
                List<String> userSelectedFiltersForCategory = userSelectedFilterKeys.stream().filter(category::contains).collect(toList());
                filteredLinks.set(linksWithAnyUserSelectedFilterKeys(filteredLinks.get(), userSelectedFiltersForCategory).collect(toList()));
            });
            return filteredLinks.get();
        }
        return links;
    }

    //Filter the Navigation menu tags
    /*
    * Filtering is performed by category in order of current user filter key selection.
    * The order is important since it allows the correct logical comparison of AND/OR.
    * Other tags that are part of the last selected category are added back into the complete collection
    * to allow for further selection of tags in the same category that would otherwise have been filtered out.
    */
    private void applyFiltersToNavKeys(final List<String> userSelectedFilterKeys, final List<CatalogueLink> links, List<Set<String>> orderedFilterKeysByCategory) {
        if (!userSelectedFilterKeys.isEmpty()) {
            AtomicReference<List<CatalogueLink>> filteredLinks = new AtomicReference<>(links);
            orderedFilterKeysByCategory.forEach(category -> {
                Set<String> filtersForCategoryFromLinks = filtersForCategoryFromLinks(filteredLinks.get(), category);
                List<String> userSelectedFiltersForCategory = filtersForCategory(userSelectedFilterKeys, category);
                List<CatalogueLink> filteredLinksForCategory = linksWithAnyUserSelectedFilterKeys(filteredLinks.get(), userSelectedFiltersForCategory).collect(toList());
                filteredLinks.set(filteredLinksForCategory);
                filtersForCategoryFromLinks.addAll(filtersNotInCollection(filteredLinksForCategory, filtersForCategoryFromLinks));
                addToFilters(filtersForCategoryFromLinks);
                Set<String> allFilteredLinksKeys = filteredLinks.get().stream().flatMap(link -> facetNavHelper.getAllTagsForLink(link).stream()).collect(Collectors.toSet());

                removeFromFilters(
                        filters.stream()
                                .filter(navFilter -> !collectionsContainKey(category, allFilteredLinksKeys, navFilter.filterKey))
                                .map(navFilter -> navFilter.filterKey)
                                .collect(Collectors.toSet())
                );
            });
        } else {
            addToFilters(allKeysFromLinks());
        }
    }

    //Sort filter keys into categories and order by select filter keys order.
    private List<Set<String>> filterKeysSortedByCategory(Filters rawFilters, List<String> userSelectedFilterKeys) {
        Set<Set<String>> linkFiltersByCategory = rawFilters.getSections()
            .stream()
            .map(section -> section.getKeysInSection()
                .stream()
                .filter(allKeysFromLinks()::contains)
                .collect(Collectors.toSet()))
            .collect(Collectors.toSet());

        List<Set<String>> categorisedFilterKeysOrderedBySelection = new ArrayList<>();

        userSelectedFilterKeys.forEach(filterKey -> {
            Optional<Set<String>> categoryForFilterKey = linkFiltersByCategory.stream().filter(filters -> filters.contains(filterKey)).findFirst();
            if (categoryForFilterKey.isPresent()) {
                categorisedFilterKeysOrderedBySelection.remove(categoryForFilterKey.get());
                categorisedFilterKeysOrderedBySelection.add(categoryForFilterKey.get());
            }
        });
        return categorisedFilterKeysOrderedBySelection;
    }

    private Set<String> filtersNotInCollection(List<CatalogueLink> filteredLinks, Set<String> filters) {
        return filteredLinks
                .stream()
                .map(facetNavHelper::getAllTagsForLink)
                .flatMap(Collection::stream)
                .distinct()
                .filter(tag -> !filters.contains(tag))
                .collect(Collectors.toSet());
    }

    private Set<String> filtersForCategoryFromLinks(List<CatalogueLink> filteredLinks, Set<String> category) {
        return filteredLinks
                .stream()
                .map(facetNavHelper::getAllTagsForLink)
                .flatMap(Collection::stream)
                .distinct()
                .filter(category::contains).collect(Collectors.toSet());
    }

    private List<String> filtersForCategory(List<String> keys, Set<String> category) {
        return keys.stream().filter(category::contains).collect(toList());
    }

    private void addToFilters(Set<String> keys) {
        filters.addAll(keys.stream().map(key -> new NavFilter(key, 0)).collect(toList()));
    }

    private void removeFromFilters(Set<String> keys) {
        filters.removeIf(navFilter -> keys.contains(navFilter.filterKey));
    }

    private void updateUserSelectedFilters(List<String> userSelectedFilterKeys) {
        Set<String> keysFromLinks = allKeysFromLinks();
        this.selectedFilterKeys = userSelectedFilterKeys.stream().filter(keysFromLinks::contains).collect(toList());
    }

    private Stream<CatalogueLink> linksWithAnyUserSelectedFilterKeys(final List<CatalogueLink> links,
                                                                       final List<String> userSelectedFilterKeys) {
        return links.stream()
                .filter(link -> userSelectedFilterKeys.isEmpty() || link.taggedWith(userSelectedFilterKeys));
    }

    private int countOfLinksWithKey(String key, final List<CatalogueLink> links) {
        return (int) links.stream()
                .filter(link -> link.taggedWith(key)).count();
    }

    private Set<String> allKeysFromLinks() {
        return allTags();
    }

    private Set<String> allTags() {
        return facetNavHelper.getAllTags();
    }

    private static boolean collectionsContainKey(Set<String> collection1, Set<String> collection2, String key) {
        return collection1.contains(key) || collection2.contains(key);
    }
}
