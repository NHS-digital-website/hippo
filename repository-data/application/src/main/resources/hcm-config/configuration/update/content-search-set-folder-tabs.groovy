package org.hippoecm.frontend.plugins.cms.admin.updater

import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node

import static org.hippoecm.repository.HippoStdNodeType.NT_RELAXED

class UpdaterTemplate extends BaseNodeUpdateVisitor {

    boolean doUpdate(Node node) {

        if (node.getPath().equals("/content/documents/corporate-website/data-and-information") || node.getPath().equals("/content/documents/corporate-website/national-indicator-library") || node.getPath().equals("/content/documents/corporate-website/publication-system")) {
            if (node.canAddMixin(NT_RELAXED)) {
                node.addMixin(NT_RELAXED)
            }
            node.setProperty("searchTab", "data")
            return true
        }

        if (node.getPath().equals("/content/documents/corporate-website/services")) {
            if (node.canAddMixin(NT_RELAXED)) {
                node.addMixin(NT_RELAXED)
            }
            node.setProperty("searchTab", "services")
            return true
        }


        if (node.getPath().equals("/content/documents/corporate-website/news-and-events")) {
            if (node.canAddMixin(NT_RELAXED)) {
                node.addMixin(NT_RELAXED)
            }
            node.setProperty("searchTab", "news")
            return true
        }


        log.debug "Updating node ${node.path}"
        return false
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }

}
