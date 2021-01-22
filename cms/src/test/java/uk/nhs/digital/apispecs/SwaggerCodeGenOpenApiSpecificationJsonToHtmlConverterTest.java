package uk.nhs.digital.apispecs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;
import static uk.nhs.digital.test.util.FileUtils.contentOfFileFromClasspath;
import static uk.nhs.digital.test.util.StringTestUtils.ignoringWhiteSpacesIn;

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
import uk.nhs.digital.apispecs.swagger.SwaggerCodeGenOpenApiSpecificationJsonToHtmlConverter;
import uk.nhs.digital.test.util.TestDataCache;

@RunWith(DataProviderRunner.class)
public class SwaggerCodeGenOpenApiSpecificationJsonToHtmlConverterTest {

    private TestDataCache cache = TestDataCache.create();

    private final String specificationId = "123456";

    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Mock private ApiSpecificationDocument apiSpecificationDocument;

    private SwaggerCodeGenOpenApiSpecificationJsonToHtmlConverter swaggerCodeGenApiSpecHtmlProvider;

    @Before
    public void setUp() {
        initMocks(this);

        given(apiSpecificationDocument.getId()).willReturn(specificationId);

        swaggerCodeGenApiSpecHtmlProvider = new SwaggerCodeGenOpenApiSpecificationJsonToHtmlConverter();
    }

    @Test
    public void getHtmlForSpec_rendersCompleteOpenApiJsonAsHtml() {

        // given
        final String specificationJson = from("oasV3_complete.json");

        final String expectedSpecHtml = from("oasV3_complete.html");

        // when
        final String actualSpecHtml = swaggerCodeGenApiSpecHtmlProvider.htmlFrom(specificationJson);

        // then
        assertThat(
            "A complete spec has been rendered as HTML using customised Swagger CodeGen v3",
            ignoringWhiteSpacesIn(actualSpecHtml),
            is(ignoringWhiteSpacesIn(expectedSpecHtml))
        );
    }

    @Test
    public void getHtmlForSpec_throwsException_onCodeGenFailure() {

        // given
        final String invalidSpecificationJson = "invalid specification JSON";

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Failed to generate HTML for OpenAPI specification.");
        expectedException.expectCause(isA(NullPointerException.class));

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
        return new Object[][] {
            // thePropertyIs    outcomeDescription propertyJson                                                            expectation
            {"absent",          "rendered",        "",                                                                     containsString(">Try this API<")},
            {"false",           "rendered",        "\"x-spec-publication\": {\"try-this-api\": {\"disabled\": false}},",   containsString(">Try this API<")},
            {"true",            "not rendered",    "\"x-spec-publication\": {\"try-this-api\": {\"disabled\": true}},",    not(containsString(">Try this API<"))}
        };
        // @formatter:on
    }

    @Test
    public void rendersEndpointsHeading_whenOperationsNotTagged() {

        // given
        final String specificationJson = from("oasV3_operationsNotTagged.json");

        final String expectedSpecHtml = from("oasV3_operationsNotTagged.html");

        // when
        final String actualSpecHtml = swaggerCodeGenApiSpecHtmlProvider.htmlFrom(specificationJson);

        // then
        assertThat(
            "Heading 'Endpoints' has been generated for operations with no tags - in the side nav and in the content area.",
            ignoringWhiteSpacesIn(actualSpecHtml),
            is(ignoringWhiteSpacesIn(expectedSpecHtml))
        );
    }

    @Test
    public void rendersEndpointsHeadings_whenOperationsTagged() {

        // given
        final String specificationJson = from("oasV3_operationsTagged.json");

        final String expectedSpecHtml = from("oasV3_operationsTagged.html");

        // when
        final String actualSpecHtml = swaggerCodeGenApiSpecHtmlProvider.htmlFrom(specificationJson);

        // then
        assertThat(
            "Headings 'Endpoint:' have been generated for each operation with a tag - in the side nav and in the content area.",
            ignoringWhiteSpacesIn(actualSpecHtml),
            is(ignoringWhiteSpacesIn(expectedSpecHtml))
        );
    }

    @Test
    public void hidesRequestHeading_whenSectionIsEmpty() {

        // given
        final String specificationJson = from("oasV3_withNoRequest.json");

        final String expectedSpecHtml = from("oasV3_withNoRequest.html");

        // when
        final String actualSpecHtml = swaggerCodeGenApiSpecHtmlProvider.htmlFrom(specificationJson);

        // then
        assertThat(
            "Heading 'Request' is not rendered when request content absent from the specification.",
            ignoringWhiteSpacesIn(actualSpecHtml),
            is(ignoringWhiteSpacesIn(expectedSpecHtml))
        );
    }

    @Test
    public void hidesResponseHeading_whenSectionIsEmpty() {

        // given
        final String specificationJson = from("oasV3_withNoResponse.json");

        final String expectedSpecHtml = from("oasV3_withNoResponse.html");

        // when
        final String actualSpecHtml = swaggerCodeGenApiSpecHtmlProvider.htmlFrom(specificationJson);

        // then
        assertThat(
            "Heading 'Response' is not rendered when response content absent from the specification.",
            ignoringWhiteSpacesIn(actualSpecHtml),
            is(ignoringWhiteSpacesIn(expectedSpecHtml))
        );
    }

    private String from(final String fileName) {
        return cache.get(fileName, () -> contentOfFileFromClasspath(
            "/test-data/api-specifications/SwaggerCodeGenOpenApiSpecificationJsonToHtmlConverterTest/" + fileName)
        );
    }
}
