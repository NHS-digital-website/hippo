package org.hippoecm.frontend.plugins.cms.admin.updater

import org.apache.jackrabbit.value.BinaryValue
import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node

import static org.apache.jackrabbit.JcrConstants.JCR_DATA

class Migration20180604CleanUpEmbeddedResources extends BaseNodeUpdateVisitor {

    boolean doUpdate(Node node) {
        if (node.hasProperty(JCR_DATA)) {
            log.debug "Deleting data from node ${node.path}"

            JcrUtils.ensureIsCheckedOut(node)
            node.setProperty(JCR_DATA, new BinaryValue(""))

            return true
        }

        return false
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }

}
