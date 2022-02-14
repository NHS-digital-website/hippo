package uk.nhs.digital.common.components.apispecification.swagger.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static uk.nhs.digital.test.util.TestFileUtils.contentOfFileFromClasspath;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.nhs.digital.common.components.apispecification.swagger.request.bodyextractor.ToPrettyJsonStringDeserializer;

@RunWith(DataProviderRunner.class)
public class ToPrettyJsonStringDeserializerTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
        .configure(MapperFeature.USE_ANNOTATIONS, true);

    @Test
    @UseDataProvider("inputsAndExpected")
    public void prettyPrints(final String testCase, final String testFileName) throws JsonProcessingException {

        // given
        final String input = from(testFileName + ".json");
        final String expectedText = from(testFileName + ".txt");

        // when
        // instantiates and invokes ToPrettyJsonStringDeserializer
        final String actualParsedText = objectMapper.readValue(input, TestDto.class).getExample();

        // then
        assertThat(
            "Deserialises and pretty-prints " + testCase + ".",
            actualParsedText,
            is(expectedText)
        );
    }

    @DataProvider
    public static Object[][] inputsAndExpected() {

        // @formatter:off
        return new String[][]{

            // testCase                                                             testFileName (+ .json, .txt)

            // JSON defined as object
            {"JSON defined as an object",                                           "jsonObject"},
            {"JSON defined as an object with property with JSON defined as text",   "jsonObject_withJsonText"},
            {"JSON defined as an object with property with XML defined as text",    "jsonObject_withXmlText"},

            // JSON defined as text
            {"JSON defined as a text", "jsonText"},
            {"JSON defined as a text with property with JSON defined as text",      "jsonText_withJsonText"},
            {"JSON defined as a text with property with XML defined as text",       "jsonText_withXmlText"},

            // XML
            {"XML defined as a text",                                               "xmlText"},

            // Plain text
            {"plain text",                                                          "plainText"}
        };
        // @formatter:on
    }

    private static String from(final String testFileName) {
        return contentOfFileFromClasspath(
            "/test-data/api-specifications/ToPrettyJsonStringDeserializerTest/" + testFileName
        );
    }

    public static class TestDto {

        @JsonDeserialize(using = ToPrettyJsonStringDeserializer.class)
        private String example;

        private String getExample() {
            return example;
        }
    }
}
