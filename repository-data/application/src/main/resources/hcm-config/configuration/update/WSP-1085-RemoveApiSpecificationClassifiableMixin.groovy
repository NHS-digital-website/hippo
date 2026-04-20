package org.hippoecm.frontend.plugins.cms.admin.updater

import org.onehippo.repository.update.BaseNodeUpdateVisitor
import org.onehippo.taxonomy.api.TaxonomyNodeTypes

import javax.jcr.Node
import javax.jcr.Property
import javax.jcr.Value

/**
 * Removes the legacy hippotaxonomy:classifiable mixin from website:apispecification
 * document variants after their taxonomy selections have been migrated off the
 * legacy mixin-backed field.
 */
class RemoveApiSpecificationClassifiableMixin extends BaseNodeUpdateVisitor {

    private static final String CLASSIFIABLE_MIXIN =
        TaxonomyNodeTypes.NODETYPE_HIPPOTAXONOMY_CLASSIFIABLE
    private static final String JCR_MIXIN_TYPES_PROPERTY = "jcr:mixinTypes"

    boolean doUpdate(Node node) {
        if (!hasDirectMixin(node, CLASSIFIABLE_MIXIN)) {
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

    private boolean hasDirectMixin(Node node, String mixinName) {
        if (!node.hasProperty(JCR_MIXIN_TYPES_PROPERTY)) {
            return false
        }

        Property mixinTypes = node.getProperty(JCR_MIXIN_TYPES_PROPERTY)
        for (Value value : mixinTypes.getValues()) {
            if (mixinName == value.getString()) {
                return true
            }
        }

        return false
    }
}
