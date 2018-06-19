package org.hippoecm.frontend.plugins.cms.admin.updater

import org.apache.jackrabbit.value.StringValue
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.Value
/**
 * Gdpr Summary list is only taking into account the first letter of the title.
 * This script adds the sortvy property directly in the facet node configuration
 */
class GdprSummaryOrder extends BaseNodeUpdateVisitor {

    private static final String PROPERTY_FACNAV_SORT = "hippofacnav:sortby"

    boolean doUpdate(Node node) {
        try {
            log.info("attempting to update node: " + node.getPath())

            Value[] values = new Value[1]
            values[0] = new StringValue("website:title")
            node.setProperty(PROPERTY_FACNAV_SORT, values)

            return true
        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }

    boolean undoUpdate(Node node) {
        try {
            log.info("attempting to undo migration for node: " + node.getPath())

            if (node.hasProperty(PROPERTY_FACNAV_SORT)) {
                node.getProperty(PROPERTY_FACNAV_SORT).remove()
                log.info("removed property " + PROPERTY_FACNAV_SORT)
            }

            return true
        } catch (e) {
            log.error("Failed to (undo) process record.", e)
        }

        return false
    }
}
