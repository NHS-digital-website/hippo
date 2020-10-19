package uk.nhs.digital.apispecs.swagger.request.examplerenderer;

import static uk.nhs.digital.test.util.AssertionUtils.assertClassHasFieldWithAnnotationWithAttributeValue;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.junit.Test;
import uk.nhs.digital.apispecs.swagger.request.bodyextractor.ToPrettyJsonStringDeserializer;

public class CodegenParamSchemaTest {

    @Test
    public void exampleValueIsDeserialised_usingCustomDeserializer() {

        assertClassHasFieldWithAnnotationWithAttributeValue(
            CodegenParamSchema.class, "example",
            JsonDeserialize.class, "using", ToPrettyJsonStringDeserializer.class
        );
    }
}