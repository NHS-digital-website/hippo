package uk.nhs.digital.ps.chart.model;

import uk.nhs.digital.ps.chart.ChartType;

import java.util.List;

public class HighchartsModel extends AbstractHighchartsModel {
    private final Chart chart;
    private final Axis xAxis;
    private final Axis yAxis;
    private final PlotOptions plotOptions;

    public HighchartsModel(ChartType type, String title, List<Series> series, String yAxisTitle, String xAxisTitle, List<String> categories) {
        super(title, series);

        this.chart = new Chart(type);
        this.xAxis = new Axis(xAxisTitle, categories);
        this.yAxis = new Axis(yAxisTitle, null);
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

    public PlotOptions getPlotOptions() {
        return plotOptions;
    }
}
