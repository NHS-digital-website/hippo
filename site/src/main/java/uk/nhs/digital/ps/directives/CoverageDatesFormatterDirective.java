package uk.nhs.digital.ps.directives;

import static java.text.MessageFormat.format;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

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
public class CoverageDatesFormatterDirective extends DateFormatterDirective {

    private static final String START_PARAM_NAME = "start";
    private static final String END_PARAM_NAME = "end";
    private static final String SNAPSHOT_WORDING = "Snapshot on ";

    private static final SimpleDateFormat ISO_FORMAT;

    static {
        ISO_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        ISO_FORMAT.setTimeZone(TIME_ZONE);
    }

    @Override
    public void execute(Environment environment, Map parameters, TemplateModel[] templateModels,
                        TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {

        assertRequiredParameterPresent(parameters, environment, START_PARAM_NAME);
        assertRequiredParameterPresent(parameters, environment, END_PARAM_NAME);

        final Date start = getValueAsDate(parameters, START_PARAM_NAME);
        final Date end = getValueAsDate(parameters, END_PARAM_NAME);

        boolean schemaFormat = parameters.containsKey("schemaFormat");

        final String result = schemaFormat ? formatSchemaFormat(start, end) : formatCoverageDates(start, end);

        environment.getOut().append(result);
    }

    private String formatSchemaFormat(Date start, Date end) {
        return ISO_FORMAT.format(start) + "/" + ISO_FORMAT.format(end);
    }

    private String formatCoverageDates(final Date start, final Date end) {
        if (start.equals(end)) {
            return format("{0} {1}", SNAPSHOT_WORDING ,formatDate(start));  // Snapshot date
        } else {
            return format("{0} to {1}", formatDate(start), formatDate(end)); // Range date
        }
    }
}
