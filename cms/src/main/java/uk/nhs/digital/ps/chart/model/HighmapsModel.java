package uk.nhs.digital.ps.chart.model;

import static java.util.Collections.singletonList;

public class HighmapsModel extends AbstractHighchartsModel {
    private final MapChart chart;
    private final ColorAxis colorAxis;
    private final MapNavigation mapNavigation;

    public HighmapsModel(String map, String title, Series series) {
        super(title, singletonList(series));

        this.chart = new MapChart(map);
        this.colorAxis = new ColorAxis(0);
        this.mapNavigation = new MapNavigation();
    }

    public MapChart getChart() {
        return chart;
    }

    public ColorAxis getColorAxis() {
        return colorAxis;
    }

    public MapNavigation getMapNavigation() {
        return mapNavigation;
    }
}
