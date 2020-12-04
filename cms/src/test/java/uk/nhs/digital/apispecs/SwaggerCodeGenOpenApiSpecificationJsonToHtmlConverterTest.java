package uk.nhs.digital.apispecs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;
import static uk.nhs.digital.test.util.FileUtils.contentOfFileFromClasspath;
import static uk.nhs.digital.test.util.StringTestUtils.ignoringBlankLinesIn;

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

@RunWith(DataProviderRunner.class)
public class SwaggerCodeGenOpenApiSpecificationJsonToHtmlConverterTest {

    private static final String TEST_DATA_FILES_PATH =
        "/test-data/api-specifications/SwaggerCodeGenOpenApiSpecificationJsonToHtmlConverterTest";

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
    public void getHtmlForSpec_convertsOpenApiJsonToHtml() {

        // given
        final String specificationJson = from("oasV3_complete.json");

        final String expectedSpecHtml = from("oasV3_complete.html");

        // when
        final String actualSpecHtml = swaggerCodeGenApiSpecHtmlProvider.htmlFrom(specificationJson);

        // then
        assertThat(
            "Specification HTML has been generated using customised Swagger CodeGen v3",
            ignoringBlankLinesIn(actualSpecHtml),
            is(ignoringBlankLinesIn(expectedSpecHtml))
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
        final String specificationJson = from("oasV3_withCustomExtension.json.template").replaceAll("X-PROPERTY-PLACEHOLDER", propertyJson);

        // when
        final String actualSpecHtml = swaggerCodeGenApiSpecHtmlProvider.htmlFrom(specificationJson);

        // then
        assertThat(
            "Try this API button is " + outcomeDescription + " when property x-spec-publication.try-this-api.disabled is " + thePropertyIs,
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
    public void hideRequestHeaderWhenSectionIsEmpty() {

        // given
        final String specificationJson = from("oasV3_withNoRequest.json");

        final String expectedSpecHtml = from("oasV3_withNoRequest.html");

        // when
        final String actualSpecHtml = swaggerCodeGenApiSpecHtmlProvider.htmlFrom(specificationJson);

        // then
        assertThat(
            "Specification HTML has been generated using customised Swagger CodeGen v3",
            ignoringBlankLinesIn(actualSpecHtml),
            is(ignoringBlankLinesIn(expectedSpecHtml))
        );
    }

    @Test
    public void hideResponseHeaderWhenSectionIsEmpty() {

        // given
        final String specificationJson = from("oasV3_withNoResponse.json");

        final String expectedSpecHtml = from("oasV3_withNoResponse.html");

        // when
        final String actualSpecHtml = swaggerCodeGenApiSpecHtmlProvider.htmlFrom(specificationJson);

        // then
        assertThat(
            "Specification HTML has been generated using customised Swagger CodeGen v3",
            ignoringBlankLinesIn(actualSpecHtml),
            is(ignoringBlankLinesIn(expectedSpecHtml))
        );
    }

    private String from(final String fileName) {
        return contentOfFileFromClasspath(TEST_DATA_FILES_PATH + "/" + fileName);
    }
}
