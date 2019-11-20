package uk.nhs.digital.ps.beans.structuredText;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(DataProviderRunner.class)
public class UnorderedListTest {

    @Test
    @UseDataProvider("lists")
    public void parsesList(final String input, final List<Paragraph> expectedElements) throws Exception {

        //given
        final BulletList actual = new BulletList(input);

        // when
        List<String> actualItems = actual.getItems();

        // then
        for (int i = 0; i < actualItems.size(); i++) {
            assertThat("List Item matches: " + expectedElements.get(i),
                actualItems.get(i), is(expectedElements.get(i++)));
        }
    }

    @DataProvider
    public static Object[][] lists() {
        return new Object[][]{
            {
                "* First item.",

                asList(
                    "First item."
                )
            },
            {
                asOneString(
                    "* First item.\n",
                    "* Second item.\n",
                    "* Third item."
                ),
                asList(
                    "First item.",
                    "Second item.",
                    "Third item."
                )
            },
            {
                asOneString(
                    "  *  First item.\n",
                    "  *  Second item.\n",
                    "  *  Third item."
                ),
                asList(
                    "First item.",
                    "Second item.",
                    "Third item."
                )
            },
            {
                asOneString(
                    " * First item\n",
                    "   with long text.\n",
                    " * Second item.\n",
                    " * Third item."
                ),
                asList(
                    "First item with long text.",
                    "Second item.",
                    "Third item."
                )
            },
            {
                asOneString(
                    "*  First item.\n",
                    "*  Second item with * character.\n",
                    "*  Third item.\n"
                ),
                asList(
                    "First item.",
                    "Second item with * character.",
                    "Third item."
                )
            },
        };
    }

    private static String asOneString(final String... text) {
        return Arrays.stream(text).collect(Collectors.joining(""));
    }
}
