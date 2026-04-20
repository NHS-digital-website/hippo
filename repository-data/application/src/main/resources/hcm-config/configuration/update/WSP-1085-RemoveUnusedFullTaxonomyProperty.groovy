package org.hippoecm.frontend.plugins.cms.admin.updater

import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node

/**
 * Removes the derived common:FullTaxonomy property from document variants for
 * doctypes that have been verified not to use it in site code.
 */
class RemoveUnusedFullTaxonomyProperty extends BaseNodeUpdateVisitor {

    private static final String FULL_TAXONOMY_PROPERTY = "common:FullTaxonomy"

    boolean doUpdate(Node node) {
        if (!node.hasProperty(FULL_TAXONOMY_PROPERTY)) {
            log.debug("Skipping {} because {} is already absent", node.getPath(), FULL_TAXONOMY_PROPERTY)
            return false
        }

        log.info("Removing {} from {}", FULL_TAXONOMY_PROPERTY, node.getPath())
        node.getProperty(FULL_TAXONOMY_PROPERTY).remove()
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
