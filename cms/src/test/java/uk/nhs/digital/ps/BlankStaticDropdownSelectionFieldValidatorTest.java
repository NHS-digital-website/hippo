package uk.nhs.digital.ps;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.hippoecm.frontend.model.JcrNodeModel;
import org.hippoecm.frontend.model.properties.JcrPropertyValueModel;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.types.IFieldDescriptor;
import org.hippoecm.frontend.types.ITypeDescriptor;
import org.hippoecm.frontend.validation.IFieldValidator;
import org.hippoecm.frontend.validation.ValidationException;
import org.hippoecm.frontend.validation.Violation;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import javax.jcr.Value;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static java.text.MessageFormat.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.MockitoAnnotations.initMocks;

public class BlankStaticDropdownSelectionFieldValidatorTest {

    private static final String SUPPORTED_FIELD_TYPE_NAME = "StaticDropdown";
    private static final String FIELD_DISPLAY_NAME_KEY = "fieldDisplayName";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private IFieldValidator fieldValidator;
    @Mock private IFieldDescriptor fieldDescriptor;
    @Mock private ITypeDescriptor typeDescriptor;
    @Mock private JcrNodeModel documentNodeModel;
    @Mock private JcrPropertyValueModel fieldModel;
    @Mock private IPluginConfig pluginConfig;
    @Mock private IPluginContext pluginContext;
    @Mock private IModel<String> violationMessageTranslationModel;

    private BlankStaticDropdownSelectionFieldValidator blankStaticDropdownSelectionFieldValidator;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        given(pluginConfig.getName()).willReturn("someTestConfig");

        initialiseWicketApplication();

        blankStaticDropdownSelectionFieldValidator = new BlankStaticDropdownSelectionFieldValidator(
            pluginContext,
            pluginConfig
        );
    }

    @Test
    public void reportsError_whenCalledForFieldOfUnsupportedType() throws Exception {

        // given
        programMocksForPreValidationTest();
        reset(typeDescriptor);

        final String unsupportedFieldTypeName = "unsupportedFieldTypeName";
        given(typeDescriptor.getType()).willReturn(unsupportedFieldTypeName);

        expectedException.expect(ValidationException.class);
        expectedException.expectMessage(format(
            "Cannot validate field ''{0}'' of type ''{1}''. This validator only supports fields of type ''{2}''.",
            fieldValidator.getFieldDescriptor().getPath(),
            unsupportedFieldTypeName,
            SUPPORTED_FIELD_TYPE_NAME
        ));

        // when
        blankStaticDropdownSelectionFieldValidator.preValidation(fieldValidator);

        // then
        // expectations as specified in 'given'
    }

    @Test
    public void reportsError_whenFieldNameParameterMissing() throws Exception {

        // given
        programMocksForPreValidationTest();
        reset(pluginConfig);

        expectedException.expect(ValidationException.class);
        expectedException.expectMessage(format(
            "Required parameter ''{0}'' is missing.",
            FIELD_DISPLAY_NAME_KEY
        ));

        // when
        blankStaticDropdownSelectionFieldValidator.preValidation(fieldValidator);

        // then
        // expectations as specified in 'given'
    }

    @Test
    public void reportsValidationViolation_forBlankFieldValue() throws Exception {

        // given
        final Violation expectedViolation = new Violation(Collections.emptySet(), violationMessageTranslationModel);

        final String blankFieldValue = "";
        final Value valueObject = mock(Value.class);
        given(valueObject.getString()).willReturn(blankFieldValue);
        given(fieldModel.getValue()).willReturn(valueObject);

        given(fieldValidator.newValueViolation(eq(fieldModel), isA(IModel.class)))
            .willReturn(expectedViolation);

        // when
        final Set<Violation> actualViolations =
            blankStaticDropdownSelectionFieldValidator.validate(fieldValidator, documentNodeModel, fieldModel);

        // then
        then(valueObject).should().getString();

        assertThat("Exactly one violation has been reported.", actualViolations, hasSize(1));

        final Violation actualViolation = actualViolations.iterator().next();
        assertThat("Violation is not null", actualViolation, notNullValue());
        assertThat("Correct violation has been reported.", actualViolation.getMessage(),
            is(violationMessageTranslationModel));
    }

    @Test
    public void reportsNoValidationViolations_forPopulatedField() throws Exception {

        // given
        final Value valueObject = mock(Value.class);
        given(valueObject.getString()).willReturn("a non blank value");
        given(fieldModel.getValue()).willReturn(valueObject);

        // when
        final Set<Violation> actualValidationViolations =
            blankStaticDropdownSelectionFieldValidator.validate(fieldValidator, documentNodeModel, fieldModel);

        // then
        then(valueObject).should().getString();
        assertThat("No violation has been reported.", actualValidationViolations, is(empty()));
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


    /**
     * Program common mocks with default, valid behaviours.
     * Test methods that need to test error conditions may reset and then reprogram individual mocks as needed.
     */
    private void programMocksForPreValidationTest() {

        given(pluginConfig.containsKey(FIELD_DISPLAY_NAME_KEY)).willReturn(true);

        given(fieldValidator.getFieldType()).willReturn(typeDescriptor);
        given(typeDescriptor.getType()).willReturn(SUPPORTED_FIELD_TYPE_NAME);

        given(fieldValidator.getFieldDescriptor()).willReturn(fieldDescriptor);
        given(fieldDescriptor.getPath()).willReturn(newRandomString());
    }

    private String newRandomString() {
        return UUID.randomUUID().toString();
    }
}
