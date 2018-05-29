package uk.nhs.digital.ps.chart;

import uk.nhs.digital.ps.chart.model.Series;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChartData {
    private List<String> categories;
    private HashMap<Integer, Series> series;
    private String xAxisTitle;
    private String yAxisTitle;

    protected ChartData() {
        categories = new ArrayList<>();
        series = new HashMap<>();
    }

    protected List<String> getCategories() {
        return categories;
    }

    protected HashMap<Integer, Series> getSeries() {
        return series;
    }

    protected String getxAxisTitle() {
        return xAxisTitle;
    }

    protected void setxAxisTitle(String xAxisTitle) {
        this.xAxisTitle = xAxisTitle;
    }

    protected String getyAxisTitle() {
        return yAxisTitle;
    }

    protected void setyAxisTitle(String yAxisTitle) {
        this.yAxisTitle = yAxisTitle;
    }
}
