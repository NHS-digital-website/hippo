package uk.nhs.digital.ps.directives;

import static java.text.MessageFormat.format;

import freemarker.core.Environment;
import freemarker.template.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

/**
 * Example usage in Freemarker template:
 * <pre>
 *
 *     <#assign formatCoverageDates="uk.nhs.digital.ps.directives.CoverageDatesFormatterDirective"?new() >
 *         ...
 *     &lt;p&gt;<@formatCoverageDates start=document.coverageStart end=document.coverageEnd/>.&lt;/p&gt;
 * </pre>
 *
 */
public class DateFormatterDirective implements TemplateDirectiveModel {

    public static final TimeZone TIME_ZONE = TimeZone.getTimeZone("Europe/London");

    private static final String PARAM_NAME = "date";
    private static final SimpleDateFormat DATE_FORMAT;

    static {
        DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy");
        DATE_FORMAT.setTimeZone(TIME_ZONE);
    }

    @Override
    public void execute(Environment environment, Map parameters, TemplateModel[] templateModels,
                        TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {

        assertRequiredParameterPresent(parameters, environment, PARAM_NAME);

        final Date date = getValueAsDate(parameters, PARAM_NAME);
        final String result = formatDate(date);

        environment.getOut().append(result);
    }

    protected String formatDate(final Date dateToFormat) {
        return DATE_FORMAT.format(dateToFormat);
    }

    protected Date getValueAsDate(final Map parameters, final String paramName) {
        return ((SimpleDate)parameters.get(paramName)).getAsDate();
    }

    protected void assertRequiredParameterPresent(final Map parameters, final Environment environment, String parameterName) throws TemplateException {
        if (!parameters.containsKey(parameterName)) {
            throw new TemplateException(format("Required parameter ''{0}'' was not provided to template {1}.",
                parameterName, getClass().getName()), environment);
        }
    }
}
