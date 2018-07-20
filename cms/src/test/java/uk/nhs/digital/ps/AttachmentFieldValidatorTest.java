package uk.nhs.digital.ps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;
import static uk.nhs.digital.externalstorage.ExternalStorageConstants.NODE_TYPE_EXTERNAL_RESOURCE;

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
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.nodetype.NodeType;

public class AttachmentFieldValidatorTest {

    private static final String HIPPO_FILENAME_PROPERTY_NAME = "hippo:filename";
    private static final String DEFAULT_ATTACHMENT_NAME_WHEN_NO_FILE_UPLOADED = "externalstorage:resource";
    private static final String HIPPO_ATTACHMENT_PRIMARY_NODE_TYPE = "publicationsystem:extattachment";
    private static final String HIPPO_ATTACHMENT_DISPLAY_NAME_PROPERTY_NAME = "publicationsystem:displayName";

    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Mock private IFieldValidator fieldValidator;
    @Mock private ITypeDescriptor typeDescriptor;
    @Mock private JcrNodeModel documentNodeModel;
    @Mock private JcrNodeModel attachmentNodeModel;
    @Mock private JcrNodeModel externalStorageNodeModel;
    @Mock private IPluginConfig pluginConfig;
    @Mock private IPluginContext pluginContext;
    @Mock private IModel<String> violationMessageTranslationModel;
    @Mock private NodeIterator nodeIterator;
    @Mock private Node node;
    @Mock private Property property;

    private AttachmentFieldValidator attachmentFieldValidator;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        given(pluginConfig.getName()).willReturn("someTestConfig");

        initialiseWicketApplication();

        attachmentFieldValidator = new AttachmentFieldValidator(
            pluginContext,
            pluginConfig
        );

        given(externalStorageNodeModel.getParentModel()).willReturn(attachmentNodeModel);
        given(attachmentNodeModel.getParentModel()).willReturn(documentNodeModel);
        given(documentNodeModel.getNode()).willReturn(node);

        given(externalStorageNodeModel.getNode()).willReturn(node);
        given(node.getNodes()).willReturn(nodeIterator);
    }

    @Test
    public void reportsError_whenCalledForInvalidFieldType() throws Exception {

        // given
        given(fieldValidator.getFieldType()).willReturn(typeDescriptor);
        final String bogusFieldTypeName = newRandomString();
        given(typeDescriptor.getType()).willReturn(bogusFieldTypeName);

        expectedException.expect(ValidationException.class);
        expectedException.expectMessage(
            MessageFormat.format("Cannot validate the attachment field. Expected field of type ''{0}'' but got ''{1}''.",
                NODE_TYPE_EXTERNAL_RESOURCE, bogusFieldTypeName));

        // when
        attachmentFieldValidator.preValidation(fieldValidator);

        // then
        // expectations as specified in 'given'
    }

    @Test
    public void reportsValidationViloation_forBlankUploadField() throws Exception {

        // given
        final Violation expectedViolation = new Violation(Collections.emptySet(), violationMessageTranslationModel);

        final Node attachmentNode = mock(Node.class);
        given(attachmentNodeModel.getNode()).willReturn(attachmentNode);

        final Property attachmentFileNameProperty = mock(Property.class);
        given(attachmentFileNameProperty.getString()).willReturn(DEFAULT_ATTACHMENT_NAME_WHEN_NO_FILE_UPLOADED);

        given(attachmentNode.getProperty(HIPPO_FILENAME_PROPERTY_NAME)).willReturn(attachmentFileNameProperty);

        given(fieldValidator.newValueViolation(eq(attachmentNodeModel), isA(IModel.class)))
            .willReturn(expectedViolation);

        // when
        final Set<Violation> actualValidationViolations =
            attachmentFieldValidator.validate(fieldValidator, documentNodeModel, attachmentNodeModel);

        // then
        then(attachmentNode).should().getProperty(HIPPO_FILENAME_PROPERTY_NAME);

        assertThat("Exactly one violation has been reported.", actualValidationViolations, hasSize(1));

        final Violation actualViolation = actualValidationViolations.iterator().next();
        assertThat("Violation is not null", actualViolation, notNullValue());
        assertThat("Correct violation has been reported.", actualViolation.getMessage(),
            is(violationMessageTranslationModel));
    }

    @Test
    public void reportsValidationViolation_forDuplicateDisplayName() throws Exception {

        // given
        final Violation expectedViolation = new Violation(Collections.emptySet(), violationMessageTranslationModel);

        final String displayName = newRandomString();

        final Node attachmentOneNode = generateMockAttachmentNode(displayName);
        final Node attachmentTwoNode = generateMockAttachmentNode(displayName);
        given(node.getProperty(HIPPO_FILENAME_PROPERTY_NAME)).willReturn(property);

        given(nodeIterator.hasNext())
            .willReturn(true)
            .willReturn(true)
            .willReturn(false);
        given(nodeIterator.nextNode())
            .willReturn(attachmentOneNode)
            .willReturn(attachmentTwoNode);
        given(node.getNodes()).willReturn(nodeIterator);

        given(fieldValidator.newValueViolation(eq(externalStorageNodeModel), isA(IModel.class)))
            .willReturn(expectedViolation);

        // when
        final Set<Violation> actualValidationViolations =
            attachmentFieldValidator.validate(fieldValidator, documentNodeModel, externalStorageNodeModel);

        // then
        then(attachmentOneNode).should().getProperty(HIPPO_ATTACHMENT_DISPLAY_NAME_PROPERTY_NAME);
        then(attachmentTwoNode).should().getProperty(HIPPO_ATTACHMENT_DISPLAY_NAME_PROPERTY_NAME);

        assertThat("Exactly one violation has been reported.", actualValidationViolations, hasSize(1));

        final Violation actualViolation = actualValidationViolations.iterator().next();
        assertThat("Violation is not null", actualViolation, notNullValue());
        assertThat("Correct violation has been reported.", actualViolation.getMessage(),
            is(violationMessageTranslationModel));
    }

    @Test
    public void reportsNoValidationViolation_forUniqueDisplayNames() throws Exception {

        // given
        final Node attachmentOneNode = generateMockAttachmentNode(newRandomString());
        final Node attachmentTwoNode = generateMockAttachmentNode(newRandomString());
        given(node.getProperty(HIPPO_FILENAME_PROPERTY_NAME)).willReturn(property);

        given(nodeIterator.hasNext())
            .willReturn(true)
            .willReturn(true)
            .willReturn(false);
        given(nodeIterator.nextNode())
            .willReturn(attachmentOneNode)
            .willReturn(attachmentTwoNode);
        given(node.getNodes()).willReturn(nodeIterator);

        // when
        final Set<Violation> actualValidationViolations =
            attachmentFieldValidator.validate(fieldValidator, documentNodeModel, externalStorageNodeModel);

        // then
        then(attachmentOneNode).should().getProperty(HIPPO_ATTACHMENT_DISPLAY_NAME_PROPERTY_NAME);
        then(attachmentTwoNode).should().getProperty(HIPPO_ATTACHMENT_DISPLAY_NAME_PROPERTY_NAME);

        assertThat("No violation has been reported.", actualValidationViolations, is(empty()));
    }

    private Node generateMockAttachmentNode(String displayName) throws Exception {

        final Node attachmentNode = mock(Node.class);
        final NodeType nodeType = mock(NodeType.class);

        final Property attachmentFileNameProperty = mock(Property.class);
        given(attachmentFileNameProperty.getString()).willReturn(displayName);

        // Attachment filename
        given(attachmentNode.getProperty(HIPPO_FILENAME_PROPERTY_NAME)).willReturn(attachmentFileNameProperty);

        // Attachment display name
        given(attachmentNode.getProperty(HIPPO_ATTACHMENT_DISPLAY_NAME_PROPERTY_NAME)).willReturn(attachmentFileNameProperty);

        // Attachment type and name
        given(attachmentNode.getPrimaryNodeType()).willReturn(nodeType);
        given(nodeType.getName()).willReturn(HIPPO_ATTACHMENT_PRIMARY_NODE_TYPE);

        return attachmentNode;
    }

    @Test
    public void reportsNoValidationViolations_forPopulatedUploadField() throws Exception {

        // given
        final Node attachmentNode = mock(Node.class);

        given(externalStorageNodeModel.getNode()).willReturn(attachmentNode);
        given(externalStorageNodeModel.getNode().getNodes()).willReturn(nodeIterator);

        final Property attachmentFileNameProperty = mock(Property.class);
        final String validFileName = newRandomString();
        given(attachmentFileNameProperty.getString()).willReturn(validFileName);

        given(attachmentNode.getProperty(HIPPO_FILENAME_PROPERTY_NAME)).willReturn(attachmentFileNameProperty);

        // when
        final Set<Violation> actualValidationViolations =
            attachmentFieldValidator.validate(fieldValidator, documentNodeModel, externalStorageNodeModel);

        // then
        then(attachmentNode).should().getProperty(HIPPO_FILENAME_PROPERTY_NAME);

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
