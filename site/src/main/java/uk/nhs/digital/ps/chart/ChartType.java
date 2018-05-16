package uk.nhs.digital.ps.chart;

public enum ChartType {
    PIE,
    BAR;

    public String getHighChartsType() {
        return name().toLowerCase();
    }
}
