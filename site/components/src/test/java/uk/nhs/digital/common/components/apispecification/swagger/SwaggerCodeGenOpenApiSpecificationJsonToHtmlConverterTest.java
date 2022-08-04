package uk.nhs.digital.common.components.apispecification.swagger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;
import static org.mockito.MockitoAnnotations.openMocks;
import static uk.nhs.digital.test.util.StringTestUtils.*;
import static uk.nhs.digital.test.util.TestFileUtils.contentOfFileFromClasspath;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.nhs.digital.test.util.TestDataCache;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;


@RunWith(DataProviderRunner.class)
public class SwaggerCodeGenOpenApiSpecificationJsonToHtmlConverterTest {

    private final TestDataCache cache = TestDataCache.create();

    private SwaggerCodeGenOpenApiSpecificationJsonToHtmlConverter swaggerCodeGenApiSpecHtmlProvider;

    @Before
    public void setUp() throws MalformedURLException {
        openMocks(this);

        final URL templatesDir = Paths.get("../webapp/src/main/resources/api-specification/codegen-templates").toAbsolutePath().normalize().toUri().toURL();

        swaggerCodeGenApiSpecHtmlProvider = new SwaggerCodeGenOpenApiSpecificationJsonToHtmlConverter(templatesDir);
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

        // when
        final String actualSpecHtml = swaggerCodeGenApiSpecHtmlProvider.htmlFrom(specificationJson);

        // then
        // @formatter:off
        assertThat(
            "A spec has been rendered with all supported request parameters.",
            actualSpecHtml,
            stringContainsInOrder(
                ">get<", ">/op-with-request-params-all-kinds/{path-param-a}/{path-param-b}<",
                    ">Request</h4>",
                        ">Path parameters</h5>", ">Name<", ">Description<",
                            ">path-param-a<", ">Required<",
                            ">path-param-b<", ">Required<",
                        ">Query parameters</h5>", ">Name<", ">Description<",
                            ">query-param-a<",
                            ">query-param-b<",
                        ">Headers</h5>", ">Name<", ">Description<",
                            ">header-param-a<",
                            ">header-param-b<",

                ">get<", ">/op-with-params-in-supported-data-formats<",
                    ">Request</h4>",
                        ">Headers</h5>", ">Name<", ">Description<",
                            ">Param-String<",
                                ">String<",
                                "Default value:", ">[string-a]&amp;&lt;string-b&gt;<",
                                "Allowed values:", ">[string-a]&amp;&lt;string-b&gt;<",">[string-c]&amp;&lt;string-d&gt;<", ">null<", "><", ">&lt; &gt;<", ">&amp;<",
                            ">Param-String-Date<",
                                ">date<", ">(date)<",
                                "Default value:", ">1984-01-01<",
                                "Example:", ">2020-02-29<",
                            ">Param-String-Date-Time<",
                                ">Date<", ">(date-time)<",
                                "Example:", ">2020-02-29T23:59:59Z<",
                            ">Param-Integer-32bit-int<",
                                ">Integer<", ">(int32)<",
                                "Default value:", ">-10<",
                                "Example:", ">-11<",
                            ">Param-Integer-64bit-int<",
                                ">Long<", ">(int64)<",
                                "Default value:", ">-14<",
                                "Example:", ">-12<",
                            ">Param-Number-Float<",
                                ">Float<", ">(float)<",
                                "Default value:", ">-1.41<",
                                "Example:", ">-1.42<",
                            ">Param-Number-Double<",
                                ">Double<", ">(double)<",
                                "Default value:", ">-1.4<",
                                "Allowed values:", ">-1.4<", ">-1.43<",
                            ">Param-Boolean<",
                                ">Boolean<",
                                "Default value:", ">false<",
                                "Example:", ">true<",
                            ">Param-Array-Primitive<",
                                ">List<",
                                "Example:", ">[-1.42, 0, &quot;string-value-b&quot;]<",
                            ">Param-Array-Of-JSON<",
                                ">List<",
                                "Example:", ">[{\n  &quot;c&quot;: &quot;cc&quot;\n}, {\n  &quot;d&quot;: &quot;ee&quot;\n}]<",
                            ">Param-Object<",
                                ">Object<",
                                "Example:", ">{\n  &quot;html-sensitive-chars&quot;: &quot;&lt; &gt; &amp;&quot; \n}<",
                    ">Response</h4>",
                        ">HTTP status: 200</h5>",
                            ">Headers</h6>", ">Name<", ">Description<",
                                ">Param-String<",
                                    ">String<",
                                    "Default value:", ">[string-a]&amp;&lt;string-b&gt;<",
                                    "Allowed values:", ">[string-a]&amp;&lt;string-b&gt;<", ">[string-c]&amp;&lt;string-d&gt;<", ">null<", "><", ">&lt; &gt;<", ">&amp;<",
                                ">Param-String-Date<",
                                    ">date<", ">(date)<",
                                    "Default value:", ">1984-01-01<",
                                    "Example:", ">2020-02-29<",
                                ">Param-String-Date-Time<",
                                    ">Date<", ">(date-time)<",
                                    "Example:", ">2020-02-29T23:59:59Z<",
                                ">Param-Integer-32bit-int<",
                                    ">Integer<", ">(int32)<",
                                    "Default value:", ">-10<",
                                    "Example:", ">-11<",
                                ">Param-Integer-64bit-int<",
                                    ">Long<", ">(int64)<",
                                    "Default value:", ">-14<",
                                    "Example:", ">-12<",
                                ">Param-Number-Float<",
                                    ">Float<", ">(float)<",
                                    "Default value:", ">-1.41<",
                                    "Example:", ">-1.42<",
                                ">Param-Number-Double<",
                                    ">Double<", ">(double)<",
                                    "Default value:", ">-1.4<",
                                    "Allowed values:", ">-1.4<", ">-1.43<",
                                ">Param-Boolean<",
                                    ">Boolean<",
                                    "Default value:", ">false<",
                                    "Example:", ">true<",
                                ">Param-Array-Primitive<",
                                    ">List<",
                                    "Example:", ">[-1.42, 0, &quot;string-value-b&quot;]<",
                                ">Param-Array-Of-JSON<",
                                    ">List<",
                                    "Example:", ">[{\n  &quot;c&quot;: &quot;cc&quot;\n}, {\n  &quot;d&quot;: &quot;ee&quot;\n}]<",
                                ">Param-Object<",
                                    ">Object<",
                                    "Example:", ">{\n  &quot;html-sensitive-chars&quot;: &quot;&lt; &gt; &amp;&quot; \n}<",

                ">get<", ">/op-with-params-with-all-min-max-exclusives<",
                    ">Request</h4>",
                        ">Headers</h5>", ">Name<", ">Description<",
                            ">param-with-min-inclusive-max-exclusive<",
                                ">String<",
                                "Maximum:", ">10.1<", "(exclusive)",
                                "Minimum:", ">-10.11<", "(inclusive)",
                            ">param-with-min-exclusive-max-inclusive<",
                                ">String<",
                                "Maximum:", ">10.11<", "(inclusive)",
                                "Minimum:", ">-10.12<", "(exclusive)",
                            ">param-with-min-max-inclusive-by-default<",
                                ">String<",
                                "Maximum:", ">10.14<", "(inclusive)",
                                "Minimum:", ">-10.13<", "(inclusive)",

                ">get<", ">/op-with-request-param-with-all-fields<",
                    ">Request</h4>",
                        ">Headers</h5>", ">Name<", ">Description<",
                            ">param-with-all-fields<",
                                ">String<",
                                "Default value:", ">[string-a]&amp;&lt;string-b&gt;<",
                                "Pattern:",
                                    ">/^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])T\\d{2}:\\d{2}:\\d{2}\\+\\d{2}:\\d{2})$"
                                        + "|^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])T\\d{2}:\\d{2}:\\d{2})$|^([12]\\d{3}-(0[1-9]"
                                        + "|1[0-2])-(0[1-9]|[12]\\d|3[01])T\\d{2}:\\d{2}:\\d{2}\\+\\d{2}:\\d{2})$|^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$/<",
                                "Maximum:", ">10.1<", "(exclusive)",
                                "Minimum:", ">-10.11<", "(exclusive)",
                                "Max length:", "10",
                                "Min length:", "0",
                                "Allowed values:", ">[string-a]&amp;&lt;string-b&gt;<", ">null<", ">10.2<", "><", ">&lt; &gt;<", ">&amp;<",
                                ">Required<",
                    ">Response</h4>",
                        ">HTTP status: 200</h5>",
                            ">Headers</h6>", ">Name<", ">Description<",
                                ">param-with-all-fields<",
                                    ">String<",
                                    "Default value:", ">[string-a]&amp;&lt;string-b&gt;<",
                                    "Pattern:", ">/^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])T\\d{2}:\\d{2}:\\d{2}\\+\\d{2}:\\d{2})$"
                                        + "|^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])T\\d{2}:\\d{2}:\\d{2})$|^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]"
                                        + "|[12]\\d|3[01])T\\d{2}:\\d{2}:\\d{2}\\+\\d{2}:\\d{2})$|^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$/<",
                                    "Max length:", "10",
                                    "Min length:", "0",
                                    "Allowed values:", ">[string-a]&amp;&lt;string-b&gt;<", ">null<", ">10.2<", "><", ">&lt; &gt;<", ">&amp;<",

                ">get<", ">/op-with-response-headers<",
                    ">Response</h4>",
                        ">HTTP status: 200</h5>",
                            ">Headers</h6>", ">Name<", ">Description<",
                                ">response-header-a<",
                                    ">String<",
                                ">response-header-b<",
                                    ">String<"
            )
        );
        // @formatter:on
    }

    @Test(expected = RuntimeException.class)
    public void throwsException_onCodeGenFailure() {

        // given
        final String invalidSpecificationJson = "invalid specification JSON";

        // when
        swaggerCodeGenApiSpecHtmlProvider.htmlFrom(invalidSpecificationJson);
        fail("Failed to generate HTML for OpenAPI specification.");

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

        // when
        final String actualSpecHtml = swaggerCodeGenApiSpecHtmlProvider.htmlFrom(specificationJson);

        // then
        // @formatter:off
        assertThat(
            "OperationID rendered as it is on the specification - Camel case and snake case tested.",
            actualSpecHtml,
            stringContainsInOrder(

                // side nav
                "\"#api--snake_case\"",                 "Get snake_case operation",
                "\"#api-Default-12345\"",               "Put numbers operation",
                "\"#api-Default-ALLCAPS\"",             "Post ALLCAPS operation",
                "\"#api-Default-kebab-case\"",          "Post kebab-case operation",
                "\"#api-Default-mixed123withNubmers\"", "Delete mixed123withNubmers operation",
                "\"#api-Default-with_space\"",          "Post with space operation",
                "api-Patients-camelCase",               "Get camelCase operation",

                // content
                "\"api--snake_case\"",                 ">Get snake_case operation<",             "'//snake_case'",
                "\"api-Default-12345\"",               ">Put numbers operation<",                "'/default/12345'",
                "\"api-Default-ALLCAPS\"",             ">Post ALLCAPS operation<",               "'/default/ALLCAPS'",
                "\"api-Default-kebab-case\"",          ">Post kebab-case operation<",            "'/default/kebab-case'",
                "\"api-Default-mixed123withNubmers\"", ">Delete mixed123withNubmers operation<", "'/default/mixed123withNubmers'",
                "\"api-Default-with_space\"",          ">Post with space operation<",            "'/default/with_space'",
                "\"api-Patients-camelCase\"",          ">Get camelCase operation<",              "'/Patients/camelCase'"
            )
        );
        // @formatter:on
    }

    private String from(final String fileName) {
        return cache.get(fileName, () -> contentOfFileFromClasspath(
            "/test-data/api-specifications/SwaggerCodeGenOpenApiSpecificationJsonToHtmlConverterTest/" + fileName)
        );
    }
}
