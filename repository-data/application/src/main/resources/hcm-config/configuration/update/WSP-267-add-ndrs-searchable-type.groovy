package org.hippoecm.frontend.plugins.cms.admin.updater

import javax.jcr.Node

import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

/**
 * Ensures key NDRS doctypes expose the common:searchableNdrsType flag for search filtering.
 */
class Wsp267AddNdrsSearchableType extends BaseNodeUpdateVisitor {

    private static final String PROPERTY = "common:searchableNdrsType"
    private static final Set<String> TARGET_PRIMARY_TYPES = [
        "website:general",
        "website:publishedwork",
        "website:publishedworkchapter"
    ] as Set

    @Override
    boolean doUpdate(final Node node) {
        try {
            return updateNode(node)
        } catch (Exception e) {
            log.error("Failed to update node ${node.path}", e)
            return false
        }
    }

    private boolean updateNode(final Node node) {
        final String nodeType = node.primaryNodeType.name

        if (!TARGET_PRIMARY_TYPES.contains(nodeType)) {
            return false
        }

        final String path = node.path
        if (!path.startsWith("/content/documents/corporate-website/ndrs")) {
            return false
        }

        // Respect manual overrides: if the property exists and is explicitly set, leave it alone.
        if (node.hasProperty(PROPERTY)) {
            log.info("Skipped ${path} – ${PROPERTY} already set to ${node.getProperty(PROPERTY).boolean}")
            return false
        }

        JcrUtils.ensureIsCheckedOut(node)
        node.setProperty(PROPERTY, true)
        log.info("Updated ${path} – set ${PROPERTY}=true")
        return true
    }

    @Override
    boolean undoUpdate(final Node node) {
        throw new UnsupportedOperationException("Undo not supported")
    }
}
