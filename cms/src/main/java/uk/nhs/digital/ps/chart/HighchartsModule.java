package uk.nhs.digital.ps.chart;

import org.onehippo.cms7.services.HippoServiceRegistry;
import org.onehippo.repository.modules.DaemonModule;
import org.onehippo.repository.modules.ProvidesService;
import uk.nhs.digital.ps.chart.input.DelegatingHighchartsInputParser;
import uk.nhs.digital.ps.chart.input.HighchartsInputParser;
import uk.nhs.digital.ps.chart.input.HighchartsJcrNodeReader;
import uk.nhs.digital.ps.chart.input.HighchartsJcrNodeReaderImpl;
import uk.nhs.digital.ps.chart.input.HighmapsXlsxInputParser;
import uk.nhs.digital.ps.chart.input.ScatterHighchartsXlsxInputParser;
import uk.nhs.digital.ps.chart.input.SeriesHighchartsXlsxInputParser;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

@ProvidesService(types = {HighchartsInputParser.class, HighchartsJcrNodeReader.class})
public class HighchartsModule implements DaemonModule {

    private HighchartsInputParser chartParser;
    private HighchartsJcrNodeReader jcrNodeReader;

    @Override
    public void initialize(final Session session) throws RepositoryException {

        chartParser = new DelegatingHighchartsInputParser(
            new SeriesHighchartsXlsxInputParser(),
            new ScatterHighchartsXlsxInputParser(),
            new HighmapsXlsxInputParser()
        );
        HippoServiceRegistry.register(chartParser, HighchartsInputParser.class);

        jcrNodeReader = new HighchartsJcrNodeReaderImpl();
        HippoServiceRegistry.register(jcrNodeReader, HighchartsJcrNodeReader.class);
    }

    @Override
    public void shutdown() {
        HippoServiceRegistry.unregister(chartParser, HighchartsInputParser.class);
        chartParser = null;

        HippoServiceRegistry.unregister(jcrNodeReader, HighchartsJcrNodeReader.class);
        jcrNodeReader = null;
    }
}
