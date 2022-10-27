package uk.nhs.digital.ps.chart.parameters;

import javax.jcr.Binary;

public class HighchartsParameters extends AbstractVisualisationParameters {

    private String yTitle;

    public HighchartsParameters(final String type,
                                final String title,
                                final String yTitle,
                                final Binary inputFileContent) {
        super(type, inputFileContent, title);

        this.yTitle = yTitle;
    }

    public String getYTitle() {
        return yTitle;
    }

}
