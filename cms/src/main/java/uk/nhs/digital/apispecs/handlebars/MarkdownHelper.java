package uk.nhs.digital.apispecs.handlebars;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import uk.nhs.digital.apispecs.commonmark.CommonmarkMarkdownConverter;

import java.util.Optional;

public class MarkdownHelper implements Helper<String> {

    public static final String NAME = "markdown";

    private final CommonmarkMarkdownConverter commonmarkMarkdownConverter;

    public MarkdownHelper(final CommonmarkMarkdownConverter commonmarkMarkdownConverter) {
        this.commonmarkMarkdownConverter = commonmarkMarkdownConverter;
    }

    @Override
    public String apply(final String markdown, final Options options) {

        try {
            return render(markdown);

        } catch (final Exception e) {
            throw new RuntimeException("Failed to render markdown.", e);
        }
    }

    private String render(final String markdown) {
        return Optional.ofNullable(markdown)
            .map(commonmarkMarkdownConverter::toHtml)
            .orElse("");
    }
}
