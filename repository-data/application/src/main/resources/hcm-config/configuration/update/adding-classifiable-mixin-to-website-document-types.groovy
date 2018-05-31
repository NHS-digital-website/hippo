package org.hippoecm.frontend.plugins.cms.admin.updater

import org.apache.commons.lang.StringUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node

class CleaningDocbasesHavingPathsUpdateVisitor extends BaseNodeUpdateVisitor {

    boolean doUpdate(Node node) {
        log.debug "Visiting ${node.path}"
        //check if it's possible to add the classifiable mixin
        if (node.canAddMixin("hippotaxonomy:classifiable")) {
            //adding that mixin
            log.debug "Adding the hippotaxonomy:classifiable mixin"
            node.addMixin("hippotaxonomy:classifiable")
            return true
        }
        //in case is not possible, don't persist any changes
        return false

    }

    boolean undoUpdate(Node node) {
        log.debug "Removing the hippotaxonomy:classifiable mixin from ${node.path}"
        node.removeMixin("hippotaxonomy:classifiable")
        return true
    }

    void destroy() {
    }

}
