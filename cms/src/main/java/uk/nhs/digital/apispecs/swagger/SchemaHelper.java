package uk.nhs.digital.apispecs.swagger;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import io.swagger.v3.oas.models.media.Schema;

import java.io.IOException;
import java.io.UncheckedIOException;

public class SchemaHelper implements Helper<Schema> {

    public static final String NAME = "render_schema";

    private final Handlebars handlebars;
    private Template template;

    public SchemaHelper() {
        final ClassPathTemplateLoader classPathTemplateLoader =
            new ClassPathTemplateLoader("/api-specification/codegen-templates/", ".mustache");

        handlebars = new Handlebars(classPathTemplateLoader)
            .parentScopeResolution(false)
            .infiniteLoops(true)
            .registerHelper(IndentationHelper.NAME, new IndentationHelper())
            .prettyPrint(true)
        ;

        try {

            template = handlebars.compile("schema_object");

        } catch (final IOException e) {
            throw new UncheckedIOException("Failed to register Handlebars schema rendering helper.", e);
        }
    }

    @Override
    public Object apply(final Schema schemaObject, final Options options) {

        try {
            return template.apply(schemaObject);

        } catch (final Exception e) {
            throw new RuntimeException("Failed to render schema.", e);
        }
    }
}
