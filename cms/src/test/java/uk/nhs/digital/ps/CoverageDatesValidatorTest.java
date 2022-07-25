package uk.nhs.digital.ps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.Violation;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import javax.jcr.Node;
import javax.jcr.Property;

public class CoverageDatesValidatorTest {
    private static final String HIPPO_COVERAGE_START_PROPERTY_NAME = "publicationsystem:CoverageStart";
    private static final String HIPPO_COVERAGE_END_PROPERTY_NAME = "publicationsystem:CoverageEnd";
    private static final String HIPPO_NOMINAL_DATE_PROPERTY_NAME = "publicationsystem:NominalDate";
    
    @Mock
    private ValidationContext validationContext;

    @Mock
    private Violation violation;

    private CoverageDatesValidator coverageDatesValidator;

    @Before
    public void setUp() throws Exception {
        openMocks(this);
        coverageDatesValidator = new CoverageDatesValidator();
    }

    @Test
    public void reportsNoValidationViolations_forCorrectlyPopulatedDates() throws Exception {

        final Node parentNodeMock = mock(Node.class);
        given(validationContext.getParentNode()).willReturn(parentNodeMock);

        final Property coverageStartProperty = mock(Property.class);
        given(parentNodeMock.getProperty(HIPPO_COVERAGE_START_PROPERTY_NAME)).willReturn(coverageStartProperty);
        given(coverageStartProperty.getDate()).willReturn(generateHippoDateTodayAddDays(-2));

        final Property coverageEndProperty = mock(Property.class);
        given(parentNodeMock.getProperty(HIPPO_COVERAGE_END_PROPERTY_NAME)).willReturn(coverageEndProperty);
        given(coverageEndProperty.getDate()).willReturn(generateHippoDateTodayAddDays(-1));

        final Property nominalDateProperty = mock(Property.class);
        given(parentNodeMock.getProperty(HIPPO_NOMINAL_DATE_PROPERTY_NAME)).willReturn(nominalDateProperty);
        given(nominalDateProperty.getDate()).willReturn(generateHippoDateTodayAddDays(10));

        final Optional<Violation> violation = coverageDatesValidator.validate(validationContext, new Date());

        // then
        then(parentNodeMock).should().getProperty(HIPPO_COVERAGE_START_PROPERTY_NAME);
        then(parentNodeMock).should().getProperty(HIPPO_COVERAGE_END_PROPERTY_NAME);
        then(parentNodeMock).should().getProperty(HIPPO_NOMINAL_DATE_PROPERTY_NAME);

        assertThat("No violation has been reported.", !violation.isPresent());
    }

    @Test
    public void reportsError_whenEndDateLessThanStartDate() throws Exception {

        final Node parentNodeMock = mock(Node.class);
        given(validationContext.getParentNode()).willReturn(parentNodeMock);

        final Property coverageStartProperty = mock(Property.class);
        given(parentNodeMock.getProperty(HIPPO_COVERAGE_START_PROPERTY_NAME)).willReturn(coverageStartProperty);
        given(coverageStartProperty.getDate()).willReturn(generateHippoDateTodayAddDays(-2));

        final Property coverageEndProperty = mock(Property.class);
        given(parentNodeMock.getProperty(HIPPO_COVERAGE_END_PROPERTY_NAME)).willReturn(coverageEndProperty);
        given(coverageEndProperty.getDate()).willReturn(generateHippoDateTodayAddDays(-4));

        final Property nominalDateProperty = mock(Property.class);
        given(parentNodeMock.getProperty(HIPPO_NOMINAL_DATE_PROPERTY_NAME)).willReturn(nominalDateProperty);
        given(nominalDateProperty.getDate()).willReturn(generateHippoDateTodayAddDays(10));

        given(validationContext.createViolation("end-before-start")).willReturn(violation);

        final Optional<Violation> violation = coverageDatesValidator.validate(validationContext, new Date());

        // then
        then(parentNodeMock).should().getProperty(HIPPO_COVERAGE_START_PROPERTY_NAME);
        then(parentNodeMock).should().getProperty(HIPPO_COVERAGE_END_PROPERTY_NAME);
        then(parentNodeMock).should().getProperty(HIPPO_NOMINAL_DATE_PROPERTY_NAME);
        then(validationContext).should().createViolation("end-before-start");

        assertThat("Violation has been reported.", violation.isPresent());

    }

    @Test
    public void reportsError_whenEndDateButNoStartDate() throws Exception {

        final Node parentNodeMock = mock(Node.class);
        given(validationContext.getParentNode()).willReturn(parentNodeMock);

        final Property coverageStartProperty = mock(Property.class);
        given(parentNodeMock.getProperty(HIPPO_COVERAGE_START_PROPERTY_NAME)).willReturn(coverageStartProperty);
        given(coverageStartProperty.getDate()).willReturn(generateHippoDateEmpty());

        final Property coverageEndProperty = mock(Property.class);
        given(parentNodeMock.getProperty(HIPPO_COVERAGE_END_PROPERTY_NAME)).willReturn(coverageEndProperty);
        given(coverageEndProperty.getDate()).willReturn(generateHippoDateTodayAddDays(-4));

        final Property nominalDateProperty = mock(Property.class);
        given(parentNodeMock.getProperty(HIPPO_NOMINAL_DATE_PROPERTY_NAME)).willReturn(nominalDateProperty);
        given(nominalDateProperty.getDate()).willReturn(generateHippoDateTodayAddDays(10));

        given(validationContext.createViolation("no-start-provided")).willReturn(violation);

        final Optional<Violation> violation = coverageDatesValidator.validate(validationContext, new Date());

        // then
        then(parentNodeMock).should().getProperty(HIPPO_COVERAGE_START_PROPERTY_NAME);
        then(parentNodeMock).should().getProperty(HIPPO_COVERAGE_END_PROPERTY_NAME);
        then(parentNodeMock).should().getProperty(HIPPO_NOMINAL_DATE_PROPERTY_NAME);
        then(validationContext).should().createViolation("no-start-provided");

        assertThat("Violation has been reported.", violation.isPresent());

    }

    @Test
    public void reportsError_whenStartDateButNoEndDate() throws Exception {

        final Node parentNodeMock = mock(Node.class);
        given(validationContext.getParentNode()).willReturn(parentNodeMock);

        final Property coverageStartProperty = mock(Property.class);
        given(parentNodeMock.getProperty(HIPPO_COVERAGE_START_PROPERTY_NAME)).willReturn(coverageStartProperty);
        given(coverageStartProperty.getDate()).willReturn(generateHippoDateTodayAddDays(-2));

        final Property coverageEndProperty = mock(Property.class);
        given(parentNodeMock.getProperty(HIPPO_COVERAGE_END_PROPERTY_NAME)).willReturn(coverageEndProperty);
        given(coverageEndProperty.getDate()).willReturn(generateHippoDateEmpty());

        final Property nominalDateProperty = mock(Property.class);
        given(parentNodeMock.getProperty(HIPPO_NOMINAL_DATE_PROPERTY_NAME)).willReturn(nominalDateProperty);
        given(nominalDateProperty.getDate()).willReturn(generateHippoDateTodayAddDays(10));

        given(validationContext.createViolation("no-end-provided")).willReturn(violation);

        final Optional<Violation> violation = coverageDatesValidator.validate(validationContext, new Date());

        // then
        then(parentNodeMock).should().getProperty(HIPPO_COVERAGE_START_PROPERTY_NAME);
        then(parentNodeMock).should().getProperty(HIPPO_COVERAGE_END_PROPERTY_NAME);
        then(parentNodeMock).should().getProperty(HIPPO_NOMINAL_DATE_PROPERTY_NAME);
        then(validationContext).should().createViolation("no-end-provided");

        assertThat("Violation has been reported.", violation.isPresent());
    }


    @Test
    public void reportsError_whenCoverageDatesGreaterThanNominalDate() throws Exception {
        final Node parentNodeMock = mock(Node.class);
        given(validationContext.getParentNode()).willReturn(parentNodeMock);

        final Property coverageStartProperty = mock(Property.class);
        given(parentNodeMock.getProperty(HIPPO_COVERAGE_START_PROPERTY_NAME)).willReturn(coverageStartProperty);
        given(coverageStartProperty.getDate()).willReturn(generateHippoDateTodayAddDays(-2));

        final Property coverageEndProperty = mock(Property.class);
        given(parentNodeMock.getProperty(HIPPO_COVERAGE_END_PROPERTY_NAME)).willReturn(coverageEndProperty);
        given(coverageEndProperty.getDate()).willReturn(generateHippoDateTodayAddDays(-1));

        final Property nominalDateProperty = mock(Property.class);
        given(parentNodeMock.getProperty(HIPPO_NOMINAL_DATE_PROPERTY_NAME)).willReturn(nominalDateProperty);
        given(nominalDateProperty.getDate()).willReturn(generateHippoDateTodayAddDays(-10));

        given(validationContext.createViolation("dates-after-nominal")).willReturn(violation);

        final Optional<Violation> violation = coverageDatesValidator.validate(validationContext, new Date());

        // then
        then(parentNodeMock).should().getProperty(HIPPO_COVERAGE_START_PROPERTY_NAME);
        then(parentNodeMock).should().getProperty(HIPPO_COVERAGE_END_PROPERTY_NAME);
        then(parentNodeMock).should().getProperty(HIPPO_NOMINAL_DATE_PROPERTY_NAME);
        then(validationContext).should().createViolation("dates-after-nominal");

        assertThat("Violation has been reported.", violation.isPresent());
    }

    private Calendar generateHippoDateEmpty() {
        // Hippo empty dates are stored as 0001-01-01T12:00:00Z
        Calendar emptyDate = Calendar.getInstance();
        emptyDate.clear();
        emptyDate.set(1, Calendar.JANUARY, 1, 0, 0, 0);

        return emptyDate;
    }

    private Calendar generateHippoDateTodayAddDays(int daysToAdd) {
        Calendar date = Calendar.getInstance();
        date.add(Calendar.DAY_OF_MONTH, daysToAdd);
        return date;
    }
}
