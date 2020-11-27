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
            boolean mailDelivery = false;
            if (node.hasProperty("eforms:mailformdata")) {
                mailDelivery = Boolean.parseBoolean(node.getProperty("eforms:mailformdata").getString());
            }

            boolean apiDelivery = false;
            if (node.hasProperty("eforms:govdeliveryapi")) {
                apiDelivery = Boolean.parseBoolean(node.getProperty("eforms:govdeliveryapi").getString());
            }

            boolean apiScriptServiceDelivery = false;
            if (node.hasProperty("eforms:govdeliveryScriptService")) {
                apiScriptServiceDelivery = Boolean.parseBoolean(node.getProperty("eforms:govdeliveryScriptService").getString());
            }

            if (mailDelivery && apiDelivery || mailDelivery && apiScriptServiceDelivery || apiScriptServiceDelivery && apiDelivery) {
                return Optional.of(validationContext.createViolation());
            }

        } catch (RepositoryException e) {
            LOGGER.error("Error while validating form", e);
        }

        return Optional.empty();
    }
}
