package uk.nhs.digital.ps.directives;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import freemarker.core.Environment;
import freemarker.template.SimpleNumber;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@RunWith(DataProviderRunner.class)
public class FileSizeFormatterDirectiveTest {

    private static final String BYTES_COUNT_PARAM_NAME = "bytesCount";

    private FileSizeFormatterDirective fileSizeFormatterDirective;

    private Template template;
    private Environment environment;
    private TemplateModel[] loopVariables;
    private TemplateDirectiveBody body;
    private Map<String, SimpleNumber> parameters;
    private Writer writer;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        template = mock(Template.class);
        writer = mock(Writer.class);
        environment = new Environment(template, null, writer);

        parameters = new HashMap(1);
        final int arbitraryByteCount = 10; // some value is required by most of the test cases
        parameters.put(BYTES_COUNT_PARAM_NAME, new SimpleNumber(arbitraryByteCount));

        loopVariables = new TemplateModel[0];
        body = null;

        fileSizeFormatterDirective = new FileSizeFormatterDirective();
    }

    @DataProvider
    public static Object[][] validInputsAndExpectedOutputs() {

        return new Object[][]{

            // bytesCount        expectedOutput
            {  0,                       "0 B"},
            {  1,                       "1 B"},
            {500,                     "500 B"},
            {999,                     "999 B"},

            {  1_000d,                "1.0 kB"},
            {  1_500d,                "1.5 kB"},
            {999_999d,             "1000.0 kB"},

            {  1_000_000d,            "1.0 MB"},
            {  1_500_000d,            "1.5 MB"},
            {999_999_999d,         "1000.0 MB"},

            {  1_000_000_000d,        "1.0 GB"},
            {  1_500_000_000d,        "1.5 GB"},
            {999_999_999_999d,     "1000.0 GB"},

            {  1_000_000_000_000d,    "1.0 TB"},
            {  1_500_000_000_000d,    "1.5 TB"},
        };
    }

    @Test
    @UseDataProvider("validInputsAndExpectedOutputs")
    public void formatsValidFileSizeToHumanReadableValues(final double bytesCount, final String expectedOutput) throws
        Exception {

        // given
        parameters.put(BYTES_COUNT_PARAM_NAME, new SimpleNumber(bytesCount));

        // when
        fileSizeFormatterDirective.execute(environment, parameters, loopVariables, body);

        // then
        verify(writer).append(expectedOutput);
    }

    @Test
    public void reportsErrorOnMissingRequriedParameter() throws Exception {

        // given
        parameters.remove(BYTES_COUNT_PARAM_NAME);

        expectedException.expect(TemplateException.class);
        expectedException.expectMessage("Required parameter 'bytesCount' was not provided to template uk.nhs.digital"
            + ".ps.directives.FileSizeFormatterDirective.");

        // when
        fileSizeFormatterDirective.execute(environment, parameters, loopVariables, body);

        // then
        // the framework asserts expected exception
    }

}
