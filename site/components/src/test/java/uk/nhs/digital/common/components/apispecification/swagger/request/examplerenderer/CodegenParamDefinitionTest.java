package uk.nhs.digital.common.components.apispecification.swagger.request.examplerenderer;

import static uk.nhs.digital.test.util.AssertionUtils.assertClassHasFieldWithAnnotationWithAttributeValue;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.junit.Test;
import uk.nhs.digital.common.components.apispecification.swagger.request.bodyextractor.ToPrettyJsonStringDeserializer;

public class CodegenParamDefinitionTest {

    @Test
    public void exampleValueIsDeserialised_usingCustomDeserializer() {

        assertClassHasFieldWithAnnotationWithAttributeValue(
            CodegenParamDefinition.class, "example",
            JsonDeserialize.class, "using", ToPrettyJsonStringDeserializer.class
        );
    }
}