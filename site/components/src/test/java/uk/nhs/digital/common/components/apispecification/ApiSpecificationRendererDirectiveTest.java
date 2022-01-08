package uk.nhs.digital.common.components.apispecification;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static uk.nhs.digital.test.TestLogger.LogAssertor.debug;
import static uk.nhs.digital.test.TestLogger.LogAssertor.error;

import com.google.common.collect.ImmutableMap;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.SimpleScalar;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.hippoecm.hst.mock.core.container.MockComponentManager;
import org.hippoecm.hst.site.HstServices;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.nhs.digital.test.TestLoggerRule;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

@RunWith(DataProviderRunner.class)
public class ApiSpecificationRendererDirectiveTest {

    @Rule
    public TestLoggerRule logger = TestLoggerRule.targeting(ApiSpecificationRendererDirective.class);

    private Writer writer;
    private Environment environment;

    private ApiSpecificationRendererDirective apiSpecificationRendererDirective;
    private OpenApiSpecificationJsonToHtmlConverter converter;

    @Before
    public void setUp() throws Exception {
        writer = mock(Writer.class);
        converter = mock(OpenApiSpecificationJsonToHtmlConverter.class);

        final Template irrelevantTemplate = Template.getPlainTextTemplate(
            "irrelevant template name",
            "irrelevant template content",
            new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS)
        );

        environment = new Environment(irrelevantTemplate, null, writer);

        final MockComponentManager mockComponentManager = new MockComponentManager();
        mockComponentManager.addComponent("apiSpecificationRenderer", converter);

        HstServices.setComponentManager(mockComponentManager);

        apiSpecificationRendererDirective = new ApiSpecificationRendererDirective();
    }

    @Test
    public void returnsContentProducedByOpenApiSpecificationJsonToHtmlConverter() throws TemplateException, IOException {

        // given
        final String inputSpecificationJson = "{\"irrelevant\":\"payload\"}";
        final String expectedRenderedContent = "<html><body>rendered specification</body></html>";
        final String expectedPath = "/developer/api-specification/some-spec";

        given(converter.htmlFrom(inputSpecificationJson))
            .willReturn(expectedRenderedContent);

        final Map<String, Object> parameters = ImmutableMap.of(
            "specificationJson", new SimpleScalar(inputSpecificationJson),
            "path", new SimpleScalar(expectedPath)
        );

        // when
        apiSpecificationRendererDirective.execute(environment, parameters, null, null);

        // then
        then(writer).should().append(expectedRenderedContent);

        logger.shouldReceive(
            debug("Rendering API specification " + expectedPath + ": start"),
            debug("Rendering API specification " + expectedPath + ": done")
        );
    }

    @Test
    public void logsAndRethrowsErrorOnRenderingFailure() {

        // given
        final String inputSpecificationJson = "{\"irrelevant\":\"payload\"}";
        final String expectedPath = "/developer/api-specification/some-spec";

        final RuntimeException expectedException = new RuntimeException("Invalid specification json.");
        given(converter.htmlFrom(inputSpecificationJson))
            .willThrow(expectedException);

        final Map<String, Object> parameters = ImmutableMap.of(
            "specificationJson", new SimpleScalar(inputSpecificationJson),
            "path", new SimpleScalar(expectedPath)
        );

        // when
        final RuntimeException actualException = assertThrows(
            "Rethrows exception.",
            RuntimeException.class,
            () -> apiSpecificationRendererDirective.execute(environment, parameters, null, null)
        );

        // then
        then(writer).shouldHaveNoInteractions();

        assertThat(
            "Rethrows original exception",
            actualException,
            is(expectedException)
        );

        logger.shouldReceive(
            debug("Rendering API specification " + expectedPath + ": start"),
            error("Rendering API specification " + expectedPath + ": failed")
                .withException("Invalid specification json.")
        );
    }

    @Test
    @DataProvider(value = {
        "",
        " ",
        "\n",
        "null"
        }
    )
    public void doesNothingAndDoesNotFail_onNullEmptyOrBlankJson(final String inputSpecificationJson) throws TemplateException, IOException {

        // given
        final String expectedPath = "/developer/api-specification/some-spec";

        // this is because 'pure' null cannot be submitted as attribute value to the @DataProvider notation
        final String input = "null".equals(inputSpecificationJson) ? null : inputSpecificationJson;

        final Map<String, Object> parameters = ImmutableMap.of(
            "specificationJson", new SimpleScalar(input),
            "path", new SimpleScalar(expectedPath)
        );

        // when
        apiSpecificationRendererDirective.execute(environment, parameters, null, null);

        // then
        then(writer).shouldHaveNoInteractions();
        then(converter).shouldHaveNoInteractions();

        logger.shouldReceive(
            debug("No API specification JSON available for " + expectedPath)
        );
    }
}