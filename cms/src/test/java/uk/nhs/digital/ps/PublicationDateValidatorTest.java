package uk.nhs.digital.ps;

import static java.text.MessageFormat.format;
import static java.util.Collections.emptySet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.MockitoAnnotations.initMocks;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.hippoecm.frontend.HippoTester;
import org.hippoecm.frontend.model.JcrNodeModel;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.types.IFieldDescriptor;
import org.hippoecm.frontend.types.ITypeDescriptor;
import org.hippoecm.frontend.validation.IFieldValidator;
import org.hippoecm.frontend.validation.ValidationException;
import org.hippoecm.frontend.validation.Violation;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import uk.nhs.digital.common.util.TimeProvider;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.function.Supplier;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;

@RunWith(DataProviderRunner.class)
public class PublicationDateValidatorTest {

    private static final String DATE_FIELD_TYPE_NAME = "CalendarDate";
    private static final String DATE_PROPERTY_NAME = "publicationsystem:NominalDate";
    private static final String PUBLICLY_ACCESSIBLE_PROPERTY_NAME = "publicationsystem:PubliclyAccessible";
    private static final String MAX_YEARS_AHEAD_PARAM_KEY = "validPeriodLengthInYears";

    private static final boolean PUB_FINALISED_STATUS = true;
    private static final boolean PUB_UPCOMING_STATUS = false;

    private static final int MAX_YEARS_AHEAD = 7;

    // Always UTC as calculated on server. No need to test variations of NOW as it always gets
    // truncated down to midnight.
    private static final String NOW = "2018-06-16T20:16:00Z";

    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Mock private IFieldValidator fieldValidator;
    @Mock private IFieldDescriptor fieldDescriptor;
    @Mock private ITypeDescriptor typeDescriptor;
    @Mock private Node documentNode;
    @Mock private JcrNodeModel documentNodeModel;
    @Mock private JcrNodeModel fieldModel;
    @Mock private IPluginConfig pluginConfig;
    @Mock private IPluginContext pluginContext;
    @Mock private IModel<String> violationMessageTranslationModel;
    @Mock private Property nominalDateProperty;
    @Mock private Property publiclyAccessibleProperty;

    private static InstantSupplierStub instantSupplierStub;

    private PublicationDateValidator publicationDateValidator;
    private static final String VIOLATION_MESSAGE_TRANSLATION_KEY = "publicationsystem-publication-date";
    private HippoTester hippoTester;

    @BeforeClass
    public static void init() throws IllegalAccessException {
        instantSupplierStub = new InstantSupplierStub();
        setMockTimeSupplier(instantSupplierStub);

        instantSupplierStub.setRawDateTime(NOW);
    }

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        initialiseWicketApplication();

        programMocksForValidationTestForHappyPath();

        publicationDateValidator = new PublicationDateValidator(
            pluginContext,
            pluginConfig
        );
    }

    @Test
    public void failsPreValidation_whenCalledForInvalidFieldType() throws Exception {

        // given
        final String unsupportedFieldTypeName = newRandomString();
        given(typeDescriptor.getName()).willReturn(unsupportedFieldTypeName);

        expectedException.expect(ValidationException.class);
        expectedException.expectMessage(format(
            "Cannot validate field ''{0}'' of type ''{1}''. Expected field of type ''{2}''.",
            fieldValidator.getFieldDescriptor().getPath(),
            unsupportedFieldTypeName,
            DATE_FIELD_TYPE_NAME
        ));

        // when
        publicationDateValidator.preValidation(fieldValidator);

        // then
        // expectations as specified in 'given'
    }

    @Test
    public void failsPreValidation_whenCalledForInvalidProperty() throws Exception {

        // given
        final String unsupportedFieldNodePath = newRandomString();
        given(fieldDescriptor.getPath()).willReturn(unsupportedFieldNodePath);

        expectedException.expect(ValidationException.class);
        expectedException.expectMessage(format(
            "Cannot find property {0} in node ''{1}'' with path ''{2}''.",
            DATE_PROPERTY_NAME,
            fieldValidator.getFieldDescriptor().getPath(),
            unsupportedFieldNodePath
        ));

        // when
        publicationDateValidator.preValidation(fieldValidator);

        // then
        // expectations as specified in 'given'
    }

    @Test
    @UseDataProvider("datesOutOfValidRange")
    public void reportsViolation_forUpcomingPublicationDateOutsideOfAllowedRange(
        final String publicationDateInUserTx,
        final String userOffset,
        final String testCaseDescription
    ) throws ValidationException, RepositoryException {

        // given
        given(nominalDateProperty.getDate()).willReturn(calendarFor(publicationDateInUserTx, userOffset));

        setUserTimezoneUtcOffset(userOffset);

        final Violation expectedViolation = new Violation(
            emptySet(), violationMessageTranslationModel
        );

        given(fieldValidator.newValueViolation(
            eq(documentNodeModel),
            argThat((IModel<String> argument) -> argument.getObject().contains(
                VIOLATION_MESSAGE_TRANSLATION_KEY
            ))))
            .willReturn(expectedViolation);

        // when
        final Set<Violation> actualValidationViolations =
            publicationDateValidator.validate(fieldValidator, documentNodeModel, fieldModel);

        // then
        then(documentNode).should().getProperty(PUBLICLY_ACCESSIBLE_PROPERTY_NAME);
        then(documentNode).should().getProperty(DATE_PROPERTY_NAME);

        assertThat("Exactly one violation has been reported for date " + testCaseDescription,
            actualValidationViolations, hasSize(1)
        );

        final Violation actualViolation = actualValidationViolations.iterator().next();

        assertThat("Violation is not null for date " + testCaseDescription,
            actualViolation, notNullValue()
        );
        assertThat("Correct violation has been reported for date " + testCaseDescription,
            actualViolation.getMessage(), is(violationMessageTranslationModel)
        );
    }

    @Test
    @UseDataProvider("datesWithinValidRange")
    public void reportsNoViolation_forUpcomingPublicationDateWithinValidRange(
        final String publicationDateInUserTz,
        final String userOffset,
        final String testCaseDescription
    ) throws ValidationException, RepositoryException {

        // given
        given(nominalDateProperty.getDate()).willReturn(calendarFor(publicationDateInUserTz, userOffset));

        setUserTimezoneUtcOffset(userOffset);

        // when
        final Set<Violation> actualValidationViolations =
            publicationDateValidator.validate(fieldValidator, documentNodeModel, fieldModel);

        // then
        then(documentNode).should().getProperty(DATE_PROPERTY_NAME);

        assertThat("No violation has been reported for date " + testCaseDescription,
            actualValidationViolations, hasSize(0)
        );
    }

    @Test
    public void ignoresFinalizedPublication() throws RepositoryException, ValidationException {

        // given
        given(publiclyAccessibleProperty.getBoolean()).willReturn(PUB_FINALISED_STATUS);

        // when
        final Set<Violation> actualValidationViolations =
            publicationDateValidator.validate(fieldValidator, documentNodeModel, fieldModel);

        // then
        assertThat("No violation has been reported.", actualValidationViolations, is(empty()));

        then(documentNode).should(never()).getProperty(DATE_PROPERTY_NAME);
        then(pluginConfig).should(never()).getAsInteger(MAX_YEARS_AHEAD_PARAM_KEY);
    }

    @DataProvider
    public static Object[][] datesOutOfValidRange() {
        return new Object[][] {

            // NOW: 2018-06-16T20:16:00Z

            // publication date  tz offset      test case description
            // ----------------|-------------|-----------------------------------------

            // dates BEFORE the valid range:

            // Western timezones:
            {"2018-06-14",       "-12:00",       "farther before the valid range"},
            {"2018-06-15",       "-12:00",       "right before the valid range"},
            {"2018-06-14",       "-06:00",       "farther before the valid range"},
            {"2018-06-15",       "-06:00",       "right before the valid range"},
            {"2018-06-14",       "-01:00",       "farther before the valid range"},
            {"2018-06-15",       "-01:00",       "right before the valid range"},

            // UTC
            {"2018-06-14",       "+00:00",       "farther before the valid range"},
            {"2018-06-15",       "+00:00",       "one day before the valid range"},

            // Eastern timezones
            {"2018-06-14",       "+01:00",       "farther before the valid range"},
            {"2018-06-15",       "+01:00",       "right before the valid range"},
            {"2018-06-15",       "+06:00",       "farther before the valid range"},
            {"2018-06-16",       "+06:00",       "right before the valid range"},
            {"2018-06-15",       "+12:00",       "farther before the valid range"},
            {"2018-06-16",       "+12:00",       "right before the valid range"},

            // dates AFTER the valid range:

            // Western timezones
            {"2025-06-16",       "-12:00",       "right after the valid range"},
            {"2025-06-17",       "-12:00",       "farther after the valid range"},
            {"2025-06-16",       "-06:00",       "right after the valid range"},
            {"2025-06-17",       "-06:00",       "farther after the valid range"},
            {"2025-06-16",       "-01:00",       "right after the valid range"},
            {"2025-06-17",       "-01:00",       "farther after the valid range"},

            // UTC
            {"2025-06-16",       "+00:00",       "right after the valid range"},
            {"2025-06-17",       "+00:00",       "farther after the valid range"},

            // Eastern timezones
            {"2025-06-16",       "+01:00",       "right after the valid range"},
            {"2025-06-17",       "+01:00",       "farther after the valid range"},
            {"2025-06-17",       "+06:00",       "right after the valid range"},
            {"2025-06-18",       "+06:00",       "farther after the valid range"},
            {"2025-06-17",       "+12:00",       "right after the valid range"},
            {"2025-06-18",       "+12:00",       "farther after the valid range"},
        };
    }

    @DataProvider
    public static Object[][] datesWithinValidRange() {
        return new Object[][] {

            // NOW: 2018-06-16T20:16:00Z

            // publication date  tz offset      test case description
            // ----------------|-------------|-----------------------------------------

            // dates at the BEGINNING of the valid range:

            // Western timezones
            {"2018-06-16",       "-12:00",     "right after the beginning of the valid range"},
            {"2018-06-17",       "-12:00",     "farther after the beginning of the valid range"},
            {"2018-06-16",       "-06:00",     "right after the beginning of the valid range"},
            {"2018-06-17",       "-06:00",     "farther after the beginning of the valid range"},
            {"2018-06-16",       "-01:00",     "right after the beginning of the valid range"},
            {"2018-06-17",       "-01:00",     "farther after the beginning of the valid range"},

            // UTC
            {"2018-06-16",       "+00:00",     "right after the beginning of the valid range"},
            {"2018-06-17",       "+00:00",     "farther after the beginning of the valid range"},

            // Eastern timezones
            {"2018-06-16",       "+01:00",     "right after the beginning of the valid range"},
            {"2018-06-17",       "+01:00",     "farther after the beginning of the valid range"},
            {"2018-06-17",       "+06:00",     "right after the beginning of the valid range"},
            {"2018-06-18",       "+06:00",     "farther after the beginning of the valid range"},
            {"2018-06-17",       "+12:00",     "right after the beginning of the valid range"},
            {"2018-06-18",       "+12:00",     "farther after the beginning of the valid range"},

            // dates at the END of the valid range:

            // Western timezones
            {"2025-06-14",       "-12:00",     "farther before the end of the valid range"},
            {"2025-06-15",       "-12:00",     "right before the end of the valid range"},
            {"2025-06-14",       "-06:00",     "farther before the end of the valid range"},
            {"2025-06-15",       "-06:00",     "right before the end of the valid range"},
            {"2025-06-14",       "-01:00",     "farther before the end of the valid range"},
            {"2025-06-15",       "-01:00",     "right before the end of the valid range"},

            // UTC
            {"2025-06-14",       "+00:00",     "farther before the end of the valid range"},
            {"2025-06-15",       "+00:00",     "right before the end of the valid range"},

            // Eastern timezones
            {"2025-06-14",       "+01:00",     "farther before the end of the valid range"},
            {"2025-06-15",       "+01:00",     "right before the end of the valid range"},
            {"2025-06-15",       "+06:00",     "farther before the end of the valid range"},
            {"2025-06-16",       "+06:00",     "right before the end of the valid range"},
            {"2025-06-15",       "+12:00",     "farther before the end of the valid range"},
            {"2025-06-16",       "+12:00",     "right before the end of the valid range"},
        };
    }

    /**
     * For given date and timezone offset, returns calendar date representing value
     * saved by Hippo in the database for Publication Date values received from CMS
     * UI.
     */
    private Calendar calendarFor(final String date, final String zoneOffset) {

        final ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(
            LocalDateTime.of(LocalDate.parse(date), LocalTime.MIDNIGHT),
            ZoneOffset.of(zoneOffset),
            ZoneId.of("Z") // server's timezone (Zulu = UTC)
        );

        return GregorianCalendar.from(zonedDateTime);
    }

    private void initialiseWicketApplication() {
        // This initialises various statically stored values that Wicket uses,
        // most notably, Session and Application whose absence was causing NullPointerException during call to
        // org.hippoecm.frontend.editor.validator.plugins.AbstractCmsValidator.getTranslation(java.lang.String).

        hippoTester = new HippoTester();
    }

    private void setUserTimezoneUtcOffset(final String utcOffset) {

        ((WebClientInfo)hippoTester.getSession().getClientInfo())
            .getProperties()
            .setTimeZone(TimeZone.getTimeZone(ZoneId.of(utcOffset)));
    }

    private String newRandomString() {
        return UUID.randomUUID().toString();
    }

    private void programMocksForValidationTestForHappyPath() throws RepositoryException {

        // setup for pre-validation

        given(pluginConfig.getName()).willReturn("SomeTestConfig");
        given(pluginConfig.getAsInteger(MAX_YEARS_AHEAD_PARAM_KEY)).willReturn(MAX_YEARS_AHEAD);

        given(fieldValidator.getFieldDescriptor()).willReturn(fieldDescriptor);

        given(fieldDescriptor.getTypeDescriptor()).willReturn(typeDescriptor);
        given(fieldDescriptor.getPath()).willReturn(DATE_PROPERTY_NAME);

        given(typeDescriptor.getName()).willReturn(DATE_FIELD_TYPE_NAME);

        // setup for actual validation

        final Calendar publicationDateWithinValidRange = calendarFor("2018-06-20", "+00:00");
        given(nominalDateProperty.getDate()).willReturn(publicationDateWithinValidRange);
        given(documentNode.getProperty(DATE_PROPERTY_NAME)).willReturn(nominalDateProperty);

        given(documentNode.getProperty(PUBLICLY_ACCESSIBLE_PROPERTY_NAME))
            .willReturn(publiclyAccessibleProperty);
        given(publiclyAccessibleProperty.getBoolean()).willReturn(
            PublicationDateValidatorTest.PUB_UPCOMING_STATUS);

        given(documentNodeModel.getNode()).willReturn(documentNode);
    }

    private static void setMockTimeSupplier(final Supplier<Instant> instantSupplierStub)
        throws IllegalAccessException {

        final Field nowInstantSupplierField =
            FieldUtils.getDeclaredField(TimeProvider.class, "nowInstantSupplier", true);

        FieldUtils.writeStaticField(nowInstantSupplierField, instantSupplierStub, true);
    }

    private static class InstantSupplierStub implements Supplier<Instant> {

        private String rawDateTime;

        void setRawDateTime(final String rawDateTime) {
            this.rawDateTime = rawDateTime;
        }

        @Override
        public Instant get() {
            return Instant.from(ZonedDateTime.parse(rawDateTime));
        }
    }
}
