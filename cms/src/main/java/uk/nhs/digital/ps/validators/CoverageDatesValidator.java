package uk.nhs.digital.ps.validators;

import static java.text.MessageFormat.format;

import org.hippoecm.frontend.validation.ValidationException;
import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.Validator;
import org.onehippo.cms.services.validation.api.Violation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

public class CoverageDatesValidator implements Validator<Date> {

    private static final Logger LOGGER = LoggerFactory
        .getLogger(CoverageDatesValidator.class);

    private static final String HIPPO_COVERAGE_START_PROPERTY_NAME = "publicationsystem:CoverageStart";
    private static final String HIPPO_COVERAGE_END_PROPERTY_NAME = "publicationsystem:CoverageEnd";
    private static final String HIPPO_NOMINAL_DATE_PROPERTY_NAME = "publicationsystem:NominalDate";

    @Override
    public Optional<Violation> validate(ValidationContext context, Date value) {
        final Node parentNode = context.getParentNode();

        try {
            final Calendar start = getDateValue(parentNode, HIPPO_COVERAGE_START_PROPERTY_NAME);
            final Calendar end = getDateValue(parentNode, HIPPO_COVERAGE_END_PROPERTY_NAME);
            final Calendar nominal = getDateValue(parentNode, HIPPO_NOMINAL_DATE_PROPERTY_NAME);

            // coverageEnd but no coverageStart
            if (!isHippoEmptyDate(end) && isHippoEmptyDate(start)) {
                return Optional.of(context.createViolation("no-start-provided"));
            }

            // coverageStart but no coverageEnd
            if (!isHippoEmptyDate(start) && isHippoEmptyDate(end)) {
                return Optional.of(context.createViolation("no-end-provided"));
            }

            if (!isHippoEmptyDate(start) && !isHippoEmptyDate(end)) {
                // coverageEnd is before coverageStart
                if (end.before(start)) {
                    return Optional.of(context.createViolation("end-before-start"));
                }

                // coverage dates CANNOT BE AFTER Publication Date
                if (!isHippoEmptyDate(nominal)
                    && (start.after(nominal) || end.after(nominal))) {
                    return Optional.of(context.createViolation("dates-after-nominal"));
                }
            }
        } catch (ValidationException e) {
            LOGGER.error("Error occurred during validation ", e);
        }

        return Optional.empty();
    }

    private Calendar getDateValue(final Node documentModel, String propertyName) throws ValidationException {
        try {
            return documentModel.getProperty(propertyName).getDate();
        } catch (final RepositoryException repositoryException) {
            throw new ValidationException(format("Failed to read field ''{0}'' value", propertyName));
        }
    }

    private boolean isHippoEmptyDate(final Calendar date) {
        return date.get(Calendar.YEAR) == 1 && date.get(Calendar.MONTH) == 0 && date.get(Calendar.DAY_OF_MONTH) == 1;
    }
}
