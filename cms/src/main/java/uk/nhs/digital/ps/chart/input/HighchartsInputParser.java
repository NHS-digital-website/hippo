package uk.nhs.digital.ps.chart.input;

import uk.nhs.digital.ps.chart.model.AbstractVisualisationModel;
import uk.nhs.digital.ps.chart.parameters.AbstractVisualisationParameters;

public interface HighchartsInputParser {

    AbstractVisualisationModel parse(AbstractVisualisationParameters config);
}
