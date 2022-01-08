package uk.nhs.digital.common.components.apispecification.handlebars;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.Optional;

public class VariableValueHelper implements Helper<String> {
    public static final String NAME = "variable";
    public static final VariableValueHelper INSTANCE = new VariableValueHelper();

    private VariableValueHelper() {
        // private to promote the use of INSTANCE
    }

    @Override public Object apply(final String variableName, final Options options) throws IOException {
        return Optional.ofNullable(options.data(variableName)).orElse(StringUtils.EMPTY);
    }
}
