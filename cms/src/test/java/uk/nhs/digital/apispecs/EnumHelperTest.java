package uk.nhs.digital.apispecs;

import static com.onehippo.cms7.inference.engine.core.util.ArraysUtils.asList;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.nhs.digital.apispecs.handlebars.EnumHelper;

import java.util.Collection;

@RunWith(DataProviderRunner.class)
public class EnumHelperTest {

    @Test
    @UseDataProvider("enumValuesOfVariousTypes")
    public void rendersEnum_ofVariousTypes(
        final String testCaseDescription,
        final Collection<?> enumValues,
        final String firstRenderedValue,
        final String secondRenderedValue
    ) {
        // given
        final EnumHelper helper = EnumHelper.INSTANCE;

        final String expectedRendering = format(
            "<code class=\"codeinline\">%s</code>, <code class=\"codeinline\">%s</code>",
            firstRenderedValue,
            secondRenderedValue
        );

        // when
        final String actualRendering = helper.apply(enumValues, null);

        // then
        assertThat("Enum with " + testCaseDescription + " is rendered.",
            actualRendering,
            is(expectedRendering)
        );
    }

    @DataProvider
    public static Object[][] enumValuesOfVariousTypes() {
        // @formatter:off
        return new Object[][]{
            // testCaseDescription  enumValues                      firstRenderedValue  secondRenderedValue
            {"strings",             asList("string-a", "string-b"), "string-a",         "string-b"},
            {"booleans",            asList(true, false),            "true",             "false"},
            {"numbers",             asList(-1.42, 0),               "-1.42",            "0"},
            {"nulls",               asList("string-a", null),       "string-a",         "null"},
            {"empty strings",       asList("string-a", ""),         "string-a",         ""}
        };
        // @formatter:on
    }
}
