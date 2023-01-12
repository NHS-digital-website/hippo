package uk.nhs.digital.ps.workflow.highcharts;

import static uk.nhs.digital.ps.PublicationSystemConstants.NODE_TYPE_CHART;
import static uk.nhs.digital.ps.PublicationSystemConstants.NODE_TYPE_MAP;
import static uk.nhs.digital.ps.PublicationSystemConstants.PROPERTY_CHART_CONFIG;

import org.onehippo.repository.documentworkflow.DocumentVariant;
import org.onehippo.repository.documentworkflow.task.AbstractDocumentTask;
import uk.nhs.digital.JcrQueryHelper;
import uk.nhs.digital.common.util.json.JsonSerialiser;
import uk.nhs.digital.ps.chart.input.HighchartsInputParser;
import uk.nhs.digital.ps.chart.input.HighchartsJcrNodeReader;
import uk.nhs.digital.ps.chart.model.AbstractVisualisationModel;
import uk.nhs.digital.ps.chart.parameters.AbstractVisualisationParameters;

import java.util.Iterator;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

public class HighchartsInputConversionTask extends AbstractDocumentTask {

    private final JsonSerialiser jsonSerialiser;
    private final HighchartsInputParser parser;
    private final HighchartsJcrNodeReader nodeReader;

    private DocumentVariant variant;

    HighchartsInputConversionTask(final JsonSerialiser jsonSerialiser,
                                  final HighchartsInputParser parser,
                                  final HighchartsJcrNodeReader nodeReader) {
        this.jsonSerialiser = jsonSerialiser;
        this.parser = parser;
        this.nodeReader = nodeReader;
    }

    @Override
    protected Object doExecute() throws RepositoryException {

        processNodes(NODE_TYPE_CHART);

        processNodes(NODE_TYPE_MAP);

        return null;
    }

    private void processNodes(String nodeType) throws RepositoryException {
        final Iterator<Node> nodes = findNodes(nodeType);

        while (nodes.hasNext()) {

            final Node chartConfigNode = nodes.next();

            final AbstractVisualisationParameters chartConfig = nodeReader.readParameters(chartConfigNode);

            final AbstractVisualisationModel chart = parseChartInput(chartConfig);

            final String chartConfigJson = toJson(chart);

            chartConfigNode.setProperty(PROPERTY_CHART_CONFIG, chartConfigJson);
        }
    }

    public void setVariant(final DocumentVariant variant) {
        this.variant = variant;
    }

    private DocumentVariant getVariant() {
        return variant;
    }

    private AbstractVisualisationModel parseChartInput(final AbstractVisualisationParameters chartConfig) throws RepositoryException {
        try {
            return parser.parse(chartConfig);
        } catch (final Exception ex) {
            throw new RepositoryException("Failed to parse chart input file", ex);
        }
    }

    private String toJson(final AbstractVisualisationModel chart) throws RepositoryException {
        try {
            return jsonSerialiser.toJson(chart);
        } catch (Exception ex) {
            throw new RepositoryException("Failed to convert chart input file.", ex);
        }
    }

    private NodeIterator findNodes(String primaryType) throws RepositoryException {
        return JcrQueryHelper.findDescendantNodes(
            getVariant().getNode(getWorkflowContext().getInternalWorkflowSession()),
            primaryType
        ).getNodes();
    }
}
