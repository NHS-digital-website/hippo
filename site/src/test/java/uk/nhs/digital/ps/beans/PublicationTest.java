package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.provider.jcr.JCRValueProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.nhs.digital.ps.test.util.ReflectionHelper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;
import static uk.nhs.digital.ps.beans.RestrictableDateTest.assertRestrictableDate;

public class PublicationTest {

    private static final String NOMINAL_DATE_PROPERTY_KEY = "publicationsystem:NominalDate";

    @Mock protected JCRValueProvider valueProvider;

    private final Map<String, Object> beanProperties = new HashMap<>();
    private Publication publication;
    private Calendar cutOffDate;
    private static final int WEEKS_TO_CUTOFF = 8;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        beanProperties.clear();
        given(valueProvider.getProperties()).willReturn(beanProperties);

        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_MONTH, WEEKS_TO_CUTOFF);
        cutOffDate = calendar;

        publication = new Publication();
        ReflectionHelper.setField(publication, "valueProvider", valueProvider);
    }

    @Test
    public void returnsRestrictedNominalPublicationDate_whenItFallsAfterTheCutOffPoint() throws Exception {

        // given
        final Calendar dayAfterCutOff = (Calendar) cutOffDate.clone();
        dayAfterCutOff.add(Calendar.DAY_OF_MONTH, 1);
        setBeanProperty(NOMINAL_DATE_PROPERTY_KEY, dayAfterCutOff);

        final LocalDate dayAfterCutoffLocalDate = toLocalDate(dayAfterCutOff);

        // when
        final RestrictableDate actualNominalPublicationDate = publication.getNominalPublicationDate();

        // then
        assertRestrictableDate(actualNominalPublicationDate, true, dayAfterCutoffLocalDate);
    }

    @Test
    public void returnsFullNominalPublicationDate_whenIfFallsBeforeTheCutOffPoint() throws Exception {

        // given
        setBeanProperty(NOMINAL_DATE_PROPERTY_KEY, cutOffDate);
        final LocalDate cutOffLocalDate = toLocalDate(cutOffDate);

        // when
        final RestrictableDate actualNominalPublicationDate = publication.getNominalPublicationDate();

        // then
        assertRestrictableDate(actualNominalPublicationDate, false, cutOffLocalDate);
    }

    @Test
    public void returnsNull_whenNoNominalPublicationDateHasBeenSet() throws Exception {

        // given
        // no Nominal Publication Date value has been set

        // when
        final RestrictableDate actualNominalPublicationDate = publication.getNominalPublicationDate();

        // then
        assertThat("No value was returned.", actualNominalPublicationDate, is(nullValue()));

    }

    private void setBeanProperty(final String propertyKey, final Object value) {
        beanProperties.put(propertyKey, value);
    }

    private static LocalDate toLocalDate(final Calendar calendar) {
        return LocalDate.from(
            LocalDateTime.ofInstant(
                calendar.toInstant(),
                calendar.getTimeZone().toZoneId()
            )
        );
    }
}
