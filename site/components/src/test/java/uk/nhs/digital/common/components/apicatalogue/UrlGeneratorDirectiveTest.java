package uk.nhs.digital.common.components.apicatalogue;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.google.common.collect.ImmutableMap;
import freemarker.core.Environment;
import freemarker.template.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class UrlGeneratorDirectiveTest {

    private Writer writer;
    private Environment environment;
    private UrlGeneratorDirective urlGeneratorDirective;
    private SimpleSequence filters;

    @Before
    public void setUp() throws Exception {
        writer = mock(Writer.class);

        final Template irrelevantTemplate = Template.getPlainTextTemplate(
            "irrelevant template name",
            "irrelevant template content",
            new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS)
        );

        filters = new SimpleSequence(new SimpleObjectWrapper(new Version("2.3.0")));

        environment = new Environment(irrelevantTemplate, null, writer);

        urlGeneratorDirective = new UrlGeneratorDirective();
    }

    @Test
    public void generatesUrlWithAllQueryParameters_whenFiltersSelected_andDeprecatedAndRetiredToBeShown() throws TemplateException, IOException {

        // given
        filters.add("inpatient");
        filters.add("hospital");

        final Map<String, Object> parameters = ImmutableMap.of(
            "baseUrl", new SimpleScalar("/site/developer/api-catalogue"),
            "showDeprecatedAndRetired", TemplateBooleanModel.TRUE,
            "filters", filters
        );

        // when
        urlGeneratorDirective.execute(environment, parameters, null, null);

        // then
        then(writer).should().append("/site/developer/api-catalogue?showDeprecatedAndRetired&filters=inpatient,hospital");
    }

    @Test
    public void generatesUrlWithOnlyFiltersQueryParameters_whenFiltersSelected_andDeprecatedAndRetiredToBeHidden() throws TemplateException, IOException {

        // given
        filters.add("inpatient");
        filters.add("hospital");

        final Map<String, Object> parameters = ImmutableMap.of(
            "baseUrl", new SimpleScalar("/site/developer/api-catalogue"),
            "showDeprecatedAndRetired", TemplateBooleanModel.FALSE,
            "filters", filters
        );

        // when
        urlGeneratorDirective.execute(environment, parameters, null, null);

        // then
        then(writer).should().append("/site/developer/api-catalogue?filters=inpatient,hospital");
    }

    @Test
    public void generatesUrlWithOnlyShowDeprecatedAndRetiredQueryParameter_whenNoFiltersSelected_andDeprecatedAndRetiredToBeShown() throws TemplateException, IOException {

        // given
        final Map<String, Object> parameters = ImmutableMap.of(
            "baseUrl", new SimpleScalar("/site/developer/api-catalogue"),
            "showDeprecatedAndRetired", TemplateBooleanModel.TRUE,
            "filters", filters
        );

        // when
        urlGeneratorDirective.execute(environment, parameters, null, null);

        // then
        then(writer).should().append("/site/developer/api-catalogue?showDeprecatedAndRetired");
    }

    @Test
    public void generatesUrlWithNoParameters_whenNoFiltersSelected_andDeprecatedAndRetiredToBeHidden() throws TemplateException, IOException {

        // given
        final Map<String, Object> parameters =  ImmutableMap.of(
            "baseUrl", new SimpleScalar("/site/developer/api-catalogue"),
            "showDeprecatedAndRetired", TemplateBooleanModel.FALSE,
            "filters", filters
        );

        // when
        urlGeneratorDirective.execute(environment, parameters, null, null);

        // then
        then(writer).should().append("/site/developer/api-catalogue");
    }
}