package uk.nhs.digital.ps.directives;

import static java.text.MessageFormat.format;

import freemarker.core.Environment;
import freemarker.template.*;

import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * Converts byte count to a human-readable value; for example, value 1536 displays as "1.5 KiB".
 * </p><p>
 * Required template parameter: {@code bytesCount}.
 * </p><p>
 * Example usage in Freemarker template:
 * </p>
 * <pre>
 *
 *     <#assign formatFileSize="uk.nhs.digital.ps.directives.FileSizeFormatterDirective"?new() >
 *     ...
 *     &lt;span&gt;size: <@formatFileSize bytesCount=attachment.length/>&lt;/span&gt;
 * </pre>
 */
public class FileSizeFormatterDirective implements TemplateDirectiveModel {

    private static final String BYTES_COUNT_PARAM_NAME = "bytesCount";

    @Override
    public void execute(final Environment environment, final Map parameters, final TemplateModel[] loopVariables,
                        final TemplateDirectiveBody body) throws TemplateException, IOException {

        assertRequiredParameterPresent(parameters, environment);

        final long bytesCount = getValueAsLong(parameters, BYTES_COUNT_PARAM_NAME);

        final String humanFriendlyFileSize = getHumanFriendlyFileSize(bytesCount);

        environment.getOut().append(humanFriendlyFileSize);
    }

    private String getHumanFriendlyFileSize(final long bytesCount) {

        // Modified version of http://programming.guide/java/formatting-byte-size-to-human-readable-format.html
        // to only support decimal units.

        final int unit = 1000;
        if (bytesCount < unit) {
            return bytesCount + " B";
        }
        final int exp = (int) (Math.log(bytesCount) / Math.log(unit));
        final String pre = String.valueOf("kMGTPE".charAt(exp - 1));
        return String.format("%.1f %sB", bytesCount / Math.pow(unit, exp), pre);
    }

    private long getValueAsLong(final Map parameters, final String paramName) {
        return ((SimpleNumber) parameters.get(paramName)).getAsNumber().longValue();
    }

    private void assertRequiredParameterPresent(final Map parameters, final Environment environment)
        throws TemplateException {

        if (!parameters.containsKey(BYTES_COUNT_PARAM_NAME)) {
            throw new TemplateException(format("Required parameter ''{0}'' was not provided to template {1}.",
                BYTES_COUNT_PARAM_NAME, getClass().getName()), environment);
        }
    }
}
