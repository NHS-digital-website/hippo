package uk.nhs.digital.common.enums;

public enum SearchArea {
    ALL,
    DATA,
    SERVICES,
    NEWS;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
