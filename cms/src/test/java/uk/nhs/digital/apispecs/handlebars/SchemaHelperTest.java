package uk.nhs.digital.apispecs.handlebars;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static uk.nhs.digital.test.util.FileUtils.contentOfFileFromClasspath;
import static uk.nhs.digital.test.util.ReflectionTestUtils.setField;
import static uk.nhs.digital.test.util.StringTestUtils.Placeholders.placeholders;
import static uk.nhs.digital.test.util.StringTestUtils.dropBlankLines;

import com.github.jknack.handlebars.HandlebarsException;
import com.github.jknack.handlebars.Options;
import com.google.common.collect.ImmutableMap;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.parser.OpenAPIV3Parser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import uk.nhs.digital.test.util.FileUtils;
import uk.nhs.digital.test.util.StringTestUtils.Placeholders;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.Optional;

@RunWith(DataProviderRunner.class)
public class SchemaHelperTest {

    @Rule public ExpectedException expectedException = ExpectedException.none();

    private static final String TEST_DATA_FILES_CLASSPATH = "/test-data/api-specifications/SchemaHelperTest";
    private static final String PROPERTY_PLACEHOLDER_X_OF = "xOfPropertyPlaceholder";

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
        final String expectedSchemaHtml = dropBlankLines(readFrom("schemaObjectCompleteTopLevel-simpleFields.html"));

        final Schema schemaObject = fromJsonFile("schemaObjectCompleteTopLevel-simpleFields.json");

        // when
        final String actualSchemaHtml = renderSchemaDroppingBlankLines(schemaObject);

        // then
        assertThat("All 'simple' fields of Schema Object are rendered in HTML.",
            actualSchemaHtml,
            is(expectedSchemaHtml)
        );
    }

    @Test
    public void rendersNumericalFieldsSetToZero() {

        // given
        final Schema schemaObject = new ObjectSchema()
            .multipleOf(BigDecimal.ZERO)
            .minimum(BigDecimal.ZERO)
            .maximum(BigDecimal.ZERO)
            .minLength(0)
            .maxLength(0)
            .maxItems(0)
            .minItems(0)
            .minProperties(0)
            .maxProperties(0);

        // when
        final String actualSchemaHtml = renderSchemaDroppingBlankLines(schemaObject);

        // then
        assertThat("Numerical properties are rendered if they have value of zero.",
            actualSchemaHtml,
            allOf(
                containsString("<div>Multiple of: <code class=\"codeinline multipleof\">0</code><div>"),
                containsString("<div>Maximum: <code class=\"codeinline maximum\">0</code>"),
                containsString("<code class=\"codeinline exclusivemaximum\">(inclusive)</code>"),
                containsString("<div>Minimum: <code class=\"codeinline minimum\">0</code>"),
                containsString("<code class=\"codeinline exclusiveminimum\">(inclusive)</code>"),
                containsString("<div>Max length: <code class=\"codeinline maxlength\">0</code></div>"),
                containsString("<div>Min length: <code class=\"codeinline minlength\">0</code></div>"),
                containsString("<div>Max items: <code class=\"codeinline maxitems\">0</code></div>"),
                containsString("<div>Min items: <code class=\"codeinline minitems\">0</code></div>"),
                containsString("<div>Max properties: <code class=\"codeinline maxproperties\">0</code></div>"),
                containsString("<div>Min properties: <code class=\"codeinline minproperties\">0</code></div>")
            )
        );
    }

    @Test
    public void doesNotRenderFieldsAbsentFromSpecification() {

        // given
        final Schema schemaObject = new ObjectSchema();

        final String expectedSchemaHtml = dropBlankLines(readFrom("schemaObject-empty.html"));

        // when
        final String actualSchemaHtml = renderSchemaDroppingBlankLines(schemaObject);

        // then
        assertThat("Fields absent from the specifications are not rendered.",
            actualSchemaHtml,
            is(expectedSchemaHtml)
        );
    }

    @Test
    public void rendersCompleteHierarchyOfSchemaObjectsWithTheirFieldsAndIndentationsAsHtml_traversingFieldsProperties() {

        // given
        final String expectedSchemaHtml = dropBlankLines(readFrom("schemaObjectsMultiLevelHierarchy-properties.html"));

        final Schema schemaObject = fromJsonFile("schemaObjectsMultiLevelHierarchy-properties.json");

        // when
        final String actualSchemaHtml = renderSchemaDroppingBlankLines(schemaObject);

        // then
        assertThat("All Schema Objects in the hierarchy are rendered in HTML.",
            actualSchemaHtml,
            is(expectedSchemaHtml)
        );
    }

    @Test
    @UseDataProvider("propertyNameProvider_xOf")
    public void rendersCompleteHierarchyOfSchemaObjectsWithTheirFieldsAndIndentationsAsHtml_traversingFieldsXOf(final String propertyName) {

        // given
        final String expectedSchemaHtml = dropBlankLines(readFrom("schemaObjectsMultiLevelHierarchy-xOf.html"))
            .replaceAll(PROPERTY_PLACEHOLDER_X_OF, propertyName);

        final Schema schemaObject = fromJsonFile("schemaObjectsMultiLevelHierarchy-xOf.json",
            placeholders().with(PROPERTY_PLACEHOLDER_X_OF, propertyName)
        );

        // when
        final String actualSchemaHtml = renderSchemaDroppingBlankLines(schemaObject);

        // then
        assertThat("All Schema Objects in the hierarchy are rendered in HTML.",
            actualSchemaHtml,
            is(expectedSchemaHtml)
        );
    }

    @Test
    public void rendersCompleteHierarchyOfSchemaObjectsWithTheirFieldsAndIndentationsAsHtml_traversingFieldsItems() {

        // given
        final String expectedSchemaHtml = dropBlankLines(readFrom("schemaObjectsMultiLevelHierarchy-items.html"));

        final Schema schemaObject = fromJsonFile("schemaObjectsMultiLevelHierarchy-items.json");

        // when
        final String actualSchemaHtml = renderSchemaDroppingBlankLines(schemaObject);

        // then
        assertThat("All Schema Objects in the hierarchy are rendered in HTML.",
            actualSchemaHtml,
            is(expectedSchemaHtml)
        );
    }

    @Test
    public void exclusiveMaximum_rendered_asExclusive_whenExclusiveMaximumIsTrue() {

        // given
        final Schema schemaObject = new ObjectSchema()
            .maximum(BigDecimal.ZERO)
            .exclusiveMaximum(true);

        // when
        final String actualSchemaHtml = renderSchemaDroppingBlankLines(schemaObject);

        // then
        assertThat("Exclusive maximum is rendered as exclusive.",
            actualSchemaHtml,
            containsString("<code class=\"codeinline exclusivemaximum\">(exclusive)</code>")
        );
    }

    @Test
    public void exclusiveMaximum_rendered_asInclusive_whenExclusiveMaximumIsFalse() {

        // given
        final Schema schemaObject = new ObjectSchema()
            .maximum(BigDecimal.ZERO)
            .exclusiveMaximum(false);

        // when
        final String actualSchemaHtml = renderSchemaDroppingBlankLines(schemaObject);

        // then
        assertThat("Exclusive maximum is rendered as inclusive.",
            actualSchemaHtml,
            containsString("<code class=\"codeinline exclusivemaximum\">(inclusive)</code>")
        );
    }

    @Test
    public void exclusiveMaximum_notRendered_whenMaximumIsAbsent() {

        // given
        final Schema schemaObject = new ObjectSchema().exclusiveMaximum(true);

        // when
        final String actualSchemaHtml = renderSchemaDroppingBlankLines(schemaObject);

        // then
        assertThat("Exclusive maximum is not rendered.",
            actualSchemaHtml,
            allOf(
                not(containsString("(inclusive)")),
                not(containsString("(exclusive)"))
            ));
    }

    @Test
    public void exclusiveMinimum_rendered_asExclusive_whenExclusiveMinimumIsTrue() {

        // given
        final Schema schemaObject = new ObjectSchema()
            .minimum(BigDecimal.ZERO)
            .exclusiveMinimum(true);

        // when
        final String actualSchemaHtml = renderSchemaDroppingBlankLines(schemaObject);

        // then
        assertThat("Exclusive minimum is rendered as exclusive.",
            actualSchemaHtml,
            containsString("<code class=\"codeinline exclusiveminimum\">(exclusive)</code>")
        );
    }

    @Test
    public void exclusiveMinimum_rendered_asInclusive_whenExclusiveMinimumIsFalse() {

        // given
        final Schema schemaObject = new ObjectSchema()
            .minimum(BigDecimal.ZERO)
            .exclusiveMinimum(false);

        // when
        final String actualSchemaHtml = renderSchemaDroppingBlankLines(schemaObject);

        // then
        assertThat("Exclusive minimum is rendered as inclusive.",
            actualSchemaHtml,
            containsString("<code class=\"codeinline exclusiveminimum\">(inclusive)</code>")
        );
    }

    @Test
    public void exclusiveMinimum_notRendered_whenMinimumIsAbsent() {

        // given
        final Schema schemaObject = new ObjectSchema().exclusiveMinimum(true);

        // when
        final String actualSchemaHtml = renderSchemaDroppingBlankLines(schemaObject);

        // then
        assertThat("Exclusive minimum is not rendered.",
            actualSchemaHtml,
            allOf(
                not(containsString("(inclusive)")),
                not(containsString("(exclusive)"))
            ));
    }

    @Test
    public void itemsRow_rendered_whenNeitherOfXOfPropertiesArePresent() {

        // given
        final Schema schemaObject = schemaWithItemsWithNoXOf();

        // when
        final String actualSchemaHtml = renderSchemaDroppingBlankLines(schemaObject);

        // then
        assertThat("HTML contains 'items' element.",
            actualSchemaHtml,
            containsString(">items object<")
        );
    }

    @Test
    @UseDataProvider("propertyNameProvider_xOf")
    public void itemsRow_notRendered_whenEitherOfXOfPropertiesArePresent(final String propertyName) {

        // given
        final Schema schemaObject = schemaWithXOfPropertyUnderItemsObject(propertyName);

        // when
        final String actualSchemaHtml = renderSchemaDroppingBlankLines(schemaObject);

        // then
        assertThat("HTML does not contain 'items' row.",
            actualSchemaHtml,
            not(containsString(">items object<"))
        );
    }

    @Test
    @UseDataProvider("propertyNameProvider_xOf")
    public void property_xOf_rendered_whenPresent_inObjectOtherThanItems(final String propertyName) {

        // given
        final Schema schemaObject = schemaWithXOfPropertyUnderNonItemsObject(propertyName);

        // when
        final String actualSchemaHtml = renderSchemaDroppingBlankLines(schemaObject);

        // then
        assertThat("HTML contains '" + propertyName + "' element.",
            actualSchemaHtml,
            stringContainsInOrder(
                "padding-left: 0em;",           // root
                "padding-left: 1em;",           //   non-items
                "padding-left: 2em;",           //     oneOf
                ">" + propertyName + "<",       //
                "padding-left: 3em;",           //       A
                ">" + propertyName + " - A<",   //
                "padding-left: 3em;",           //       B
                ">" + propertyName + " - B<"
            )
        );
    }

    @Test
    @UseDataProvider("propertyNameProvider_xOf")
    public void property_xOf_rendered_whenPresent_inItems(final String propertyName) {

        // given
        final Schema schemaObject = schemaWithXOfPropertyUnderItemsObject(propertyName);

        // when
        final String actualSchemaHtml = renderSchemaDroppingBlankLines(schemaObject);

        // then
        assertThat("HTML contains '" + propertyName + "' element.",
            actualSchemaHtml,
            stringContainsInOrder(
                "padding-left: 0em;",           // root
                "padding-left: 1em;",           //   array-schema
                "padding-left: 2em;",           //     oneOf
                ">" + propertyName + "<",       //
                "padding-left: 3em;",           //       A
                ">" + propertyName + " - A<",   //
                "padding-left: 3em;",           //       B
                ">" + propertyName + " - B<"
            )
        );
    }

    @Test
    @UseDataProvider("propertyNameProvider_xOf")
    public void property_xOf_notRendered_whenAbsent(final String propertyName) {

        // given
        final Schema schemaObject = schemaWithItemsWithNoXOf();

        // when
        final String actualSchemaHtml = renderSchemaDroppingBlankLines(schemaObject);

        // then
        assertThat("HTML contains '" + propertyName + "' element.",
            actualSchemaHtml,
            not(containsString(">" + propertyName + "<"))
        );
    }

    @Test
    @UseDataProvider("schemaTypesToDefaultValues")
    public void rendersDefault_forSchemas_ofVariousTypes(
        final String testCaseDescription,
        final String schemaType,
        final Object defaultValue,
        final String expectedRenderedValue
    ) {
        // given
        final Schema schemaObject = fromJsonFile("schemaObject-defaultField.json",
            placeholders()
                .with("typePlaceholder", schemaType)
                .with("defaultValuePlaceholder", doubleQuotedIfString(schemaType, defaultValue))
        );

        // when
        final String actualSchemaHtml = renderSchemaDroppingBlankLines(schemaObject);

        // then
        assertThat("Default " + testCaseDescription + " value is rendered for schema of type '" + schemaType + "'.",
            actualSchemaHtml,
            containsString("<div>Default: <code class=\"codeinline default\">" + expectedRenderedValue + "</code></div>")
        );
    }

    @DataProvider
    public static Object[][] schemaTypesToDefaultValues() {
        // @formatter:off
        return new Object[][]{
            // test case description       schema type  default value - JSON           expected rendered value
            {"simple string",              "string",    "simple default string value", "simple default string value"},
            {"boolean truthy",             "boolean",   true,                          "true"},
            {"boolean falsy, not null",    "boolean",   false,                         "false"},
            {"numerical truthy",           "number",    -1.42,                         "-1.42"},
            {"numerical falsy, not null",  "number",    0,                             "0"},
            {"JSON object",                "object",    "{\"property\":\"value\"}",    "{&quot;property&quot;:&quot;value&quot;}"},
            {"JSON array",                 "array",     "[\"item-a\", \"item-b\"]",    "[&quot;item-a&quot;,&quot;item-b&quot;]"}
        };
        // @formatter:on
    }

    @Test
    @UseDataProvider("enumValuesOfVariousTypes")
    public void rendersEnum_ofVariousTypes(
        final String testCaseDescription,
        final Object enumJson,
        final String firstRenderedValue,
        final String secondRenderedValue
    ) {
        // given
        final Schema schemaObject = fromJsonFile("schemaObject-enumField.json",
            placeholders()
                .with("enumValuePlaceholder", enumJson)
        );

        final String expectedRendering = format(
            "<div>Allowed values: <code class=\"codeinline\">%s</code>, <code class=\"codeinline\">%s</code></div>",
            firstRenderedValue,
            secondRenderedValue
        );

        // when
        final String actualSchemaHtml = renderSchemaDroppingBlankLines(schemaObject);

        // then
        assertThat("Enum with " + testCaseDescription + " is rendered.",
            actualSchemaHtml,
            containsString(expectedRendering)
        );
    }

    @DataProvider
    public static Object[][] enumValuesOfVariousTypes() {
        // @formatter:off
        return new Object[][]{
            // testCaseDescription  enumJson                        firstRenderedValue  secondRenderedValue
            {"strings",             "[\"string-a\", \"string-b\"]", "string-a",         "string-b"},
            {"booleans",            "[true, false]",                "true",             "false"},
            {"numbers",             "[-1.42, 0]",                   "-1.42",            "0"},
            {"nulls",               "[\"string-a\", null]",         "string-a",         "null"},
            {"empty strings",       "[\"string-a\", \"\"]",         "string-a",         ""}
        };
        // @formatter:on
    }

    @Test
    public void rendersRequired_forObjectsUnderProperties_whereParentHasRequiredFieldWithTheirNames() {

        // given
        final Schema schemaObject = fromJsonFile("schemaObject-requiredField.json");

        // when
        final String actualSchemaHtml = renderSchemaDroppingBlankLines(schemaObject);

        // then
        assertThat("HTML with 'required' status rendered for the appropriate object.",
            actualSchemaHtml,
            stringContainsInOrder(
                ">first-object<", ">required<",
                ">second-object<",
                ">third-object<", ">required<"
            )
        );
    }

    @Test
    public void throwsExceptionOnSchemaRenderingFailure() {

        // given
        final Schema schemaObject = new ObjectSchema().description("a description");

        given(markdownHelper.apply(any(), any())).willThrow(new RuntimeException());

        expectedException.expectMessage("Failed to render schema.");
        expectedException.expect(SchemaRenderingException.class);
        expectedException.expectCause(instanceOf(HandlebarsException.class));

        // when
        schemaHelper.apply(schemaObject, null);

        // then

        // expectations set up in 'given' are satisfied
    }

    /**
     * <p>
     * Invokes the action under test, removing blank lines from the actual output
     * rendered.
     * <p>
     * The removal of blank lines helps in readability when debugging tests,
     * and in reducing the number of times test data files need updating:
     * <p>
     * In test cases that focus on, say, a small number of fields, the actual output
     * often contains many blank lines which makes it harder to see the complete
     * structure in one glance. Blank lines are redundant in HTML output and
     * removing them 'shrinks' the content to the size that makes is much easier
     * to analyse it.
     * <p>
     * Changes to the templates result in the updated output to stop matching
     * the test files, but the differences are very often limited to the blank
     * lines which, as said above, have not actual impact on the displayed
     * content. Ignoring those lines during assertions reduces the number
     * times the test files need to be updated whilst not invalidating their
     * value as a reference content.
     */
    private String renderSchemaDroppingBlankLines(final Schema schemaObject) {
        return dropBlankLines(schemaHelper.apply(schemaObject, null));
    }

    @DataProvider
    public static Object[][] propertyNameProvider_xOf() {
        return new Object[][]{{"oneOf"}, {"anyOf"}, {"allOf"}};
    }

    private Object doubleQuotedIfString(final String schemaType, final Object value) {
        return "string".equals(schemaType) ? ("\"" + value + "\"") : value;
    }

    private Schema schemaWithItemsWithNoXOf() {

        final ComposedSchema items = new ComposedSchema();
        items.title("items object");

        return new ObjectSchema()
            .properties(
                ImmutableMap.of("array-schema", new ArraySchema().items(items))
            );
    }

    private Schema schemaWithXOfPropertyUnderNonItemsObject(final String propertyName) {
        final ComposedSchema notItemSchemaObject = new ComposedSchema();
        notItemSchemaObject.title("not-items schema object");

        setField(notItemSchemaObject, propertyName, asList(
            new ObjectSchema().title(propertyName + " - A"),
            new ObjectSchema().title(propertyName + " - B")
        ));

        final Schema schemaObject = new ObjectSchema()
            .title("root schema object")
            .properties(
                ImmutableMap.of("not-items-schema-object", notItemSchemaObject)
            );
        return schemaObject;
    }

    private Schema schemaWithXOfPropertyUnderItemsObject(final String propertyName) {

        final ComposedSchema items = new ComposedSchema();
        items.title("items object");

        setField(items, propertyName, asList(
            new ObjectSchema().title(propertyName + " - A"),
            new ObjectSchema().title(propertyName + " - B")
        ));

        final Schema schemaObject = new ObjectSchema()
            .title("root object")
            .properties(
                ImmutableMap.of("array-schema", new ArraySchema().items(items))
            );
        return schemaObject;
    }

    private String classPathOf(final String testDataFileName) {
        return TEST_DATA_FILES_CLASSPATH + "/" + testDataFileName;
    }

    private Schema fromJsonFile(final String specJsonFileName) {
        return fromJsonFile(specJsonFileName, placeholders());
    }

    private Schema fromJsonFile(
        final String specJsonTemplateFileName,
        final Placeholders placeholders
    ) {
        File targetSpecJsonFile = null;
        try {
            final String specJsonWithPlaceholders =
                contentOfFileFromClasspath(classPathOf(specJsonTemplateFileName));

            final String specJsonWithResolvedPlaceholders = placeholders.resolveIn(specJsonWithPlaceholders);

            targetSpecJsonFile = Files.createTempFile(specJsonTemplateFileName, ".tmp").toFile();

            org.apache.commons.io.FileUtils.write(targetSpecJsonFile, specJsonWithResolvedPlaceholders, "UTF-8");

            return fromFileWithClasspath(targetSpecJsonFile.getAbsolutePath());

        } catch (final Exception e) {
            throw new RuntimeException("Failed to read in schema from " + specJsonTemplateFileName, e);

        } finally {
            Optional.ofNullable(targetSpecJsonFile).ifPresent(FileUtils::deleteFileOrEmptyDirIfExists);
        }
    }

    private Schema fromFileWithClasspath(final String testFileClasspathPath) {
        final OpenAPI openApi = new OpenAPIV3Parser().read(testFileClasspathPath);

        return openApi
            .getPaths()
            .get("/test")
            .getPost()
            .getRequestBody()
            .getContent()
            .get("application/json")
            .getSchema();
    }

    private String readFrom(final String testDataFileName) {
        return contentOfFileFromClasspath(classPathOf(testDataFileName));
    }
}