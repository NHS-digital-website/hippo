package uk.nhs.digital.ps.validators;

import org.hippoecm.frontend.validation.ValidationException;
import org.hippoecm.repository.impl.NodeDecorator;
import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.Validator;
import org.onehippo.cms.services.validation.api.Violation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;


public class AttachmentFieldValidator implements Validator<NodeDecorator> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttachmentFieldValidator.class);

    private static final String HIPPO_FILENAME_PROPERTY_NAME = "hippo:filename";
    private static final String DEFAULT_ATTACHMENT_NAME_WHEN_NO_FILE_UPLOADED = "externalstorage:resource";
    private static final String HIPPO_ATTACHMENT_PRIMARY_NODE_TYPE = "publicationsystem:extattachment";
    private static final String HIPPO_ATTACHMENT_DISPLAY_NAME_PROPERTY_NAME = "publicationsystem:displayName";


    @Override
    public Optional<Violation> validate(ValidationContext context, NodeDecorator value) {

        final Node configNode = context.getDocumentNode();
        try {
            if (noFileUploadedToAttachmentFields(configNode)) {
                return Optional.of(context.createViolation("blank-attachment"));
            } else {
                // only perform check if file has been uploaded
                if (duplicateAttachmentDisplayNameExists(configNode)) {
                    return Optional.of(context.createViolation("duplicate-name"));
                }
            }
        } catch (ValidationException e) {
            LOGGER.error("Error occured during validation: ", e);
        }

        return Optional.empty();
    }

    private boolean noFileUploadedToAttachmentFields(final Node configNode) throws ValidationException {

        try {
            final NodeIterator nodeIterator = configNode.getNodes();
            String filename = null;
            while (nodeIterator.hasNext()) {
                final Node node = nodeIterator.nextNode();

                if (node.getPrimaryNodeType().getName().equals(HIPPO_ATTACHMENT_PRIMARY_NODE_TYPE)) {
                    final NodeIterator extAttachmentNodeIterator = node.getNodes();
                    while (extAttachmentNodeIterator.hasNext()) {
                        final Node nextNode = extAttachmentNodeIterator.nextNode();
                        if (nextNode.getPrimaryNodeType().getName().equals(DEFAULT_ATTACHMENT_NAME_WHEN_NO_FILE_UPLOADED)) {
                            filename = nextNode.getProperty(HIPPO_FILENAME_PROPERTY_NAME).getString();
                        }
                    }
                }
            }

            return filename != null && filename.equals(DEFAULT_ATTACHMENT_NAME_WHEN_NO_FILE_UPLOADED);

        } catch (RepositoryException repositoryException) {
            throw new ValidationException("Failed to read attachment's filename", repositoryException);
        }
    }

    private boolean duplicateAttachmentDisplayNameExists(final Node configNode) throws ValidationException {

        try {
            List<String> documentAttachmentNames = new ArrayList<>();
            final NodeIterator nodeIterator = configNode.getNodes();
            while (nodeIterator.hasNext()) {
                final Node node = nodeIterator.nextNode();

                if (node.getPrimaryNodeType().getName().equals(HIPPO_ATTACHMENT_PRIMARY_NODE_TYPE)) {
                    final String displayName = node.getProperty(HIPPO_ATTACHMENT_DISPLAY_NAME_PROPERTY_NAME).getString().toLowerCase();

                    if (displayName.isEmpty()) {
                        continue;
                    }
                    if (!documentAttachmentNames.contains(displayName)) {
                        documentAttachmentNames.add(displayName);
                    } else {
                        return true;
                    }
                }
            }
        } catch (RepositoryException repositoryException) {
            throw new ValidationException("Failed to read attachment's display name.", repositoryException);
        }
        return false;
    }
}
