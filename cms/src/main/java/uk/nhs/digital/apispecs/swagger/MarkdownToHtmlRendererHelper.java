package uk.nhs.digital.apispecs.swagger;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import uk.nhs.digital.apispecs.commonmark.CommonmarkMarkdownConverter;

import java.util.Optional;

public class MarkdownToHtmlRendererHelper implements Helper<String> {

    public static final String NAME = "markdown";

    private final CommonmarkMarkdownConverter commonmarkMarkdownConverter;

    public MarkdownToHtmlRendererHelper(final CommonmarkMarkdownConverter commonmarkMarkdownConverter) {
        this.commonmarkMarkdownConverter = commonmarkMarkdownConverter;
    }

    @Override public Object apply(final String markdown, final Options options) {

        return Optional.ofNullable(markdown)
            .map(commonmarkMarkdownConverter::toHtml)
            .orElse("");
    }
}
