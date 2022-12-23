package uk.nhs.digital.ps.chart.enums;

public enum IconType {
    SQUARE("Square"),
    PERSON("Person"),
    WINE_GLASS("Wine Glass");

    private final String iconType;

    IconType(String iconType) {
        this.iconType = iconType;
    }

    public String getIconType() {
        return iconType;
    }

    public static IconType toIconType(String typeString) {
        return valueOf(typeString.toUpperCase().replace(" ", "_"));
    }
}
