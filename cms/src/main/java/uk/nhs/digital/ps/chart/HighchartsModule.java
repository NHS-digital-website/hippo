package uk.nhs.digital.ps.chart;

import org.onehippo.cms7.services.HippoServiceRegistry;
import org.onehippo.repository.modules.DaemonModule;
import org.onehippo.repository.modules.ProvidesService;
import uk.nhs.digital.ps.chart.input.DelegatingHighchartsInputParser;
import uk.nhs.digital.ps.chart.input.HighchartsInputParser;
import uk.nhs.digital.ps.chart.input.ScatterHighchartsXlsxParser;
import uk.nhs.digital.ps.chart.input.SeriesHighchartsXlsxInputParser;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

@ProvidesService(types = HighchartsInputParser.class)
public class HighchartsModule implements DaemonModule {

    private HighchartsInputParser chartParser;

    @Override
    public void initialize(final Session session) throws RepositoryException {

        chartParser = new DelegatingHighchartsInputParser(
            new SeriesHighchartsXlsxInputParser(),
            new ScatterHighchartsXlsxParser()
        );
        HippoServiceRegistry.registerService(chartParser, HighchartsInputParser.class);
    }

    @Override
    public void shutdown() {
        HippoServiceRegistry.unregisterService(chartParser, HighchartsInputParser.class);
        chartParser = null;
    }
}
