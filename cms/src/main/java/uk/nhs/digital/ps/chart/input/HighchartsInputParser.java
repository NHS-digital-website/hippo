package uk.nhs.digital.ps.chart.input;

import uk.nhs.digital.ps.chart.parameters.AbstractVisualisationParameters;
import uk.nhs.digital.ps.chart.model.AbstractHighchartsModel;

public interface HighchartsInputParser {

    AbstractHighchartsModel parse(AbstractVisualisationParameters config);
}
