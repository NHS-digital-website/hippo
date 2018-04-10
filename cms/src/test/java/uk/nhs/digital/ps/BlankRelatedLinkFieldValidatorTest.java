package uk.nhs.digital.ps;


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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import javax.jcr.Node;
import javax.jcr.Property;

public class BlankRelatedLinkFieldValidatorTest {

    private static final String HIPPO_RELATED_LINK_FIELD_TYPE_NAME = "publicationsystem:relatedlink";
    private static final String HIPPO_RELATED_LINK_URL_PROPERTY_NAME = "publicationsystem:linkUrl";
    private static final String VALID_URL = "http://digital.nhs.uk";

    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Mock private IFieldValidator fieldValidator;
    @Mock private ITypeDescriptor typeDescriptor;
    @Mock private JcrNodeModel documentNodeModel;
    @Mock private JcrNodeModel relatedLinkNodeModel;
    @Mock private IPluginConfig pluginConfig;
    @Mock private IPluginContext pluginContext;
    @Mock private IModel<String> violationMessageTranslationModel;

    private BlankRelatedLinkFieldValidator blankRelatedLinkFieldValidator;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        given(pluginConfig.getName()).willReturn("SomeTestConfig");

        initialiseWicketApplication();

        blankRelatedLinkFieldValidator = new BlankRelatedLinkFieldValidator(
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
            MessageFormat.format("Cannot validate the related link field. Expected field of type ''{0}'' but got ''{1}''.",
                HIPPO_RELATED_LINK_FIELD_TYPE_NAME, bogusFieldTypeName));

        // when
        blankRelatedLinkFieldValidator.preValidation(fieldValidator);

        // then
        // expectations as specified in 'given'
    }

    @Test
    public void reportsValidationViolation_forBlankRelatedLinkUrlFieldValue() throws Exception {

        // given
        final Violation expectedViolation = new Violation(Collections.emptySet(), violationMessageTranslationModel);

        final Node relatedLinkNode = mock(Node.class);
        given(relatedLinkNodeModel.getNode()).willReturn(relatedLinkNode);

        final Property relatedLinkUrlProperty = mock(Property.class);
        given(relatedLinkUrlProperty.getString()).willReturn("");

        given(relatedLinkNode.getProperty(HIPPO_RELATED_LINK_URL_PROPERTY_NAME)).willReturn(relatedLinkUrlProperty);

        given(fieldValidator.newValueViolation(eq(relatedLinkNodeModel), isA(IModel.class)))
            .willReturn(expectedViolation);

        // when
        final Set<Violation> actualValidationViolations =
            blankRelatedLinkFieldValidator.validate(fieldValidator, documentNodeModel,relatedLinkNodeModel);

        // then
        then(relatedLinkNode).should().getProperty(HIPPO_RELATED_LINK_URL_PROPERTY_NAME);

        assertThat("Exactly one violation has been reported", actualValidationViolations, hasSize(1));

        final Violation actualViolation = actualValidationViolations.iterator().next();
        assertThat("Violation is not null", actualViolation, notNullValue());
        assertThat("Correct violation has been reported.",actualViolation.getMessage(),
            is(violationMessageTranslationModel));
    }

    @Test
    public void reportsNoValidationViolations_forPopulatedLinkUrlField() throws Exception {

        // given
        final Node relatedLinkNode = mock(Node.class);
        given(relatedLinkNodeModel.getNode()).willReturn(relatedLinkNode);

        final Property relatedLinkUrlProperty = mock(Property.class);
        given(relatedLinkUrlProperty.getString()).willReturn(VALID_URL);

        given(relatedLinkNode.getProperty(HIPPO_RELATED_LINK_URL_PROPERTY_NAME)).willReturn(relatedLinkUrlProperty);

        // when
        final Set<Violation> actualValidationViolations =
            blankRelatedLinkFieldValidator.validate(fieldValidator, documentNodeModel, relatedLinkNodeModel);

        // then
        then(relatedLinkNode).should().getProperty(HIPPO_RELATED_LINK_URL_PROPERTY_NAME);

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
