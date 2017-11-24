package uk.nhs.digital.ps.migrator.task;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import uk.nhs.digital.ps.migrator.misc.TextHelper;

@Ignore("These are just experiments of tools (potentially) useful for the migrator, not actual tests")
public class UrlNameGeneratorTest {

    // @formatter:off
    private static final String[] EXAMPLE_NAMES = {
        "Domain 3 - Helping people to recover from episodes of ill health or following injury (NOF)",
        "Geography, demography and socio-economic factors",
        "1a Potential years of life lost (PYLL) from causes considered amenable to healthcare (retired as of May-15)",
        "3.1 Total health gain as assessed by patients for elective procedures (retired as of May-15)",
        "Summary Hospital-level Mortality Indicator (SHMI) - Deaths associated with hospitalisation, England: Recent publications",
        "--text with leading and trailing, multiple dashes--",
        "text_with_underscores",
    };
    // @formatter:on


    @Before
    public void setUp() throws Exception {
        System.out.println("");
        System.out.println("* ");
        System.out.println("* Example (source) values:");
        System.out.println("* ");
        System.out.println("");

        for (final String exampleName: EXAMPLE_NAMES) {
            System.out.println(exampleName);
        }
    }

    @Test
    public void testUrlNameGeneration_SHA_1() throws Exception {

        // given
        System.out.println("");
        System.out.println("* ");
        System.out.println("* Names are replaced by SHA-1 hashcodes:");
        System.out.println("* ");
        System.out.println("");

        for (final String exampleName: EXAMPLE_NAMES) {
            // when
            final String result = TextHelper.toShaOne(exampleName);

            // then
            System.out.println(result);
        }
    }

    @Test
    public void testUrlNameGeneration_customSanitiser() throws Exception {

        // given
        System.out.println("");
        System.out.println("* ");
        System.out.println("* Anything that isn't an alphanumeric gets replaced with '-':");
        System.out.println("* ");
        System.out.println("");

        for (final String exampleName: EXAMPLE_NAMES) {

            // when
            final String result = TextHelper.toLowerCaseDashedValue(exampleName);

            // then
            System.out.println(result);
        }
    }

    @Test
    public void testUrlNameGeneration_customSanitiserShortening() throws Exception {

        // given
        System.out.println("");
        System.out.println("* ");
        System.out.println("* Anything that isn't an alphanumeric gets replaced with '-' with words shortened to max three characters:");
        System.out.println("* ");
        System.out.println("");

        for (final String exampleName: EXAMPLE_NAMES) {

            // when
            final String result = TextHelper.toLowerCaseDashedShortValue(exampleName);

            // then
            System.out.println(result);
        }
    }
}
