package uk.nhs.digital.common.components.catalogue.filters;

import static java.util.stream.Collectors.toList;

import uk.nhs.digital.common.components.catalogue.CatalogueLink;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FiltersAndLinks {
    public Set<String> filters;
    public List<CatalogueLink> links;
    public List<String> validUserSelectedFilterKeys;

    public FiltersAndLinks(final List<String> userSelectedFilterKeys, final List<CatalogueLink> links, final Filters rawFilters) {
        List<Set<String>> orderedFiltersKeysByCategory = filterKeysSortedByCategory(rawFilters, links, userSelectedFilterKeys);
        applyFiltersToLinks(userSelectedFilterKeys, links, orderedFiltersKeysByCategory);
        applyFiltersToNavKeys(userSelectedFilterKeys, links, orderedFiltersKeysByCategory);
        updateUserSelectedFilters(userSelectedFilterKeys);
    }

    private void applyFiltersToLinks(final List<String> userSelectedFilterKeys, final List<CatalogueLink> links, List<Set<String>> orderedFilterKeysByCategory) {
        if (!userSelectedFilterKeys.isEmpty()) {
            this.links = linksWithAnyUserSelectedFilterKeys(links, userSelectedFilterKeys).collect(toList());
            orderedFilterKeysByCategory.forEach(category -> {
                List<String> userSelectedFiltersForCategory = userSelectedFilterKeys.stream().filter(category::contains).collect(toList());
                this.links = linksWithAnyUserSelectedFilterKeys(this.links, userSelectedFiltersForCategory).collect(toList());
            });
        } else {
            this.links = linksWithAnyUserSelectedFilterKeys(links, userSelectedFilterKeys).collect(toList());
        }

    }

    private void applyFiltersToNavKeys(final List<String> userSelectedFilterKeys, final List<CatalogueLink> links, List<Set<String>> orderedFilterKeysByCategory) {
        //List to hold filtered links after all category filtering
        AtomicReference<List<CatalogueLink>> filteredLinks = new AtomicReference<>(links);
        //List to hold remaining filters after each category filtering pass
        List<String> remainingFilters = new ArrayList<>();
        //For each category
        orderedFilterKeysByCategory.forEach(category -> {
            //Get all filter keys for given category that exist in filtered documents
            Set<String> filtersForCategoryFromLinks = filteredLinks.get()
                    .stream()
                    .flatMap(link -> link.allTaxonomyKeysOfReferencedDoc().stream().filter(category::contains))
                    .collect(Collectors.toSet());
            //Get all user selected filter keys for this category
            List<String> userSelectedFiltersForCategory = userSelectedFilterKeys.stream().filter(category::contains).collect(toList());
            //Filter links (already filtered links from other category) to apply selected filter keys for this category with OR condition
            List<CatalogueLink> filteredLinksForCategory = linksWithAnyUserSelectedFilterKeys(filteredLinks.get(), userSelectedFiltersForCategory).collect(toList());
            //Set the value of filtered links to only the results of the filtering for this category
            filteredLinks.set(filteredLinksForCategory);
            //Add all filter keys for this category that don't already exist after filtering
            filtersForCategoryFromLinks.addAll(
                    filteredLinksForCategory
                            .stream()
                            .flatMap(link -> link.allTaxonomyKeysOfReferencedDoc().stream().filter(filter -> !filtersForCategoryFromLinks.contains(filter)))
                            .collect(Collectors.toSet())
            );
            //Add all filter keys for this category to the remaining filters for all category filtering
            remainingFilters.addAll(filtersForCategoryFromLinks);
            //Get all the filter keys for all filtered links
            Set<String> allFilteredLinksKeys = filteredLinks.get().stream().flatMap(link -> link.allTaxonomyKeysOfReferencedDoc().stream()).collect(Collectors.toSet());
            //Remove from remaining filters if they aren't for this category and do not exist in any of the filtered links
            remainingFilters.removeIf(filter -> !category.contains(filter) && !allFilteredLinksKeys.contains(filter));
        });
        filters = new HashSet<>(remainingFilters);
    }

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
            Set<String> categoryForFilterKey = linkFiltersByCategory.stream().filter(filters -> filters.contains(filterKey)).findFirst().get();
            categorisedFilterKeysOrderedBySelection.remove(categoryForFilterKey);
            categorisedFilterKeysOrderedBySelection.add(categoryForFilterKey);
        });
        return categorisedFilterKeysOrderedBySelection;
    }

    private void updateUserSelectedFilters(List<String> userSelectedFilterKeys) {
        Set<String> keysFromLinks = allKeysFromLinks(links);
        this.validUserSelectedFilterKeys = userSelectedFilterKeys.stream().filter(keysFromLinks::contains).collect(toList());
    }

    private Stream<CatalogueLink> linksWithAnyUserSelectedFilterKeys(final List<CatalogueLink> links,
                                                                       final List<String> userSelectedFilterKeys) {
        return links.stream()
                .filter(link -> userSelectedFilterKeys.isEmpty() || link.taggedWith(userSelectedFilterKeys));
    }

    private Set<String> allKeysFromLinks(List<CatalogueLink> links) {
        return links.stream().flatMap(link -> link.allTaxonomyKeysOfReferencedDoc().stream()).collect(Collectors.toSet());
    }

}
