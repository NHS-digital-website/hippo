package uk.nhs.digital.common.components.apispecification;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static uk.nhs.digital.test.TestLogger.LogAssertor.*;

import com.google.common.collect.ImmutableMap;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
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
import org.mockito.Mock;
import uk.nhs.digital.cache.Cache;
import uk.nhs.digital.test.TestLoggerRule;
import uk.nhs.digital.test.mockito.MockitoSessionTestBase;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.function.Supplier;

@RunWith(DataProviderRunner.class)
public class ApiSpecificationRendererDirectiveTest extends MockitoSessionTestBase {

    @Rule
    public TestLoggerRule logger = TestLoggerRule.targeting(ApiSpecificationRendererDirective.class);

    private Environment environment;

    @Mock private Writer writer;
    @Mock private OpenApiSpecificationJsonToHtmlConverter converter;
    @Mock private Cache<String, String> cache;

    private final String inputSpecificationJson = "{\"irrelevant\":\"payload\"}";
    private final String expectedRenderedContent = "<html><body>rendered specification</body></html>";
    private final String expectedPath = "/developer/api-specification/some-spec";
    private final String documentHandleUuid = "c9844d45-5bff-4236-bfe2-477c1d590a6f";

    private ApiSpecificationRendererDirective apiSpecificationRendererDirective;
    private Map<String, Object> directiveArgs;

    @Before
    public void setUp() throws Exception {

        givenContextForDirectiveInvocationByFreemarker();
        givenApiSpecJsonToHtmlConverter();
        givenCache();
        givenDependenciesRegisteredInHstServicesComponentManager();

        apiSpecificationRendererDirective = new ApiSpecificationRendererDirective();
    }

    @Test
    public void returnsContentProducedByOpenApiSpecificationJsonToHtmlConverter() throws TemplateException, IOException {

        // given
        // setUp()

        // when
        apiSpecificationRendererDirective.execute(environment, directiveArgs, null, null);

        // then
        then(writer).should().append(expectedRenderedContent);

        logger.shouldReceive(
            info("Rendering API specification: START; " + expectedPath + " (" + documentHandleUuid + ")."),
            info("Rendering API specification: DONE; " + expectedPath + " (" + documentHandleUuid + ").")
        );
    }

    @Test
    public void logsAndRethrowsErrorOnRenderingFailure() {

        // given
        final RuntimeException expectedException = new RuntimeException("Invalid specification json.");
        given(converter.htmlFrom(inputSpecificationJson)).willThrow(expectedException);

        // when
        final RuntimeException actualException = assertThrows(
            "Rethrows exception.",
            RuntimeException.class,
            () -> apiSpecificationRendererDirective.execute(environment, directiveArgs, null, null)
        );

        // then
        then(writer).shouldHaveNoInteractions();

        assertThat(
            "Rethrows original exception",
            actualException,
            is(expectedException)
        );

        logger.shouldReceive(
            info("Rendering API specification: START; " + expectedPath + " (" + documentHandleUuid + ")."),
            error("Rendering API specification: FAILED; " + expectedPath + "(" + documentHandleUuid + ").")
                .withException("Invalid specification json.")
        );
    }

    @Test
    public void logsAndRethrowsErrorOnCachingFailure() {

        // given
        final RuntimeException expectedException = new RuntimeException("Failed to save rendered value to cache.");
        given(cache.get(eq(documentHandleUuid), any(Supplier.class))).willThrow(expectedException);

        // when
        final RuntimeException actualException = assertThrows(
            "Rethrows exception.",
            RuntimeException.class,
            () -> apiSpecificationRendererDirective.execute(environment, directiveArgs, null, null)
        );

        // then
        then(writer).shouldHaveNoInteractions();
        then(converter).shouldHaveNoInteractions();

        assertThat(
            "Rethrows original exception",
            actualException,
            is(expectedException)
        );

        logger.shouldReceive(
            error("Rendering API specification: FAILED; " + expectedPath + "(" + documentHandleUuid + ").")
                .withException("Failed to save rendered value to cache.")
        );
    }

    @Test
    @UseDataProvider("blankOrNullStrings")
    public void doesNothingAndDoesNotFail_onNullEmptyOrBlankJson(
        final String variantDescription, // ignored, this is just to have something meaningful for test runner to print.
        final String inputSpecificationJson
    ) throws TemplateException, IOException {

        // given
        // setUp()

        final Map<String, Object> directiveArgs = directiveArgsWith(inputSpecificationJson);

        // when
        apiSpecificationRendererDirective.execute(environment, directiveArgs, null, null);

        // then
        then(writer).shouldHaveNoInteractions();
        then(converter).shouldHaveNoInteractions();
        then(cache).shouldHaveNoInteractions();

        logger.shouldReceive(
            warn("No API specification JSON available for " + expectedPath + " (" + documentHandleUuid +  ").")
        );
    }

    @DataProvider
    public static Object[][] blankOrNullStrings() {
        return new Object[][]{
            {"empty string", ""},
            {"space", " "},
            {"new line", "\n"},
            {"tab", "\t"},
            {"null", null}
        };
    }

    private void givenCache() {

        // emulate cache behaviour invoking provided value factory
        given(cache.get(eq(documentHandleUuid), any(Supplier.class)))
            .will(invocation -> ((Supplier<String>) invocation.getArgument(1)).get());
    }

    private void givenApiSpecJsonToHtmlConverter() {
        given(converter.htmlFrom(inputSpecificationJson)).willReturn(expectedRenderedContent);
    }

    private void givenDependenciesRegisteredInHstServicesComponentManager() {
        final MockComponentManager mockComponentManager = new MockComponentManager();
        mockComponentManager.addComponent("apiSpecificationRenderer", converter);
        mockComponentManager.addComponent("heavyContentCache", cache);

        HstServices.setComponentManager(mockComponentManager);
    }

    private void givenContextForDirectiveInvocationByFreemarker() {
        final Template irrelevantTemplate = Template.getPlainTextTemplate(
            "irrelevant template name",
            "irrelevant template content",
            new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS)
        );

        directiveArgs = directiveArgsWith(inputSpecificationJson);

        environment = new Environment(irrelevantTemplate, null, writer);
    }

    private ImmutableMap<String, Object> directiveArgsWith(final String inputSpecificationJson) {
        return ImmutableMap.of(
            "specificationJson", new SimpleScalar(inputSpecificationJson),
            "path", new SimpleScalar(expectedPath),
            "documentHandleUuid", new SimpleScalar(documentHandleUuid)
        );
    }
}