package uk.nhs.digital.ps.chart.input;

import org.onehippo.cms7.services.SingletonService;
import uk.nhs.digital.ps.chart.AbstractHighchartsParameters;
import uk.nhs.digital.ps.chart.model.AbstractHighchartsModel;

@SingletonService
public interface HighchartsInputParser {

    AbstractHighchartsModel parse(AbstractHighchartsParameters config);
}
