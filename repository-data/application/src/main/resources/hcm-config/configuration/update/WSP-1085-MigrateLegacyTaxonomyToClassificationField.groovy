package org.hippoecm.frontend.plugins.cms.admin.updater

import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor
import org.onehippo.taxonomy.api.TaxonomyNodeTypes

import javax.jcr.Node
import javax.jcr.Property
import javax.jcr.Value

/**
 * Migrates legacy taxonomy data from hippotaxonomy:keys into the existing
 * taxonomyClassificationField property used by the document types that currently
 * have both the legacy classifiable mixin and the newer taxonomy field.
 *
 * After copying values, the script removes the legacy property and the
 * hippotaxonomy:classifiable mixin from the document variant.
 */
class MigrateLegacyTaxonomyToClassificationField extends BaseNodeUpdateVisitor {

    private static final String LEGACY_KEYS_PROPERTY = "hippotaxonomy:keys"
    private static final String CLASSIFIABLE_MIXIN =
        TaxonomyNodeTypes.NODETYPE_HIPPOTAXONOMY_CLASSIFIABLE
    private static final String JCR_MIXIN_TYPES_PROPERTY = "jcr:mixinTypes"

    private static final Map<String, String> TARGET_PROPERTY_BY_PRIMARY_TYPE = [
        "nationalindicatorlibrary:indicator"    : "common:taxonomyClassificationField",
        "publicationsystem:dataset"             : "common:taxonomyClassificationField",
        "publicationsystem:legacypublication"   : "common:taxonomyClassificationField",
        "publicationsystem:publication"         : "common:taxonomyClassificationField",
        "website:apimaster"                     : "website:taxonomyClassificationField",
        "website:blog"                          : "website:taxonomyClassificationField",
        "website:bloghub"                       : "website:taxonomyClassificationField",
        "website:cyberalert"                    : "website:taxonomyClassificationField",
        "website:event"                         : "website:taxonomyClassificationField",
        "website:general"                       : "website:taxonomyClassificationField",
        "website:hub"                           : "website:taxonomyClassificationField",
        "website:news"                          : "website:taxonomyClassificationField",
        "website:publishedwork"                 : "website:taxonomyClassificationField",
        "website:publishedworkchapter"          : "website:taxonomyClassificationField",
        "website:service"                       : "website:taxonomyClassificationField"
    ]

    boolean doUpdate(Node node) {
        String primaryType = node.getPrimaryNodeType().getName()
        String targetProperty = TARGET_PROPERTY_BY_PRIMARY_TYPE[primaryType]

        if (targetProperty == null) {
            log.warn("Skipping node {} because primary type {} is not configured for migration",
                node.getPath(), primaryType)
            return false
        }

        boolean changed = false
        String[] legacyKeys = JcrUtils.getMultipleStringProperty(node, LEGACY_KEYS_PROPERTY, null)

        if (legacyKeys != null) {
            String[] existingTargetKeys = JcrUtils.getMultipleStringProperty(node, targetProperty, null)

            if (!Arrays.equals(legacyKeys, existingTargetKeys)) {
                log.info("Copying taxonomy values from {} to {} on {}",
                    LEGACY_KEYS_PROPERTY, targetProperty, node.getPath())
                node.setProperty(targetProperty, legacyKeys)
                changed = true
            }

            if (node.hasProperty(LEGACY_KEYS_PROPERTY)) {
                log.info("Removing legacy taxonomy property {} from {}",
                    LEGACY_KEYS_PROPERTY, node.getPath())
                node.getProperty(LEGACY_KEYS_PROPERTY).remove()
                changed = true
            }
        } else {
            log.debug("No {} property on {}, only removing legacy mixin if present",
                LEGACY_KEYS_PROPERTY, node.getPath())
        }

        if (hasDirectMixin(node, CLASSIFIABLE_MIXIN)) {
            log.info("Removing legacy taxonomy mixin from {}", node.getPath())
            node.removeMixin(CLASSIFIABLE_MIXIN)
            changed = true
        }

        return changed
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
