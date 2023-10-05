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

    public FiltersAndLinks(final Set<String> userSelectedFilterKeys, final List<CatalogueLink> links, final Filters rawFilters) {
        applyFiltersToLinks(userSelectedFilterKeys, rawFilters, links);
        applyFiltersToNavKeys(userSelectedFilterKeys, links, rawFilters);
    }

    private void applyFiltersToLinks(final Set<String> userSelectedFilterKeys, final Filters rawFilters, final List<CatalogueLink> links) {
        if (!userSelectedFilterKeys.isEmpty()) {
            this.links = linksWithAnyUserSelectedFilterKeys(links, userSelectedFilterKeys).collect(toList());
            List<Set<String>> orderedFilterKeysByCategory = filterKeysSortedByCategory(rawFilters, links, userSelectedFilterKeys);
            orderedFilterKeysByCategory.forEach(category -> {
                Set<String> userSelectedFiltersForCategory = userSelectedFilterKeys.stream().filter(category::contains).collect(Collectors.toSet());
                this.links = linksWithAnyUserSelectedFilterKeys(this.links, userSelectedFiltersForCategory).collect(toList());
            });
        } else {
            this.links = linksWithAnyUserSelectedFilterKeys(links, userSelectedFilterKeys).collect(toList());
        }

    }

    private void applyFiltersToNavKeys(final Set<String> userSelectedFilterKeys, final List<CatalogueLink> links, final Filters rawFilters) {
        //Get filter keys sorted into categories and ordered by selection order
        List<Set<String>> orderedFiltersKeysByCategory = filterKeysSortedByCategory(rawFilters, links, userSelectedFilterKeys);
        //List to hold filtered links after all category filtering
        AtomicReference<List<CatalogueLink>> filteredLinks = new AtomicReference<>(links);
        //List to hold remaining filters after each category filtering pass
        List<String> remainingFilters = new ArrayList<>();
        //For each category
        orderedFiltersKeysByCategory.forEach(category -> {
            //Get all filter keys for given category that exist in filtered documents
            Set<String> filtersForCategoryFromLinks = filteredLinks.get()
                    .stream()
                    .flatMap(link -> link.allTaxonomyKeysOfReferencedDoc().stream().filter(category::contains))
                    .collect(Collectors.toSet());
            //Get all user selected filter keys for this category
            Set<String> userSelectedFiltersForCategory = userSelectedFilterKeys.stream().filter(category::contains).collect(Collectors.toSet());
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

    private List<Set<String>> filterKeysSortedByCategory(Filters rawFilters, List<CatalogueLink> links, Set<String> userSelectedFilterKeys) {
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

    private Stream<CatalogueLink> linksWithAnyUserSelectedFilterKeys(final List<CatalogueLink> links,
                                                                       final Set<String> userSelectedFilterKeys) {
        return links.stream()
                .filter(link -> userSelectedFilterKeys.isEmpty() || link.taggedWith(userSelectedFilterKeys));
    }

    private Set<String> allKeysFromLinks(List<CatalogueLink> links) {
        return links.stream().flatMap(link -> link.allTaxonomyKeysOfReferencedDoc().stream()).collect(Collectors.toSet());
    }

}
