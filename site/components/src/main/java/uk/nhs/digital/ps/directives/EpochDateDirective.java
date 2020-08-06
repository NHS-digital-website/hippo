package uk.nhs.digital.ps.directives;

import static java.text.MessageFormat.format;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

/**
 * Formats Epoch Dates
 * Sample input: 1.523462766E9
 * Sample Output: 11 April 2018
 */
public class EpochDateDirective implements TemplateDirectiveModel {

    private static final TimeZone TIME_ZONE = TimeZone.getTimeZone("Europe/London");
    private static final String PARAM_NAME = "date";
    private static final SimpleDateFormat DATE_FORMAT;

    static {
        DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy");
        DATE_FORMAT.setTimeZone(TIME_ZONE);
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        assertRequiredParameterPresent(params, env);
        long epoch = Long.parseLong(params.get(PARAM_NAME).toString());
        Date date = new Date(epoch * 1000);
        env.getOut().append(DATE_FORMAT.format(date));
    }

    protected void assertRequiredParameterPresent(final Map parameters, final Environment environment) throws TemplateException {
        if (!parameters.containsKey(PARAM_NAME)) {
            throw new TemplateException(format("Required parameter ''{0}'' was not provided to template {1}.",
                PARAM_NAME, getClass().getName()), environment);
        }
    }
}
