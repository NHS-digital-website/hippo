package uk.nhs.digital.ps.chart;

public enum MapSource {
    BRITISH_ISLES_COUNTRIES("custom/british-isles"),
    BRITISH_ISLES_COUNTIES("custom/british-isles-all");

    private final String highmapsMapKey;

    MapSource(String highmapsMapKey) {
        this.highmapsMapKey = highmapsMapKey;
    }

    public String getHighmapsMapKey() {
        return highmapsMapKey;
    }

    public static MapSource toMapType(String mapSource) {
        return valueOf(mapSource.toUpperCase().replace(" ", "_"));
    }
}
