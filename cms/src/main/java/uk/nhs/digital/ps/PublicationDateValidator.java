package uk.nhs.digital.ps;

import static java.text.MessageFormat.format;
import static java.time.temporal.ChronoUnit.DAYS;

import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.hippoecm.frontend.editor.validator.plugins.AbstractCmsValidator;
import org.hippoecm.frontend.l10n.ResourceBundleModel;
import org.hippoecm.frontend.model.JcrNodeModel;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.session.PluginUserSession;
import org.hippoecm.frontend.validation.IFieldValidator;
import org.hippoecm.frontend.validation.ValidationException;
import org.hippoecm.frontend.validation.Violation;
import uk.nhs.digital.common.util.TimeProvider;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;
import javax.jcr.RepositoryException;

public class PublicationDateValidator extends AbstractCmsValidator {

    private static final String VIOLATION_MESSAGE_TEMPLATE_KEY =
        "publicationsystem-publication-date";
    private static final String VALIDATORS_TRANSLATION_KEY = "hippo:cms.validators";
    private static final String DATE_FIELD_TYPE_NAME = "CalendarDate";
    private static final String VALID_PERIOD_YEARS_KEY = "validPeriodLengthInYears";

    private static final String PUBLICLY_ACCESSIBLE_PROPERTY_NAME =
        "publicationsystem:PubliclyAccessible";
    private static final String DATE_PROPERTY_NAME = "publicationsystem:NominalDate";


    @SuppressWarnings("WeakerAccess") // Hippo CMS requires the constructor to be public
    public PublicationDateValidator(final IPluginContext pluginContext,
                                    final IPluginConfig pluginConfig) {
        super(pluginContext, pluginConfig);
    }

    @Override
    public void preValidation(final IFieldValidator fieldValidator) throws ValidationException {

        final String actualFieldTypeName = fieldValidator.getFieldDescriptor().getTypeDescriptor()
            .getName();

        if (!DATE_FIELD_TYPE_NAME.equals(actualFieldTypeName)) {
            throw new ValidationException(format(
                "Cannot validate field ''{0}'' of type ''{1}''. Expected field of type ''{2}''.",
                fieldValidator.getFieldDescriptor().getPath(),
                actualFieldTypeName,
                DATE_FIELD_TYPE_NAME
            ));
        }

        final String actualFieldNodePathName = fieldValidator.getFieldDescriptor().getPath();
        if (!(DATE_PROPERTY_NAME.equals(actualFieldNodePathName))) {
            throw new ValidationException(format(
                "Cannot find property {0} in node ''{1}'' with path ''{2}''.",
                DATE_PROPERTY_NAME,
                fieldValidator.getFieldDescriptor().getPath(),
                actualFieldNodePathName
            ));
        }
    }

    @Override
    public Set<Violation> validate(final IFieldValidator fieldValidator,
                                   final JcrNodeModel documentModel,
                                   final IModel coverageDateFieldModel
    ) throws ValidationException {

        final Set<Violation> violations = new HashSet<>();

        if (isPublicationFinalised(documentModel)) {
            return violations;
        }

        final Calendar publicationDateRaw = getDatePropertyValue(documentModel, DATE_PROPERTY_NAME);

        // Date received by the server from the UI does not contain a time component but is saved
        // into the database as UTC date/time. The time component comes from applying user
        // timezone's offset (raw + DST where observed). In the case of eastern timezones, this
        // changes the day to previous one, which makes it more difficult to make direct compare
        // against UTC date obtained as 'now':
        //
        // For example, for:
        // user in timezone:    input date:   gets saved as:              UTC offset:
        //
        // America/Anchorage    11/11/2018    2018-11-11T09:00:00.000Z    -09:00
        // America/Anchorage    16/08/2018    2018-08-16T08:00:00.000Z    -08:00 (DST)
        //
        // America/New_York     11/11/2018    2018-11-11T05:00:00.000Z    -05:00
        // America/New_York     16/08/2018    2018-08-16T04:00:00.000Z    -04:00 (DST)
        //
        // UTC                  11/11/2018    2018-11-11T00:00:00.000Z     00:00
        // UTC                  16/08/2018    2018-08-16T00:00:00.000Z     00:00
        //
        // Europe/London        11/11/2018    2018-11-11T00:00:00.000Z    +00:00
        // Europe/London        16/08/2018    2018-08-15T23:00:00.000Z    +01:00
        //
        // Europe/Warsaw        11/11/2018    2018-11-10T23:00:00.000Z    +01:00
        // Europe/Warsaw        16/08/2018    2018-08-15T22:00:00.000Z    +02:00 (DST)
        //
        // Europe/Moscow        11/11/2018    2018-11-10T21:00:00.000Z    +03:00
        // Europe/Moscow        16/08/2018    2018-08-15T21:00:00.000Z    +03:00 (no DST in Russia)
        //
        // We want to compare just _dates_ (ignoring the time component) but we can't just simply
        // 'drop' time components of date/time values available on the server  (the 'now' and the
        // saved Publication Date) as this, in many cases, would not given the correct alignment
        // of dates. We have to express both the 'now' and the Publication Date available locally
        // (in UTC) in the timezone of the user first (to re-align the date components), before
        // dropping the time component and comparing dates on just 'days' component.

        final Instant serverNow = TimeProvider.getNowInstant();

        final TimeZone userTimeZone = PluginUserSession.get()
            .getClientInfo()
            .getProperties()
            .getTimeZone();

        final LocalDate publicationDateInUserTz = publicationDateRaw
            .toInstant()
            .atZone(userTimeZone.toZoneId())
            .toLocalDate();

        final LocalDate validPeriodDayFirst = serverNow
            .atZone(userTimeZone.toZoneId())
            .toLocalDate();

        final LocalDate validPeriodDayLast = validPeriodDayFirst
            .plusYears(getValidPeriodLengthInYears())
            .minus(1, DAYS);

        if (publicationDateInUserTz.isBefore(validPeriodDayFirst) || publicationDateInUserTz.isAfter(validPeriodDayLast)) {
            violations.add(
                fieldValidator.newValueViolation(documentModel, getValidationViolationMessage())
            );
        }

        return violations;
    }

    private boolean isPublicationFinalised(final JcrNodeModel documentModel)
        throws ValidationException {
        return getBooleanPropertyValue(documentModel, PUBLICLY_ACCESSIBLE_PROPERTY_NAME);
    }

    private Integer getValidPeriodLengthInYears() {
        return getPluginConfig().getAsInteger(VALID_PERIOD_YEARS_KEY);
    }

    private Calendar getDatePropertyValue(final JcrNodeModel documentModel,
                                          final String propertyName
    ) throws ValidationException {
        try {
            return documentModel.getNode().getProperty(propertyName).getDate();
        } catch (final RepositoryException repositoryException) {
            throw new ValidationException(format(
                "Failed to read property ''{0}'' value", propertyName
            ));
        }
    }

    private boolean getBooleanPropertyValue(final JcrNodeModel documentModel,
                                            final String propertyName
    ) throws ValidationException {
        try {
            return documentModel.getNode().getProperty(propertyName).getBoolean();
        } catch (final RepositoryException repositoryException) {
            throw new ValidationException(format(
                "Failed to read property ''{0}'' value", propertyName
            ));
        }
    }

    private IModel<String> getValidationViolationMessage() {

        final IModel<String> validationViolationMessageTemplate = getViolationMessageTemplate();

        return new IModel<String>() {

            @Override
            public void detach() {
                validationViolationMessageTemplate.detach();
            }

            @Override
            public String getObject() {
                return resolvePlaceholders(validationViolationMessageTemplate.getObject());
            }

            @Override
            public void setObject(final String object) {
                validationViolationMessageTemplate.setObject(object);
            }
        };
    }

    private IModel<String> getViolationMessageTemplate() {
        return new ResourceBundleModel(
            VALIDATORS_TRANSLATION_KEY,
            VIOLATION_MESSAGE_TEMPLATE_KEY,
            Session.get().getLocale()
        );
    }

    private String resolvePlaceholders(final String violationMessageTemplate) {
        return format(violationMessageTemplate, getPluginConfig().getString(
            VALID_PERIOD_YEARS_KEY));
    }

}
