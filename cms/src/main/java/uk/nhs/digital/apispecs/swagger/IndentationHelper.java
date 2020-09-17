package uk.nhs.digital.apispecs.swagger;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import io.swagger.v3.oas.models.media.Schema;

public class IndentationHelper implements Helper<Schema> {

    public static final String NAME = "indentation";

    @Override
    public Object apply(final Schema schemaObject, final Options options) {

        try {
            int indentationLevel = 0;
            for (Context parent = options.context.parent(); parent != null; parent = parent.parent()) {
                indentationLevel++;
            }

            return indentationLevel;

        } catch (final Exception e) {
            throw new RuntimeException("Failed to render schema.", e);
        }
    }
}
