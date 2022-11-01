package uk.nhs.digital.ps.chart.model;

import uk.nhs.digital.ps.chart.enums.ChartType;

import java.util.List;

public class VisualisationModel extends AbstractVisualisationModel {
    private final Chart chart;
    private final Axis xAxis;
    private final Axis yAxis;

    public VisualisationModel(ChartType type, String title, List<Series> series, String yAxisTitle, String xAxisTitle, List<String> categories) {
        super(title, series);

        this.chart = new Chart(type);
        this.xAxis = new Axis(xAxisTitle, categories);
        this.yAxis = new Axis(yAxisTitle, null);
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

}
