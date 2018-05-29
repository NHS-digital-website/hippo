package uk.nhs.digital.ps.chart.input;

import org.onehippo.cms7.services.SingletonService;
import uk.nhs.digital.ps.ChartConfig;
import uk.nhs.digital.ps.chart.SeriesChart;

@SingletonService
public interface HighchartsInputParser {

    SeriesChart parse(ChartConfig chartConfig);
}
