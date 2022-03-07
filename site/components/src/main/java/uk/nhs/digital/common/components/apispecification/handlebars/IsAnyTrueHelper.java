package uk.nhs.digital.common.components.apispecification.handlebars;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import org.apache.commons.lang.Validate;

import java.io.IOException;
import java.util.Arrays;

public class IsAnyTrueHelper implements Helper<String> {

    public static final IsAnyTrueHelper INSTANCE = new IsAnyTrueHelper();

    public static final String NAME = "isAnyTrue";

    @Override public Object apply(final String items, final Options options) throws IOException {

        Validate.notEmpty(options.params,"At least one parameter is required but none was provided.");
        Validate.allElementsOfType(Arrays.asList(options.params), Boolean.class, "All parameters are required to be boolean but at least one provided value was not.");

        final Options.Buffer buffer = options.buffer();

        if (hasAnyTrueParam(options)) {
            buffer.append(options.fn());
        } else {
            buffer.append(options.inverse());
        }

        return buffer;
    }

    private boolean hasAnyTrueParam(final Options options) {
        return Arrays.stream(options.params)
            .map(param -> (Boolean)param)
            .anyMatch(param -> param);
    }
}
