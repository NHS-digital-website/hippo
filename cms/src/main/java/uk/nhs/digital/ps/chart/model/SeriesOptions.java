package uk.nhs.digital.ps.chart.model;

public class SeriesOptions {
    private final String stacking;

    public SeriesOptions(String stackingType) {
        this.stacking = stackingType;
    }

    public String getStacking() {
        return stacking;
    }
}
