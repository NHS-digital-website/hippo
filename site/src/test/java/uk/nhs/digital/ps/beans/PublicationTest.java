package uk.nhs.digital.ps.beans;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.hippoecm.hst.provider.jcr.JCRValueProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import uk.nhs.digital.ps.site.exceptions.DataRestrictionViolationException;
import uk.nhs.digital.ps.test.util.ReflectionHelper;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;
import static uk.nhs.digital.ps.beans.RestrictableDateTest.assertRestrictableDate;


@RunWith(DataProviderRunner.class)
public class PublicationTest {

    private static final String NOMINAL_DATE_PROPERTY_KEY = "publicationsystem:NominalDate";
    private static final String PUBLICLY_ACCESSIBLE_PROPERTY_KEY = "publicationsystem:PubliclyAccessible";
    private static final String ATTACHMENTS_PROPERTY_KEY = "publicationsystem:attachments";
    private static final String RELATED_LINKS_PROPERTY_KEY = "publicationsystem:RelatedLinks";
    private static final String RESOURCE_LINKS_PROPERTY_KEY = "publicationsystem:ResourceLinks";

    @Mock private JCRValueProvider valueProvider;
    @Mock private Node node;
    @Mock private NodeIterator nodeIterator;

    private final Map<String, Object> beanProperties = new HashMap<>();
    private Publication publication;
    private Calendar cutOffDate;
    private static final int WEEKS_TO_CUTOFF = 8;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        beanProperties.clear();

        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_MONTH, WEEKS_TO_CUTOFF);
        cutOffDate = calendar;

        publication = new Publication();

        given(valueProvider.getProperties()).willReturn(beanProperties);

        given(nodeIterator.hasNext()).willReturn(false);
        given(node.getNodes(ATTACHMENTS_PROPERTY_KEY)).willReturn(nodeIterator);
        given(node.getNodes(RELATED_LINKS_PROPERTY_KEY)).willReturn(nodeIterator);
        given(node.getNodes(RESOURCE_LINKS_PROPERTY_KEY)).willReturn(nodeIterator);

        ReflectionHelper.setField(publication, "name", "arbitrary-name");
        ReflectionHelper.setField(publication, "valueProvider", valueProvider);
        ReflectionHelper.setField(publication, "node", node);
    }

    // NOTE: usually, with tests involving time, obtaining 'now' value would be facilitated via custom time provider
    // which, mocked in tests, would enable precise control over the value of 'now'. In this case, however, it's
    // easy to manipulate the value of 'Nominal Publication Date' - which has to be set anyway - and so, for simplicity
    // reasons, it was decided to not to introduce additional complexity in a form time provider. This will be easy
    // to change should more complex cases arise, however.

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

    @Test
    @UseDataProvider("gettersForbiddenInUpcomingPublication")
    public void restrictsGetters_whenPublicationNotPubliclyAvailable(
        final Method forbiddenGetter) throws Exception {

        // given
        setBeanProperty(PUBLICLY_ACCESSIBLE_PROPERTY_KEY, false);

        try {
            // when
            forbiddenGetter.invoke(publication);

            // then
            fail("No exception was thrown from getter " + forbiddenGetter);

        } catch (final InvocationTargetException e) {
            assertThat("Getter " + forbiddenGetter + " throws exception of correct type.", e.getCause(),
                instanceOf(DataRestrictionViolationException.class)
            );
            assertThat("Getter " + forbiddenGetter + " throws exception with correct message.", e.getCause().getMessage(),
                startsWith("Property is not available when publication is flagged as 'not publicly accessible':")
            );
        } catch (final Exception e) {
            throw new Error("Failed to test '" + forbiddenGetter + " because of an error;" +
                " see stack trace below for details.", e
            );
        }
    }

    @Test
    @UseDataProvider("allPublicGetters")
    public void permitsAllGetters_whenPublicationFlaggedAsPubliclyAvailable(final Method permittedGetter) throws Exception {

        // given
        setBeanProperty(PUBLICLY_ACCESSIBLE_PROPERTY_KEY, true);

        try {
            // when
            permittedGetter.invoke(publication);

            // then
            // pass
        } catch (final Throwable e) {
            throw new AssertionError(
                "No exception was expected to be thrown from '" + permittedGetter + "' but one was emitted;" +
                    " see stack trace below for details.", e
            );
        }
    }

    @DataProvider
    public static List<Method> allPublicGetters() {

        return stream(Publication.class.getDeclaredMethods())
            .filter(method -> Modifier.isPublic(method.getModifiers()))
            .filter(method -> !Modifier.isStatic(method.getModifiers()))
            .filter(method -> method.getName().startsWith("get") || method.getName().startsWith("is"))
            .filter(method -> !method.getReturnType().equals(Void.class))
            .collect(toList());
    }

    @DataProvider
    public static List<Method> gettersForbiddenInUpcomingPublication() {

        final List<String> gettersPermittedWhenUpcoming = asList(
            "getTitle",
            "getNominalPublicationDate",
            "isPubliclyAccessible",
            "getSelfLinkBean"
        );

        return allPublicGetters().stream()
            .filter(method -> !gettersPermittedWhenUpcoming.contains(method.getName()))
            .collect(toList());
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
