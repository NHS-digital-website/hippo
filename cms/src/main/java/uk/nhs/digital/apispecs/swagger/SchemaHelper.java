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

    private static final String TEMPLATES_CLASSPATH = "/api-specification/codegen-templates/";
    private static final String TEMPLATE_EXTENSION = ".mustache";
    private static final String TEMPLATE_FILE_NAME = "schema_object";

    private Template template;

    public SchemaHelper(final MarkdownHelper helper) {
        template = initialiseTemplate(helper);
    }

    @Override
    public String apply(final Schema schemaObject, final Options options) {

        try {
            return template.apply(schemaObject);

        } catch (final Exception e) {
            throw new RuntimeException("Failed to render schema.", e);
        }
    }

    private Template initialiseTemplate(final MarkdownHelper helper) {

        final Handlebars handlebars = initialiseHandlebars(helper);

        return compileTemplate(handlebars, TEMPLATE_FILE_NAME);
    }

    private Template compileTemplate(final Handlebars handlebars, final String templateFileName) {
        try {
            return handlebars.compile(templateFileName);

        } catch (final IOException e) {
            throw new UncheckedIOException("Failed to register Handlebars schema rendering helper.", e);
        }
    }

    private Handlebars initialiseHandlebars(final MarkdownHelper helper) {

        final ClassPathTemplateLoader classPathTemplateLoader =
            new ClassPathTemplateLoader(TEMPLATES_CLASSPATH, TEMPLATE_EXTENSION);

        return new Handlebars(classPathTemplateLoader)
            .parentScopeResolution(false)
            .infiniteLoops(true)
            .prettyPrint(true)
            .registerHelper(MarkdownHelper.NAME, helper)
            .registerHelper(IndentationHelper.NAME, IndentationHelper.INSTANCE);
    }
}
