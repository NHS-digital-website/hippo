package uk.nhs.digital.common.components.apispecification.handlebars;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public class HasOneItemHelper implements Helper<Collection<?>> {

    public static final HasOneItemHelper INSTANCE = new HasOneItemHelper();

    public static final String NAME = "hasOneItem";

    @Override public Options.Buffer apply(final Collection<?> items, final Options options) throws IOException {
        final Options.Buffer buffer = options.buffer();

        if (hasOneItem(items)) {
            buffer.append(options.fn());
        } else {
            buffer.append(options.inverse());
        }

        return buffer;
    }

    private boolean hasOneItem(final Collection<?> items) {
        return Optional.ofNullable(items)
            .map(objects -> objects.size() == 1)
            .orElse(false);
    }
}
