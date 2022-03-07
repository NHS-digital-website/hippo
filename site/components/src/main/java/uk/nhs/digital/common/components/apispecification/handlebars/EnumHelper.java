package uk.nhs.digital.common.components.apispecification.handlebars;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.Collection;
import java.util.Optional;

public class EnumHelper implements Helper<Collection<?>> {

    public static final EnumHelper INSTANCE = new EnumHelper();

    public static final String NAME = "enumeration";

    private EnumHelper() {
        // private to encourage use of INSTANCE
    }

    @Override public String apply(final Collection<?> args, final Options options) {

        return Optional.ofNullable(args).orElse(emptyList()).stream()
            .map(String::valueOf)
            .map(StringEscapeUtils::escapeHtml)
            .map(arg -> format("<span class=\"nhsd-a-text-highlight nhsd-a-text-highlight--code\">%s</span>", arg))
            .collect(joining(", "));
    }
}
