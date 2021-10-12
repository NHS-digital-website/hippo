package uk.nhs.digital.apispecs.swagger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;
import static uk.nhs.digital.test.util.StringTestUtils.*;
import static uk.nhs.digital.test.util.TestFileUtils.contentOfFileFromClasspath;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import uk.nhs.digital.apispecs.model.ApiSpecificationDocument;
import uk.nhs.digital.test.util.TestDataCache;


@RunWith(DataProviderRunner.class)
public class SwaggerCodeGenOpenApiSpecificationJsonToHtmlConverterTest {

    private final TestDataCache cache = TestDataCache.create();

    private final String specificationId = "123456";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private ApiSpecificationDocument apiSpecificationDocument;

    private SwaggerCodeGenOpenApiSpecificationJsonToHtmlConverter swaggerCodeGenApiSpecHtmlProvider;

    @Before
    public void setUp() {
        initMocks(this);

        given(apiSpecificationDocument.getId()).willReturn(specificationId);

        swaggerCodeGenApiSpecHtmlProvider = new SwaggerCodeGenOpenApiSpecificationJsonToHtmlConverter();
    }

    @Test
    public void rendersSpecification_asHtml_fromCompleteOasJson() {

        // given
        final String specificationJson = from("oasV3_complete.json");

        final String expectedSpecHtml = from("oasV3_complete.html");

        // when
        final String actualSpecHtml = swaggerCodeGenApiSpecHtmlProvider.htmlFrom(specificationJson);

        // then
        assertThat(
            "A complete spec has been rendered as HTML using customised Swagger CodeGen v3",
            ignoringUuids(ignoringWhiteSpacesIn(actualSpecHtml)),
            is(ignoringUuids(ignoringWhiteSpacesIn(expectedSpecHtml)))
        );
    }

    @Test
    public void rendersSpecification_withoutFailure_whenComponentsPropertyIsAbsent() {

        // given
        final String incompleteSpecificationJson = from("oasV3_incomplete_no_components-field.json");

        // when
        final String actualSpecHtml = swaggerCodeGenApiSpecHtmlProvider.htmlFrom(incompleteSpecificationJson);

        // then
        assertThat(
            "Specification is rendered with no error (i.e. 'at all') when the top-level 'components' field is absent from the source JSON",
            actualSpecHtml,
            containsString("Specification that does not define the top-level 'component' field.")
        );
    }

    @Test
    public void rendersOperationsWithPathObjectParameters_whenOperationsDoNotDefineTheirOwn() {

        // given
        final String incompleteSpecificationJson = from("oasV3_operation_with_no_own_parameters.json");

        // when
        final String actualSpecHtml = swaggerCodeGenApiSpecHtmlProvider.htmlFrom(incompleteSpecificationJson);

        // then
        assertThat(
            "Parameters rendered for operations are combination of their own and path's params.",
            ignoringUuids(ignoringWhiteSpacesIn(actualSpecHtml)),
            stringContainsInOrder(
                // "Path with no params: Operation with no parameters",
                // "Path with no params: Operation with parameters",
                // "Path with params: Operation with no parameters",
                // "Path with params: Operation with non-overlapping parameters",
                // "Path with params: Operation with overlapping parameters",

                ">Path with no params: Operation with no parameters</h3>",
                // This awkward concatenation of the description of the above operation with the heading of the following one
                // ensures there are no parameters displayed for the above operation where they none were expected.
                ">Operation with no parameters of its own. Expectation: result should contain no parameters.</p>\n"
                + "<h3 id=\"api-Default-path-with-no-params--operation-with-own-params-no-overlap-with-path\" class=\"nhsd-t-heading-l\">"

                + "Path with no params: Operation with parameters</h3>",
                ">Request<",
                ">Path parameters<",
                ">PathPathParam<", ">Path parameter defined as path variable.<",
                ">Query parameters<",
                ">PathQueryParam<", ">Path parameter expected as a query parameter.<",
                ">Headers<",
                ">PathHeaderParam<", ">Path parameter expected as a header.<",

                ">Path with params: Operation with non-overlapping parameters<",
                ">Request<",
                ">Path parameters<",
                ">PathPathParam<", ">Path parameter defined as path variable.<",
                ">Query parameters<",
                ">PathQueryParam<", "Path parameter expected as a query parameter.",
                ">OperationQueryParamPost<", ">Operation parameter expected as a query parameter.<",
                ">Headers<",
                ">PathHeaderParam<", ">Path parameter expected as a header.<",
                ">OperationHeaderParamPost<", ">Operation parameter expected as a header.<",

                ">Path with params: Operation with overlapping parameters<",
                ">Request<",
                ">Path parameters<",
                ">PathPathParam<", ">Path parameter defined as path variable - re-defined in the operation.<",
                ">Query parameters<",
                ">PathQueryParam<", ">Path parameter expected as a query parameter - re-defined in the operation.<",
                ">OperationQueryParamPut<", ">Operation parameter expected as a query parameter.<",
                ">Headers<",
                ">PathHeaderParam<", ">Path parameter expected as a header - re-defined in the operation.<",
                ">OperationHeaderParamPut<", ">Operation parameter expected as a header.<"
            )
        );
    }

    @Test
    public void rendersAllSupportedRequestAndResponseParameters() {

        // given
        final String specificationJson = from("oasV3_requestResponseParams.json");

        final String expectedSpecHtml = from("oasV3_requestResponseParams.html");

        // when
        final String actualSpecHtml = swaggerCodeGenApiSpecHtmlProvider.htmlFrom(specificationJson);

        // then
        assertThat(
            "A spec has been rendered with all supported request parameters.",
            ignoringUuids(ignoringWhiteSpacesIn(actualSpecHtml)),
            is(ignoringUuids(ignoringWhiteSpacesIn(expectedSpecHtml)))
        );
    }

    @Test
    public void throwsException_onCodeGenFailure() {

        // given
        final String invalidSpecificationJson = "invalid specification JSON";

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Failed to generate HTML for OpenAPI specification.");
        expectedException.expectCause(isA(RuntimeException.class));

        // when
        swaggerCodeGenApiSpecHtmlProvider.htmlFrom(invalidSpecificationJson);

        // then
        // expectations set in 'given' are satisfied
    }

    @Test
    @UseDataProvider("tryThisApiCustomExtensionPropertyValues")
    public void rendersTryThisApiButton_dependingOnCustomPropertyState(
        final String thePropertyIs,
        final String outcomeDescription,
        final String propertyJson,
        final Matcher<String> meetsExpectation
    ) {

        // given
        final String specificationJson = from("oasV3_withCustomExtension.json.template")
            .replaceAll("X-PROPERTY-PLACEHOLDER", propertyJson);

        // when
        final String actualSpecHtml = swaggerCodeGenApiSpecHtmlProvider.htmlFrom(specificationJson);

        // then
        assertThat(
            "Button 'Try this API' is " + outcomeDescription + " when property x-spec-publication.try-this-api.disabled is " + thePropertyIs,
            actualSpecHtml,
            meetsExpectation
        );
    }

    @DataProvider
    public static Object[][] tryThisApiCustomExtensionPropertyValues() {
        // @formatter:off
        return new Object[][]{
            // thePropertyIs    outcomeDescription propertyJson                                                            expectation
            {"absent", "rendered", "", containsString(">Enable Javascript to try this API<")},
            {"false", "rendered", "\"x-spec-publication\": {\"try-this-api\": {\"disabled\": false}},", containsString(">Enable Javascript to try this API<")},
            {"true", "not rendered", "\"x-spec-publication\": {\"try-this-api\": {\"disabled\": true}},", not(containsString(">Enable Javascript to try this API<"))}
        };
        // @formatter:on
    }

    @Test
    public void rendersEndpointsHeading_whenOperationOrderNotSupplied() {

        // given
        final String specificationJson = from("oasV3_operationOrderNotSupplied.json");

        // when
        final String actualSpecHtml = swaggerCodeGenApiSpecHtmlProvider.htmlFrom(specificationJson);

        // then
        assertThat(
            "Heading 'Endpoints' generated for operations not included in the ordering metadata - in the side nav and in the content area.",
            ignoringWhiteSpacesIn(actualSpecHtml),
            stringContainsInOrder(
                // side nav
                "<a class=\"nhsd-a-link\" href=\"#api-endpoints\">\nEndpoints\n</a>",
                "<a class=\"nhsd-a-link\" href=\"#api-Default-path_aGet\">\nGet operation A\n</a>",
                "<a class=\"nhsd-a-link\" href=\"#api-Default-path_aPost\">\nPost operation A\n</a>",
                "<a class=\"nhsd-a-link\" href=\"#api-Default-path_bGet\">\nGet operation B\n</a>",
                "<a class=\"nhsd-a-link\" href=\"#api-Default-path_bPost\">\nPost operation B\n</a>",
                // content area
                "<h2 id=\"api-endpoints\" class=\"nhsd-t-heading-xl\">Endpoints</h2>",
                "<h3 id=\"api-Default-path_aGet\" class=\"nhsd-t-heading-l\">Get operation A</h3>",
                "<h3 id=\"api-Default-path_aPost\" class=\"nhsd-t-heading-l\">Post operation A</h3>",
                "<h3 id=\"api-Default-path_bGet\" class=\"nhsd-t-heading-l\">Get operation B</h3>",
                "<h3 id=\"api-Default-path_bPost\" class=\"nhsd-t-heading-l\">Post operation B</h3>"
            )
        );
    }

    @Test
    public void rendersEndpointsHeadings_inSpecifiedOrder_whenOrderMetadataSupplied() {
        // given
        final String specificationJson = from("oasV3_operationOrderSupplied.json");

        // when
        final String actualSpecHtml = swaggerCodeGenApiSpecHtmlProvider.htmlFrom(specificationJson);

        // then

        // Assert that:
        // - groups ordered by custom order
        // - endpoints ordered within the groups by custom order
        // - paths with suffixes handled and rendered according to custom order
        // - endpoints not defined in the metadata ignored

        // Endpoints
        //   Post operation B
        //   Get operation A with suffix 1
        // Endpoints: Resource B
        //   Get operation B
        // Endpoints: Resource C
        //   Post operation A with suffix 2
        //   Post operation A with suffix 1
        // Endpoints: Resource A
        //   Post operation A
        //   Get operation A

        assertThat(
            "Headings 'Endpoint:' generated for each operation included in the ordering metadata - in the side nav and in the content area.",
            ignoringWhiteSpacesIn(actualSpecHtml),
            stringContainsInOrder(
                // nav menu with custom order
                "<a class=\"nhsd-a-link\" href=\"#api-endpoints\">\nEndpoints\n</a>",
                "<a class=\"nhsd-a-link\" href=\"#api-IgnoredTagA-path_bPost\">\nPost operation B\n</a>",
                "<a class=\"nhsd-a-link\" href=\"#api-IgnoredTagB-path_awith_suffix_1Get\">\nGet operation A with suffix 1\n</a>",
                "<a class=\"nhsd-a-link\" href=\"#api-Resource B\">\nEndpoints: Resource B\n</a>",
                "<a class=\"nhsd-a-link\" href=\"#api-IgnoredTagC-path_bGet\">\nGet operation B\n</a>",
                "<a class=\"nhsd-a-link\" href=\"#api-Resource C\">\nEndpoints: Resource C\n</a>",
                "<a class=\"nhsd-a-link\" href=\"#api-IgnoredTagB-path_awith_suffix_2Post\">\nPOST operation A with suffix 2\n</a>",
                "<a class=\"nhsd-a-link\" href=\"#api-IgnoredTagC-path_awith_suffix_1Post\">\nPost operation A with suffix 1\n</a>",
                "<a class=\"nhsd-a-link\" href=\"#api-Resource A\">\nEndpoints: Resource A\n</a>",
                "<a class=\"nhsd-a-link\" href=\"#api-IgnoredTagB-path_aPost\">\nPost operation A\n</a>",
                "<a class=\"nhsd-a-link\" href=\"#api-IgnoredTagA-path_aGet\">\nGet operation A\n</a>",
                // spec headings in custom order
                "<h2 id=\"api-endpoints\" class=\"nhsd-t-heading-xl\">Endpoints</h2>\n",
                "<h3 id=\"api-IgnoredTagA-path_bPost\" class=\"nhsd-t-heading-l\">Post operation B</h3>\n",
                "<h3 id=\"api-IgnoredTagB-path_awith_suffix_1Get\" class=\"nhsd-t-heading-l\">Get operation A with suffix 1</h3>\n",
                "<h2 id=\"api-Resource B\" class=\"nhsd-t-heading-xl\">Endpoints: Resource B</h2>\n",
                "<h3 id=\"api-IgnoredTagC-path_bGet\" class=\"nhsd-t-heading-l\">Get operation B</h3>\n",
                "<h2 id=\"api-Resource C\" class=\"nhsd-t-heading-xl\">Endpoints: Resource C</h2>\n",
                "<h3 id=\"api-IgnoredTagB-path_awith_suffix_2Post\" class=\"nhsd-t-heading-l\">POST operation A with suffix 2</h3>\n",
                "<h3 id=\"api-IgnoredTagC-path_awith_suffix_1Post\" class=\"nhsd-t-heading-l\">Post operation A with suffix 1</h3>\n",
                "<h2 id=\"api-Resource A\" class=\"nhsd-t-heading-xl\">Endpoints: Resource A</h2>\n",
                "<h3 id=\"api-IgnoredTagB-path_aPost\" class=\"nhsd-t-heading-l\">Post operation A</h3>\n",
                "<h3 id=\"api-IgnoredTagA-path_aGet\" class=\"nhsd-t-heading-l\">Get operation A</h3>\n"
            )
        );
    }



    @Test
    public void rendersExample_forRequestAndResponseParameters_dependingOnWhetherEnumIsDefined() {
        // given
        final String specificationJson = from("oasV3_examples_vs_enums.json");

        final String headerAllowedValues = "</p><p class=\"nhsd-t-body\">Allowed values: <span class=\"nhsd-a-text-highlight "
            + "nhsd-a-text-highlight--code\">value-a</span>, <span class=\"nhsd-a-text-highlight nhsd-a-text-highlight--code\">value-b</span></p></td>";

        final String headerExample = "</p><p class=\"nhsd-t-body\">Example: <span class=\"nhsd-a-text-highlight nhsd-a-text-highlight--code\">value-a</span></p></td>";

        final String schemaAllowedValues = "</p></div><div>Allowed values: <span class=\"nhsd-a-text-highlight nhsd-a-text-highlight--code\">value-a</span>, <span "
            + "class=\"nhsd-a-text-highlight nhsd-a-text-highlight--code\">value-b</span></div></td>";

        final String schemaExample = "</p></div><div>Example: <span class=\"nhsd-a-text-highlight nhsd-a-text-highlight--code\">value-a</span></div></td>";

        // when
        final String actualSpecHtml = swaggerCodeGenApiSpecHtmlProvider.htmlFrom(specificationJson);

        // then
        // @formatter:off
        assertThat("Request and response parameter example values are rendered only when no enum (Allowed Values) are defined.",
            ignoringNewLinesIn(ignoringWhiteSpacesIn(actualSpecHtml)),
            stringContainsInOrder(
                "Request parameter with example and with enum."         + headerAllowedValues,
                "Request parameter with no example but with enum."      + headerAllowedValues,
                "Request parameter with example and no enum."           + headerExample,

                "Response header with example and with enum."           + headerAllowedValues,
                "Response header with example and no enum."             + headerExample,
                "Response header with no example but with enum."        + headerAllowedValues,

                "Request body schema with example and with enum."       + schemaAllowedValues,
                "Request body schema with example and no enum."         + schemaExample,
                "Request body schema with no example but with enum."    + schemaAllowedValues,

                "Response body schema with example and with enum."      + schemaAllowedValues,
                "Response body schema with example and no enum."        + schemaExample,
                "Response body schema with no example but with enum."   + schemaAllowedValues
            )
        );
        // @formatter:on
    }

    @Test
    public void hidesRequestHeading_whenSectionIsEmpty() {

        // given
        final String specificationJson = from("oasV3_withNoRequest.json");

        // when
        final String actualSpecHtml = swaggerCodeGenApiSpecHtmlProvider.htmlFrom(specificationJson);

        // then
        assertThat(
            "Heading 'Request' is not rendered when request content absent from the specification.",
            ignoringWhiteSpacesIn(actualSpecHtml),
            not(containsString(">Request<"))
        );
    }

    @Test
    public void hidesResponseHeading_whenSectionIsEmpty() {

        // given
        final String specificationJson = from("oasV3_withNoResponse.json");

        // when
        final String actualSpecHtml = swaggerCodeGenApiSpecHtmlProvider.htmlFrom(specificationJson);

        // then
        assertThat(
            "Heading 'Response' is not rendered when response content absent from the specification.",
            ignoringWhiteSpacesIn(actualSpecHtml),
            not(containsString(">Response<"))
        );
    }

    @Test
    public void rendersOperationIds_normalisedToContiguousString() {
        // given
        final String specificationJson = from("oasV3_operationIdsInVariousFormats.json");

        final String expectedSpecHtml = from("oasV3_operationIdsInVariousFormats.html");

        // when
        final String actualSpecHtml = swaggerCodeGenApiSpecHtmlProvider.htmlFrom(specificationJson);

        // then
        assertThat(
            "OperationID rendered as it is on the specification - Camel case and snake case tested.",
            ignoringUuids(ignoringWhiteSpacesIn(actualSpecHtml)),
            is(ignoringUuids(ignoringWhiteSpacesIn(expectedSpecHtml)))
        );
    }

    private String from(final String fileName) {
        return cache.get(fileName, () -> contentOfFileFromClasspath(
            "/test-data/api-specifications/SwaggerCodeGenOpenApiSpecificationJsonToHtmlConverterTest/" + fileName)
        );
    }
}
