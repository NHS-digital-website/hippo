package uk.nhs.digital.ps.chart;

import javax.jcr.Binary;

public class HighchartsParameters extends AbstractHighchartsParameters {

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
