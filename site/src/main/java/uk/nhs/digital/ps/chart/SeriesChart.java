package uk.nhs.digital.ps.chart;

import uk.nhs.digital.ps.chart.model.Chart;
import uk.nhs.digital.ps.chart.model.Series;
import uk.nhs.digital.ps.chart.model.Titled;

import java.util.List;

public class SeriesChart<T> extends Titled {
    private final Chart chart;
    private final List<Series> series;

    public SeriesChart(ChartType type, String title, List<Series> series) {
        super(title);
        this.chart = new Chart(type);
        this.series = series;
    }
}
