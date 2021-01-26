package uk.nhs.digital.apispecs.swagger.request.bodyextractor;

import static org.apache.commons.lang3.StringUtils.removeEnd;
import static org.apache.commons.lang3.StringUtils.removeStart;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class ToPrettyJsonStringDeserializer extends StdDeserializer<String> {

    public ToPrettyJsonStringDeserializer() {
        this(null);
    }

    public ToPrettyJsonStringDeserializer(final Class<?> vc) {
        super(vc);
    }

    @Override
    public String deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {

        final String prettyString = parseAsJson(parser);

        return sanitise(prettyString);
    }

    private String sanitise(final String text) {

        String sanitisedText = stripLeadingAndTrailingQuoteCharacters(text);
        sanitisedText = unescapeQuotes(sanitisedText);

        return sanitisedText;
    }

    private String unescapeQuotes(final String text) {
        return text.replaceAll("\\\\\"", "\"");
    }

    private String parseAsJson(final JsonParser parser) throws IOException {
        return parser.getCodec().<JsonNode>readTree(parser).toPrettyString();
    }

    private String stripLeadingAndTrailingQuoteCharacters(final String inputString) {

        String cleanedString = removeStart(inputString, "\"");
        cleanedString = removeEnd(cleanedString, "\"");

        return cleanedString;
    }
}
