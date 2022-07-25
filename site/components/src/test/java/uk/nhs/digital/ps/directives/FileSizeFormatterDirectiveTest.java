package uk.nhs.digital.ps.directives;

import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import freemarker.core.Environment;
import freemarker.template.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@RunWith(DataProviderRunner.class)
public class FileSizeFormatterDirectiveTest {

    private static final String BYTES_COUNT_PARAM_NAME = "bytesCount";

    private FileSizeFormatterDirective fileSizeFormatterDirective;

    private Environment environment;
    private TemplateModel[] loopVariables;
    private TemplateDirectiveBody body;
    private Map<String, SimpleNumber> parameters;
    private Writer writer;

    @Before
    public void setUp() throws Exception {
        Template template = mock(Template.class);
        Configuration configuration = mock(Configuration.class);
        Version version = new Version("2.3.28");
        given(template.getConfiguration()).willReturn(configuration);
        given(configuration.getIncompatibleImprovements()).willReturn(version);
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

    @Test(expected = TemplateException.class)
    public void reportsErrorOnMissingRequriedParameter() throws Exception {

        // given
        parameters.remove(BYTES_COUNT_PARAM_NAME);

        // when
        fileSizeFormatterDirective.execute(environment, parameters, loopVariables, body);
        fail("Required parameter 'bytesCount' was not provided to template uk.nhs.digital"
            + ".ps.directives.FileSizeFormatterDirective.");

        // then
        // the framework asserts expected exception
    }

}
