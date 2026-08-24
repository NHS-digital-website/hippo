package uk.nhs.digital.highlighter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HighlighterTest {

    @Test
    public void highlightsCodeUsingConfiguredJavascriptEngine() {
        final String highlighted = Highlighter.INSTANCE.paint("public class Example {}", Language.JAVA);

        assertThat(highlighted, containsString("hljs-keyword"));
        assertThat(highlighted, containsString("public"));
    }

    @Test
    public void returnsSourceWhenLanguageIsNotSupported() {
        final String source = "some code";

        assertEquals(source, Highlighter.INSTANCE.paint(source, null));
    }
}
