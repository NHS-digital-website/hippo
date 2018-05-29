package uk.nhs.digital.ps.chart.input;

import uk.nhs.digital.ps.chart.ChartType;

public interface SpecialisedHighchartsInputParser extends HighchartsInputParser {

    boolean supports(ChartType chartType);
}
