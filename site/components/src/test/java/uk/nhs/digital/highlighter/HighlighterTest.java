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
    public void escapesSourceWhenLanguageIsNotSupported() {
        final String source = "<script>alert('x')</script>";

        assertEquals("&lt;script&gt;alert('x')&lt;/script&gt;", Highlighter.INSTANCE.paint(source, null));
    }

    @Test
    public void returnsEmptyStringWhenSourceIsNull() {
        assertEquals("", Highlighter.INSTANCE.paint(null, Language.JAVA));
    }
}
