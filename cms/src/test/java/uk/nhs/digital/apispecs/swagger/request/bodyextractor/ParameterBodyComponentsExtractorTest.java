package uk.nhs.digital.apispecs.swagger.request.bodyextractor;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static uk.nhs.digital.test.util.FileUtils.fileContentFromClasspath;

import com.fasterxml.jackson.core.JsonParseException;
import io.swagger.codegen.v3.CodegenParameter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import uk.nhs.digital.test.util.ReflectionTestUtils;

import java.util.Optional;

public class ParameterBodyComponentsExtractorTest {

    private static final String TEST_DATA_FILES_PATH =
        "/test-data/api-specifications/" + ParameterBodyComponentsExtractorTest.class.getSimpleName();

    @Rule public ExpectedException expectedException = ExpectedException.none();

    final ParameterBodyComponentsExtractor parameterBodyComponentsExtractor = new ParameterBodyComponentsExtractor();

    @Test
    public void extractsBody_fromCodegenParameterJsonSchema() {

        // given
        final String parameterJsonDefinition = from("requestBodyPartOfParameterDefinition.json");
        final CodegenParameter codegenParameter = bodyCodegenParameterWith(parameterJsonDefinition);

        final RequestBody expectedCompleteRequestBody = completeRequestBody();

        // when
        final Optional<RequestBody> actualRequestBody = parameterBodyComponentsExtractor.extractBody(codegenParameter);

        // then
        assertThat("Returns a value.",
            actualRequestBody.isPresent(),
            is(true)
        );

        assertThat("Returns RequestBody model matching the structure defined in JSON.",
            actualRequestBody.get(),
            is(expectedCompleteRequestBody)
        );
    }

    @Test
    public void doesNothing_whenCodegenParameterIsNotBodyParam() {

        // given
        final CodegenParameter notBodyCodegenParameter = new CodegenParameter();

        // when
        final Optional<RequestBody> actualRequestBody = parameterBodyComponentsExtractor.extractBody(notBodyCodegenParameter);

        // then
        assertThat("Returns no value.",
            actualRequestBody.isPresent(),
            is(false)
        );
    }

    @Test
    public void throwsException_withUnderlyingCause_onFailureToProcessCodegenParameter() {

        // given
        final String invalidParameterJsonDefinition = "{ invalid JSON }";
        final CodegenParameter bodyCodegenParameterWithInvalidJsonSchema = bodyCodegenParameterWith(invalidParameterJsonDefinition);

        expectedException.expectMessage("Failed to extract request body model of parameter param-name(data-type)");
        expectedException.expectCause(instanceOf(JsonParseException.class));

        // when
        parameterBodyComponentsExtractor.extractBody(bodyCodegenParameterWithInvalidJsonSchema);

        // then
        // expectations set up in 'given' are satisfied
    }

    private RequestBody completeRequestBody() {
        final RequestBodyMediaTypeObjects requestBodyMediaTypeObjects = new RequestBodyMediaTypeObjects();
        requestBodyMediaTypeObjects.put("application/json", new RequestBodyMediaTypeObject());
        requestBodyMediaTypeObjects.put("application/xml", new RequestBodyMediaTypeObject());

        final RequestBody requestBody = new RequestBody();
        ReflectionTestUtils.setField(requestBody, "content", requestBodyMediaTypeObjects);
        return requestBody;
    }

    private CodegenParameter bodyCodegenParameterWith(final String parameterJsonDefinition) {

        final CodegenParameter codegenParameter = new CodegenParameter();
        codegenParameter.jsonSchema = parameterJsonDefinition;
        codegenParameter.vendorExtensions.put("x-is-body-param", true);

        // only used here for codegenParameter.toString():
        codegenParameter.baseName = "param-name";
        codegenParameter.dataType = "data-type";

        return codegenParameter;
    }

    private static String from(final String testFileName) {
        return fileContentFromClasspath(TEST_DATA_FILES_PATH + "/" + testFileName);
    }
}