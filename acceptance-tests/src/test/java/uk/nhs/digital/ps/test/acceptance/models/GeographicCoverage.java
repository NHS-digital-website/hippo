package uk.nhs.digital.ps.test.acceptance.models;

public enum GeographicCoverage {
    ENGLAND("England"),
    NORTHERN_IRELAND("Northern Ireland"),
    REPUBLIC_OF_IRELAND("Republic of Ireland"),
    SCOTLAND("Scotland"),
    WALES("Wales");

    private final String displayValue;

    GeographicCoverage(final String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
