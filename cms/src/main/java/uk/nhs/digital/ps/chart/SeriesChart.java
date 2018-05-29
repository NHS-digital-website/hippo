package uk.nhs.digital.ps.chart;

import uk.nhs.digital.ps.chart.model.*;

import java.util.List;

public class SeriesChart extends Titled {
    private final Chart chart;
    private final List<Series> series;
    private final Axis xAxis;
    private final Axis yAxis;
    private final PlotOptions plotOptions;

    public SeriesChart(ChartType type, String title, List<Series> series, String yAxisTitle, String xAxisTitle, List<String> categories) {
        super(title);

        this.chart = new Chart(type);
        this.series = series;
        this.xAxis = new Axis(xAxisTitle, categories);
        this.yAxis = new Axis(yAxisTitle, categories);
        this.plotOptions = type.isStacked() ? new PlotOptions("normal") : null;
    }

    public Axis getxAxis() {
        return xAxis;
    }

    public Axis getyAxis() {
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
