package uk.nhs.digital.ps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.Violation;
import org.onehippo.repository.mock.MockNode;

import java.time.LocalDate;
import java.util.*;
import javax.jcr.Node;


public class PublicationDateValidatorTest {


    @Mock
    private ValidationContext validationContext;

    @Mock
    private Violation violation;

    private PublicationDateValidator publicationDateValidator;

    private static final Long validPeriodLengthInYears = 7L;

    @Before
    public void setUp() throws Exception {
        openMocks(this);
    }

    @Test
    public void publicationFinalisedNoViolation() throws Exception {
        Node config = MockNode.root();
        config.setProperty("validPeriodLengthInYears", validPeriodLengthInYears);
        config.setProperty("publicationsystem:PubliclyAccessible", true);

        publicationDateValidator = new PublicationDateValidator(config);

        given(validationContext.getDocumentNode()).willReturn(config);

        final Optional<Violation> violation = publicationDateValidator.validate(validationContext, new Date());

        assertThat("No violation has been reported.", !violation.isPresent());
    }

    @Test
    public void publicationDatesCorrect() throws Exception {
        Node config = MockNode.root();
        config.setProperty("validPeriodLengthInYears", validPeriodLengthInYears);
        config.setProperty("publicationsystem:PubliclyAccessible", false);
        config.setProperty("publicationsystem:NominalDate", String.valueOf(LocalDate.now().minusWeeks(1)));

        Calendar oneWeekAgo = Calendar.getInstance();
        oneWeekAgo.add(Calendar.YEAR, 2);

        config.setProperty("publicationsystem:NominalDate", oneWeekAgo);

        publicationDateValidator = new PublicationDateValidator(config);

        given(validationContext.getDocumentNode()).willReturn(config);

        final Optional<Violation> violation = publicationDateValidator.validate(validationContext, new Date());

        assertThat("No Violation present, Dates are correct", !violation.isPresent());
    }

    @Test
    public void publicationInThePast() throws Exception {
        Node config = MockNode.root();
        config.setProperty("validPeriodLengthInYears", validPeriodLengthInYears);
        config.setProperty("publicationsystem:PubliclyAccessible", false);
        config.setProperty("publicationsystem:NominalDate", String.valueOf(LocalDate.now().minusWeeks(1)));

        Map<String, String> variableSub = new HashMap<>();
        variableSub.put("validPeriod", validPeriodLengthInYears.toString());

        Calendar oneWeekAgo = Calendar.getInstance();
        oneWeekAgo.add(Calendar.WEEK_OF_YEAR, -1);

        config.setProperty("publicationsystem:NominalDate", oneWeekAgo);

        publicationDateValidator = new PublicationDateValidator(config);

        given(validationContext.getDocumentNode()).willReturn(config);
        given(validationContext.createViolation(variableSub)).willReturn(violation);

        final Optional<Violation> violation = publicationDateValidator.validate(validationContext, new Date());

        then(validationContext).should().createViolation(variableSub);
        assertThat("Violation present, Date is in the past", violation.isPresent());
    }

    @Test
    public void publicationPastFutureYearLimit() throws Exception {
        Node config = MockNode.root();
        config.setProperty("validPeriodLengthInYears", validPeriodLengthInYears);
        config.setProperty("publicationsystem:PubliclyAccessible", false);
        config.setProperty("publicationsystem:NominalDate", String.valueOf(LocalDate.now().minusWeeks(1)));

        Map<String, String> variableSub = new HashMap<>();
        variableSub.put("validPeriod", validPeriodLengthInYears.toString());

        Calendar oneWeekAgo = Calendar.getInstance();
        oneWeekAgo.add(Calendar.YEAR, 8);

        config.setProperty("publicationsystem:NominalDate", oneWeekAgo);

        publicationDateValidator = new PublicationDateValidator(config);

        given(validationContext.getDocumentNode()).willReturn(config);
        given(validationContext.createViolation(variableSub)).willReturn(violation);

        final Optional<Violation> violation = publicationDateValidator.validate(validationContext, new Date());

        then(validationContext).should().createViolation(variableSub);
        assertThat("Violation present, Date is too far in the past", violation.isPresent());
    }
}
