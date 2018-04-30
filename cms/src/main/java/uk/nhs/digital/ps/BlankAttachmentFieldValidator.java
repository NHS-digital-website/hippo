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

import java.util.HashSet;
import java.util.Set;
import javax.jcr.RepositoryException;

public class BlankAttachmentFieldValidator extends AbstractCmsValidator {

    private static final String HIPPO_FILENAME_PROPERTY_NAME = "hippo:filename";
    private static final String DEFAULT_ATTACHMENT_NAME_WHEN_NO_FILE_UPLOADED = "externalstorage:resource";

    @SuppressWarnings("WeakerAccess") // Hippo CMS requires the constructor to be public
    public BlankAttachmentFieldValidator(final IPluginContext context, final IPluginConfig config) {
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
            violations.add(fieldValidator.newValueViolation(currentAttachmentModel, getTranslation()));
        }

        return violations;
    }

    private boolean noFileUploadedToCurrentField(final IModel fileUploadField) throws ValidationException {

        final String fileName = extractFileName(fileUploadField);

        return DEFAULT_ATTACHMENT_NAME_WHEN_NO_FILE_UPLOADED.equals(fileName);
    }

    private String extractFileName(final IModel fileUploadField) throws ValidationException {
        try {
            return ((JcrNodeModel)fileUploadField).getNode().getProperty(HIPPO_FILENAME_PROPERTY_NAME).getString();
        } catch (final RepositoryException repositoryException) {
            throw new ValidationException("Failed to read attachement's name.", repositoryException);
        }
    }
}
