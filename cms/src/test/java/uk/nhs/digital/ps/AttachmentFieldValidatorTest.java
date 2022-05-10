package uk.nhs.digital.ps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.MockitoAnnotations.openMocks;

import org.hippoecm.repository.impl.NodeDecorator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.Violation;
import org.onehippo.repository.mock.MockNode;

import java.util.Optional;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

public class AttachmentFieldValidatorTest {

    private AttachmentFieldValidator attachmentFieldValidator;

    @Mock
    private ValidationContext validationContextMock;

    @Mock
    private NodeDecorator nodeDecoratorMock;

    @Mock
    private Violation violationMock;

    private static final String HIPPO_FILENAME_PROPERTY_NAME = "hippo:filename";
    private static final String DEFAULT_ATTACHMENT_NAME_WHEN_NO_FILE_UPLOADED = "externalstorage:resource";
    private static final String HIPPO_ATTACHMENT_PRIMARY_NODE_TYPE = "publicationsystem:extattachment";
    private static final String HIPPO_ATTACHMENT_DISPLAY_NAME_PROPERTY_NAME = "publicationsystem:displayName";


    @Before
    public void setUp() throws Exception {
        openMocks(this);

        attachmentFieldValidator = new AttachmentFieldValidator();
    }


    @Test
    public void reportsValidationViloation_forBlankUploadField() throws Exception {

        Node node = new MockNode("document", "doc");
        node.addNode("attachment", HIPPO_ATTACHMENT_PRIMARY_NODE_TYPE);
        node.addNode("attachment/resource", DEFAULT_ATTACHMENT_NAME_WHEN_NO_FILE_UPLOADED)
            .setProperty(HIPPO_FILENAME_PROPERTY_NAME, DEFAULT_ATTACHMENT_NAME_WHEN_NO_FILE_UPLOADED);

        given(validationContextMock.getDocumentNode()).willReturn(node);
        given(validationContextMock.createViolation("blank-attachment")).willReturn(violationMock);

        final Optional<Violation> violation = attachmentFieldValidator.validate(validationContextMock, nodeDecoratorMock);

        assertThat("Violation has been reported, blank upload", violation.isPresent());
        verify(validationContextMock).createViolation("blank-attachment");

    }


    @Test
    public void reportsValidationViloation_forDuplicateName() throws Exception {

        Node node = createNodeConfiguration(true);

        given(validationContextMock.getDocumentNode()).willReturn(node);
        given(validationContextMock.createViolation("duplicate-name")).willReturn(violationMock);

        final Optional<Violation> violation = attachmentFieldValidator.validate(validationContextMock, nodeDecoratorMock);

        assertThat("Violation has been reported, Duplicate display name", violation.isPresent());
        verify(validationContextMock).createViolation("duplicate-name");

    }

    @Test
    public void reportsNoViolation() throws Exception {

        Node node = createNodeConfiguration(false);

        given(validationContextMock.getDocumentNode()).willReturn(node);
        given(validationContextMock.createViolation("duplicate-name")).willReturn(violationMock);

        final Optional<Violation> violation = attachmentFieldValidator.validate(validationContextMock, nodeDecoratorMock);

        assertThat("Violation has not been reported", !violation.isPresent());

    }

    private Node createNodeConfiguration(boolean duplicateDisplayName) throws RepositoryException {
        String displayName1 = "displayname1";
        String displayName2 = "displayname2";

        if (duplicateDisplayName) {
            displayName1 = displayName2;
        }

        Node node = new MockNode("document", "doc");
        node.addNode("attachment", HIPPO_ATTACHMENT_PRIMARY_NODE_TYPE)
            .setProperty(HIPPO_FILENAME_PROPERTY_NAME, "duplicateFileName");
        node.addNode("attachment2", HIPPO_ATTACHMENT_PRIMARY_NODE_TYPE)
            .setProperty(HIPPO_FILENAME_PROPERTY_NAME, "duplicateFileName");
        node.addNode("attachment/resource", DEFAULT_ATTACHMENT_NAME_WHEN_NO_FILE_UPLOADED)
            .setProperty(HIPPO_FILENAME_PROPERTY_NAME, "another_filename");

        node.getNode("attachment").setProperty(HIPPO_ATTACHMENT_DISPLAY_NAME_PROPERTY_NAME, displayName1);
        node.getNode("attachment2").setProperty(HIPPO_ATTACHMENT_DISPLAY_NAME_PROPERTY_NAME, displayName2);

        return node;
    }
}
