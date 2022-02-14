package uk.nhs.digital.common.components.apispecification.swagger.request.bodyextractor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.apache.commons.lang3.StringUtils;

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

        return isJsonObject(parser)
            ? parseAsJsonObject(parser, context)
            : parseAsString(parser);
    }

    private boolean isJsonObject(final JsonParser parser) throws IOException {

        // Evaluates to true for values defined as JSON objects, e.g.:
        //
        //    { "some": "property" }

        // Evaluates to false for values defined as strings, e.g.:
        //
        //    "{ \"some\": \"property\" }"
        //
        //    "plain text"
        //
        //    "<xml> <element>text value</element> </xml>"

        return StringUtils.length(parser.getText()) <= 1;
    }

    private String parseAsJsonObject(final JsonParser parser, final DeserializationContext context) throws IOException {
        return context.readTree(parser).toPrettyString();
    }

    private String parseAsString(final JsonParser parser) throws IOException {
        return parser.getText();
    }
}
