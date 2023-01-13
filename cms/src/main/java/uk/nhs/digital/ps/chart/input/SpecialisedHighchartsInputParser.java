package uk.nhs.digital.ps.chart.input;

import uk.nhs.digital.ps.chart.enums.ChartType;
import uk.nhs.digital.ps.chart.model.AbstractVisualisationModel;
import uk.nhs.digital.ps.chart.parameters.AbstractVisualisationParameters;

public interface SpecialisedHighchartsInputParser {

    AbstractVisualisationModel parse(AbstractVisualisationParameters config);

    boolean supports(ChartType chartType);
}
