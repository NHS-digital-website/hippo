package uk.nhs.digital.ps.directives;

import static java.text.MessageFormat.format;

import freemarker.core.Environment;
import freemarker.ext.beans.BeanModel;
import freemarker.ext.beans.StringModel;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import uk.nhs.digital.ps.beans.RestrictableDate;

import java.io.IOException;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * Formats instances of {@linkplain RestrictableDate} for display.
 * </p><p>
 * Required template parameter: {@code value}. Can be {@code null} in which case a zero-length string will be generated.
 * </p><p>
 * Example usage in Freemarker template:
 * </p>
 * <pre>

 *     <#assign formatRestrictableDate="uk.nhs.digital.ps.directives.RestrictableDateFormatterDirective"?new() />
 *     ...
 *     &lt;span&gt;Date: <@formatRestrictableDate value=document.nominalPublicationDate/>&lt;/span&gt;
 * </pre>
 */
public class RestrictableDateFormatterDirective implements TemplateDirectiveModel {

    private static final String DATE_PARAM_NAME = "value";

    private static final String DATE_PATTERN_RESTRICTED = "{0} {1,number,#}";
    private static final String DATE_PATTERN_FULL = "{0} {1} {2,number,#}";

    private static final TextStyle MONTH_DISPLAY_STYLE = TextStyle.SHORT;
    private static final Locale LOCALE = Locale.UK;

    private static final RestrictableDateFormatter NULL_DATE_FORMATTER = () -> "";

    @Override
    public void execute(final Environment environment, final Map parameters, final TemplateModel[] loopVariables,
                        final TemplateDirectiveBody body) throws TemplateException, IOException {

        assertRequiredParameterPresent(parameters, environment);

        final Optional<RestrictableDate> restrictableDate = extractRestrictableDateFrom(parameters);

        final String formattedRestrictableDate = getFormatterFor(restrictableDate).format();

        environment.getOut().append(formattedRestrictableDate);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private RestrictableDateFormatter getFormatterFor(final Optional<RestrictableDate> restrictableDateOptional) {
        return restrictableDateOptional
            .map(restrictableDate ->
                restrictableDate.isRestricted()
                    ? getRestrictedDateFormatter(restrictableDate)
                    : getFullDateFormatter(restrictableDate))
            .orElse(NULL_DATE_FORMATTER);
    }

    private RestrictableDateFormatter getFullDateFormatter(final RestrictableDate fullDate) {
        return () -> format(DATE_PATTERN_FULL,
            fullDate.getDayOfMonth(),
            fullDate.getMonth().getDisplayName(MONTH_DISPLAY_STYLE, LOCALE),
            fullDate.getYear());
    }

    private RestrictableDateFormatter getRestrictedDateFormatter(final RestrictableDate restrictedDate) {
        return () -> format(DATE_PATTERN_RESTRICTED,
            restrictedDate.getMonth().getDisplayName(MONTH_DISPLAY_STYLE, LOCALE),
            restrictedDate.getYear());
    }

    private Optional<RestrictableDate> extractRestrictableDateFrom(final Map parameters) {

        return Optional.ofNullable(parameters.get(DATE_PARAM_NAME))
            .map(parameter -> (StringModel) parameter)
            .map(BeanModel::getWrappedObject)
            .map(wrappedObject -> (RestrictableDate)wrappedObject);
    }

    private void assertRequiredParameterPresent(final Map parameters, final Environment environment)
        throws TemplateException {

        if (!parameters.containsKey(DATE_PARAM_NAME)) {
            throw new TemplateException(format("Required parameter ''{0}'' was not provided to template {1}.",
                DATE_PARAM_NAME, getClass().getName()), environment);
        }
    }

    @FunctionalInterface
    interface RestrictableDateFormatter {
        String format();
    }
}
