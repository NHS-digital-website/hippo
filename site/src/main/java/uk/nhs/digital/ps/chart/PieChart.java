package uk.nhs.digital.ps.chart;

import static java.util.Collections.singletonList;

import uk.nhs.digital.ps.chart.model.*;

import java.util.List;

public class PieChart extends SeriesChart<Point> {
    public PieChart(String title, List<String> categories, String yAxisTitle, List<Series> series) {
        // We only have one series in a pie chart so just get the first
        super(ChartType.PIE, title, singletonList(series.get(0)));
    }
}
