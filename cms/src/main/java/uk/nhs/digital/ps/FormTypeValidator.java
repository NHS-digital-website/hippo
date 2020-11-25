package uk.nhs.digital.ps;

import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.Validator;
import org.onehippo.cms.services.validation.api.Violation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * Validates that only mail or api form delivery is selected
 */
public class FormTypeValidator implements Validator<Node> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FormTypeValidator.class);

    @Override
    public Optional<Violation> validate(ValidationContext validationContext, Node node) {
        try {
            final boolean mailDelivery = Boolean.parseBoolean(node.getProperty("eforms:mailformdata").getString());
            final boolean apiDelivery = Boolean.parseBoolean(node.getProperty("eforms:govdeliveryapi").getString());

            if (mailDelivery && apiDelivery) {
                return Optional.of(validationContext.createViolation());
            }
        } catch (RepositoryException e) {
            LOGGER.error("Error while validating form", e);
        }

        return Optional.empty();
    }
}
