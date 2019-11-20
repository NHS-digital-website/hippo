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
public class StructuredTextTest {

    @Test
    @UseDataProvider("paragraphs")
    public void parsesParagraphs(final String input, final List<Paragraph> expectedElements) throws Exception {

        //given
        final StructuredText actual = new StructuredText(input);

        // when
        List<Element> actualElements = actual.getElements();

        // then
        for (int i = 0; i < actualElements.size(); i++) {
            assertThat("Paragraph element matches",
                expectedElements.get(i).toString(), is(expectedElements.get(i).toString()));
        }
    }

    @Test
    @UseDataProvider("paragraphsAndLists")
    public void parsesParagraphsAndLists(final String input, final List<Element> expectedElements) throws Exception {

        //given
        final StructuredText actual = new StructuredText(input);

        // when
        List<Element> actualElements = actual.getElements();

        // then
        for (int i = 0; i < actualElements.size(); i++) {
            assertThat("Paragraph element matches",
                expectedElements.get(i).getClass().getSimpleName(),
                is(expectedElements.get(i).getClass().getSimpleName())
            );
        }
    }

    @DataProvider
    public static Object[][] paragraphs() {
        return new Object[][]{
            {
                "First paragraph lorem ipsum dolor.",

                asList(
                    new Paragraph("First paragraph lorem ipsum dolor.")
                )
            },
            {
                "First paragraph lorem ipsum dolor.\n"
                    + "\n\n\n\n",

                asList(
                    new Paragraph("First paragraph lorem ipsum dolor.")
                )
            },
            {
                "\n\n\n\n"
                    + "First paragraph lorem ipsum dolor."
                    + "\n\n\n\n",

                asList(
                    new Paragraph("First paragraph lorem ipsum dolor.")
                )
            },
            {
                "\n\n  \n\n"
                    + "First paragraph lorem ipsum dolor."
                    + "\n\n    \n\n   \n\n\n  ",

                asList(
                    new Paragraph("First paragraph lorem ipsum dolor.")
                )
            },
            {
                "First paragraph lorem ipsum dolor.\n"
                    + "\n"
                    + "Second paragraph",

                asList(
                    new Paragraph("First paragraph lorem ipsum dolor."),
                    new Paragraph("Second paragraph")
                )
            },
            {
                "First paragraph lorem ipsum dolor.\n"
                    + "\n\n\n\n"
                    + "Second paragraph",

                asList(
                    new Paragraph("First paragraph lorem ipsum dolor."),
                    new Paragraph("Second paragraph")
                )
            },
            {
                "First paragraph.\n"
                    + "\n\n\n\n"
                    + "Second paragraph."
                    + "\n\n\n"
                    + "Third paragraph.",

                asList(
                    new Paragraph("First paragraph."),
                    new Paragraph("Second paragraph."),
                    new Paragraph("Third paragraph.")
                )
            },
        };
    }

    @DataProvider
    public static Object[][] paragraphsAndLists() {
        return new Object[][]{
            {
                asOneString(
                    "First paragraph lorem ipsum dolor.\n",
                    "\n",
                    "* first list item\n",
                    "* second list item"
                ),
                asList(
                    new Paragraph("First paragraph lorem ipsum dolor."),
                    new BulletList("* first list item\n* second list item")
                )
            },
            {
                asOneString(
                    "First paragraph lorem ipsum dolor.\n",
                    "\n",
                    "* first list item\n",
                    "* second list item\n",
                    "\n",
                    "Second paragraph sit ament.\n"
                ),
                asList(
                    new Paragraph("First paragraph lorem ipsum dolor."),
                    new BulletList("* first list item\n* second list item"),
                    new Paragraph("Second paragraph sit ament.")
                )
            },
            {
                asOneString(
                    "\n\n  \n\n",
                    "* first list item\n",
                    "* second list item\n",
                    "\n",
                    "First paragraph lorem ipsum dolor.\n"
                ),
                asList(
                    new BulletList("* first list item\n* second list item"),
                    new Paragraph("First paragraph lorem ipsum dolor.")
                )
            }
        };
    }

    private static String asOneString(final String... text) {
        return Arrays.stream(text).collect(Collectors.joining(""));
    }
}
