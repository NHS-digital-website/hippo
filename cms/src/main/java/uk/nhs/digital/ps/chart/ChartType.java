package uk.nhs.digital.ps.chart;

public enum ChartType {
    PIE("pie"),
    BAR("bar"),
    COLUMN("column"),
    STACKED_BAR("bar", true),
    STACKED_COLUMN("column", true),
    LINE("line"),
    SCATTER_PLOT("scatter"),
    FUNNEL_PLOT("funnel"),
    AREA_MAP(null);

    private final String highChartsType;
    private final boolean stacked;

    ChartType(String highChartsType) {
        this(highChartsType, false);
    }

    ChartType(String highChartsType, boolean stacked) {
        this.highChartsType = highChartsType;
        this.stacked = stacked;
    }

    public String getHighChartsType() {
        return highChartsType;
    }

    public boolean isStacked() {
        return stacked;
    }

    public static ChartType toChartType(String typeString) {
        return valueOf(typeString.toUpperCase().replace(" ", "_"));
    }
}
