package uk.nhs.digital.apispecs.handlebars;

import static java.util.Collections.emptyList;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class EnumHelper implements Helper<Collection<String>> {

    public static final String NAME = "enum";

    @Override public Object apply(final Collection<String> args, final Options options) {

        return Optional.ofNullable(args).orElse(emptyList()).stream()
            .map(arg -> String.format("<code class=\"codeinline\">%s</code>", arg))
            .collect(Collectors.joining(", "));
    }
}
