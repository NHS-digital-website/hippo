package uk.nhs.digital.ps.test.acceptance.models;

public enum InformationType {
    AUDIT("Audit"),
    EXPERIMENTAL_STATISTICS("Experimental statistics"),
    NATIONAL_STATISTICS("National statistics"),
    OFFICIAL_STATISTICS("Official statistics"),
    OPEN_DATA("Open data"),
    OTHER_REPORTS_AND_STATS("Other reports and statistics"),
    SURVEY("Survey");


    private final String displayName;

    InformationType(final String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
