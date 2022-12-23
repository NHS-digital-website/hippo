package uk.nhs.digital.ps.chart.model;

import uk.nhs.digital.ps.chart.enums.VisualisationColourOption;


public class VisualisationColour {
    private final String colourOption;

    public VisualisationColour(VisualisationColourOption colourOption) {
        this.colourOption = colourOption.getVisualisationColour();
    }

    public String getColourOption() {
        return colourOption;
    }

}
