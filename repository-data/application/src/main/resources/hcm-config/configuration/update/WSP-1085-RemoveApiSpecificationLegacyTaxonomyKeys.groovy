package org.hippoecm.frontend.plugins.cms.admin.updater

import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node

/**
 * Removes the legacy hippotaxonomy:keys property from website:apispecification
 * document variants after taxonomy selections have been migrated to the new
 * taxonomy field.
 */
class RemoveApiSpecificationLegacyTaxonomyKeys extends BaseNodeUpdateVisitor {

    private static final String LEGACY_KEYS_PROPERTY = "hippotaxonomy:keys"

    boolean doUpdate(Node node) {
        if (!node.hasProperty(LEGACY_KEYS_PROPERTY)) {
            log.debug("Skipping {} because {} is already absent", node.getPath(), LEGACY_KEYS_PROPERTY)
            return false
        }

        log.info("Removing {} from {}", LEGACY_KEYS_PROPERTY, node.getPath())
        node.getProperty(LEGACY_KEYS_PROPERTY).remove()
        return true
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException("Updater does not implement undoUpdate method")
    }

    boolean logSkippedNodePaths() {
        return false
    }

    boolean skipCheckoutNodes() {
        return false
    }
}
