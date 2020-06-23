package uk.nhs.digital.apispecs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.initMocks;
import static uk.nhs.digital.test.util.FileUtils.fileContentFromClasspath;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import uk.nhs.digital.apispecs.model.ApiSpecificationDocument;
import uk.nhs.digital.apispecs.swagger.SwaggerCodeGenApiSpecificationHtmlProvider;

public class SwaggerCodeGenApiSpecificationHtmlProviderTest {

    private static final String TEST_DATA_FILES_PATH =
        "/test-data/api-specifications/SwaggerCodeGenApiSpecHtmlProviderTest";

    private final String specificationId = "123456";

    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Mock private OpenApiSpecificationRepository apigeeService;

    @Mock private ApiSpecificationDocument apiSpecificationDocument;

    private SwaggerCodeGenApiSpecificationHtmlProvider swaggerCodeGenApiSpecHtmlProvider;

    @Before
    public void setUp() {
        initMocks(this);

        given(apiSpecificationDocument.getId()).willReturn(specificationId);

        swaggerCodeGenApiSpecHtmlProvider = new SwaggerCodeGenApiSpecificationHtmlProvider(apigeeService);
    }

    @Test
    public void getHtmlForSpec_convertsProvidedOpenApiJsonToHtml() {

        // given
        final String apigeeApiSpecificationJson = apigeeApiSpecificationJson();
        given(apigeeService.apiSpecificationJsonForSpecId(any(String.class))).willReturn(apigeeApiSpecificationJson);

        // when
        final String actualSpecHtml = swaggerCodeGenApiSpecHtmlProvider.getHtmlForSpec(apiSpecificationDocument);

        // then
        final String expectedSpecHtml = customisedSwaggerCodeGenGeneratedSpecificationHtml();

        then(apigeeService).should().apiSpecificationJsonForSpecId(specificationId);

        assertThat(
            "Specification HTML has been generated using customised Swagger CodeGen v3",
            actualSpecHtml,
            is(expectedSpecHtml)
        );
    }

    @Test
    public void getHtmlForSpec_throwsException_onApigeeFailure() {

        // given
        final RuntimeException apigeeServiceException = new RuntimeException();
        given(apigeeService.apiSpecificationJsonForSpecId(any(String.class))).willThrow(apigeeServiceException);

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(startsWith("Failed to generate HTML for specification "));
        expectedException.expectCause(sameInstance(apigeeServiceException));

        // when
        swaggerCodeGenApiSpecHtmlProvider.getHtmlForSpec(apiSpecificationDocument);

        // then
        // expectations set in 'given' are satisfied
    }

    @Test
    public void getHtmlForSpec_throwsException_onCodeGenFailure() {

        // given
        final String invalidSpecificationJson = "invalid specification JSON";
        given(apigeeService.apiSpecificationJsonForSpecId(any(String.class))).willReturn(invalidSpecificationJson);

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(startsWith("Failed to generate HTML for specification "));
        expectedException.expectCause(isA(NullPointerException.class));

        // when
        swaggerCodeGenApiSpecHtmlProvider.getHtmlForSpec(apiSpecificationDocument);

        // then
        // expectations set in 'given' are satisfied
    }

    private String apigeeApiSpecificationJson() {
        return fileContentFromClasspath(TEST_DATA_FILES_PATH + "/openapi-v3-specification.json");
    }

    private String customisedSwaggerCodeGenGeneratedSpecificationHtml() {
        return fileContentFromClasspath(TEST_DATA_FILES_PATH + "/customised-codegen-v3-generated-spec.html");
    }
}