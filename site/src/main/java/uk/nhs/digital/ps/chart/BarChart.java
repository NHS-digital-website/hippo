package uk.nhs.digital.ps.chart;

import uk.nhs.digital.ps.chart.model.Categories;
import uk.nhs.digital.ps.chart.model.Series;
import uk.nhs.digital.ps.chart.model.Titled;

import java.util.List;

public class BarChart extends SeriesChart<Double> {
    private final Categories xAxis;
    private final Titled yAxis;

    public BarChart(String title, List<String> categories, String yAxisTitle, List<Series> series) {
        super(ChartType.BAR, title, series);

        this.xAxis = new Categories(categories);
        this.yAxis = new Titled(yAxisTitle);
    }
}
