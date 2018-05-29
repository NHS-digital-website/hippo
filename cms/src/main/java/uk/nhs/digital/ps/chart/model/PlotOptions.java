package uk.nhs.digital.ps.chart.model;

public class PlotOptions {
    private SeriesOptions series;

    public PlotOptions(String stackingType) {
        this.series = new SeriesOptions(stackingType);
    }

    public SeriesOptions getSeries() {
        return series;
    }
}
