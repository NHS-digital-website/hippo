package uk.nhs.digital.ps.chart;

import uk.nhs.digital.ps.chart.model.*;

import java.util.List;

public class SeriesChart extends Titled {
    private final Chart chart;
    private final List<Series> series;
    private final Categories xAxis;
    private final Titled yAxis;
    private final PlotOptions plotOptions;

    public SeriesChart(ChartType type, String title, List<Series> series, String yAxisTitle, List<String> categories) {
        super(title);

        this.chart = new Chart(type);
        this.series = series;
        this.xAxis = new Categories(categories);
        this.yAxis = new Titled(yAxisTitle);
        this.plotOptions = type.isStacked() ? new PlotOptions("normal") : null;
    }

    public Categories getxAxis() {
        return xAxis;
    }

    public Titled getyAxis() {
        return yAxis;
    }

    public Chart getChart() {
        return chart;
    }

    public List<Series> getSeries() {
        return series;
    }

    public PlotOptions getPlotOptions() {
        return plotOptions;
    }
}
