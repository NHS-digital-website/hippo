package uk.nhs.digital.ps.chart;

import uk.nhs.digital.ps.chart.model.*;

import java.util.List;

public class SeriesChart extends Titled {
    private final Categories xAxis;
    private final Titled yAxis;
    private final Chart chart;
    private final List<Series> series;
    private final PlotOptions plotOptions;

    public SeriesChart(ChartType type, String title, List<Series> series, String yAxisTitle, List<String> categories) {
        super(title);

        this.chart = new Chart(type);
        this.series = series;
        this.xAxis = new Categories(categories);
        this.yAxis = new Titled(yAxisTitle);
        this.plotOptions = type.isStacked() ? new PlotOptions("normal") : null;
    }
}
