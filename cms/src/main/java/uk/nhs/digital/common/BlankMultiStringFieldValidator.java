package uk.nhs.digital.common;

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

/**
 * Hippo CMS provides mandatory option for text fields.  Whilst this works for
 * single fields in documents, it doesnt work if the text field exists within a
 * compound field where multiple items allowed.  This validator ensures if the user
 * clicks on the +Add button, that the text entry has been populated. One example
 * in this project is the friendlyUrls compound field found within many document
 * types such as General, Service etc.
 */
public class BlankMultiStringFieldValidator extends AbstractCmsValidator {

    private static final String FIELD_TYPE = "String";

    @SuppressWarnings("WeakerAccess") // Hippo CMS requires the constructor to be public
    public BlankMultiStringFieldValidator(final IPluginContext context, final IPluginConfig config) {
        super(context, config);
    }

    @Override
    public void preValidation(final IFieldValidator fieldValidator) throws ValidationException {

        final String actualFieldTypeName = fieldValidator.getFieldType().getType();

        if (!FIELD_TYPE.equals(actualFieldTypeName)) {
            throw new ValidationException(format(
                "Cannot validate field. Expected field of type ''{0}'' but got ''{1}''.",
                FIELD_TYPE, actualFieldTypeName)
            );
        }
    }

    @Override
    public Set<Violation> validate(final IFieldValidator fieldValidator,
                                   final JcrNodeModel documentModel,
                                   final IModel model) throws ValidationException {

        final Set<Violation> violations = new HashSet<>();

        String value = (String) model.getObject();
        if (StringUtils.isBlank(value)) {
            violations.add(fieldValidator.newValueViolation(model, getTranslation()));
        }

        return violations;
    }
}
