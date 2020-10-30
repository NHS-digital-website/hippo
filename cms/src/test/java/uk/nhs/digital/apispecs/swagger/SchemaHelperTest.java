package uk.nhs.digital.apispecs.swagger;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static uk.nhs.digital.test.util.FileUtils.fileContentFromClasspath;

import com.github.jknack.handlebars.HandlebarsException;
import com.github.jknack.handlebars.Options;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.parser.OpenAPIV3Parser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;

public class SchemaHelperTest {

    @Rule public ExpectedException expectedException = ExpectedException.none();

    private static final String TEST_DATA_FILES_CLASSPATH = "/test-data/api-specifications/SchemaHelperTest";

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
    public void rendersAllSimpleFieldsOfSingleSchemaObjectAsHtml() {

        // given
        final String expectedSchemaHtml = readFrom("schemaObjectCompleteTopLevel-simpleFields.html");

        final Schema schemaObject = from("schemaObjectCompleteTopLevel-simpleFields.json");

        // when
        final String actualSchemaHtml = schemaHelper.apply(schemaObject, null);

        // then
        assertThat("All 'simple' fields of Schema Object are rendered in HTML.",
            actualSchemaHtml,
            is(expectedSchemaHtml)
        );
    }

    @Test
    public void rendersNumericalFieldsSetToZero() {

        // given
        final String expectedSchemaHtml = readFrom("schemaObjectCompleteTopLevel-numericalFieldsZero.html");

        final Schema schemaObject = from("schemaObjectCompleteTopLevel-numericalFieldsZero.json");

        // when
        final String actualSchemaHtml = schemaHelper.apply(schemaObject, null);

        // then
        assertThat("All 'simple' fields of Schema Object are rendered in HTML.",
            actualSchemaHtml,
            is(expectedSchemaHtml)
        );
    }

    @Test
    public void doesNotRenderFieldsAbsentFromSpecification() {

        // given
        final String expectedSchemaHtml = readFrom("schemaObject-empty.html");

        final Schema schemaObject = new ObjectSchema();

        // when
        final String actualSchemaHtml = schemaHelper.apply(schemaObject, null);

        // then
        assertThat("Fields absent from the specifications are not rendered.",
            actualSchemaHtml,
            is(expectedSchemaHtml)
        );
    }

    @Test
    public void rendersCompleteHierarchyOfSchemaObjectsAsHtml_traversingFieldsProperties() {

        // given
        // One object at each level with simple fields are defined here
        // to demonstrate that the 'simple' fields are rendered at more than the top level
        // but the focus is on correct traversing of the hierarchy of nested objects.
        final String expectedSchemaHtml = readFrom("schemaObjectsMultiLevelHierarchy-properties.html");

        final Schema schemaObject = from("schemaObjectsMultiLevelHierarchy-properties.json");

        // when
        final String actualSchemaHtml = schemaHelper.apply(schemaObject, null);

        // then
        assertThat("All Schema Objects in the hierarchy are rendered in HTML.",
            actualSchemaHtml,
            is(expectedSchemaHtml)
        );
    }

    @Test
    public void rendersCompleteHierarchyOfSchemaObjectsAsHtml_traversingFieldsItems() {

        // given
        // One object at each level with simple fields are defined here
        // to demonstrate that the 'simple' fields are rendered at more than the top level
        // but the focus is on correct traversing of the hierarchy of nested objects.
        final String expectedSchemaHtml = readFrom("schemaObjectsMultiLevelHierarchy-items.html");

        final Schema schemaObject = from("schemaObjectsMultiLevelHierarchy-items.json");

        // when
        final String actualSchemaHtml = schemaHelper.apply(schemaObject, null);

        // then
        assertThat("All Schema Objects in the hierarchy are rendered in HTML.",
            actualSchemaHtml,
            is(expectedSchemaHtml)
        );
    }

    @Test
    public void exclusiveMaximumRenderedAsExclusiveWhenExclusiveMaximumIsTrue() {

        // given
        final Schema schemaObject = new ObjectSchema().maximum(BigDecimal.valueOf(10)).exclusiveMaximum(true);

        // when
        final String actualSchemaHtml = schemaHelper.apply(schemaObject, null);

        // then
        assertThat("Exclusive maximum is rendered as exclusive.",
            actualSchemaHtml,
            containsString("(exclusive)")
        );
    }

    @Test
    public void exclusiveMaximumRenderedAsInclusiveWhenExclusiveMaximumIsFalse() {

        // given
        final Schema schemaObject = new ObjectSchema().maximum(BigDecimal.valueOf(10)).exclusiveMaximum(false);

        // when
        final String actualSchemaHtml = schemaHelper.apply(schemaObject, null);

        // then
        assertThat("Exclusive maximum is rendered as inclusive.",
            actualSchemaHtml,
            containsString("(inclusive)")
        );
    }

    @Test
    public void exclusiveMaximumNotRenderedWhenMaximumIsAbsent() {

        // given
        final Schema schemaObject = new ObjectSchema().exclusiveMaximum(true);

        // when
        final String actualSchemaHtml = schemaHelper.apply(schemaObject, null);

        // then
        assertThat("Exclusive maximum is not rendered.",
            actualSchemaHtml,
            allOf(
                not(containsString("(inclusive)")),
                not(containsString("(exclusive)"))
            ));
    }

    @Test
    public void exclusiveMinimumRenderedAsExclusiveWhenExclusiveMinimumIsTrue() {

        // given
        final Schema schemaObject = new ObjectSchema().minimum(BigDecimal.valueOf(10)).exclusiveMinimum(true);

        // when
        final String actualSchemaHtml = schemaHelper.apply(schemaObject, null);

        // then
        assertThat("Exclusive minimum is rendered as exclusive.",
            actualSchemaHtml,
            containsString("(exclusive)")
        );
    }

    @Test
    public void exclusiveMinimumRenderedAsInclusiveWhenExclusiveMinimumIsFalse() {

        // given
        final Schema schemaObject = new ObjectSchema().minimum(BigDecimal.valueOf(10)).exclusiveMinimum(false);

        // when
        final String actualSchemaHtml = schemaHelper.apply(schemaObject, null);

        // then
        assertThat("Exclusive minimum is rendered as inclusive.",
            actualSchemaHtml,
            containsString("(inclusive)")
        );
    }

    @Test
    public void exclusiveMinimumNotRenderedWhenMinimumIsAbsent() {

        // given
        final Schema schemaObject = new ObjectSchema().exclusiveMinimum(true);

        // when
        final String actualSchemaHtml = schemaHelper.apply(schemaObject, null);

        // then
        assertThat("Exclusive minimum is not rendered.",
            actualSchemaHtml,
            allOf(
                not(containsString("(inclusive)")),
                not(containsString("(exclusive)"))
            ));
    }

    @Test
    public void throwsExceptionOnSchemaRenderingFailure() {

        // given
        final Schema schemaObject = new ObjectSchema().description("a description");

        given(markdownHelper.apply(any(), any())).willThrow(new RuntimeException());

        expectedException.expectMessage("Failed to render schema.");
        expectedException.expect(RuntimeException.class);
        expectedException.expectCause(instanceOf(HandlebarsException.class));

        // when
        schemaHelper.apply(schemaObject, null);

        // then

        // expectations set up in 'given' are satisfied
    }

    private Schema from(final String specJsonFileName) {
        final OpenAPI openApi = new OpenAPIV3Parser().read(TEST_DATA_FILES_CLASSPATH + "/" + specJsonFileName);

        return openApi.getPaths().get("/test").getPost().getRequestBody().getContent().get("application/json").getSchema();
    }

    private String readFrom(final String testDataFileName) {
        return fileContentFromClasspath(TEST_DATA_FILES_CLASSPATH + "/" + testDataFileName);
    }
}