package uk.nhs.digital.ps;

import static java.text.MessageFormat.format;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.hippoecm.frontend.editor.validator.plugins.AbstractCmsValidator;
import org.hippoecm.frontend.l10n.ResourceBundleModel;
import org.hippoecm.frontend.model.JcrNodeModel;
import org.hippoecm.frontend.model.properties.JcrPropertyValueModel;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.validation.IFieldValidator;
import org.hippoecm.frontend.validation.ValidationException;
import org.hippoecm.frontend.validation.Violation;

import java.util.*;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

public class StaticDropdownSelectionFieldValidator extends AbstractCmsValidator {

    private static final String SUPPORTED_FIELD_TYPE_NAME = "StaticDropdown";
    private static final String FIELD_DISPLAY_NAME_KEY = "fieldDisplayName";
    private static final String BLANK_VALUE_MESSAGE_TEMPLATE_KEY = "publicationsystem-blank-staticdropdown";
    private static final String DUPLICATE_VALUE_MESSAGE_TEMPLATE_KEY = "publicationsystem-duplicate-staticdropdown";
    private static final String VALIDATORS_TRANSLATION_KEY = "hippo:cms.validators";

    @SuppressWarnings("WeakerAccess") // Hippo CMS requires the constructor to be public
    public StaticDropdownSelectionFieldValidator(final IPluginContext pluginContext, final IPluginConfig pluginConfig) {
        super(pluginContext, pluginConfig);
    }

    @Override
    public void preValidation(final IFieldValidator fieldValidator) throws ValidationException {

        if (!getPluginConfig().containsKey(FIELD_DISPLAY_NAME_KEY)) {
            throw new ValidationException(format("Required parameter ''{0}'' is missing.", FIELD_DISPLAY_NAME_KEY));
        }

        final String actualFieldTypeName = fieldValidator.getFieldDescriptor().getTypeDescriptor().getName();

        if (!SUPPORTED_FIELD_TYPE_NAME.equals(actualFieldTypeName)) {
            throw new ValidationException(format(
                "Cannot validate field ''{0}'' of type ''{1}''. This validator only supports fields of type ''{2}''.",
                fieldValidator.getFieldDescriptor().getPath(),
                actualFieldTypeName,
                SUPPORTED_FIELD_TYPE_NAME
            ));
        }
    }

    @Override
    public Set<Violation> validate(final IFieldValidator fieldValidator,
                                   final JcrNodeModel documentModel,
                                   final IModel fieldModel) throws ValidationException {

        final Set<Violation> violations = new HashSet<>();

        if (noValueSelectedInCurrentField(fieldModel)) {
            violations.add(fieldValidator.newValueViolation(fieldModel, getViolationMessage(getViolationMessageTemplate(BLANK_VALUE_MESSAGE_TEMPLATE_KEY))));
        }

        if (hasDuplicateValues((JcrPropertyValueModel) fieldModel)) {
            violations.add(fieldValidator.newValueViolation(fieldModel, getViolationMessage(getViolationMessageTemplate(DUPLICATE_VALUE_MESSAGE_TEMPLATE_KEY))));
        }

        return violations;
    }

    private boolean hasDuplicateValues(JcrPropertyValueModel fieldModel) throws ValidationException {
        try {
            List<Value> values = Arrays.asList(fieldModel.getJcrPropertymodel().getProperty().getValues());

            return values.stream().anyMatch(value -> Collections.frequency(values, value) > 1);
        } catch (RepositoryException e) {
            throw new ValidationException("Failed to validate duplicate property values.", e);
        }
    }

    private boolean noValueSelectedInCurrentField(final IModel fieldModel) throws ValidationException {

        final String selectedValue = extractSelectedValue(fieldModel);

        return StringUtils.isBlank(selectedValue);
    }

    private String extractSelectedValue(final IModel fieldModel) throws ValidationException {
        try {
            return ((JcrPropertyValueModel) fieldModel).getValue().getString();
        } catch (final RepositoryException repositoryException) {
            throw new ValidationException("Failed to read field's value.", repositoryException);
        }
    }

    private IModel<String> getViolationMessage(IModel<String> messageTemplate) {
        return new IModel<String>() {

            @Override
            public void detach() {
                messageTemplate.detach();
            }

            @Override
            public String getObject() {
                return resolvePlaceholders(messageTemplate.getObject());
            }

            @Override
            public void setObject(final String object) {
                messageTemplate.setObject(object);
            }
        };
    }

    private IModel<String> getViolationMessageTemplate(String violationMessageTemplateKey) {
        return new ResourceBundleModel(
            VALIDATORS_TRANSLATION_KEY,
            violationMessageTemplateKey,
            Session.get().getLocale()
        );
    }

    private String resolvePlaceholders(final String violationMessageTemplate) {
        return format(violationMessageTemplate, getPluginConfig().getString(FIELD_DISPLAY_NAME_KEY));
    }
}
