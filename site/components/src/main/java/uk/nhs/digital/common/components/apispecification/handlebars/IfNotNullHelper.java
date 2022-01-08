package uk.nhs.digital.common.components.apispecification.handlebars;

import static java.util.Objects.nonNull;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

import java.io.IOException;

/**
 * <p>
 * 'Block' helper, rendering content depending on whether the given argument is not {@code null}.
 * <p>
 * This addresses limitations of the default 'truthy/falsy' logic employed by Handlebars,
 * where values of '{@code 0}' or '{@code false}' ar treated as 'falsy' and not rendered.
 * <p>
 * For example, the following template does <b>not</b> render anything if {@code myZeroValueProperty}
 * property were set to zero rather than {@code null}:
 * <pre>
 * {{#myZeroValueProperty}}My label: {{this}}{{/myZeroValueProperty}}.
 * </pre>
 * <p>To have 'falsy' values of properties rendered if the properties themselves are present use:
 * <pre>
 * {{#<b>ifNotNull</b> myZeroValueProperty}}My label: {{myZeroValueProperty}}{{/<b>ifNotNull</b>}}.
 * </pre>
 */
public class IfNotNullHelper implements Helper<Object> {

    public static final IfNotNullHelper INSTANCE = new IfNotNullHelper();

    public static final String NAME = "ifNotNull";

    private IfNotNullHelper() {
        // private to encourage use of INSTANCE
    }

    @Override public Options.Buffer apply(final Object model, final Options options) {

        try {
            return renderedIfNotNull(model, options);

        } catch (final Exception e) {
            throw  new RuntimeException("Failed to render property; provided value: " + model, e);
        }
    }

    private Options.Buffer renderedIfNotNull(final Object model, final Options options) throws IOException {

        final Options.Buffer buffer = options.buffer();

        if (nonNull(model)) {
            buffer.append(options.fn());
        } else {
            buffer.append(options.inverse());
        }
        return buffer;
    }
}
