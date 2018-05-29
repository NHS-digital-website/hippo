package uk.nhs.digital.ps.chart.model;

public class Tooltip {
    private final String pointFormat;
    private final String headerFormat;

    public Tooltip(String pointFormat, String headerFormat) {
        this.pointFormat = pointFormat;
        this.headerFormat = headerFormat;
    }

    public String getPointFormat() {
        return pointFormat;
    }

    public String getHeaderFormat() {
        return headerFormat;
    }
}
