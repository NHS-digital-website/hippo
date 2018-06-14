package uk.nhs.digital.ps.chart.input;

import org.onehippo.cms7.services.SingletonService;
import uk.nhs.digital.ps.chart.AbstractHighchartsParameters;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

@SingletonService
public interface HighchartsJcrNodeReader {
    AbstractHighchartsParameters readParameters(Node node) throws RepositoryException;
}
