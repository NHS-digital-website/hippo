package uk.nhs.digital.common.components.apicatalogue;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.google.common.collect.ImmutableMap;
import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.SimpleScalar;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class FilterDescriptionDirectiveTest {

    private Writer writer;
    private Environment environment;
    private FilterDescriptionDirective filterDescriptionDirective;

    @Before
    public void setUp() throws Exception {
        writer = mock(Writer.class);

        final Template irrelevantTemplate = Template.getPlainTextTemplate(
            "irrelevant template name",
            "irrelevant template content",
            new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS)
        );

        environment = new Environment(irrelevantTemplate, null, writer);

        filterDescriptionDirective = new FilterDescriptionDirective();
    }

    @Test
    public void rendersDescriptionInMarkdown_asHtml() throws TemplateException, IOException {

        // given
        final String descriptionInMarkdown = String.join("\n",
            "A paragraph of text to be rendered as paragraph.",
            "",
            "Another paragraph <a href=\"https://digital.nhs.uk\">with some HTML</a> to be escaped.",
            "",
            "And another with script element to be escaped. <script>function danger() {alert('danger');}</script>",
            "",
            "",
            "Yet another paragraph with [an evil link](javascript://haxors.r.us) to be sanitised.",
            "",
            "And another one with a [a valid link](https://saints.r.us) to be rendered with no sanitisation."
        );

        final String expectedDescriptionInHtml = String.join("\n",
            "<p>A paragraph of text to be rendered as paragraph.</p>",
            "<p>Another paragraph &lt;a href=&quot;https://digital.nhs.uk&quot;&gt;with some HTML&lt;/a&gt; to be escaped.</p>",
            "<p>And another with script element to be escaped. &lt;script&gt;function danger() {alert('danger');}&lt;/script&gt;</p>",
            "<p>Yet another paragraph with <a rel=\"nofollow\" href=\"\" class=\"nhsd-a-link\" target=\"_blank\">an evil link</a> to be sanitised.</p>",
            "<p>And another one with a <a rel=\"nofollow\" href=\"https://saints.r.us\" class=\"nhsd-a-link\" target=\"_blank\">a valid link</a>"
                + " to be rendered with no sanitisation.</p>",
            ""
        );

        final Map<String, Object> parameters = ImmutableMap.of(
            "description", new SimpleScalar(descriptionInMarkdown)
        );

        // when
        filterDescriptionDirective.execute(environment, parameters, null, null);

        // then
        then(writer).should().append(expectedDescriptionInHtml);
    }
}