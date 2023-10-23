package uk.nhs.digital.common.components.catalogue.filters;

import static java.util.stream.Collectors.toList;

import uk.nhs.digital.common.components.catalogue.CatalogueLink;

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
    public Set<String> filters = new HashSet<>();
    public List<CatalogueLink> links;
    public List<String> selectedFilterKeys;

    public FiltersAndLinks(final List<String> userSelectedFilterKeys, final List<CatalogueLink> links, final Filters rawFilters) {
        List<Set<String>> orderedFiltersKeysByCategory = filterKeysSortedByCategory(rawFilters, links, userSelectedFilterKeys);
        this.links = linksWithAnyUserSelectedFilterKeys(links, userSelectedFilterKeys).collect(toList());
        applyFiltersToLinks(userSelectedFilterKeys, orderedFiltersKeysByCategory);
        applyFiltersToNavKeys(userSelectedFilterKeys, links, orderedFiltersKeysByCategory);
        updateUserSelectedFilters(userSelectedFilterKeys);
    }

    //Filter the catalogue links with the currently selected filter keys using all filter keys ordered by selection and sorted into their categories.
    private void applyFiltersToLinks(final List<String> userSelectedFilterKeys, List<Set<String>> orderedFilterKeysByCategory) {
        if (!userSelectedFilterKeys.isEmpty()) {
            orderedFilterKeysByCategory.forEach(category -> {
                List<String> userSelectedFiltersForCategory = userSelectedFilterKeys.stream().filter(category::contains).collect(toList());
                this.links = linksWithAnyUserSelectedFilterKeys(this.links, userSelectedFiltersForCategory).collect(toList());
            });
        }
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
                Set<String> allFilteredLinksKeys = filteredLinks.get().stream().flatMap(link -> link.allTaxonomyKeysOfReferencedDoc().stream()).collect(Collectors.toSet());
                removeFromFilters(filters.stream().filter(key -> !collectionsContainKey(category, allFilteredLinksKeys, key)).collect(Collectors.toSet()));
            });
        } else {
            addToFilters(allKeysFromLinks(links));
        }
    }

    //Sort filter keys into categories and order by select filter keys order.
    private List<Set<String>> filterKeysSortedByCategory(Filters rawFilters, List<CatalogueLink> links, List<String> userSelectedFilterKeys) {
        Set<Set<String>> linkFiltersByCategory = rawFilters.getSections()
            .stream()
            .map(section -> section.getKeysInSection()
                .stream()
                .filter(allKeysFromLinks(links)::contains)
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
            .flatMap(link -> link.allTaxonomyKeysOfReferencedDoc().stream().filter(filter -> !filters.contains(filter)))
            .collect(Collectors.toSet());
    }

    private Set<String> filtersForCategoryFromLinks(List<CatalogueLink> filteredLinks, Set<String> category) {
        return filteredLinks
            .stream()
            .flatMap(link -> link.allTaxonomyKeysOfReferencedDoc().stream().filter(category::contains))
            .collect(Collectors.toSet());
    }

    private List<String> filtersForCategory(List<String> keys, Set<String> category) {
        return keys.stream().filter(category::contains).collect(toList());
    }

    private void addToFilters(Set<String> keys) {
        filters.addAll(keys);
    }

    private void removeFromFilters(Set<String> keys) {
        filters.removeAll(keys);
    }

    private void updateUserSelectedFilters(List<String> userSelectedFilterKeys) {
        Set<String> keysFromLinks = allKeysFromLinks(links);
        this.selectedFilterKeys = userSelectedFilterKeys.stream().filter(keysFromLinks::contains).collect(toList());
    }

    private Stream<CatalogueLink> linksWithAnyUserSelectedFilterKeys(final List<CatalogueLink> links,
                                                                       final List<String> userSelectedFilterKeys) {
        return links.stream()
                .filter(link -> userSelectedFilterKeys.isEmpty() || link.taggedWith(userSelectedFilterKeys));
    }

    private Set<String> allKeysFromLinks(List<CatalogueLink> links) {
        return links.stream().flatMap(link -> link.allTaxonomyKeysOfReferencedDoc().stream()).collect(Collectors.toSet());
    }

    private static boolean collectionsContainKey(Set<String> collection1, Set<String> collection2, String key) {
        return collection1.contains(key) || collection2.contains(key);
    }
}
