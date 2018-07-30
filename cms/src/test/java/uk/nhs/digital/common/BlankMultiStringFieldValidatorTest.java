package uk.nhs.digital.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.initMocks;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.hippoecm.frontend.model.JcrNodeModel;
import org.hippoecm.frontend.model.properties.JcrPropertyValueModel;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.types.ITypeDescriptor;
import org.hippoecm.frontend.validation.IFieldValidator;
import org.hippoecm.frontend.validation.ValidationException;
import org.hippoecm.frontend.validation.Violation;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public class BlankMultiStringFieldValidatorTest {

    private static final String VALID_FIELD_TYPE = "String";

    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Mock private IFieldValidator fieldValidator;
    @Mock private ITypeDescriptor typeDescriptor;
    @Mock private JcrNodeModel documentNodeModel;
    @Mock private JcrPropertyValueModel fieldModel;
    @Mock private IPluginConfig pluginConfig;
    @Mock private IPluginContext pluginContext;
    @Mock private IModel<String> violationMessageTranslationModel;

    private BlankMultiStringFieldValidator blankMultiStringFieldValidator;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        given(pluginConfig.getName()).willReturn("SomeTestConfig");

        initialiseWicketApplication();

        blankMultiStringFieldValidator = new BlankMultiStringFieldValidator(
            pluginContext,
            pluginConfig
        );
    }

    @Test
    public void reportsError_whenCalledForInvalidFieldType() throws Exception {

        // given
        given(fieldValidator.getFieldType()).willReturn(typeDescriptor);
        final String bogusFieldTypeName = newRandomString();
        given(typeDescriptor.getType()).willReturn(bogusFieldTypeName);

        expectedException.expect(ValidationException.class);
        expectedException.expectMessage(
            MessageFormat.format("Cannot validate field. Expected field of type ''{0}'' but got ''{1}''.",
                VALID_FIELD_TYPE, bogusFieldTypeName));

        // when
        blankMultiStringFieldValidator.preValidation(fieldValidator);

        // then
        // expectations as specified in 'given'
    }

    @Test
    public void reportsValidationViolation_forBlankStringFieldValue() throws Exception {

        // given
        final Violation expectedViolation = new Violation(Collections.emptySet(), violationMessageTranslationModel);

        given(fieldModel.getObject()).willReturn("");
        given(fieldValidator.newValueViolation(eq(fieldModel), isA(IModel.class)))
            .willReturn(expectedViolation);

        // when
        final Set<Violation> actualViolations =
            blankMultiStringFieldValidator.validate(fieldValidator, documentNodeModel, fieldModel);

        // then
        then(fieldModel).should().getObject();
        assertThat("Exactly one violation has been reported.", actualViolations, hasSize(1));

        final Violation actualViolation = actualViolations.iterator().next();
        assertThat("Violation is not null", actualViolation, notNullValue());
        assertThat("Correct violation has been reported.", actualViolation.getMessage(),
            is(violationMessageTranslationModel));
    }

    @Test
    public void reportsNoValidationViolations_forPopulatedStringField() throws Exception {

        // given
        given(fieldModel.getObject()).willReturn("a non blank value");

        // when
        final Set<Violation> actualValidationViolations =
            blankMultiStringFieldValidator.validate(fieldValidator, documentNodeModel, fieldModel);

        // then
        then(fieldModel).should().getObject();
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

    private String newRandomString() {
        return UUID.randomUUID().toString();
    }
}
