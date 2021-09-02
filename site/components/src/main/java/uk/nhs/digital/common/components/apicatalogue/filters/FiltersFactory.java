package uk.nhs.digital.common.components.apicatalogue.filters;

public interface FiltersFactory {
    Filters filtersFromMappingYaml(String yaml);
}
