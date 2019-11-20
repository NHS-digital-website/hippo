package uk.nhs.digital.ps.chart.input;

import uk.nhs.digital.ps.chart.AbstractHighchartsParameters;
import uk.nhs.digital.ps.chart.model.AbstractHighchartsModel;

public interface HighchartsInputParser {

    AbstractHighchartsModel parse(AbstractHighchartsParameters config);
}
