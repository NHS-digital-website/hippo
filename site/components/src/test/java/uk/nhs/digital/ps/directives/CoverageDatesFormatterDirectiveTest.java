package uk.nhs.digital.ps.directives;

import static org.junit.Assert.assertEquals;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.SimpleDate;
import freemarker.template.Template;
import freemarker.template.TemplateDateModel;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


@RunWith(MockitoJUnitRunner.class)
public class CoverageDatesFormatterDirectiveTest {

    private CoverageDatesFormatterDirective directive;
    private Configuration configuration;

    @Before
    public void setUp() {
        directive = new CoverageDatesFormatterDirective();
        configuration = new Configuration(Configuration.VERSION_2_3_31);
        configuration.setLogTemplateExceptions(false);
    }


    /* =========================
       ✅ Valid formatting tests
       ========================= */

    @Test
    public void shouldFormatSingleCoverageDateAsStartEqualsEnd() throws Exception {
        StringWriter out = new StringWriter();
        Environment env = createEnvironment(out);

        Map<String, TemplateModel> params = new HashMap<>();
        params.put("start", toSimpleDate(LocalDate.of(2024, 1, 1)));
        params.put("end", toSimpleDate(LocalDate.of(2024, 1, 1)));

        directive.execute(env, params, null, (TemplateDirectiveBody) null);

        // adjust if actual output differs

        assertEquals(
            "Snapshot on 01 Jan 2024",
            out.toString().replaceAll("\\s+", " ").trim()
        );

    }

    @Test
    public void shouldFormatCoverageDateRange() throws Exception {
        StringWriter out = new StringWriter();
        Environment env = createEnvironment(out);

        Map<String, TemplateModel> params = new HashMap<>();
        params.put("start", toSimpleDate(LocalDate.of(2024, 1, 1)));
        params.put("end", toSimpleDate(LocalDate.of(2024, 3, 1)));

        directive.execute(env, params, null, (TemplateDirectiveBody) null);

        assertEquals("01 Jan 2024 to 01 Mar 2024", out.toString());
    }

    @Test
    public void shouldFormatSameMonthRangeAsDayLevelRange() throws Exception {
        StringWriter out = new StringWriter();
        Environment env = createEnvironment(out);

        Map<String, TemplateModel> params = new HashMap<>();
        params.put("start", toSimpleDate(LocalDate.of(2024, 2, 1)));
        params.put("end", toSimpleDate(LocalDate.of(2024, 2, 20)));

        directive.execute(env, params, null, (TemplateDirectiveBody) null);

        assertEquals("01 Feb 2024 to 20 Feb 2024", out.toString());
    }


    /* =========================
       ❌ Required / null tests
       ========================= */

    @Test(expected = TemplateException.class)
    public void shouldFailWhenStartParameterIsMissing() throws Exception {
        StringWriter out = new StringWriter();
        Environment env = createEnvironment(out);

        directive.execute(env, new HashMap<>(), null, (TemplateDirectiveBody) null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailWhenStartIsNull() throws Exception {
        StringWriter out = new StringWriter();
        Environment env = createEnvironment(out);

        Map<String, TemplateModel> params = new HashMap<>();
        params.put("start", null);
        params.put("end", toSimpleDate(LocalDate.of(2024, 1, 1)));

        directive.execute(env, params, null, (TemplateDirectiveBody) null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailWhenEndIsNull() throws Exception {
        StringWriter out = new StringWriter();
        Environment env = createEnvironment(out);

        Map<String, TemplateModel> params = new HashMap<>();
        params.put("start", toSimpleDate(LocalDate.of(2024, 1, 1)));
        params.put("end", null);

        directive.execute(env, params, null, (TemplateDirectiveBody) null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailWhenStartAndEndAreNull() throws Exception {
        StringWriter out = new StringWriter();
        Environment env = createEnvironment(out);

        Map<String, TemplateModel> params = new HashMap<>();
        params.put("start", null);
        params.put("end", null);

        directive.execute(env, params, null, (TemplateDirectiveBody) null);
    }

    /* =========================
       🔧 Helper methods
       ========================= */


    private Environment createEnvironment(StringWriter out) throws Exception {
        Template template = new Template("test", new StringReader(""), configuration);
        return template.createProcessingEnvironment(new HashMap<>(), out);
    }

    private SimpleDate toSimpleDate(LocalDate localDate) {
        return new SimpleDate(Date.valueOf(localDate), TemplateDateModel.DATE);
    }
}
