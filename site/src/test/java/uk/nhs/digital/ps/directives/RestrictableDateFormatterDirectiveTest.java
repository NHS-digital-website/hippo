package uk.nhs.digital.ps.directives;

import static java.text.MessageFormat.format;
import static java.time.Month.APRIL;
import static java.time.Month.AUGUST;
import static java.time.Month.DECEMBER;
import static java.time.Month.FEBRUARY;
import static java.time.Month.JANUARY;
import static java.time.Month.JULY;
import static java.time.Month.JUNE;
import static java.time.Month.MARCH;
import static java.time.Month.MAY;
import static java.time.Month.NOVEMBER;
import static java.time.Month.OCTOBER;
import static java.time.Month.SEPTEMBER;
import static java.util.stream.Collectors.toList;
import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.initMocks;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.ext.beans.StringModel;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import uk.nhs.digital.ps.beans.RestrictableDate;

import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RunWith(DataProviderRunner.class)
public class RestrictableDateFormatterDirectiveTest {

    private static final String DATE_PARAM_NAME = "value";

    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Mock private Template template;
    @Mock private Writer writer;

    private BeansWrapper beansWrapper;
    private Environment environment;
    private Map parameters;
    private TemplateModel[] loopVariables;
    private TemplateDirectiveBody body;

    private RestrictableDateFormatterDirective formatterDirective;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        loopVariables = new TemplateModel[0];
        body = null;

        parameters = new HashMap();

        beansWrapper = new BeansWrapperBuilder(Configuration.getVersion()).build();

        environment = new Environment(template, null, writer);

        formatterDirective = new RestrictableDateFormatterDirective();
    }

    @Test
    @UseDataProvider("validDates")
    public void formatsFullNominalPublicationDate(final LocalDate referenceDate) throws Exception {

        final RestrictableDate restrictableDate = RestrictableDate.fullDateFrom(referenceDate);

        final String expectedFormattedDate =
                  referenceDate.getDayOfMonth()
            + " " + referenceDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.UK)
            + " " + referenceDate.getYear();

        testRestrictableDateFormatting(restrictableDate, expectedFormattedDate);
    }

    @Test
    @UseDataProvider("validDates")
    public void formatsRestrictedNominalPublicationDate(final LocalDate referenceDate) throws Exception {

        final RestrictableDate restrictableDate = RestrictableDate.restrictedDateFrom(referenceDate);

        final String expectedFormattedDate =
                    referenceDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.UK)
            + " " + referenceDate.getYear();

        testRestrictableDateFormatting(restrictableDate, expectedFormattedDate);
    }

    @Test
    public void returnsEmptyString_onNoDateAvailable() throws Exception {

        final RestrictableDate restrictableDate = null;

        final String expectedFormattedDate = "";

        testRestrictableDateFormatting(restrictableDate, expectedFormattedDate);
    }

    @Test
    public void reportsErrorOnMissingRequriedParameter() throws Exception {

        // given
        // no DATE_PARAM_NAME entry in parameters

        expectedException.expect(TemplateException.class);
        expectedException.expectMessage(format("Required parameter ''{0}'' was not provided to template {1}.",
            DATE_PARAM_NAME, RestrictableDateFormatterDirective.class.getName()));

        // when
        formatterDirective.execute(environment, parameters, loopVariables, body);

        // then
        // the framework asserts expected exception
    }

    @DataProvider
    public static List<LocalDate> validDates() {

        // One test case for each month with varying years and days:
        return datesArrayToList(new Object[][]{
            // year,  month,     dayOfMonth
            {2000,    JANUARY,   27},
            {2000,    FEBRUARY,  26},
            {2000,    MARCH,     13},
            {2000,    APRIL,     12},
            {2000,    MAY,       11},
            {2001,    JUNE,      10},
            {2001,    JULY,       9},
            {2001,    AUGUST,     8},
            {2003,    SEPTEMBER,  7},
            {2003,    OCTOBER,    6},
            {2003,    NOVEMBER,   5},
            {2004,    DECEMBER,   4},
        });
    }

    private void testRestrictableDateFormatting(final RestrictableDate restrictableDate,
                                                final String expectedFormattedDate)
        throws TemplateException, IOException {

        // given
        setBeanProperty(DATE_PARAM_NAME, restrictableDate);

        // when
        formatterDirective.execute(environment, parameters, loopVariables, body);

        // then
        then(writer).should().append(expectedFormattedDate);
    }

    private void setBeanProperty(final String propertyKey, final Object value) {
        parameters.put(propertyKey, value == null ? null : new StringModel(value, beansWrapper));
    }

    private static List<LocalDate> datesArrayToList(final Object[][] dates) {
        return Arrays.stream(dates)
            .map(calendarComponents -> LocalDate.of(
                (int)calendarComponents[0],
                ((Month)calendarComponents[1]),
                (int)calendarComponents[2])
            )
            .collect(toList());
    }
}
