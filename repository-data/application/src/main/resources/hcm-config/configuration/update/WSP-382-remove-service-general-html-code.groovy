package org.hippoecm.frontend.plugins.cms.admin.updater

import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.RepositoryException

class RemoveServiceGeneralHtmlCode extends BaseNodeUpdateVisitor {

    private static final String HTML_CODE = "website:htmlCode"
    private static final Set<String> SUPPORTED_TYPES = [
        "website:service",
        "website:general"
    ] as Set

    @Override
    boolean doUpdate(Node node) {
        String nodeType = jcrPrimaryType(node)

        if (!SUPPORTED_TYPES.contains(nodeType)) {
            log.error("Can only be invoked for Service or General documents but actual node type was ${nodeType}. Backing off: ${node.path}")
            return false
        }

        if (!node.hasProperty(HTML_CODE)) {
            log.info("No ${HTML_CODE} property found on ${node.path}")
            return false
        }

        String value = node.getProperty(HTML_CODE).getString()
        boolean hasValue = value != null && value.trim().length() > 0
        log.info("Found ${HTML_CODE} on ${node.path}; non-empty=${hasValue}; length=${value == null ? 0 : value.length()}")

        JcrUtils.ensureIsCheckedOut(node)
        node.getProperty(HTML_CODE).remove()
        log.info("Removed ${HTML_CODE} from ${node.path}")

        return true
    }

    @Override
    boolean undoUpdate(Node node) throws RepositoryException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Removal of ${HTML_CODE} cannot be reverted by this updater.")
    }

    private String jcrPrimaryType(Node node) {
        node.getProperty("jcr:primaryType").value.getString()
    }
}
