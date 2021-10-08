package uk.nhs.digital.common.components.apicatalogue;

import static java.util.Arrays.asList;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.google.common.collect.ImmutableMap;
import freemarker.core.Environment;
import freemarker.template.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

public class UrlGeneratorDirectiveTest {

    private Writer writer;
    private Environment environment;
    private UrlGeneratorDirective urlGeneratorDirective;
    private SimpleSequence filterKeys;

    @Before
    public void setUp() throws Exception {
        writer = mock(Writer.class);

        final Template irrelevantTemplate = Template.getPlainTextTemplate(
            "irrelevant template name",
            "irrelevant template content",
            new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS)
        );

        filterKeys = new SimpleSequence(new SimpleObjectWrapper(new Version("2.3.0")));

        environment = new Environment(irrelevantTemplate, null, writer);

        urlGeneratorDirective = new UrlGeneratorDirective();
    }

    @Test
    public void generatesUrlWithAllQueryParameters_whenFiltersSelected_andRetiredToBeShown() throws TemplateException, IOException {

        // given
        filterKeys.add("inpatient");
        filterKeys.add("hospital");

        final Map<String, Object> parameters = ImmutableMap.of(
            "baseUrl", new SimpleScalar("/site/developer/api-catalogue"),
            "showRetired", TemplateBooleanModel.TRUE,
            "filters", filterKeys
        );

        // when
        urlGeneratorDirective.execute(environment, parameters, null, null);

        // then
        then(writer).should().append("/site/developer/api-catalogue?showRetired&filter=inpatient&filter=hospital");
    }

    @Test
    public void generatesUrlWithOnlyFiltersQueryParameters_whenFiltersSelected_andRetiredToBeHidden() throws TemplateException, IOException {

        // given
        filterKeys.add("inpatient");
        filterKeys.add("hospital");

        final Map<String, Object> parameters = ImmutableMap.of(
            "baseUrl", new SimpleScalar("/site/developer/api-catalogue"),
            "showRetired", TemplateBooleanModel.FALSE,
            "filters", filterKeys
        );

        // when
        urlGeneratorDirective.execute(environment, parameters, null, null);

        // then
        then(writer).should().append("/site/developer/api-catalogue?filter=inpatient&filter=hospital");
    }

    @Test
    public void generatesUrlWithOnlyShowRetiredQueryParameter_whenNoFiltersSelected_andRetiredToBeShown() throws TemplateException, IOException {

        // given
        final Map<String, Object> parameters = ImmutableMap.of(
            "baseUrl", new SimpleScalar("/site/developer/api-catalogue"),
            "showRetired", TemplateBooleanModel.TRUE,
            "filters", filterKeys
        );

        // when
        urlGeneratorDirective.execute(environment, parameters, null, null);

        // then
        then(writer).should().append("/site/developer/api-catalogue?showRetired");
    }

    @Test
    public void generatesUrlWithNoParameters_whenNoFiltersSelected_andRetiredToBeHidden() throws TemplateException, IOException {

        // given
        final Map<String, Object> parameters =  ImmutableMap.of(
            "baseUrl", new SimpleScalar("/site/developer/api-catalogue"),
            "showRetired", TemplateBooleanModel.FALSE,
            "filters", filterKeys
        );

        // when
        urlGeneratorDirective.execute(environment, parameters, null, null);

        // then
        then(writer).should().append("/site/developer/api-catalogue");
    }

    @Test
    public void urlEncodesFilterKeys() throws TemplateException, IOException {

        // given
        final List<String> rawFilterKeys = asList(
            "key?with?question?marks",
            "key,with,commas",
            "key&with&ampersands",
            "key#with#hash#marks",
            "key=with=equal=signs",
            "key with spaces",
            "key<with>angle<>brackets",
            "key|with|pipes",
            "key+with+pluses",
            "key-with-minuses"
        );

        final String expectedFilterKeys = String.join("&filter=",
            "key%3Fwith%3Fquestion%3Fmarks",
            "key%2Cwith%2Ccommas",
            "key%26with%26ampersands",
            "key%23with%23hash%23marks",
            "key%3Dwith%3Dequal%3Dsigns",
            "key+with+spaces",
            "key%3Cwith%3Eangle%3C%3Ebrackets",
            "key%7Cwith%7Cpipes",
            "key%2Bwith%2Bpluses",
            "key-with-minuses"
        );

        rawFilterKeys.forEach(filterKey -> filterKeys.add(filterKey));

        final Map<String, Object> parameters =  ImmutableMap.of(
            "baseUrl", new SimpleScalar("/site/developer/api-catalogue"),
            "showRetired", TemplateBooleanModel.FALSE,
            "filters", filterKeys
        );

        // when
        urlGeneratorDirective.execute(environment, parameters, null, null);

        // then
        then(writer).should().append("/site/developer/api-catalogue?filter=" + expectedFilterKeys);
    }
}