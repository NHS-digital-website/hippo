package uk.nhs.digital.common.components.apispecification.swagger.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static uk.nhs.digital.test.util.ReflectionTestUtils.setField;
import static uk.nhs.digital.test.util.TestFileUtils.contentOfFileFromClasspath;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Optional;

public class BodyWithMediaTypesExtractorTest {

    final BodyWithMediaTypesExtractor bodyWithMediaTypesExtractor = new BodyWithMediaTypesExtractor();

    @Test
    public void extractsBody_fromJsonSchema() {

        // given
        final String parameterJsonDefinition = from("bodyObjectWithMediaTypeObjectsDefinition.json");

        final BodyWithMediaTypeObjects expectedCompleteBodyWithMediaTypeObjects = completeBody();

        // when
        final Optional<BodyWithMediaTypeObjects> actualBodyObject =
            bodyWithMediaTypesExtractor.bodyObjectFromJsonSchema(parameterJsonDefinition, "<element type>");

        // then
        assertTrue("Returns a value.", actualBodyObject.isPresent());

        assertEquals("Returns RequestBody model matching the structure defined in JSON.", actualBodyObject.get(), expectedCompleteBodyWithMediaTypeObjects);
    }

    @Test(expected = RuntimeException.class)
    public void throwsException_withUnderlyingCause_onFailureToProcessCodegenParameter() {

        // given
        final String invalidParameterJsonDefinition = "{ invalid JSON }";

        // when
        bodyWithMediaTypesExtractor.bodyObjectFromJsonSchema(invalidParameterJsonDefinition, "<element type>");

        // then
        // expectations set up in 'given' are satisfied
    }

    private BodyWithMediaTypeObjects completeBody() {

        final ExampleObject exampleObjectA = new ExampleObject();
        setField(exampleObjectA, "summary", "Example A summary");
        setField(exampleObjectA, "description", "Example A description");
        setField(exampleObjectA, "value", "Example A value");

        final ExampleObject exampleObjectB = new ExampleObject();
        setField(exampleObjectB, "summary", "Example B summary");
        setField(exampleObjectB, "description", "Example B description");
        setField(exampleObjectB, "value", "Example B value");

        final SchemaObject schemaYml = new SchemaObject();
        setField(schemaYml, "example", "Example on schema YML");

        final MediaTypeObject mediaTypeObjectYml = new MediaTypeObject();
        setField(mediaTypeObjectYml, "schema", schemaYml);
        setField(mediaTypeObjectYml, "example", "Example on Media Type Object YML");
        setField(mediaTypeObjectYml, "examples", ImmutableMap.builder()
            .put("example-a", exampleObjectA)
            .put("example-b", exampleObjectB)
            .build()
        );

        final ExampleObject exampleObjectC = new ExampleObject();
        setField(exampleObjectC, "summary", "Example C summary");
        setField(exampleObjectC, "description", "Example C description");
        setField(exampleObjectC, "value", "Example C value");

        final ExampleObject exampleObjectD = new ExampleObject();
        setField(exampleObjectD, "summary", "Example D summary");
        setField(exampleObjectD, "description", "Example D description");
        setField(exampleObjectD, "value", "Example D value");

        final SchemaObject schemaXml = new SchemaObject();
        setField(schemaXml, "example", "Example on schema XML");

        final MediaTypeObject mediaTypeObjectXml = new MediaTypeObject();
        setField(mediaTypeObjectXml, "schema", schemaXml);
        setField(mediaTypeObjectXml, "example", "Example on Media Type Object XML");
        setField(mediaTypeObjectXml, "examples", ImmutableMap.builder()
            .put("example-c", exampleObjectC)
            .put("example-d", exampleObjectD)
            .build()
        );

        final MediaTypeObjects mediaTypeObjects = new MediaTypeObjects();
        mediaTypeObjects.put("application/yml", mediaTypeObjectYml);
        mediaTypeObjects.put("application/xml", mediaTypeObjectXml);

        final BodyWithMediaTypeObjects bodyWithMediaTypeObjects = new BodyWithMediaTypeObjects();
        setField(bodyWithMediaTypeObjects, "content", mediaTypeObjects);

        return bodyWithMediaTypeObjects;
    }

    private String from(final String testFileName) {
        return contentOfFileFromClasspath(
            "/test-data/api-specifications/BodyWithMediaTypesExtractorTest/" + testFileName
        );
    }
}
