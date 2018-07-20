package uk.nhs.digital.ps;

import static java.text.MessageFormat.format;
import static uk.nhs.digital.externalstorage.ExternalStorageConstants.NODE_TYPE_EXTERNAL_RESOURCE;

import org.apache.wicket.model.IModel;
import org.hippoecm.frontend.editor.validator.plugins.AbstractCmsValidator;
import org.hippoecm.frontend.model.JcrNodeModel;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.validation.IFieldValidator;
import org.hippoecm.frontend.validation.ValidationException;
import org.hippoecm.frontend.validation.Violation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.jcr.*;

public class AttachmentFieldValidator extends AbstractCmsValidator {

    private static final String HIPPO_FILENAME_PROPERTY_NAME = "hippo:filename";
    private static final String DEFAULT_ATTACHMENT_NAME_WHEN_NO_FILE_UPLOADED = "externalstorage:resource";
    private static final String HIPPO_ATTACHMENT_PRIMARY_NODE_TYPE = "publicationsystem:extattachment";
    private static final String HIPPO_ATTACHMENT_DISPLAY_NAME_PROPERTY_NAME = "publicationsystem:displayName";

    @SuppressWarnings("WeakerAccess") // Hippo CMS requires the constructor to be public
    public AttachmentFieldValidator(final IPluginContext context, final IPluginConfig config) {
        super(context, config);
    }

    @Override
    public void preValidation(final IFieldValidator fieldValidator) throws ValidationException {

        final String actualFieldTypeName = fieldValidator.getFieldType().getType();

        if (!NODE_TYPE_EXTERNAL_RESOURCE.equals(actualFieldTypeName)) {
            throw new ValidationException(format(
                "Cannot validate the attachment field. Expected field of type ''{0}'' but got ''{1}''.",
                NODE_TYPE_EXTERNAL_RESOURCE, actualFieldTypeName)
            );
        }
    }

    @Override
    public Set<Violation> validate(final IFieldValidator fieldValidator,
                                   final JcrNodeModel documentModel,
                                   final IModel currentAttachmentModel) throws ValidationException {

        final Set<Violation> violations = new HashSet<>();

        if (noFileUploadedToCurrentField(currentAttachmentModel)) {
            violations.add(fieldValidator.newValueViolation(currentAttachmentModel, getTranslation("blank-attachment")));
        } else {

            // only perform check if file has been uploaded
            if (duplicateAttachmentDisplayNameExists(currentAttachmentModel)) {
                violations.add(fieldValidator.newValueViolation(currentAttachmentModel, getTranslation("duplicate-name")));
            }
        }

        return violations;
    }

    private boolean noFileUploadedToCurrentField(final IModel fileUploadField) throws ValidationException {

        final String fileName = extractFileName(fileUploadField);

        return DEFAULT_ATTACHMENT_NAME_WHEN_NO_FILE_UPLOADED.equals(fileName);
    }

    private boolean duplicateAttachmentDisplayNameExists(final IModel fileUploadField) throws ValidationException {

        List<String> documentAttachmentNames = new ArrayList<>();

        try {
            // Attachments are stored in JCR with the following structure (2 attachments in example)
            //
            // Document NODE
            //      -> publicationsystem:Attachments-v3 (publicationsystem:extattachment) NODE
            //              -> publicationsystem:attachmentResource (externalstorage:resource) NODE
            //      -> publicationsystem:Attachments-v3 (publicationsystem:extattachment) NODE
            //              -> publicationsystem:attachmentResource (externalstorage:resource) NODE
            //
            // fileUploadField represents the bottom level, so traverse up to get document node,
            // then getNodes() to find ALL attachments in order to compare the display names to look for duplicates
            final JcrNodeModel documentNodeModel = ((JcrNodeModel)fileUploadField).getParentModel().getParentModel();
            final NodeIterator iterator = documentNodeModel.getNode().getNodes();

            while (iterator.hasNext()) {
                final Node node = iterator.nextNode();

                // Only want attachments nodes
                if (node.getPrimaryNodeType().getName().equals(HIPPO_ATTACHMENT_PRIMARY_NODE_TYPE)) {
                    final String displayName = node.getProperty(HIPPO_ATTACHMENT_DISPLAY_NAME_PROPERTY_NAME)
                        .getString()
                        .toLowerCase();

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
            throw new ValidationException("Failed to read attachement's display name.", repositoryException);
        }

        return false;
    }

    private String extractFileName(final IModel fileUploadField) throws ValidationException {
        try {
            return ((JcrNodeModel)fileUploadField).getNode().getProperty(HIPPO_FILENAME_PROPERTY_NAME).getString();
        } catch (final RepositoryException repositoryException) {
            throw new ValidationException("Failed to read attachement's name.", repositoryException);
        }
    }
}
