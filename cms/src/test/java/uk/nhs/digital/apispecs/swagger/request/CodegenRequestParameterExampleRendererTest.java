package uk.nhs.digital.apispecs.swagger.request;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.initMocks;
import static uk.nhs.digital.test.util.FileUtils.fileContentFromClasspath;

import com.fasterxml.jackson.core.JsonParseException;
import io.swagger.codegen.v3.CodegenParameter;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import uk.nhs.digital.apispecs.commonmark.CommonmarkMarkdownConverter;

public class CodegenRequestParameterExampleRendererTest {

    private static final String TEST_DATA_FILES_PATH = "/test-data/api-specifications/CodegenRequestParameterExampleGeneratorTest";

    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Mock private CommonmarkMarkdownConverter markdownConverter;

    private CodegenRequestParameterExampleRenderer codegenRequestParameterExampleRenderer;

    @Before
    public void setUp() {
        initMocks(this);

        codegenRequestParameterExampleRenderer = new CodegenRequestParameterExampleRenderer(markdownConverter);
    }

    @Test
    public void generatesHtml_forExampleSpecifiedInParamDefinition_evenWhenExampleIsSpecifiedInParamSchema() {

        // given
        final String expectedHtmlForExampleValue = from("exampleSpecifiedInParamDefinitionAndInSchema_expectedHtml.html");

        final String parameterJsonDefinition = from("exampleSpecifiedInParamDefinitionAndInSchema_parameterDefinition.json");
        final CodegenParameter codegenParameter = codegenParameterWith(parameterJsonDefinition);

        // when
        final String actualHtmlForExampleValue = codegenRequestParameterExampleRenderer.htmlForExampleValueOf(codegenParameter);

        // then
        assertThat("Returns HTML for single example from parameter's schema.",
            actualHtmlForExampleValue,
            is(expectedHtmlForExampleValue)
        );
    }

    @Test
    public void generatesHtml_forExamplesSpecifiedInParamDefinition_evenWhenExampleIsSpecifiedInParamSchema() {

        // given
        final String expectedHtmlForExampleValue = from("examplesSpecifiedInParamDefinition_expectedHtml.html");

        final String parameterJsonDefinition = from("examplesSpecifiedInParamDefinition_paramDefinition.json");
        final CodegenParameter codegenParameter = codegenParameterWith(parameterJsonDefinition);

        given(markdownConverter.toHtml(any()))
            .willReturn("Example A from request parameter's definition - description with <code class=\"codeinline\">Markdown</code>")
            .willReturn("Example B from request parameter's definition - description with <code class=\"codeinline\">Markdown</code>");

        // when
        final String actualHtmlForExampleValue =
            codegenRequestParameterExampleRenderer.htmlForExampleValueOf(codegenParameter);

        // then
        assertThat("Returns HTML for single example from parameter's schema.",
            actualHtmlForExampleValue,
            is(expectedHtmlForExampleValue)
        );

        then(markdownConverter).should().toHtml("Example A from request parameter's definition - description with `Markdown`");
        then(markdownConverter).should().toHtml("Example B from request parameter's definition - description with `Markdown`");
    }

    @Test
    public void generatesHtml_forExampleSpecifiedInParamSchema_whenThereIsNoExampleInParamDefinition() {

        // given
        final String expectedHtmlForExampleValue = from("exampleSpecifiedInParamSchemaNoneInDefinition_expectedHtml.html");

        final String parameterJsonDefinition = from("exampleSpecifiedInParamSchemaNoneInDefinition_parameterDefinition.json");
        final CodegenParameter codegenParameter = codegenParameterWith(parameterJsonDefinition);

        // when
        final String actualHtmlForExampleValue = codegenRequestParameterExampleRenderer.htmlForExampleValueOf(codegenParameter);

        // then
        assertThat("Returns HTML for single example from parameter's schema.",
            actualHtmlForExampleValue,
            is(expectedHtmlForExampleValue)
        );
    }

    @Test
    public void throwsException_onFailureToProcessCodegenParameter() {

        // given
        final String parameterJsonDefinition = "{ invalid JSON }";
        final CodegenParameter codegenParameter = codegenParameterWith(parameterJsonDefinition);

        this.expectedException.expectMessage("Failed to generate HTML for examples of parameter param-name(data-type)");
        this.expectedException.expectCause(instanceOf(JsonParseException.class));

        // when
        codegenRequestParameterExampleRenderer.htmlForExampleValueOf(codegenParameter);

        // then
        // expectations set up in 'given' are satisfied
    }

    @NotNull private CodegenParameter codegenParameterWith(final String parameterJsonDefinition) {

        final CodegenParameter codegenParameter = new CodegenParameter();
        codegenParameter.jsonSchema = parameterJsonDefinition;
        codegenParameter.example = "ignored value, we only read from jsonSchema";

        // only used here for codegenParameter.toString():
        codegenParameter.baseName = "param-name";
        codegenParameter.dataType = "data-type";

        return codegenParameter;
    }

    @NotNull private static String from(final String testFileName) {
        return fileContentFromClasspath(TEST_DATA_FILES_PATH + "/" + testFileName);
    }
}