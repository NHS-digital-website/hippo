package uk.nhs.digital.common.components.apispecification.handlebars.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

public class JacksonPrettyJsonHelper implements Helper<Object> {

    public static final JacksonPrettyJsonHelper INSTANCE = new JacksonPrettyJsonHelper();

    public static final String NAME = "prettyJson";

    private JacksonPrettyJsonHelper() {
        // private to encourage use of INSTANCE
    }

    @Override public String apply(final Object model, final Options options) {

        return renderAsPrettyPrintedJson(model);
    }

    private String renderAsPrettyPrintedJson(final Object example) {
        if (example instanceof JsonNode) {
            final JsonNode jsonNode = (JsonNode)example;

            return jsonNode.toPrettyString();
        }

        return String.valueOf(example);
    }
}
