package uk.nhs.digital.ps.test.acceptance.models;

public enum GeographicCoverage {
    ENGLAND("England"),
    GREAT_BRITAIN("Great Britain"),
    INTERNATIONAL("International"),
    NORTHERN_IRELAND("Northern Ireland"),
    SCOTLAND("Scotland"),
    UK("UK"),
    WALES("Wales");

    private final String displayValue;

    GeographicCoverage(final String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
