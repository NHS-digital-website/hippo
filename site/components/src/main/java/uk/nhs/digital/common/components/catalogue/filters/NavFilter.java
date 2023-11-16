package uk.nhs.digital.common.components.catalogue.filters;

public class NavFilter {
    String filterKey;
    int count;

    public NavFilter(String filterKey, int count) {
        this.filterKey = filterKey;
        this.count = count;
    }
}
