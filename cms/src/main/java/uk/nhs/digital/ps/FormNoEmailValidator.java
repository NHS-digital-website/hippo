package uk.nhs.digital.ps;

import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.Validator;
import org.onehippo.cms.services.validation.api.Violation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

/**
 * Validates that a textfield with field name "email" is entered for Gov Delivery API forms.
 */
public class FormNoEmailValidator implements Validator<Node> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FormNoEmailValidator.class);

    @Override
    public Optional<Violation> validate(ValidationContext validationContext, Node node) {

        try {
            final boolean apiEnabled = Boolean.parseBoolean(node.getProperty("eforms:govdeliveryapi").getString());
            final boolean apiScriptServiceEnabled = Boolean.parseBoolean(node.getProperty("eforms:govdeliveryScriptService").getString());
            if (apiEnabled || apiScriptServiceEnabled) {
                boolean emailFieldFound = false;
                final NodeIterator pageNodes = node.getNodes("page*");
                while (pageNodes.hasNext() && !emailFieldFound) {
                    final NodeIterator fieldNodes = pageNodes.nextNode().getNodes();
                    while (fieldNodes.hasNext() && !emailFieldFound) {
                        final Node fieldNode = fieldNodes.nextNode();
                        if (fieldNode.hasProperty("eforms:fieldname") && fieldNode.isNodeType("eforms:textfield")) {
                            final String fieldName = fieldNode.getProperty("eforms:fieldname").getString();
                            if (fieldName.equals("email")) {
                                emailFieldFound = true;
                            }
                        }
                    }
                }
                if (!emailFieldFound) {
                    return Optional.of(validationContext.createViolation());
                }
            }
        } catch (RepositoryException e) {
            LOGGER.error("Error occured while validating form", e);
        }
        return Optional.empty();
    }
}
