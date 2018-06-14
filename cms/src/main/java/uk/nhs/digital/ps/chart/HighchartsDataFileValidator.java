package uk.nhs.digital.ps.chart;

import static java.text.MessageFormat.format;

import org.apache.wicket.model.IModel;
import org.hippoecm.frontend.editor.validator.plugins.AbstractCmsValidator;
import org.hippoecm.frontend.model.JcrNodeModel;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.validation.IFieldValidator;
import org.hippoecm.frontend.validation.ValidationException;
import org.hippoecm.frontend.validation.Violation;
import org.onehippo.cms7.services.HippoServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.ps.chart.input.HighchartsInputParser;
import uk.nhs.digital.ps.chart.input.HighchartsJcrNodeReader;

import java.util.HashSet;
import java.util.Set;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

public class HighchartsDataFileValidator extends AbstractCmsValidator {

    private static final Logger log = LoggerFactory.getLogger(HighchartsDataFileValidator.class);

    private static final String TARGET_NODE_TYPE = "publicationsystem:resource";

    @SuppressWarnings("WeakerAccess") // Hippo CMS requires the constructor to be public
    public HighchartsDataFileValidator(final IPluginContext context, final IPluginConfig config) {
        super(context, config);
    }

    @Override
    public void preValidation(final IFieldValidator fieldValidator) throws ValidationException {

        final String actualFieldTypeName = fieldValidator.getFieldType().getType();

        if (!TARGET_NODE_TYPE.equals(actualFieldTypeName)) {
            throw new ValidationException(format(
                "Cannot validate the chart input file field. Expected field of type ''{0}'' but got ''{1}''.",
                TARGET_NODE_TYPE, actualFieldTypeName)
            );
        }
    }

    @Override
    public Set<Violation> validate(final IFieldValidator fieldValidator,
                                   final JcrNodeModel documentModel,
                                   final IModel uploadFieldModel
    ) throws ValidationException {
        try {
            final Set<Violation> violations = new HashSet<>();

            final Node configNode = ((JcrNodeModel) uploadFieldModel).getNode().getParent();
            final HighchartsJcrNodeReader jcrNodeReader = HippoServiceRegistry.getService(HighchartsJcrNodeReader.class);

            final AbstractHighchartsParameters parameters = jcrNodeReader.readParameters(configNode);

            if (isChartConfigInvalid(parameters)) {
                violations.add(fieldValidator.newValueViolation(uploadFieldModel, getTranslation()));
            }

            return violations;
        } catch (RepositoryException e) {
            throw new ValidationException(e);
        }
    }

    private boolean isChartConfigInvalid(final AbstractHighchartsParameters parameters) {

        // only verify chart config if the payload is actually present - if the field is required
        // it'll be rejected up by other validator(s)
        if (parameters.noInputFileContent()) {
            return false;
        }

        final HighchartsInputParser parser = HippoServiceRegistry
            .getService(HighchartsInputParser.class);

        try {
            parser.parse(parameters);
        } catch (final Exception ex) {
            // exception deliberately ignored - we use it as indication that the
            // chart config was invalid
            log.debug("Exception parsing chart", ex);
            return true;
        }
        return false;
    }

}
