package uk.nhs.digital.ps.chart.input;

import uk.nhs.digital.ps.chart.AbstractHighchartsParameters;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

public interface HighchartsJcrNodeReader {
    AbstractHighchartsParameters readParameters(Node node) throws RepositoryException;
}
