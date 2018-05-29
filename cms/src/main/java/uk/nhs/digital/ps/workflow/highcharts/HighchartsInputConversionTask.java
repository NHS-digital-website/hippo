package uk.nhs.digital.ps.workflow.highcharts;

import static uk.nhs.digital.ps.PublicationSystemConstants.*;

import org.onehippo.repository.documentworkflow.DocumentVariant;
import org.onehippo.repository.documentworkflow.task.AbstractDocumentTask;
import uk.nhs.digital.JcrQueryHelper;
import uk.nhs.digital.common.util.json.JsonSerialiser;
import uk.nhs.digital.ps.ChartConfig;
import uk.nhs.digital.ps.chart.SeriesChart;
import uk.nhs.digital.ps.chart.input.HighchartsInputParser;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

public class HighchartsInputConversionTask extends AbstractDocumentTask {

    private final JsonSerialiser jsonSerialiser;
    private final HighchartsInputParser parser;

    private DocumentVariant variant;

    HighchartsInputConversionTask(final JsonSerialiser jsonSerialiser,
                                  final HighchartsInputParser parser) {
        this.jsonSerialiser = jsonSerialiser;
        this.parser = parser;
    }

    @Override
    protected Object doExecute() throws RepositoryException {

        final NodeIterator chartConfigNodes = findChartConfigNodes();

        while (chartConfigNodes.hasNext()) {

            final Node chartConfigNode = chartConfigNodes.nextNode();

            final ChartConfig chartConfig = getChartConfig(chartConfigNode);

            final SeriesChart chart = parseChartInput(chartConfig);

            final String chartConfigJson = toJson(chart);

            chartConfigNode.setProperty(PROPERTY_CHART_CONFIG, chartConfigJson);
        }

        return null;
    }

    public void setVariant(final DocumentVariant variant) {
        this.variant = variant;
    }

    private DocumentVariant getVariant() {
        return variant;
    }

    private ChartConfig getChartConfig(final Node chartConfigNode) throws RepositoryException {
        final Node dataFileNode = chartConfigNode.getNode(NODE_TYPE_DATA_FILE);

        // required fields
        final Binary inputFile = dataFileNode.getProperty("jcr:data").getBinary();
        final String chartType = chartConfigNode.getProperty(PROPERTY_CHART_TYPE)
            .getString();
        final String title = chartConfigNode.getProperty(PROPERTY_CHART_TITLE).getString();

        // optional field
        final String yTitle = chartConfigNode.getProperty(PROPERTY_CHART_YTITLE)
            .getString();

        return new ChartConfig(chartType, title, yTitle, inputFile);
    }

    private SeriesChart parseChartInput(final ChartConfig chartConfig) throws RepositoryException {
        try {
            return parser.parse(chartConfig);
        } catch (final Exception ex) {
            throw new RepositoryException("Failed to parse chart input file", ex);
        }
    }

    private String toJson(final SeriesChart chart) throws RepositoryException {
        try {
            return jsonSerialiser.toJson(chart);
        } catch (Exception ex) {
            throw new RepositoryException("Failed to convert chart input file.", ex);
        }
    }

    private NodeIterator findChartConfigNodes() throws RepositoryException {
        return JcrQueryHelper.findDescendantNodes(
            getVariant().getNode(getWorkflowContext().getInternalWorkflowSession()),
            "publicationsystem:chartSection"
        ).getNodes();
    }
}
