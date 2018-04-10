package uk.nhs.digital.ps;

import static java.text.MessageFormat.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.MockitoAnnotations.initMocks;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.hippoecm.frontend.model.JcrNodeModel;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.types.IFieldDescriptor;
import org.hippoecm.frontend.types.ITypeDescriptor;
import org.hippoecm.frontend.validation.IFieldValidator;
import org.hippoecm.frontend.validation.ValidationException;
import org.hippoecm.frontend.validation.Violation;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.util.Calendar;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import javax.jcr.Node;
import javax.jcr.Property;

public class CoverageDatesValidatorTest {

    private static final String SUPPORTED_FIELD_TYPE_NAME = "CalendarDate";
    private static final String HIPPO_COVERAGE_START_PROPERTY_NAME = "publicationsystem:CoverageStart";
    private static final String HIPPO_COVERAGE_END_PROPERTY_NAME = "publicationsystem:CoverageEnd";
    private static final String HIPPO_NOMINAL_DATE_PROPERTY_NAME = "publicationsystem:NominalDate";

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

    private CoverageDatesValidator coverageDatesValidator;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        given(pluginConfig.getName()).willReturn("SomeTestConfig");

        initialiseWicketApplication();

        coverageDatesValidator = new CoverageDatesValidator(
            pluginContext,
            pluginConfig
        );
    }

    @Test
    public void reportsError_whenCalledForInvalidFieldType() throws Exception {

        // given
        programMocksForPreValidationTestForHappyPath();
        reset(typeDescriptor);

        final String unsupportedFieldTypeName = newRandomString();
        given(typeDescriptor.getName()).willReturn(unsupportedFieldTypeName);

        expectedException.expect(ValidationException.class);
        expectedException.expectMessage(format(
            "Cannot validate field ''{0}'' of type ''{1}''. This validator only supports fields of type ''{2}''.",
            fieldValidator.getFieldDescriptor().getPath(),
            unsupportedFieldTypeName,
            SUPPORTED_FIELD_TYPE_NAME
        ));

        // when
        coverageDatesValidator.preValidation(fieldValidator);

        // then
        // expectations as specified in 'given'
    }

    @Test
    public void reportsNoValidationViolations_forCorrectlyPopulatedDates() throws Exception {

        // Given
        final Property coverageStartProperty = mock(Property.class);
        given(coverageStartProperty.getDate()).willReturn(generateHippoDateTodayAddDays(-2));
        given(documentNode.getProperty(HIPPO_COVERAGE_START_PROPERTY_NAME)).willReturn(coverageStartProperty);

        final Property coverageEndProperty = mock(Property.class);
        given(coverageEndProperty.getDate()).willReturn(generateHippoDateTodayAddDays(-1));
        given(documentNode.getProperty(HIPPO_COVERAGE_END_PROPERTY_NAME)).willReturn(coverageEndProperty);

        final Property nominalDateProperty = mock(Property.class);
        given(nominalDateProperty.getDate()).willReturn(generateHippoDateTodayAddDays(10));
        given(documentNode.getProperty(HIPPO_NOMINAL_DATE_PROPERTY_NAME)).willReturn(nominalDateProperty);

        given(documentNodeModel.getNode()).willReturn(documentNode);

        // when
        final Set<Violation> actualValidationViolations =
            coverageDatesValidator.validate(fieldValidator, documentNodeModel, fieldModel);

        // then
        then(documentNode).should().getProperty(HIPPO_COVERAGE_START_PROPERTY_NAME);
        then(documentNode).should().getProperty(HIPPO_COVERAGE_END_PROPERTY_NAME);
        then(documentNode).should().getProperty(HIPPO_NOMINAL_DATE_PROPERTY_NAME);

        assertThat("No violation has been reported.", actualValidationViolations, is(empty()));
    }

    @Test
    public void reportsError_whenEndDateLessThanStartDate() throws Exception {

        final Violation expectedViolation = new Violation(Collections.emptySet(), violationMessageTranslationModel);

        // Given
        final Property coverageStartProperty = mock(Property.class);
        given(coverageStartProperty.getDate()).willReturn(generateHippoDateTodayAddDays(-2));
        given(documentNode.getProperty(HIPPO_COVERAGE_START_PROPERTY_NAME)).willReturn(coverageStartProperty);

        final Property coverageEndProperty = mock(Property.class);
        given(coverageEndProperty.getDate()).willReturn(generateHippoDateTodayAddDays(-4));
        given(documentNode.getProperty(HIPPO_COVERAGE_END_PROPERTY_NAME)).willReturn(coverageEndProperty);

        final Property nominalDateProperty = mock(Property.class);
        given(nominalDateProperty.getDate()).willReturn(generateHippoDateEmpty());
        given(documentNode.getProperty(HIPPO_NOMINAL_DATE_PROPERTY_NAME)).willReturn(nominalDateProperty);

        given(documentNodeModel.getNode()).willReturn(documentNode);

        given(fieldValidator.newValueViolation(eq(documentNodeModel), isA(IModel.class)))
            .willReturn(expectedViolation);

        // when
        final Set<Violation> actualValidationViolations =
            coverageDatesValidator.validate(fieldValidator, documentNodeModel, fieldModel);

        // then
        then(documentNode).should().getProperty(HIPPO_COVERAGE_START_PROPERTY_NAME);
        then(documentNode).should().getProperty(HIPPO_COVERAGE_END_PROPERTY_NAME);
        then(documentNode).should().getProperty(HIPPO_NOMINAL_DATE_PROPERTY_NAME);

        assertThat("Exactly one violation has been reported", actualValidationViolations, hasSize(1));

        final Violation actualViolation = actualValidationViolations.iterator().next();
        assertThat("Violation is not null", actualViolation, notNullValue());
        assertThat("Correct violation has been reported.",actualViolation.getMessage(),
            is(violationMessageTranslationModel));
    }

    @Test
    public void reportsError_whenEndDateButNoStartDate() throws Exception {

        final Violation expectedViolation = new Violation(Collections.emptySet(), violationMessageTranslationModel);

        // Given
        final Property coverageStartProperty = mock(Property.class);
        given(coverageStartProperty.getDate()).willReturn(generateHippoDateEmpty());
        given(documentNode.getProperty(HIPPO_COVERAGE_START_PROPERTY_NAME)).willReturn(coverageStartProperty);

        final Property coverageEndProperty = mock(Property.class);
        given(coverageEndProperty.getDate()).willReturn(generateHippoDateTodayAddDays(-7));
        given(documentNode.getProperty(HIPPO_COVERAGE_END_PROPERTY_NAME)).willReturn(coverageEndProperty);

        final Property nominalDateProperty = mock(Property.class);
        given(nominalDateProperty.getDate()).willReturn(generateHippoDateEmpty());
        given(documentNode.getProperty(HIPPO_NOMINAL_DATE_PROPERTY_NAME)).willReturn(nominalDateProperty);

        given(documentNodeModel.getNode()).willReturn(documentNode);

        given(fieldValidator.newValueViolation(eq(documentNodeModel), isA(IModel.class)))
            .willReturn(expectedViolation);

        // when
        final Set<Violation> actualValidationViolations =
            coverageDatesValidator.validate(fieldValidator, documentNodeModel, fieldModel);

        // then
        then(documentNode).should().getProperty(HIPPO_COVERAGE_START_PROPERTY_NAME);
        then(documentNode).should().getProperty(HIPPO_COVERAGE_END_PROPERTY_NAME);
        then(documentNode).should().getProperty(HIPPO_NOMINAL_DATE_PROPERTY_NAME);

        assertThat("Exactly one violation has been reported", actualValidationViolations, hasSize(1));

        final Violation actualViolation = actualValidationViolations.iterator().next();
        assertThat("Violation is not null", actualViolation, notNullValue());
        assertThat("Correct violation has been reported.",actualViolation.getMessage(),
            is(violationMessageTranslationModel));
    }

    @Test
    public void reportsError_whenStartDateButNoEndDate() throws Exception {

        final Violation expectedViolation = new Violation(Collections.emptySet(), violationMessageTranslationModel);

        // Given
        final Property coverageStartProperty = mock(Property.class);
        given(coverageStartProperty.getDate()).willReturn(generateHippoDateTodayAddDays(-7));
        given(documentNode.getProperty(HIPPO_COVERAGE_START_PROPERTY_NAME)).willReturn(coverageStartProperty);

        final Property coverageEndProperty = mock(Property.class);
        given(coverageEndProperty.getDate()).willReturn(generateHippoDateEmpty());
        given(documentNode.getProperty(HIPPO_COVERAGE_END_PROPERTY_NAME)).willReturn(coverageEndProperty);

        final Property nominalDateProperty = mock(Property.class);
        given(nominalDateProperty.getDate()).willReturn(generateHippoDateEmpty());
        given(documentNode.getProperty(HIPPO_NOMINAL_DATE_PROPERTY_NAME)).willReturn(nominalDateProperty);

        given(documentNodeModel.getNode()).willReturn(documentNode);

        given(fieldValidator.newValueViolation(eq(documentNodeModel), isA(IModel.class)))
            .willReturn(expectedViolation);

        // when
        final Set<Violation> actualValidationViolations =
            coverageDatesValidator.validate(fieldValidator, documentNodeModel, fieldModel);

        // then
        then(documentNode).should().getProperty(HIPPO_COVERAGE_START_PROPERTY_NAME);
        then(documentNode).should().getProperty(HIPPO_COVERAGE_END_PROPERTY_NAME);
        then(documentNode).should().getProperty(HIPPO_NOMINAL_DATE_PROPERTY_NAME);

        assertThat("Exactly one violation has been reported", actualValidationViolations, hasSize(1));

        final Violation actualViolation = actualValidationViolations.iterator().next();
        assertThat("Violation is not null", actualViolation, notNullValue());
        assertThat("Correct violation has been reported.",actualViolation.getMessage(),
            is(violationMessageTranslationModel));
    }

    @Test
    public void reportsError_whenCoverageDatesGreaterThanNominalDate() throws Exception {

        final Violation expectedViolation = new Violation(Collections.emptySet(), violationMessageTranslationModel);

        // Given
        final Property coverageStartProperty = mock(Property.class);
        given(coverageStartProperty.getDate()).willReturn(generateHippoDateTodayAddDays(7));
        given(documentNode.getProperty(HIPPO_COVERAGE_START_PROPERTY_NAME)).willReturn(coverageStartProperty);

        final Property coverageEndProperty = mock(Property.class);
        given(coverageEndProperty.getDate()).willReturn(generateHippoDateTodayAddDays(7));
        given(documentNode.getProperty(HIPPO_COVERAGE_END_PROPERTY_NAME)).willReturn(coverageEndProperty);

        final Property nominalDateProperty = mock(Property.class);
        given(nominalDateProperty.getDate()).willReturn(generateHippoDateTodayAddDays(2));
        given(documentNode.getProperty(HIPPO_NOMINAL_DATE_PROPERTY_NAME)).willReturn(nominalDateProperty);

        given(documentNodeModel.getNode()).willReturn(documentNode);

        given(fieldValidator.newValueViolation(eq(documentNodeModel), isA(IModel.class)))
            .willReturn(expectedViolation);

        // when
        final Set<Violation> actualValidationViolations =
            coverageDatesValidator.validate(fieldValidator, documentNodeModel, fieldModel);

        // then
        then(documentNode).should().getProperty(HIPPO_COVERAGE_START_PROPERTY_NAME);
        then(documentNode).should().getProperty(HIPPO_COVERAGE_END_PROPERTY_NAME);
        then(documentNode).should().getProperty(HIPPO_NOMINAL_DATE_PROPERTY_NAME);

        assertThat("Exactly one violation has been reported", actualValidationViolations, hasSize(1));

        final Violation actualViolation = actualValidationViolations.iterator().next();
        assertThat("Violation is not null", actualViolation, notNullValue());
        assertThat("Correct violation has been reported.",actualViolation.getMessage(),
            is(violationMessageTranslationModel));
    }

    private void initialiseWicketApplication() {
        // This initialises various statically stored values that Wicket uses,
        // most notably, Session and Application whose absence was causing NullPointerException during call to
        // org.hippoecm.frontend.editor.validator.plugins.AbstractCmsValidator.getTranslation(java.lang.String).
        new WicketTester(new WebApplication() {
            public Class<? extends Page> getHomePage() {
                return null;
            }
        });
    }

    private String newRandomString() {
        return UUID.randomUUID().toString();
    }

    private void programMocksForPreValidationTestForHappyPath() {

        given(fieldValidator.getFieldDescriptor()).willReturn(fieldDescriptor);
        given(fieldDescriptor.getTypeDescriptor()).willReturn(typeDescriptor);
        given(typeDescriptor.getName()).willReturn(SUPPORTED_FIELD_TYPE_NAME);
        given(fieldDescriptor.getPath()).willReturn(newRandomString());
    }

    private Calendar generateHippoDateEmpty() {
        // Hippo empty dates are stored as 0001-01-01T12:00:00Z
        Calendar emptyDate = Calendar.getInstance();
        emptyDate.clear();
        emptyDate.set(1,Calendar.JANUARY,1,0,0,0);

        return emptyDate;
    }

    private Calendar generateHippoDateTodayAddDays(int daysToAdd) {
        Calendar date = Calendar.getInstance();
        date.add(Calendar.DAY_OF_MONTH, daysToAdd);
        return date;
    }
}
