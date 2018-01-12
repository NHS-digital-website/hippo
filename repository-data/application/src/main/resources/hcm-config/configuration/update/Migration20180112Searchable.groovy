package org.hippoecm.frontend.plugins.cms.admin.updater

import javax.jcr.*
import org.onehippo.repository.update.BaseNodeUpdateVisitor

/**
 * This migration adds new field "common:searchable" to all known document
 * types.
 */
class Migration20180112Searchable extends BaseNodeUpdateVisitor {
    def allowedTypes = [
        "publicationsystem:publication",
        "publicationsystem:dataset",
        "publicationsystem:series",
        "publicationsystem:about"
    ];
    boolean doUpdate(Node node) {
        try {
            if (allowedTypes.contains(node.getPrimaryNodeType().getName())) {
                log.info("attempting to migrate node type: " + node.getPrimaryNodeType().getName())
                return addSearchableProperty(node)
            } else {
                log.info("Not supported node type: " + node.getPrimaryNodeType().getName())
            }
        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }

    boolean undoUpdate(Node node) {
        try {
            if (allowedTypes.contains(node.getPrimaryNodeType().getName())) {
                log.info("attempting to undo migration on node type: " + node.getPrimaryNodeType().getName())
                return removeSearchableProperty(node);
            } else {
                log.info("Not supported node type: " + node.getPrimaryNodeType().getName())
            }
        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }

    boolean addSearchableProperty(Node node) {
        Property p = node.setProperty("common:searchable", true)

        return p.getValue().boolean == true
    }

    boolean removeSearchableProperty(Node node) {
        if (! node.hasProperty("common:searchable")) {
            return true
        }

        node.getProperty("common:searchable").remove()

        return true
    }
}
