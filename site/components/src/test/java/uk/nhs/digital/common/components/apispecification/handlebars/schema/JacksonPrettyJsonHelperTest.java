package uk.nhs.digital.common.components.apispecification.handlebars.schema;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.databind.JsonNode;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.nhs.digital.test.util.RandomTestUtils;

@RunWith(DataProviderRunner.class)
public class JacksonPrettyJsonHelperTest {

    private final JacksonPrettyJsonHelper jacksonPrettyJsonHelper = JacksonPrettyJsonHelper.INSTANCE;

    @Test
    public void rendersJsonNodeObjects_usingJacksonToPrettyString() {

        // given
        final String expectedPrettyPrintedJson = RandomTestUtils.randomString("json_");

        final JsonNode jsonNode = mock(JsonNode.class);
        given(jsonNode.toPrettyString()).willReturn(expectedPrettyPrintedJson);

        // when
        final String actualRenderedString = jacksonPrettyJsonHelper.apply(jsonNode, null);

        // then
        assertThat("Returns pretty printed JSON.",
            actualRenderedString,
            is(expectedPrettyPrintedJson)
        );
    }

    @Test
    @UseDataProvider("nonJsonNodeValues")
    public void rendersAnyOtherObjectAsString(
        final String objectType,
        final Object propertyValue,
        final String expectedRendering
    ) {

        // given
        // args

        // when
        final String actualRenderedString = jacksonPrettyJsonHelper.apply(propertyValue, null);

        // then
        assertThat("Returns a(n) " + objectType + " value of given argument.",
            actualRenderedString,
            is(expectedRendering)
        );
    }

    @DataProvider
    public static Object[][] nonJsonNodeValues() {
        final Object notJsonNode = new Object() {
            @Override public String toString() {
                return "custom object, not JsonNode";
            }
        };

        // @formatter:off
        return new Object[][]{
            // objectType                   propertyValue       expectedRendering
            {"string",                      "a string value",   "a string value"},
            {"number",                      -123.45,            "-123.45"},
            {"boolean",                     false,              "false"},
            {"null",                        null,               "null"},
            {"object other than JsonNode",  notJsonNode,        "custom object, not JsonNode"},

        };
        // @formatter:on
    }
}