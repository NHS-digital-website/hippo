package uk.nhs.digital.ps.chart.input;

import static org.hippoecm.repository.util.JcrUtils.getBinaryProperty;
import static org.hippoecm.repository.util.JcrUtils.getStringProperty;
import static uk.nhs.digital.ps.PublicationSystemConstants.NODE_TYPE_CHART;
import static uk.nhs.digital.ps.PublicationSystemConstants.NODE_TYPE_DATA_FILE;
import static uk.nhs.digital.ps.PublicationSystemConstants.NODE_TYPE_MAP;
import static uk.nhs.digital.ps.PublicationSystemConstants.NODE_TYPE_VISUALISATION;
import static uk.nhs.digital.ps.PublicationSystemConstants.PROPERTY_CHART_TITLE;
import static uk.nhs.digital.ps.PublicationSystemConstants.PROPERTY_CHART_TYPE;
import static uk.nhs.digital.ps.PublicationSystemConstants.PROPERTY_CHART_YTITLE;
import static uk.nhs.digital.ps.PublicationSystemConstants.PROPERTY_MAP_SOURCE;
import static uk.nhs.digital.ps.PublicationSystemConstants.PROPERTY_TYPE_ICON;
import static uk.nhs.digital.ps.PublicationSystemConstants.PROPERTY_VISUALISATION_YTITLE;

import org.apache.jackrabbit.JcrConstants;
import uk.nhs.digital.ps.chart.enums.ChartType;
import uk.nhs.digital.ps.chart.parameters.AbstractVisualisationParameters;
import uk.nhs.digital.ps.chart.parameters.HighchartsParameters;
import uk.nhs.digital.ps.chart.parameters.HighmapsParameters;
import uk.nhs.digital.ps.chart.parameters.VisualisationParameters;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

@SuppressWarnings("PMD.TooManyStaticImports")
public class HighchartsJcrNodeReaderImpl implements HighchartsJcrNodeReader {

    @Override
    public AbstractVisualisationParameters readParameters(Node node) throws RepositoryException {
        String nodeType = node.getPrimaryNodeType().getName();
        switch (nodeType) {
            case NODE_TYPE_CHART:
                return getChartParameters(node);
            case NODE_TYPE_VISUALISATION:
                return getVisualisationParameters(node);
            case NODE_TYPE_MAP:
                return getMapParameters(node);
            default:
                throw new RuntimeException("Unhandled node type: " + nodeType);
        }
    }

    private HighchartsParameters getChartParameters(final Node chartConfigNode) throws RepositoryException {
        final Node dataFileNode = chartConfigNode.getNode(NODE_TYPE_DATA_FILE);

        // required fields
        final Binary inputFile = getBinaryProperty(dataFileNode, JcrConstants.JCR_DATA, null);
        final String chartType = getStringProperty(chartConfigNode, PROPERTY_CHART_TYPE, null);
        final String title = getStringProperty(chartConfigNode, PROPERTY_CHART_TITLE, null);

        // optional field
        final String yTitle = getStringProperty(chartConfigNode, PROPERTY_CHART_YTITLE, null);

        return new HighchartsParameters(chartType, title, yTitle, inputFile);
    }

    private VisualisationParameters getVisualisationParameters(final Node visualisationConfigNode) throws RepositoryException {
        final Node dataFileNode = visualisationConfigNode.getNode(NODE_TYPE_DATA_FILE);

        // required fields
        final Binary inputFile = getBinaryProperty(dataFileNode, JcrConstants.JCR_DATA, null);
        final String chartType = getStringProperty(visualisationConfigNode, PROPERTY_CHART_TYPE, null);
        final String title = getStringProperty(visualisationConfigNode, PROPERTY_CHART_TITLE, null);

        // optional field
        final String yTitle = getStringProperty(visualisationConfigNode, PROPERTY_VISUALISATION_YTITLE, null);
        final String iconType = getStringProperty(visualisationConfigNode, PROPERTY_TYPE_ICON, null);

        return new VisualisationParameters(chartType, title, yTitle, inputFile, iconType);
    }

    private HighmapsParameters getMapParameters(final Node mapConfigNode) throws RepositoryException {
        final Node dataFileNode = mapConfigNode.getNode(NODE_TYPE_DATA_FILE);

        // required fields
        final String type = ChartType.AREA_MAP.name(); // this is the only option currently
        final Binary inputFile = getBinaryProperty(dataFileNode, JcrConstants.JCR_DATA, null);
        final String mapSource = getStringProperty(mapConfigNode, PROPERTY_MAP_SOURCE, null);
        final String title = getStringProperty(mapConfigNode, PROPERTY_CHART_TITLE, null);

        return new HighmapsParameters(type, mapSource, title, inputFile);
    }
}
