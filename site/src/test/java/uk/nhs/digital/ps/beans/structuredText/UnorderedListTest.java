package uk.nhs.digital.ps.beans.structuredText;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

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
        int i = 0;
        for(String item: actualItems) {
            assertThat("List Item matches: " + expectedElements.get(i),
                item, is(expectedElements.get(i++)));
        }
    }

    @DataProvider
    public static Object[][] lists() {
        return new Object[][]{
            {
                "* First item.",

                new ArrayList() {{
                    add("First item.");
                }}
            },
            {
                "* First item.\n" +
                "* Second item.\n" +
                "* Third item.",

                new ArrayList() {{
                    add("First item.");
                    add("Second item.");
                    add("Third item.");
                }}
            },
            {
                "  *  First item.\n" +
                "  *  Second item.\n" +
                "  *  Third item.",

                new ArrayList() {{
                    add("First item.");
                    add("Second item.");
                    add("Third item.");
                }}
            },
            {
                " * First item\n" +
                "   with long text.\n" +
                " * Second item.\n" +
                " * Third item.",

                new ArrayList() {{
                    add("First item with long text.");
                    add("Second item.");
                    add("Third item.");
                }}
            },
            {
                "*  First item.\n" +
                "*  Second item with * character.\n" +
                "*  Third item.\n",

                new ArrayList() {{
                    add("First item.");
                    add("Second item with * character.");
                    add("Third item.");
                }}
            },
        };
    }
}
