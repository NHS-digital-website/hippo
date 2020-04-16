package uk.nhs.digital.intranet.enums;

public enum SearchArea {

    ALL,
    PEOPLE,
    NEWS,
    TASKS,
    TEAMS;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
