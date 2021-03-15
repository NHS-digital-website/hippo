package uk.nhs.digital.common.components.apicatalogue.filters;

public interface FiltersFactory {
    Filters filtersFromYaml(String yaml);
}
