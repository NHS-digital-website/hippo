package uk.nhs.digital.common.components.apispecification.swagger.request.examplerenderer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static uk.nhs.digital.test.util.TestFileUtils.contentOfFileFromClasspath;

import com.fasterxml.jackson.core.JsonParseException;
import io.swagger.codegen.v3.CodegenParameter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.nhs.digital.common.components.apispecification.commonmark.CommonmarkMarkdownConverter;

@RunWith(MockitoJUnitRunner.class)
public class CodegenParameterExampleHtmlRendererTest {

    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Mock private CommonmarkMarkdownConverter markdownConverter;

    private CodegenParameterExampleHtmlRenderer codegenParameterExampleHtmlRenderer;

    @Before
    public void setUp() {
        codegenParameterExampleHtmlRenderer = new CodegenParameterExampleHtmlRenderer(markdownConverter);
    }

    @Test
    public void rendersExampleSpecifiedInParamDefinition_evenWhenExampleIsSpecifiedInParamSchema() {

        // given
        final String expectedHtmlForExampleValue =
            "Example: <span class=\"nhsd-a-text-highlight nhsd-a-text-highlight--code\">"
                + "simple example specified in parameter's definition with HTML hostile characters &lt; &gt; &amp;</span>";

        final String parameterJsonDefinition = from("exampleSpecifiedInParamDefinitionAndInSchema_parameterDefinition.json");
        final CodegenParameter codegenParameter = codegenParameterWith(parameterJsonDefinition);

        // when
        final String actualHtmlForExampleValue = codegenParameterExampleHtmlRenderer.htmlForExampleValueOf(codegenParameter.getJsonSchema());

        // then
        assertThat("Returns HTML for single example from parameter's schema.",
            actualHtmlForExampleValue,
            is(expectedHtmlForExampleValue)
        );
    }

    @Test
    public void rendersExamplesSpecifiedInParamDefinition_evenWhenExampleIsSpecifiedInParamSchema() {

        // given
        final String expectedHtmlForExampleValue = from("examplesSpecifiedInParamDefinition_expectedHtml.html");

        final String parameterJsonDefinition = from("examplesSpecifiedInParamDefinition_paramDefinition.json");
        final CodegenParameter codegenParameter = codegenParameterWith(parameterJsonDefinition);

        given(markdownConverter.toHtml(any()))
            .willReturn("Example A from request parameter's definition - description with <span class=\"nhsd-a-text-highlight nhsd-a-text-highlight--code\">Markdown</span>")
            .willReturn("Example B from request parameter's definition - description with <span class=\"nhsd-a-text-highlight nhsd-a-text-highlight--code\">Markdown</span>");

        // when
        final String actualHtmlForExampleValue =
            codegenParameterExampleHtmlRenderer.htmlForExampleValueOf(codegenParameter.getJsonSchema());

        // then
        assertThat("Returns HTML for single example from parameter's schema.",
            actualHtmlForExampleValue,
            is(expectedHtmlForExampleValue)
        );

        then(markdownConverter).should().toHtml("Example A from request parameter's definition - description with `Markdown`");
        then(markdownConverter).should().toHtml("Example B from request parameter's definition - description with `Markdown`");
    }

    @Test
    public void rendersExampleSpecifiedInParamSchema_whenThereIsNoExampleInParamDefinition() {

        // given
        final String expectedHtmlForExampleValue =
            "Example: <span class=\"nhsd-a-text-highlight nhsd-a-text-highlight--code\">"
                + "example specified in parameter's schema with HTML hostile characters &lt; &gt; &amp;</span>";

        final String parameterJsonDefinition = from("exampleSpecifiedInParamSchemaNoneInDefinition_parameterDefinition.json");
        final CodegenParameter codegenParameter = codegenParameterWith(parameterJsonDefinition);

        // when
        final String actualHtmlForExampleValue = codegenParameterExampleHtmlRenderer.htmlForExampleValueOf(codegenParameter.getJsonSchema());

        // then
        assertThat("Returns HTML for single example from parameter's schema.",
            actualHtmlForExampleValue,
            is(expectedHtmlForExampleValue)
        );
    }

    @Test
    public void rendersExamplesHeader_whenMultipleExamplesSpecifiedInExamplesField() {

        // given
        final String parameterJsonDefinition = from("multipleExamplesSpecifiedInParamDefinition_paramDefinition.json");
        final CodegenParameter codegenParameter = codegenParameterWith(parameterJsonDefinition);

        // when
        final String actualHtmlForExampleValue = codegenParameterExampleHtmlRenderer.htmlForExampleValueOf(codegenParameter.getJsonSchema());

        // then
        assertThat("Renders plural version of the header for multiple examples in the collection.",
            actualHtmlForExampleValue,
            containsString(">Examples<")
        );
    }

    @Test
    public void rendersExampleHeader_whenSingleExampleSpecifiedInExamplesField() {

        // given
        final String parameterJsonDefinition = from("singleExampleSpecifiedInParamDefinition_paramDefinition.json");
        final CodegenParameter codegenParameter = codegenParameterWith(parameterJsonDefinition);

        // when
        final String actualHtmlForExampleValue = codegenParameterExampleHtmlRenderer.htmlForExampleValueOf(codegenParameter.getJsonSchema());

        // then
        assertThat("Renders singular version of the header for single example in the collection.",
            actualHtmlForExampleValue,
            containsString(">Example<")
        );
    }

    @Test
    public void throwsException_withUnderlyingCause_onFailureToProcessCodegenParameter() {

        // given
        final String invalidParameterJsonDefinition = "{ invalid JSON }";
        final CodegenParameter codegenParameter = codegenParameterWith(invalidParameterJsonDefinition);

        this.expectedException.expectMessage("Failed to generate HTML for examples from JSON schema: " + invalidParameterJsonDefinition);
        this.expectedException.expectCause(instanceOf(JsonParseException.class));

        // when
        codegenParameterExampleHtmlRenderer.htmlForExampleValueOf(codegenParameter.getJsonSchema());

        // then
        // expectations set up in 'given' are satisfied
    }

    private CodegenParameter codegenParameterWith(final String parameterJsonDefinition) {

        final CodegenParameter codegenParameter = new CodegenParameter();
        codegenParameter.jsonSchema = parameterJsonDefinition;
        codegenParameter.example = "ignored value, we only read from jsonSchema";

        // only used here for codegenParameter.toString():
        codegenParameter.baseName = "param-name";
        codegenParameter.dataType = "data-type";

        return codegenParameter;
    }

    private String from(final String testFileName) {
        return contentOfFileFromClasspath(
            "/test-data/api-specifications/CodegenRequestParameterExampleGeneratorTest/" + testFileName
        );
    }
}