package uk.nhs.digital.ps.chart;

import static java.text.MessageFormat.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.hippoecm.frontend.model.JcrNodeModel;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.types.ITypeDescriptor;
import org.hippoecm.frontend.validation.IFieldValidator;
import org.hippoecm.frontend.validation.ValidationException;
import org.hippoecm.frontend.validation.Violation;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.onehippo.cms7.services.HippoServiceRegistry;
import uk.nhs.digital.ps.chart.input.HighchartsInputParser;
import uk.nhs.digital.ps.chart.input.HighchartsJcrNodeReader;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

public class HighchartsDataFileValidatorTest {

    static final String PROPERTY_NAME_FILE_CONTENT = "jcr:data";

    private static final String TARGET_NODE_TYPE = "publicationsystem:resource";

    @Mock private HighchartsInputParser highchartsInputParser;
    @Mock private HighchartsJcrNodeReader highchartsJcrNodeReader;
    @Mock private AbstractHighchartsParameters highchartsParameters;

    @Mock private IFieldValidator fieldValidator;
    @Mock private ITypeDescriptor typeDescriptor;
    @Mock private JcrNodeModel documentNodeModel;
    @Mock private IPluginConfig pluginConfig;
    @Mock private IPluginContext pluginContext;
    @Mock private JcrNodeModel uploadFieldModel;
    @Mock private IModel<String> violationMessageTranslationModel;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private HighchartsDataFileValidator highchartsDataFileValidator;

    @Before
    public void setUp() {
        initMocks(this);

        given(pluginConfig.getName()).willReturn("someTestConfig");

        initialiseWicketApplication();

        HippoServiceRegistry.registerService(highchartsInputParser, HighchartsInputParser.class);
        HippoServiceRegistry.registerService(highchartsJcrNodeReader, HighchartsJcrNodeReader.class);

        highchartsDataFileValidator = new HighchartsDataFileValidator(pluginContext, pluginConfig);
    }

    @After
    public void tearDown() {
        HippoServiceRegistry.unregisterService(highchartsInputParser, HighchartsInputParser.class);
        HippoServiceRegistry.unregisterService(highchartsJcrNodeReader, HighchartsJcrNodeReader.class);
    }

    @Test
    public void reportsError_whenCalledForInvalidFieldType() throws Exception {

        // given
        given(fieldValidator.getFieldType()).willReturn(typeDescriptor);
        final String bogusFieldTypeName = newRandomString();
        given(typeDescriptor.getType()).willReturn(bogusFieldTypeName);

        expectedException.expect(ValidationException.class);
        expectedException.expectMessage(format(
            "Cannot validate the chart input file field. Expected field of type ''{0}'' but got ''{1}''.",
            TARGET_NODE_TYPE,
            bogusFieldTypeName
        ));

        // when
        highchartsDataFileValidator.preValidation(fieldValidator);

        // then
        // expectations as specified in 'given'
    }

    @Test
    public void reportsValidationViolation_forInvalidChartInputFile() throws Exception {

        // given
        setUpChartInput();

        final Violation expectedViolation = new Violation(Collections.emptySet(), violationMessageTranslationModel);

        given(fieldValidator.newValueViolation(eq(uploadFieldModel), isA(IModel.class)))
            .willReturn(expectedViolation);

        given(highchartsInputParser.parse(highchartsParameters))
            .willThrow(new RuntimeException());

        // when
        final Set<Violation> actualValidationViolations =
            highchartsDataFileValidator.validate(fieldValidator, documentNodeModel, uploadFieldModel);

        // then
        assertThat("Exactly one violation has been reported.", actualValidationViolations, hasSize(1));

        final Violation actualViolation = actualValidationViolations.iterator().next();
        assertThat("Violation is not null", actualViolation, notNullValue());
        assertThat("Correct violation has been reported.", actualViolation.getMessage(),
            is(violationMessageTranslationModel));
    }

    @Test
    public void reportsNoValidationViolation_onFailedParsing() throws Exception {

        // given
        setUpChartInput();

        // when
        final Set<Violation> actualValidationViolations =
            highchartsDataFileValidator.validate(fieldValidator, documentNodeModel, uploadFieldModel);

        // then
        then(highchartsInputParser).should().parse(highchartsParameters);

        assertThat("No violation has been reported.", actualValidationViolations, is(empty()));
    }

    private void setUpChartInput() throws RepositoryException {

        final Node chartSectionNode = mock(Node.class);

        final Node dataFileNode = mock(Node.class);
        given(uploadFieldModel.getNode()).willReturn(dataFileNode);
        given(dataFileNode.getParent()).willReturn(chartSectionNode);
        given(highchartsJcrNodeReader.readParameters(chartSectionNode)).willReturn(highchartsParameters);
    }

    private void initialiseWicketApplication() {

        // This initialises various statically stored values that Wicket uses,
        // most notably, Session and Application whose absence was causing NullPointerException during call to
        // org.hippoecm.frontend.editor.validator.plugins.AbstractCmsValidator.getTranslation(java.lang.String).
        new WicketTester(new WebApplication() {
            public Class<? extends Page> getHomePage() {
                return null;
            }
        });
    }

    private String newRandomString() {
        return UUID.randomUUID().toString();
    }
}
