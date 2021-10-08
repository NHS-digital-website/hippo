package uk.nhs.digital.intranet.model;

public class SearchFilter {
    String key;
    String name;
    long noResults;

    public SearchFilter(String key, String name, long noResults) {
        this.key = key;
        this.name = name;
        this.noResults = noResults;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public long getNoResults() {
        return noResults;
    }
}
