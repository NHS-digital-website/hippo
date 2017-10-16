package uk.nhs.digital.ps.test.acceptance.models;

public enum GeographicCoverage {
    ENGLAND("England"),
    ENGLAND_AND_NORTHERN_IRELAND("England and Northern Ireland"),
    ENGLAND_AND_SCOTLAND("England and Scotland"),
    ENGLAND_AND_WALES("England and Wales"),
    ENGLAND_SCOTLAND_AND_NORTHERN_IRELAND("England Scotland and Northern Ireland"),
    ENGLAND_WALES_AND_NORTHERN_IRELAND("England Wales and Northern Ireland"),
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
