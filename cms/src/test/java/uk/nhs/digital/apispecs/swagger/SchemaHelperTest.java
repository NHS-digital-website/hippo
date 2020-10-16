package uk.nhs.digital.apispecs.swagger;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static uk.nhs.digital.test.util.FileUtils.fileContentFromClasspath;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jknack.handlebars.HandlebarsException;
import com.github.jknack.handlebars.Options;
import io.swagger.v3.oas.models.media.ObjectSchema;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SchemaHelperTest {

    @Rule public ExpectedException expectedException = ExpectedException.none();

    private static final String TEST_DATA_FILES_CLASSPATH = "/test-data/api-specifications/SchemaHelperTest";

    private final ObjectMapper objectMapper = new ObjectMapper()
        .configure(MapperFeature.USE_ANNOTATIONS, true)
        ;

    private SchemaHelper schemaHelper;
    private MarkdownHelper markdownHelper;

    @Before
    public void setUp() {
        markdownHelper = mock(MarkdownHelper.class);

        given(markdownHelper.apply(eq("Test Schema Object description in `Markdown`."), any(Options.class)))
            .willReturn("Test Schema Object description in <code>Markdown rendered to HTML</code>.");

        schemaHelper = new SchemaHelper(markdownHelper);
    }

    @Test
    public void rendersCompleteTopLevelSchemaObjectAsHtml() {

        // given
        final String expectedSchemaHtml = readFrom("schemaObjectCompleteTopLevel.html");

        final ObjectSchema schemaObject = fromJsonFile("schemaObjectCompleteTopLevel.json");

        // when
        final String actualHtmlSchema = schemaHelper.apply(schemaObject, null);

        // then
        assertThat("All 'simple' fields of Schema Object are rendered in HTML.", actualHtmlSchema, is(expectedSchemaHtml));
    }

    @Test
    public void doesNotRenderFieldsAbsentFromTheSpecification() {

        // given
        final String expectedSchemaHtml = readFrom("schemaObjectEmpty.html");

        final ObjectSchema schemaObject = fromJson("{}");

        // when
        final String actualHtmlSchema = schemaHelper.apply(schemaObject, null);

        // then
        assertThat("Fields absent from the specifications are not rendered.", actualHtmlSchema, is(expectedSchemaHtml));
    }

    @Test
    public void exclusiveMaximumRenderedAsExclusiveWhenExclusiveMaximumIsTrue() {

        // given
        final ObjectSchema schemaObject = fromJson("{ \"maximum\": 10, \"exclusiveMaximum\": true}");

        // when
        final String actualHtmlSchema = schemaHelper.apply(schemaObject, null);

        // then
        assertThat("Exclusive maximum is rendered as exclusive.", actualHtmlSchema, containsString("(exclusive)"));
    }

    @Test
    public void exclusiveMaximumRenderedAsInclusiveWhenExclusiveMaximumIsFalse() {

        // given
        final ObjectSchema schemaObject = fromJson("{ \"maximum\": 10, \"exclusiveMaximum\": false}");

        // when
        final String actualHtmlSchema = schemaHelper.apply(schemaObject, null);

        // then
        assertThat("Exclusive maximum is rendered as inclusive.", actualHtmlSchema, containsString("(inclusive)"));
    }

    @Test
    public void exclusiveMaximumNotRenderedWhenMaximumIsAbsent() {

        // given
        final ObjectSchema schemaObject = fromJson("{ \"exclusiveMaximum\": true}");

        // when
        final String actualHtmlSchema = schemaHelper.apply(schemaObject, null);

        // then
        assertThat("Exclusive maximum is not rendered.",
            actualHtmlSchema,
            allOf(
                not(containsString("(inclusive)")),
                not(containsString("(exclusive)"))
            ));
    }

    @Test
    public void exclusiveMinimumRenderedAsExclusiveWhenExclusiveMinimumIsTrue() {

        // given
        final ObjectSchema schemaObject = fromJson("{ \"minimum\": 10, \"exclusiveMinimum\": true}");

        // when
        final String actualHtmlSchema = schemaHelper.apply(schemaObject, null);

        // then
        assertThat("Exclusive minimum is rendered as exclusive.", actualHtmlSchema, containsString("(exclusive)"));
    }

    @Test
    public void exclusiveMinimumRenderedAsInclusiveWhenExclusiveMinimumIsFalse() {

        // given
        final ObjectSchema schemaObject = fromJson("{ \"minimum\": 10, \"exclusiveMinimum\": false}");

        // when
        final String actualHtmlSchema = schemaHelper.apply(schemaObject, null);

        // then
        assertThat("Exclusive minimum is rendered as inclusive.", actualHtmlSchema, containsString("(inclusive)"));
    }

    @Test
    public void exclusiveMinimumNotRenderedWhenMinimumIsAbsent() {

        // given
        final ObjectSchema schemaObject = fromJson("{ \"exclusiveMinimum\": true}");

        // when
        final String actualHtmlSchema = schemaHelper.apply(schemaObject, null);

        // then
        assertThat("Exclusive minimum is not rendered.",
            actualHtmlSchema,
            allOf(
                not(containsString("(inclusive)")),
                not(containsString("(exclusive)"))
            ));
    }

    @Test
    public void throwsExceptionOnSchemaRenderingFailure() {

        // given
        final ObjectSchema schemaObject = fromJson("{ \"description\": \"a description\" }");

        given(markdownHelper.apply(any(), any())).willThrow(new RuntimeException());

        expectedException.expectMessage("Failed to render schema.");
        expectedException.expect(RuntimeException.class);
        expectedException.expectCause(instanceOf(HandlebarsException.class));

        // when
        schemaHelper.apply(schemaObject, null);

        // then

        // expectations set up in 'given' are satisfied
    }

    private ObjectSchema fromJsonFile(final String schemaJsonFileName) {

        final String json = readFrom(schemaJsonFileName);

        return fromJson(json);
    }

    private ObjectSchema fromJson(final String json) {
        try {
            return objectMapper.readValue(json, ObjectSchema.class);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialise given JSON.", e);
        }
    }

    private String readFrom(final String testDataFileName) {
        return fileContentFromClasspath(TEST_DATA_FILES_CLASSPATH + "/" + testDataFileName);
    }
}