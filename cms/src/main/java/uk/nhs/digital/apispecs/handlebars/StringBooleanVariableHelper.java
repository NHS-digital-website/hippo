package uk.nhs.digital.apispecs.handlebars;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

import java.util.Optional;

public class StringBooleanVariableHelper implements Helper<String> {

    public static final String NAME = "ifVariableIsFalse";

    public static StringBooleanVariableHelper INSTANCE = new StringBooleanVariableHelper();

    private StringBooleanVariableHelper() {
        // no-op; made private to promote the use of INSTANCE
    }

    @Override
    public Options.Buffer apply(
        final String variableName,
        final Options options
    ) {
        try {
            final Options.Buffer buffer = options.buffer();

            if (isVariableFalse(variableName, options)) {
                buffer.append(options.inverse());
            } else {
                buffer.append(options.fn());
            }

            return buffer;

        } catch (final Exception e) {
            throw new TemplateRenderingException("Failed to interpret value of variable " + variableName + ".", e);
        }
    }

    private boolean isVariableFalse(final String variableName, final Options options) {
        return Optional.ofNullable(options.data(variableName))
            .filter(variableValue -> variableValue instanceof String)
            .map(String.class::cast)
            .map(Boolean::parseBoolean)
            .orElse(Boolean.FALSE);
    }
}
