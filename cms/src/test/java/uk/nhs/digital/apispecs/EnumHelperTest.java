package uk.nhs.digital.apispecs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import uk.nhs.digital.apispecs.swagger.EnumHelper;

import java.util.Arrays;
import java.util.Collection;

public class EnumHelperTest {

    @Test
    public void rendersCollectionValuesAsCommaDelimitedHtmlCodeElements() {

        EnumHelper helper = new EnumHelper();
        Collection<String> gender = Arrays.asList("male", "female", "other");

        String result = (String) helper.apply(gender,null);

        assertThat(
            "Content is formatted as expected",
            result,
            is("<code class=\"codeinline\">male</code>, <code class=\"codeinline\">female</code>, <code class=\"codeinline\">other</code>"));
    }
}
