package uk.nhs.digital.apispecs.handlebars;

import static java.util.Objects.nonNull;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

import java.io.IOException;

/**
 * <p>'Block' helper, rendering content depending on whether the given numerical argument is not {@code null}.
 *
 * <p>This addresses limitations of the default logic employed by Handlebars applies 'truthy/falsy' criteria,
 * where numerical value of '{@code 0} is treated as 'falsy' and not rendered. For example, the following
 * template would not render anything if {@code myZeroValueProperty} property were set to zero rather than
 * {@code null}:
 *
 * <pre>
 * {@code {{#myZeroValueProperty}}My label: {{this}}{{/myZeroValueProperty}}}.
 * </pre>
 * <p>Example of use:
 *
 * <pre>
 * {@code {{#ifNumberNotNull myZeroValueProperty}}My label: {{myZeroValueProperty}}{{/myZeroValueProperty}}}.
 * </pre>
 */
public class NumberNotNullHelper implements Helper<Number> {

    public static final NumberNotNullHelper INSTANCE = new NumberNotNullHelper();

    public static final String NAME = "ifNumberNotNull";

    @Override public Options.Buffer apply(final Number number, final Options options) throws IOException {

        try {
            return numberRenderedIfNotNull(number, options);

        } catch (final Exception e) {
            throw  new RuntimeException("Failed to render number conditionally; provided value: " + number, e);
        }
    }

    private Options.Buffer numberRenderedIfNotNull(final Number number, final Options options) throws IOException {

        final Options.Buffer buffer = options.buffer();

        if (nonNull(number)) {
            buffer.append(options.fn());
        } else {
            buffer.append(options.inverse());
        }
        return buffer;
    }
}
