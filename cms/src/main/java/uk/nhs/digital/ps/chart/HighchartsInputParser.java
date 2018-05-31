package uk.nhs.digital.ps.chart;

import org.onehippo.cms7.services.SingletonService;
import uk.nhs.digital.ps.ChartConfig;

@SingletonService
public interface HighchartsInputParser {

    SeriesChart parse(final ChartConfig chartConfig);
}
