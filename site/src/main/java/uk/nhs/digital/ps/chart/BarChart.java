package uk.nhs.digital.ps.chart;

import uk.nhs.digital.ps.chart.model.Categories;
import uk.nhs.digital.ps.chart.model.Chart;
import uk.nhs.digital.ps.chart.model.Series;
import uk.nhs.digital.ps.chart.model.Titled;

import java.util.List;

public class BarChart extends Titled {
    private final Chart chart = new Chart("bar");
    private final Categories xAxis;
    private final Titled yAxis;
    private final List<Series> series;

    public BarChart(String title, List<String> categories, String yAxisTitle, List<Series> series) {
        super(title);

        this.xAxis = new Categories(categories);
        this.yAxis = new Titled(yAxisTitle);
        this.series = series;
    }
}
