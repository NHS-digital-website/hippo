package org.hippoecm.frontend.plugins.cms.admin.updater

import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node

/**
 * Removes the derived common:SearchableTags property from document variants for
 * doctypes that have been verified not to use it in site code.
 */
class RemoveUnusedSearchableTagsProperty extends BaseNodeUpdateVisitor {

    private static final String SEARCHABLE_TAGS_PROPERTY = "common:SearchableTags"

    boolean doUpdate(Node node) {
        if (!node.hasProperty(SEARCHABLE_TAGS_PROPERTY)) {
            log.debug("Skipping {} because {} is already absent", node.getPath(), SEARCHABLE_TAGS_PROPERTY)
            return false
        }

        log.info("Removing {} from {}", SEARCHABLE_TAGS_PROPERTY, node.getPath())
        node.getProperty(SEARCHABLE_TAGS_PROPERTY).remove()
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
