package uk.nhs.digital.ps.beans.structuredText;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(DataProviderRunner.class)
public class StructuredTextTest {

    @Test
    @UseDataProvider("paragraphs")
    public void parsesParagraphs(final String input, final List<Paragraph> expectedElements) throws Exception {

        //given
        final StructuredText actual = new StructuredText(input);

        // when
        List<Element> actualElement = actual.getElements();

        // then
        int i = 0;
        for(Element element: actualElement) {
            assertThat("Paragraph element matches",
                element.toString(), is(expectedElements.get(i++).toString()));
        }
    }

    @Test
    @UseDataProvider("paragraphsAndLists")
    public void parsesParagraphsAndLists(final String input, final List<Element> expectedElements) throws Exception {

        //given
        final StructuredText actual = new StructuredText(input);

        // when
        List<Element> actualElement = actual.getElements();

        // then
        int i = 0;
        for(Element element: actualElement) {
            assertThat("Paragraph element matches",
                element.getClass().getSimpleName(), is(expectedElements.get(i++).getClass().getSimpleName()));
        }
    }

    @DataProvider
    public static Object[][] paragraphs() {
        return new Object[][]{
            {
                "First paragraph lorem ipsum dolor.",

                new ArrayList() {{
                    add(new Paragraph("First paragraph lorem ipsum dolor."));
                }}
            },
            {
                "First paragraph lorem ipsum dolor.\n" +
                "\n\n\n\n",

                new ArrayList() {{
                    add(new Paragraph("First paragraph lorem ipsum dolor."));
                }}
            },
            {
                "\n\n\n\n" +
                "First paragraph lorem ipsum dolor." +
                "\n\n\n\n",

                new ArrayList() {{
                    add(new Paragraph("First paragraph lorem ipsum dolor."));
                }}
            },
            {
                "\n\n  \n\n" +
                "First paragraph lorem ipsum dolor." +
                "\n\n    \n\n   \n\n\n  ",

                new ArrayList() {{
                    add(new Paragraph("First paragraph lorem ipsum dolor."));
                }}
            },
            {
                "First paragraph lorem ipsum dolor.\n" +
                "\n" +
                "Second paragraph",

                new ArrayList() {{
                    add(new Paragraph("First paragraph lorem ipsum dolor."));
                    add(new Paragraph("Second paragraph"));
                }}
            },
            {
                "First paragraph lorem ipsum dolor.\n" +
                "\n\n\n\n" +
                "Second paragraph",

                new ArrayList() {{
                    add(new Paragraph("First paragraph lorem ipsum dolor."));
                    add(new Paragraph("Second paragraph"));
                }}
            },
            {
                "First paragraph.\n" +
                "\n\n\n\n" +
                "Second paragraph." +
                "\n\n\n" +
                "Third paragraph.",

                new ArrayList() {{
                    add(new Paragraph("First paragraph."));
                    add(new Paragraph("Second paragraph."));
                    add(new Paragraph("Third paragraph."));
                }}
            },
        };
    }

    @DataProvider
    public static Object[][] paragraphsAndLists() {
        return new Object[][]{
            {
                "First paragraph lorem ipsum dolor.\n" +
                "\n" +
                "* first list item\n" +
                "* second list item",

                new ArrayList() {{
                    add(new Paragraph("First paragraph lorem ipsum dolor."));
                    add(new BulletList("* first list item\n* second list item"));
                }}
            },
            {
                "First paragraph lorem ipsum dolor.\n" +
                "\n" +
                "* first list item\n" +
                "* second list item\n" +
                "\n",
                "Second paragraph sit ament.\n",

                new ArrayList() {{
                    add(new Paragraph("First paragraph lorem ipsum dolor."));
                    add(new BulletList("* first list item\n* second list item"));
                    add(new Paragraph("Second paragraph sit ament."));
                }}
            },
            {
                "\n\n  \n\n" +
                "* first list item\n" +
                "* second list item\n" +
                "\n"+
                "First paragraph lorem ipsum dolor.\n",

                new ArrayList() {{
                    add(new BulletList("* first list item\n* second list item"));
                    add(new Paragraph("First paragraph lorem ipsum dolor."));
                }}
            }
        };
    }
}
