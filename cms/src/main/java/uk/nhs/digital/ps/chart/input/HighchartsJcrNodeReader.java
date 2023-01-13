package uk.nhs.digital.ps.chart.input;

import uk.nhs.digital.ps.chart.parameters.AbstractVisualisationParameters;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

public interface HighchartsJcrNodeReader {
    AbstractVisualisationParameters readParameters(Node node) throws RepositoryException;
}
