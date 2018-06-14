package uk.nhs.digital.ps.chart.model;

public class MapChart {
    private String map;
    private String zoomType;

    public MapChart(String map) {
        this.map = map;
        this.zoomType = ""; // disable the default xy zooming
    }

    public String getMap() {
        return map;
    }

    public String getZoomType() {
        return zoomType;
    }
}
