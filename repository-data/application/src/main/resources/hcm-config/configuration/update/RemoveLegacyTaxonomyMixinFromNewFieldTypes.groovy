package org.hippoecm.frontend.plugins.cms.admin.updater

import org.onehippo.repository.update.BaseNodeUpdateVisitor
import org.onehippo.taxonomy.api.TaxonomyNodeTypes

import javax.jcr.Node

/**
 * Removes the legacy hippotaxonomy:classifiable mixin from document variants
 * that will continue to store taxonomy selections in hippotaxonomy:keys when
 * remodeled to the 15.3 taxonomy field.
 */
class RemoveLegacyTaxonomyMixinFromNewFieldTypes extends BaseNodeUpdateVisitor {

    private static final String CLASSIFIABLE_MIXIN =
        TaxonomyNodeTypes.NODETYPE_HIPPOTAXONOMY_CLASSIFIABLE

    boolean doUpdate(Node node) {
        if (!node.isNodeType(CLASSIFIABLE_MIXIN)) {
            log.debug("Skipping {} because the legacy taxonomy mixin is already absent", node.getPath())
            return false
        }

        log.info("Removing legacy taxonomy mixin from {}", node.getPath())
        node.removeMixin(CLASSIFIABLE_MIXIN)
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
