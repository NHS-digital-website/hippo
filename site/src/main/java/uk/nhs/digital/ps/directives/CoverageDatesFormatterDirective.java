package uk.nhs.digital.ps.directives;

import static java.text.MessageFormat.format;

import freemarker.core.Environment;
import freemarker.template.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

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
public class CoverageDatesFormatterDirective implements TemplateDirectiveModel {

    private static final String START_PARAM_NAME = "start";
    private static final String END_PARAM_NAME = "end";
    private static final String SNAPSHOT_WORDING = "Snapshot on ";

    @Override
    public void execute(Environment environment, Map parameters, TemplateModel[] templateModels,
                        TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {

        assertRequiredParameterPresent(parameters, environment, START_PARAM_NAME);
        assertRequiredParameterPresent(parameters, environment, END_PARAM_NAME);

        final Date start = getValueAsDate(parameters, START_PARAM_NAME);
        final Date end = getValueAsDate(parameters, END_PARAM_NAME);
        final String result = formatCoverageDates(start, end);

        environment.getOut().append(result);
    }

    private String formatCoverageDates(final Date start, final Date end) {
        if (start.equals(end)) {
            return format("{0} {1}", SNAPSHOT_WORDING ,formatDate(start));  // Snapshot date
        } else {
            return format("{0} to {1}", formatDate(start), formatDate(end));            // Range date
        }
    }

    private String formatDate(final Date dateToFormat) {
        SimpleDateFormat requiredFormat = new SimpleDateFormat("dd MMM yyyy");
        return requiredFormat.format(dateToFormat);
    }

    private Date getValueAsDate(final Map parameters, final String paramName) {
        return ((SimpleDate)parameters.get(paramName)).getAsDate();
    }

    private void assertRequiredParameterPresent(final Map parameters, final Environment environment, String parameterName) throws TemplateException {
        if (!parameters.containsKey(parameterName)) {
            throw new TemplateException(format("Required parameter ''{0}'' was not provided to template {1}.",
                parameterName, getClass().getName()), environment);
        }
    }
}
