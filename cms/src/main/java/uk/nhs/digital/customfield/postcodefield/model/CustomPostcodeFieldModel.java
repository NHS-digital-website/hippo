package uk.nhs.digital.customfield.postcodefield.model;

import com.onehippo.cms7.eforms.cms.model.AbstractFieldModel;
import org.hippoecm.frontend.model.JcrNodeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

public class CustomPostcodeFieldModel extends AbstractFieldModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomPostcodeFieldModel.class);
    private static final String PROPERTY_AUTOCOMPLETE = "autocomplete";
    private String autocomplete = "off";

    public CustomPostcodeFieldModel(JcrNodeModel nodeModel) throws RepositoryException {
        super(nodeModel);

        // Load autocomplete property from JCR node if it exists
        Node node = getNode();
        if (node.hasProperty(PROPERTY_AUTOCOMPLETE)) {
            autocomplete = node.getProperty(PROPERTY_AUTOCOMPLETE).getString();
        }
    }

    @Override
    public String getFieldType() {
        return "custompostcodefield";
    }

    public String getAutocomplete() {
        return autocomplete;
    }

    public void setAutocomplete(String autocomplete) {
        this.autocomplete = autocomplete;

        // Save autocomplete property to JCR node
        try {
            Node node = getNode();
            node.setProperty(PROPERTY_AUTOCOMPLETE, autocomplete);
        } catch (RepositoryException e) {
            LOGGER.error("Failed to save autocomplete property to JCR node.", e);
        }
    }
}