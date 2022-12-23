package uk.nhs.digital.ps.chart.enums;

public enum VisualisationColourOption {
    LIGHT("light"),
    DARK("dark"),
    COLOURED("coloured");

    private final String visualisationColour;

    VisualisationColourOption(String visualisationColour) {
        this.visualisationColour = visualisationColour;
    }

    public String getVisualisationColour() {
        return visualisationColour;
    }

    public static VisualisationColourOption toVisualisationColour(String colour) {
        return valueOf(colour.toUpperCase().replace(" ", "_"));
    }
}
