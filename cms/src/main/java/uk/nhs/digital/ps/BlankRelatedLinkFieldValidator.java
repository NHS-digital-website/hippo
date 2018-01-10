package uk.nhs.digital.ps;

import static java.text.MessageFormat.format;

import org.apache.commons.lang.StringUtils;
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

public class BlankRelatedLinkFieldValidator extends AbstractCmsValidator {

    private static final String HIPPO_RELATED_LINK_FIELD_TYPE_NAME = "publicationsystem:relatedlink";
    private static final String HIPPO_RELATED_LINK_URL_PROPERTY_NAME = "publicationsystem:linkUrl";

    @SuppressWarnings("WeakerAccess") // Hippo CMS requires the constructor to be public
    public BlankRelatedLinkFieldValidator(final IPluginContext context, final IPluginConfig config) {
        super(context, config);
    }

    @Override
    public void preValidation(final IFieldValidator fieldValidator) throws ValidationException {

        final String actualFieldTypeName = fieldValidator.getFieldType().getType();

        if (!HIPPO_RELATED_LINK_FIELD_TYPE_NAME.equals(actualFieldTypeName)) {
            throw new ValidationException(format(
                "Cannot validate the related link field. Expected field of type ''{0}'' but got ''{1}''.",
                HIPPO_RELATED_LINK_FIELD_TYPE_NAME, actualFieldTypeName)
            );
        }
    }

    @Override
    public Set<Violation> validate(final IFieldValidator fieldValidator,
                                   final JcrNodeModel documentModel,
                                   final IModel currentRelatedLinkModel) throws ValidationException {

        final Set<Violation> violations = new HashSet<>();

        if (noRelatedLinkUrlAdded(currentRelatedLinkModel)) {
            violations.add(fieldValidator.newValueViolation(currentRelatedLinkModel, getTranslation()));
        }

        return violations;
    }

    private boolean noRelatedLinkUrlAdded(final IModel relatedLinkField) throws ValidationException {

        final String url = extractUrl(relatedLinkField);

        return StringUtils.isBlank(url);
    }

    private String extractUrl(final IModel relatedLinkField) throws ValidationException {
        try {
            return ((JcrNodeModel)relatedLinkField).getNode().getProperty(HIPPO_RELATED_LINK_URL_PROPERTY_NAME).getString();
        } catch (final RepositoryException repositoryException) {
            throw new ValidationException("Failed to read related link URL.", repositoryException);
        }
    }
}
