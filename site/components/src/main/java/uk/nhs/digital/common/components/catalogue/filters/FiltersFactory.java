package uk.nhs.digital.common.components.catalogue.filters;

public interface FiltersFactory {
    Filters filtersFromMappingYaml(String yaml);
}
