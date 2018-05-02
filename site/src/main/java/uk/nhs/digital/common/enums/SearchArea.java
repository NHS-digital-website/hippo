package uk.nhs.digital.common.enums;

public enum SearchArea {
    ALL,
    DATA,
    SERVICES,
    NEWS_AND_EVENTS;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
