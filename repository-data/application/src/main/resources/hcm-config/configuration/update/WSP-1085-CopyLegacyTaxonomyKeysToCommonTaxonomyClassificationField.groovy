package org.hippoecm.frontend.plugins.cms.admin.updater

import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node

/**
 * Copies legacy taxonomy selections from hippotaxonomy:keys into
 * common:taxonomyClassificationField for doctypes that have been verified to
 * use the shared common taxonomy field.
 */
class CopyLegacyTaxonomyKeysToCommonTaxonomyClassificationField extends BaseNodeUpdateVisitor {

    private static final String LEGACY_KEYS_PROPERTY = "hippotaxonomy:keys"
    private static final String TARGET_PROPERTY = "common:taxonomyClassificationField"

    boolean doUpdate(Node node) {
        String[] legacyKeys = JcrUtils.getMultipleStringProperty(node, LEGACY_KEYS_PROPERTY, null)

        if (legacyKeys == null) {
            log.debug("Skipping {} because {} is absent", node.getPath(), LEGACY_KEYS_PROPERTY)
            return false
        }

        String[] existingTargetKeys = JcrUtils.getMultipleStringProperty(node, TARGET_PROPERTY, null)

        if (Arrays.equals(legacyKeys, existingTargetKeys)) {
            log.debug("Skipping {} because {} already matches {}", node.getPath(),
                TARGET_PROPERTY, LEGACY_KEYS_PROPERTY)
            return false
        }

        log.info("Copying taxonomy values from {} to {} on {}", LEGACY_KEYS_PROPERTY,
            TARGET_PROPERTY, node.getPath())
        node.setProperty(TARGET_PROPERTY, legacyKeys)
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
