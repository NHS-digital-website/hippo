package uk.nhs.digital.ps.chart;

import static java.text.MessageFormat.format;
import static uk.nhs.digital.ps.PublicationSystemConstants.PROPERTY_CHART_TITLE;
import static uk.nhs.digital.ps.PublicationSystemConstants.PROPERTY_CHART_TYPE;
import static uk.nhs.digital.ps.PublicationSystemConstants.PROPERTY_CHART_YTITLE;

import org.apache.wicket.model.IModel;
import org.hippoecm.frontend.editor.validator.plugins.AbstractCmsValidator;
import org.hippoecm.frontend.model.JcrNodeModel;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.validation.IFieldValidator;
import org.hippoecm.frontend.validation.ValidationException;
import org.hippoecm.frontend.validation.Violation;
import org.onehippo.cms7.services.HippoServiceRegistry;
import uk.nhs.digital.ps.ChartConfig;
import uk.nhs.digital.ps.chart.input.HighchartsInputParser;

import java.util.HashSet;
import java.util.Set;
import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;

public class HighchartsDataFileValidator extends AbstractCmsValidator {

    private static final String PROPERTY_NAME_FILE_CONTENT = "jcr:data";

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

        final Set<Violation> violations = new HashSet<>();

        final ChartConfig chartConfig = getChartConfig(uploadFieldModel);

        if (isChartConfigInvalid(chartConfig)) {
            violations.add(fieldValidator.newValueViolation(uploadFieldModel, getTranslation()));
        }

        return violations;
    }

    private ChartConfig getChartConfig(final IModel uploadFieldModel) throws ValidationException {
        try {
            final Node fileUploadNode = ((JcrNodeModel) uploadFieldModel).getNode();
            final Node chartSectionNode = ((JcrNodeModel) uploadFieldModel).getNode().getParent();

            return new ChartConfig(
                getStringValue(chartSectionNode, PROPERTY_CHART_TYPE),
                getStringValue(chartSectionNode, PROPERTY_CHART_TITLE),
                getStringValue(chartSectionNode, PROPERTY_CHART_YTITLE),
                getBinaryValue(fileUploadNode, PROPERTY_NAME_FILE_CONTENT)
            );
        } catch (final Exception repositoryException) {
            throw new ValidationException("Failed to read chart config.", repositoryException);
        }
    }

    private boolean isChartConfigInvalid(final ChartConfig chartConfig) {

        // only verify chart config if the payload is actually present - if the field is required
        // it'll be rejected up by other validator(s)
        if (chartConfig.noInputFileContent()) {
            return false;
        }

        final HighchartsInputParser parser = HippoServiceRegistry
            .getService(HighchartsInputParser.class);

        try {
            parser.parse(chartConfig);
        } catch (final Exception ex) {
            // exception deliberately ignored - we use it as indication that the
            // chart config was invalid
            return true;
        }
        return false;
    }

    private String getStringValue(final Node node,
                                  final String propertyName) throws RepositoryException {

        final Property property = node.getProperty(propertyName);
        return property == null ? null : property.getString();
    }

    private Binary getBinaryValue(final Node node,
                                  final String propertyName) throws RepositoryException {

        final Property property = node.getProperty(propertyName);
        return property == null ? null : property.getBinary();
    }

}
