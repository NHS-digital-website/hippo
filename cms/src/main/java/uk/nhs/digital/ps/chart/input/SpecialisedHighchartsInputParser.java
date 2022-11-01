package uk.nhs.digital.ps.chart.input;

import uk.nhs.digital.ps.chart.enums.ChartType;
import uk.nhs.digital.ps.chart.model.AbstractHighchartsModel;
import uk.nhs.digital.ps.chart.parameters.AbstractVisualisationParameters;

public interface SpecialisedHighchartsInputParser {

    AbstractHighchartsModel parse(AbstractVisualisationParameters config);

    boolean supports(ChartType chartType);
}
