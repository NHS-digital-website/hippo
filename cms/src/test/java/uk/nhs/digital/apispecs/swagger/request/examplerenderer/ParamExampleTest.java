package uk.nhs.digital.apispecs.swagger.request.examplerenderer;

import static uk.nhs.digital.test.util.AssertionUtils.assertClassHasFieldWithAnnotationWithAttributeValue;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.junit.Test;
import uk.nhs.digital.apispecs.swagger.request.bodyextractor.ToPrettyJsonStringDeserializer;

public class ParamExampleTest {

    @Test
    public void exampleValueIsDeserialised_usingCustomDeserializer() {

        assertClassHasFieldWithAnnotationWithAttributeValue(
            ParamExample.class, "value",
            JsonDeserialize.class, "using", ToPrettyJsonStringDeserializer.class
        );
    }
}