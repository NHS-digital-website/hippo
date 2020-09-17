package uk.nhs.digital.apispecs.swagger;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.junit.Test;
import uk.nhs.digital.test.util.FileUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SchemaHelperTest {

    private static final String TEST_DATA_FILES_CLASSPATH = "/test-data/api-specifications/SchemaHelperTest/";

    @Test
    public void rendersJsonSchemaAsHtml() {

        // given
        final SchemaHelper schemaHelper = new SchemaHelper();

        final String expectedHtmlSchema = "<div>TEST SCHEMA CONTENT</div>";

        // when
        final Object actualHtmlSchema = schemaHelper.apply(null, null);

        // then
        assertThat("JSON schema has been rendered as HTML",
            actualHtmlSchema,
            is(expectedHtmlSchema)
        );
    }

    @Test
    public void rendersMap() throws IOException {

        // given
        final Container container = new Container();
        final String templateRaw = loadFile("map.mustache");
        final Handlebars handlebars = new Handlebars();
        final Template template = handlebars.compileInline(templateRaw);

        // when
        final String output = template.apply(container);

        // then
        System.out.println(output);

    }

    @Test
    public void rendersRecursiveTemplates() throws IOException {

        // given
        final Container container = new Container();

        final Handlebars handlebars = new Handlebars()
            // .infiniteLoops(true)
            ;

        final Template template = handlebars.compile(TEST_DATA_FILES_CLASSPATH + "main_template.mustache");

        // when
        final String output = template.apply(container);

        // then
        System.out.println(output);

    }

    private String loadFile(final String templateFileName) {
        return FileUtils.fileContentFromClasspath(TEST_DATA_FILES_CLASSPATH + templateFileName);
    }

    public static class Container {

        private String property = "";

        private Map<String, String> properties = new HashMap<>();

        {
            properties.put("key-a", "value-a");
            properties.put("key-b", "value-b");
        }

        public Map<String, String> getProperties() {
            return properties;
        }
    }
}