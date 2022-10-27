package uk.nhs.digital.ps.chart.parameters;

import uk.nhs.digital.ps.chart.enums.IconType;

import javax.jcr.Binary;

public class VisualisationParameters extends AbstractVisualisationParameters {

    private String yTitle;

    private IconType iconType;

    public VisualisationParameters(final String type,
                                   final String title,
                                   final String yTitle,
                                   final Binary inputFileContent,
                                   final String iconType) {
        super(type, inputFileContent, title);

        this.yTitle = yTitle;
        this.iconType = IconType.toIconType(type);

    }

    public IconType getIconType() {
        return iconType;
    }

    public String getYTitle() {
        return yTitle;
    }

}
