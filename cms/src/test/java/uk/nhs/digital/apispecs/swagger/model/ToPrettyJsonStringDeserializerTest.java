package uk.nhs.digital.apispecs.swagger.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static uk.nhs.digital.test.util.TestFileUtils.contentOfFileFromClasspath;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.junit.Test;
import uk.nhs.digital.apispecs.swagger.request.bodyextractor.ToPrettyJsonStringDeserializer;

import java.io.IOException;

public class ToPrettyJsonStringDeserializerTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
        .configure(MapperFeature.USE_ANNOTATIONS, true);

    private final ToPrettyJsonStringDeserializer deserializer = new ToPrettyJsonStringDeserializer();

    @Test
    public void deserialisesJsonObjectToPrettyPrintedJsonString() throws IOException {

        // given
        final String jsonObject = "{\"some\":\"json\"}";
        final String jsonPrettyPrinted = "{\n  \"some\" : \"json\"\n}";

        // when
        final String actualDeserializedContent = deserializer.deserialize(
            objectMapper.getFactory().createParser(jsonObject),
            null // ignored
        );

        // then
        assertThat("Deserialises JSON content into pretty-printed JSON string.",
            actualDeserializedContent,
            is(jsonPrettyPrinted)
        );
    }

    @Test
    public void deserialisesPlainTextToPlainString_trimmingLeadingAndTrailingQuotes() throws IOException {

        // given
        final String jsonPropertyStringValue = from("plainText.json");

        final String plainTextValueWithNoSurroundingQuotes = "plain text";

        // when
        final String actualValue = objectMapper.readValue(jsonPropertyStringValue, TestDto.class).getProperty();

        // then
        assertThat(
            "Deserialises string JSON value into plain text string, trimming leading and trailing quote characters.",
            actualValue,
            is(plainTextValueWithNoSurroundingQuotes)
        );
    }

    @Test
    public void deserialisesPlainTextToPlainString_unescapingDoubleQuotesWithinTheValue() throws IOException {

        // given
        final String jsonPropertyStringValue = from("plainTextWithQuotes.json");

        final String plainTextValueWithQuotes = "plain text with \"quotes\"";

        // when
        final String actualValue = objectMapper.readValue(jsonPropertyStringValue, TestDto.class).getProperty();

        // then
        assertThat(
            "Deserialises string JSON value into plain text preserving double-quote characters within it.",
            actualValue,
            is(plainTextValueWithQuotes)
        );
    }

    @Test
    public void deserialisesJsonToPrettyPrintedString_unescapingDoubleQuotesWithinTheValue() throws IOException {

        // given
        final String jsonPropertyStringValue = from("nestedJsonWithQuotes.json");

        final String prettyPrintedJsonWithQuotes = "{\n  \"nested-property\" : \"with \"quotes\"\"\n}";

        // when
        final String actualValue = objectMapper.readValue(jsonPropertyStringValue, TestDto.class).getProperty();

        // then
        assertThat(
            "Deserialises nested JSON, preserving double quotes embedded within its property.",
            actualValue,
            is(prettyPrintedJsonWithQuotes)
        );
    }

    private String from(final String testFileName) {
        return contentOfFileFromClasspath(
            "/test-data/api-specifications/ToPrettyJsonStringDeserializerTest/" + testFileName
        );
    }

    public static class TestDto {

        @JsonDeserialize(using = ToPrettyJsonStringDeserializer.class)
        private String property;

        private String getProperty() {
            return property;
        }
    }
}
