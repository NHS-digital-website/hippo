package uk.nhs.digital.ps;

import static java.text.MessageFormat.format;

import org.apache.wicket.model.IModel;
import org.hippoecm.frontend.editor.validator.plugins.AbstractCmsValidator;
import org.hippoecm.frontend.model.JcrNodeModel;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.validation.IFieldValidator;
import org.hippoecm.frontend.validation.ValidationException;
import org.hippoecm.frontend.validation.Violation;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import javax.jcr.RepositoryException;

public class CoverageDatesValidator extends AbstractCmsValidator {

    private static final String SUPPORTED_FIELD_TYPE_NAME = "CalendarDate";
    private static final String HIPPO_COVERAGE_START_PROPERTY_NAME = "publicationsystem:CoverageStart";
    private static final String HIPPO_COVERAGE_END_PROPERTY_NAME = "publicationsystem:CoverageEnd";
    private static final String HIPPO_NOMINAL_DATE_PROPERTY_NAME = "publicationsystem:NominalDate";

    @SuppressWarnings("WeakerAccess") // Hippo CMS requires the constructor to be public
    public CoverageDatesValidator(final IPluginContext pluginContext, final IPluginConfig pluginConfig) {
        super(pluginContext, pluginConfig);
    }

    @Override
    public void preValidation(IFieldValidator fieldValidator) throws ValidationException {

        final String actualFieldTypeName = fieldValidator.getFieldDescriptor().getTypeDescriptor().getName();

        if (!SUPPORTED_FIELD_TYPE_NAME.equals(actualFieldTypeName)) {
            throw new ValidationException(format(
                "Cannot validate field ''{0}'' of type ''{1}''. This validator only supports fields of type ''{2}''.",
                fieldValidator.getFieldDescriptor().getPath(),
                actualFieldTypeName,
                SUPPORTED_FIELD_TYPE_NAME
            ));
        }
    }

    @Override
    public Set<Violation> validate(final IFieldValidator fieldValidator,
                                   final JcrNodeModel documentModel,
                                   final IModel coverageDateFieldModel) throws ValidationException {

        final Set<Violation> violations = new HashSet<>();

        // get dates
        final Calendar start = getDateValue(documentModel, HIPPO_COVERAGE_START_PROPERTY_NAME);
        final Calendar end = getDateValue(documentModel, HIPPO_COVERAGE_END_PROPERTY_NAME);
        final Calendar nominal = getDateValue(documentModel, HIPPO_NOMINAL_DATE_PROPERTY_NAME);

        // coverageEnd but no coverageStart
        if (!isHippoEmptyDate(end) && isHippoEmptyDate(start)) {
            violations.add(fieldValidator.newValueViolation(documentModel, getTranslation("no-start-provided")));
        }

        // coverageStart but no coverageEnd
        if (!isHippoEmptyDate(start) && isHippoEmptyDate(end)) {
            violations.add(fieldValidator.newValueViolation(documentModel, getTranslation("no-end-provided")));
        }

        if (!isHippoEmptyDate(start) && !isHippoEmptyDate(end)) {

            // coverageEnd is before coverageStart
            if (end.before(start)) {
                violations.add(fieldValidator.newValueViolation(documentModel, getTranslation("end-before-start")));
            }

            // coverage dates CANNOT BE AFTER Publication Date
            if (!isHippoEmptyDate(nominal)
                && (start.after(nominal) || end.after(nominal))) {
                violations.add(fieldValidator.newValueViolation(documentModel, getTranslation("dates-after-nominal")));
            }
        }

        return violations;
    }

    private Calendar getDateValue(final JcrNodeModel documentModel, String propertyName) throws ValidationException {
        try {
            return documentModel.getNode().getProperty(propertyName).getDate();
        } catch (final RepositoryException repositoryException) {
            throw new ValidationException(format("Failed to read field ''{0}'' value", propertyName));
        }
    }

    private boolean isHippoEmptyDate(final Calendar date) {
        // Hippo empty dates are stored as 0001-01-01T12:00:00Z
        return date.get(Calendar.YEAR) == 1 && date.get(Calendar.MONTH) == 0 && date.get(Calendar.DAY_OF_MONTH) == 1;
    }
}
