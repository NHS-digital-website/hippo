package uk.nhs.digital.ps.chart.parameters;

import uk.nhs.digital.ps.chart.enums.IconType;
import uk.nhs.digital.ps.chart.enums.VisualisationColourOption;

import javax.jcr.Binary;

public class VisualisationParameters extends AbstractVisualisationParameters {

    private VisualisationColourOption colour;

    private IconType iconType;

    public VisualisationParameters(final String chartType,
                                   final String title,
                                   final String colour,
                                   final Binary inputFileContent,
                                   final String iconType) {
        super(chartType, inputFileContent, title);

        this.colour = VisualisationColourOption.toVisualisationColour(colour);
        this.iconType = IconType.toIconType(iconType);

    }

    public IconType getIconType() {
        return iconType;
    }

    public VisualisationColourOption getColour() {
        return colour;
    }

}
