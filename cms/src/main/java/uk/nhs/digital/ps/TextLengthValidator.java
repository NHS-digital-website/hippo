package uk.nhs.digital.ps;

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

public class TextLengthValidator extends AbstractCmsValidator {
    private static final String MAX_LENGTH_KEY = "max_length";
    private final int maxLength;


    public TextLengthValidator(IPluginContext context, IPluginConfig config)
        throws Exception {
        super(context, config);
        if (config.containsKey(MAX_LENGTH_KEY)) {
            try {
                maxLength = Integer.valueOf(config.getString(MAX_LENGTH_KEY));
            } catch (NumberFormatException ex) {
                throw new Exception("Property '" + MAX_LENGTH_KEY + "' value '"
                    + config.getString(MAX_LENGTH_KEY) + "' not recognised as an integer", ex);
            }
        } else {
            throw new Exception("Property '" + MAX_LENGTH_KEY + "' must be specified");
        }
    }

    @Override
    public void preValidation(IFieldValidator type) throws ValidationException {
        if (!"String".equals(type.getFieldType().getType())) {
            throw new ValidationException("Invalid validation exception; cannot validate non-string field for length");
        }
    }

    @Override
    public Set<Violation> validate(IFieldValidator fieldValidator,
                                   JcrNodeModel model, IModel childModel)
        throws ValidationException {
        Set<Violation> violations = new HashSet<>();
        String value = (String) childModel.getObject();
        if (value.length() > maxLength) {
            violations.add(fieldValidator.newValueViolation(childModel, getTranslation()));
        }
        return violations;
    }
}
