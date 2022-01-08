package org.hippoecm.frontend.plugins.cms.admin.updater

import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.RepositoryException

class ApiSpecificationHtmlPropertyRemover extends BaseNodeUpdateVisitor {

    public static final String API_SPEC_DOCTYPE = "website:apispecification"
    public static final String HTML_PROPERTY_NAME = "website:html"

    @Override
    boolean doUpdate(Node node) {

        log.info("---")
        log.info("Invoked for node: ${node.path}")

        if (invalidNodeType(node)) {
            log.error("Can only be invoked for nodes of type $API_SPEC_DOCTYPE but actual node type was ${jcrPrimaryType(node)}. Backing off, node will not be updated.")
            return false
        }

        try {
            return removeHtmlPropertyFrom(node)
        } catch (e) {
            log.error("Failed to remove property `$HTML_PROPERTY_NAME` from ${node.path}.", e)
            throw e
        }
    }

    @Override
    boolean undoUpdate(Node node) throws RepositoryException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Removal of the property failed; there is nothing that can be reverted.")
    }

    private boolean removeHtmlPropertyFrom(Node node) {

        JcrUtils.ensureIsCheckedOut(node)

        if (node.hasProperty(HTML_PROPERTY_NAME)) {
            log.info("Property is present, removing...")
            node.getProperty(HTML_PROPERTY_NAME).remove()
            log.info("Done.")

            return true
        } else {
            log.info("Property is absent; nothing to do, backing off.")
            return false
        }
    }

    private boolean invalidNodeType(Node node) {
        return API_SPEC_DOCTYPE != jcrPrimaryType(node)
    }

    @Override
    void destroy() {
        log.info("---")
    }

    private String jcrPrimaryType(Node node) {
        return node.getProperty("jcr:primaryType").value.getString()
    }
}
