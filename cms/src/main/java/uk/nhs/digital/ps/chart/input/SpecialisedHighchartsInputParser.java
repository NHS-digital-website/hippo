package uk.nhs.digital.ps.chart.input;

import uk.nhs.digital.ps.chart.AbstractHighchartsParameters;
import uk.nhs.digital.ps.chart.ChartType;
import uk.nhs.digital.ps.chart.model.AbstractHighchartsModel;

public interface SpecialisedHighchartsInputParser {

    AbstractHighchartsModel parse(AbstractHighchartsParameters config);

    boolean supports(ChartType chartType);
}
