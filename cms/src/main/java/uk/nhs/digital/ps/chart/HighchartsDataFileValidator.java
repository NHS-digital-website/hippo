package uk.nhs.digital.ps.chart;

import org.hippoecm.repository.impl.NodeDecorator;
import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.Validator;
import org.onehippo.cms.services.validation.api.Violation;
import org.onehippo.cms7.services.HippoServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.ps.chart.input.HighchartsInputParser;
import uk.nhs.digital.ps.chart.input.HighchartsJcrNodeReader;

import java.util.Optional;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

public class HighchartsDataFileValidator implements Validator<NodeDecorator> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HighchartsDataFileValidator.class);

    @Override
    public Optional<Violation> validate(ValidationContext context, NodeDecorator value) {

        try {
            final Node configNode = context.getParentNode();
            final HighchartsJcrNodeReader jcrNodeReader = HippoServiceRegistry.getService(HighchartsJcrNodeReader.class);
            final AbstractHighchartsParameters parameters = jcrNodeReader.readParameters(configNode);

            if (isChartConfigInvalid(parameters)) {
                return Optional.of(context.createViolation());
            }
        } catch (NullPointerException | RepositoryException e) {
            LOGGER.error("Error occurred during validation ", e);
        }

        return Optional.empty();
    }

    private boolean isChartConfigInvalid(final AbstractHighchartsParameters parameters) {

        if (parameters.noInputFileContent()) {
            return true;
        }

        final HighchartsInputParser parser = HippoServiceRegistry.getService(HighchartsInputParser.class);

        try {
            parser.parse(parameters);
        } catch (final Exception ex) {
            // exception deliberately ignored - we use it as indication that the
            // chart config was invalid
            LOGGER.debug("Exception parsing chart", ex);
            return true;
        }
        return false;
    }
}
