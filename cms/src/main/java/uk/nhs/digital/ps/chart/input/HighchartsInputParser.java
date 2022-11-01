package uk.nhs.digital.ps.chart.input;

import uk.nhs.digital.ps.chart.model.AbstractHighchartsModel;
import uk.nhs.digital.ps.chart.parameters.AbstractVisualisationParameters;

public interface HighchartsInputParser {

    AbstractHighchartsModel parse(AbstractVisualisationParameters config);
}
