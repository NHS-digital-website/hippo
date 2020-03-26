package uk.nhs.digital.intranet.enums;

public enum SearchResultType {

    PERSON("person"),
    TASK("task"),
    NEWS("news"),
    TEAM("team");

    final String value;
    SearchResultType(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
