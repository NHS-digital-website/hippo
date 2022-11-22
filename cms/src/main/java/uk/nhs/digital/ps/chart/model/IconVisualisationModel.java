package uk.nhs.digital.ps.chart.model;

import uk.nhs.digital.ps.chart.enums.ChartType;
import uk.nhs.digital.ps.chart.enums.IconType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IconVisualisationModel extends AbstractVisualisationModel {
    private final Chart chart;

    private final Icon icon;

    private final Axis yAxis;

    private final Map<String, String> xlxsValues;

    public IconVisualisationModel(ChartType chartType, IconType iconType, String title, List<Series> series, String yAxisTitle) {
        super(title, series);

        this.xlxsValues = new HashMap<>();
        this.icon = new Icon(iconType);
        this.chart = new Chart(chartType);
        this.yAxis = new Axis(yAxisTitle, null);
    }

    public IconVisualisationModel(ChartType chartType, IconType iconType, String title, String yAxisTitle, Map<String, String> xlxsValues) {
        super(title);

        this.xlxsValues = xlxsValues;
        this.icon = new Icon(iconType);
        this.chart = new Chart(chartType);
        this.yAxis = new Axis(yAxisTitle, null);
    }


    public Axis getyAxis() {
        return yAxis;
    }

    public Chart getChart() {
        return chart;
    }

    public Icon getIcon() {
        return icon;
    }

    public Map<String, String> getXlxsValues() {
        return xlxsValues;
    }
}
