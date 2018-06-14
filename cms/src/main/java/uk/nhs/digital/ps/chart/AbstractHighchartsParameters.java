package uk.nhs.digital.ps.chart;

import javax.jcr.Binary;
import javax.jcr.RepositoryException;

public abstract class AbstractHighchartsParameters {
    private ChartType chartType;
    private Binary inputFileContent;
    private String title;

    public AbstractHighchartsParameters(final String type, final Binary inputFileContent, final String title) {
        this.chartType = ChartType.toChartType(type);
        this.inputFileContent = inputFileContent;
        this.title = title;
    }

    public ChartType getChartType() {
        return chartType;
    }

    public String getTitle() {
        return title;
    }

    public Binary getInputFileContent() {
        return inputFileContent;
    }

    public boolean noInputFileContent() {
        try {
            return inputFileContent == null || inputFileContent.getSize() == 0;
        } catch (final RepositoryException ex) {
            throw new RuntimeException("Failed to read binary size.", ex);
        }
    }
}
