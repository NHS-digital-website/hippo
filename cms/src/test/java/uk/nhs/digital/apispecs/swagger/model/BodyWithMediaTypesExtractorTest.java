package uk.nhs.digital.apispecs.swagger.model;

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

public class BodyWithMediaTypesExtractorTest {

    private static final String TEST_DATA_FILES_PATH =
        "/test-data/api-specifications/" + BodyWithMediaTypesExtractorTest.class.getSimpleName();

    @Rule public ExpectedException expectedException = ExpectedException.none();

    final BodyWithMediaTypesExtractor bodyWithMediaTypesExtractor = new BodyWithMediaTypesExtractor();

    @Test
    public void extractsBody_fromCodegenParameterJsonSchema() {

        // given
        final String parameterJsonDefinition = from("requestBodyPartOfParameterDefinition.json");
        final CodegenParameter codegenParameter = bodyCodegenParameterWith(parameterJsonDefinition);

        final BodyWithMediaTypeObjects expectedCompleteBodyWithMediaTypeObjects = completeRequestBody();

        // when
        final Optional<BodyWithMediaTypeObjects> actualRequestBody = bodyWithMediaTypesExtractor.extractBody(codegenParameter);

        // then
        assertThat("Returns a value.",
            actualRequestBody.isPresent(),
            is(true)
        );

        assertThat("Returns RequestBody model matching the structure defined in JSON.",
            actualRequestBody.get(),
            is(expectedCompleteBodyWithMediaTypeObjects)
        );
    }

    @Test
    public void doesNothing_whenCodegenParameterIsNotBodyParam() {

        // given
        final CodegenParameter notBodyCodegenParameter = new CodegenParameter();

        // when
        final Optional<BodyWithMediaTypeObjects> actualRequestBody = bodyWithMediaTypesExtractor.extractBody(notBodyCodegenParameter);

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

        expectedException.expectMessage("Failed to extract body model of request parameter param-name(data-type)");
        expectedException.expectCause(instanceOf(JsonParseException.class));

        // when
        bodyWithMediaTypesExtractor.extractBody(bodyCodegenParameterWithInvalidJsonSchema);

        // then
        // expectations set up in 'given' are satisfied
    }

    private BodyWithMediaTypeObjects completeRequestBody() {
        final MediaTypeObjects mediaTypeObjects = new MediaTypeObjects();
        mediaTypeObjects.put("application/json", new MediaTypeObject());
        mediaTypeObjects.put("application/xml", new MediaTypeObject());

        final BodyWithMediaTypeObjects bodyWithMediaTypeObjects = new BodyWithMediaTypeObjects();
        ReflectionTestUtils.setField(bodyWithMediaTypeObjects, "content", mediaTypeObjects);
        return bodyWithMediaTypeObjects;
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
