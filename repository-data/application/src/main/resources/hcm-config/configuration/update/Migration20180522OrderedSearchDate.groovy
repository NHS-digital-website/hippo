package org.hippoecm.frontend.plugins.cms.admin.updater

import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node

class Migration20180522OrderedSearchDate extends BaseNodeUpdateVisitor {

    boolean doUpdate(Node node) {
        log.debug "Updating node ${node.path}"

        // We just need to do something on this node to cause it to get saved to
        // trigger the derived data function to set the calculated value
        node.setProperty("common:orderedSearchDate", 0l)

        return true
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }
}
