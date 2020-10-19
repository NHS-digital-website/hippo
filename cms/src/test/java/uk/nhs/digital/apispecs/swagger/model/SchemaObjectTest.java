package uk.nhs.digital.apispecs.swagger.model;

import static uk.nhs.digital.test.util.AssertionUtils.assertClassHasFieldWithAnnotationWithAttributeValue;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.junit.Test;
import uk.nhs.digital.apispecs.swagger.request.bodyextractor.ToPrettyJsonStringDeserializer;

public class SchemaObjectTest {

    @Test
    public void exampleValueIsDeserialised_usingCustomDeserializer() {

        assertClassHasFieldWithAnnotationWithAttributeValue(
            SchemaObject.class, "example",
            JsonDeserialize.class, "using", ToPrettyJsonStringDeserializer.class
        );
    }
}