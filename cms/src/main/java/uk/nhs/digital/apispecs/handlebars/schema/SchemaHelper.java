package uk.nhs.digital.apispecs.handlebars.schema;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.helper.AssignHelper;
import com.github.jknack.handlebars.helper.ConditionalHelpers;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import io.swagger.v3.oas.models.media.Schema;
import uk.nhs.digital.apispecs.handlebars.*;

import java.io.IOException;
import java.io.UncheckedIOException;

public class SchemaHelper implements Helper<Schema<?>> {

    public static final String NAME = "render_schema";

    private static final String TEMPLATES_CLASSPATH = "/api-specification/codegen-templates/";
    private static final String TEMPLATE_EXTENSION = ".mustache";
    private static final String TEMPLATE_FILE_NAME = "schema_object";

    private Template template;

    public SchemaHelper(final MarkdownHelper helper) {
        template = initialiseTemplate(helper);
    }

    @Override
    public String apply(final Schema<?> schemaObject, final Options options) {

        try {
            return renderToHtml(schemaObject);

        } catch (final Exception e) {
            throw new SchemaRenderingException("Failed to render schema.", e);
        }
    }

    private String renderToHtml(final Schema<?> schemaObject) throws IOException {
        return template.apply(schemaObject);
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

    private Handlebars initialiseHandlebars(final MarkdownHelper markdownHelper) {

        final ClassPathTemplateLoader classPathTemplateLoader =
            new ClassPathTemplateLoader(TEMPLATES_CLASSPATH, TEMPLATE_EXTENSION);

        final ContextModelsStack.Factory contextStackFactory = new ContextModelsStack.Factory(new UniqueModelStackExtractor());

        return new Handlebars(classPathTemplateLoader)
            .parentScopeResolution(false)
            .infiniteLoops(true)
            .prettyPrint(true)
            .registerHelper(MarkdownHelper.NAME, markdownHelper)
            .registerHelper(IndentationHelper.NAME, new IndentationHelper(contextStackFactory))
            .registerHelper(IfNotNullHelper.NAME, IfNotNullHelper.INSTANCE)
            .registerHelper(IfRequiredHelper.NAME, new IfRequiredHelper(contextStackFactory))
            .registerHelper(ConditionalHelpers.eq.name(), ConditionalHelpers.eq)
            .registerHelper(ConditionalHelpers.or.name(), ConditionalHelpers.or)
            .registerHelper(EnumHelper.NAME, EnumHelper.INSTANCE)
            .registerHelper(TypeAnyHelper.NAME, TypeAnyHelper.INSTANCE)
            .registerHelper(JacksonPrettyJsonHelper.NAME, JacksonPrettyJsonHelper.INSTANCE)
            .registerHelper(TypeAnySanitisingHelper.NAME, TypeAnySanitisingHelper.INSTANCE)
            .registerHelper(AssignHelper.NAME, AssignHelper.INSTANCE)
            .registerHelper(VariableValueHelper.NAME, VariableValueHelper.INSTANCE)
            // below helper is registered as a HelperSource as it takes no parameters.
            // see https://github.com/jknack/handlebars.java#using-a-helpersource for further info
            .registerHelpers(UuidHelper.INSTANCE)
            ;
    }
}
