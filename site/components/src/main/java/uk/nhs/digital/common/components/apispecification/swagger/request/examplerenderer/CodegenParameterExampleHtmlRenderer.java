package uk.nhs.digital.common.components.apispecification.swagger.request.examplerenderer;

import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.nhs.digital.common.components.apispecification.commonmark.CommonmarkMarkdownConverter;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Optional;

/**
 * <p>Returns HTML value to be used for actual rendering of request parameter's or response header's example values.</p>
 *
 * <p>Note:</p>
 * <ul>
 * <li>OAS Parameter Object can have 'example' or 'examples'. CodegenParameter only has 'example'.</li>
 * <li>We're not handling the case when parameter specifies both 'example' and 'examples' as this is illegal.</li>
 * <li>By the time we are called, Codegen populates 'example' on CodegenParameter with a value that looks like a schema id rather than the actual value of the example; it seems
 * this is so that it gets later resolved by client-side JavaScript, which is what vanilla Codegen uses to render examples. We've disabled JavaScript in favour of exclusive
 * server-side rendering and so we need to populate this field ourselves.</li>
 * </ul>
 *
 * <p>See:</p>
 * <ul>
 * <li><a href="https://swagger.io/specification/#schema-object"/>OAS Schema Object</li>
 * <li><a href="https://swagger.io/specification/#parameter-object"/>OAS Parameter Object (request parameters)</li>
 * <li><a href="https://swagger.io/specification/#header-object"/>OAS Header Object (response headers)</li>
 * </ul>
 */
public class CodegenParameterExampleHtmlRenderer {

    private final CommonmarkMarkdownConverter markdownConverter;

    private final ObjectMapper jsonObjectMapper = new ObjectMapper()
        .configure(MapperFeature.USE_ANNOTATIONS, true);

    public CodegenParameterExampleHtmlRenderer(final CommonmarkMarkdownConverter markdownConverter) {
        this.markdownConverter = markdownConverter;
    }

    public String htmlForExampleValueOf(final String jsonSchema) {

        try {
            final CodegenParamDefinition codegenParamDefinition = from(jsonSchema);

            if (codegenParamDefinition.getExample().isPresent()) {
                return htmlFrom(codegenParamDefinition.getExample().get());
            }

            final Collection<ParamExample> complexExamplesFromParamDefinition = codegenParamDefinition.getExamples().values();

            if (!complexExamplesFromParamDefinition.isEmpty()) {
                return htmlFrom(complexExamplesFromParamDefinition);
            }

            final Optional<String> exampleFromParamSchema = codegenParamDefinition
                .getSchema()
                .flatMap(CodegenParamSchema::getExample);

            return exampleFromParamSchema.map(this::htmlFrom).orElse(null);

        } catch (final Exception e) {
            throw new RuntimeException("Failed to generate HTML for examples from JSON schema: " + jsonSchema, e);
        }
    }

    private String htmlFrom(final Collection<ParamExample> complexExamplesFromParamSchema) {

        //noinspection OptionalGetWithoutIsPresent
        return new StringBuilder("<strong class=\"nhsd-!t-font-weight-bold\">")
            .append(complexExamplesFromParamSchema.size() == 1 ? "Example" : "Examples")
            .append("</strong>\n")
            .append(
                complexExamplesFromParamSchema.stream()
                    .map(example -> {

                        final StringBuilder examplesHtml = new StringBuilder();

                        example.getSummary().ifPresent(summary -> examplesHtml
                            .append("<p class=\"nhsd-t-body\">")
                            .append(escapeHtml4(summary))
                            .append("</p>\n")
                        );

                        example.getDescription()
                            .map(markdownConverter::toHtml)
                            .ifPresent(descriptionHtml -> examplesHtml
                                .append("<p class=\"nhsd-t-body\">")
                                .append(descriptionHtml)
                                .append("</p>\n")
                            );

                        example.getValue().ifPresent(value -> examplesHtml
                            .append("<p class=\"nhsd-t-body\"><span class=\"nhsd-a-text-highlight nhsd-a-text-highlight--code\">")
                            .append(escapeHtml4(value))
                            .append("</span></p>\n")
                        );

                        return examplesHtml;
                    })
                    .reduce(StringBuilder::append)
                    .get()
            ).toString();
    }

    private String htmlFrom(final String simpleExampleValue) {
        return MessageFormat.format(
            "Example: <span class=\"nhsd-a-text-highlight nhsd-a-text-highlight--code\">{0}</span>",
            escapeHtml4(simpleExampleValue)
        );
    }

    private CodegenParamDefinition from(final String parameterJsonDefinition) throws JsonProcessingException {
        return jsonObjectMapper.readValue(parameterJsonDefinition, CodegenParamDefinition.class);
    }
}