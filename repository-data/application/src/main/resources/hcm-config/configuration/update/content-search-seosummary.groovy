package org.hippoecm.frontend.plugins.cms.admin.updater

import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.RepositoryException

class UpdaterTemplate extends BaseNodeUpdateVisitor {

    boolean doUpdate(Node node) {

        if (node != null) {
            setSeoSummary(node)
            return true
        }

        return false
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }

     static void setSeoSummary(Node node) throws RepositoryException {
        String nodeType = node.getPrimaryNodeType().getName();
        if (nodeType.equals("publicationsystem:publication") || nodeType.equals("publicationsystem:legacypublication")
            || nodeType.equals("nationalindicatorlibrary:indicator")) {
            if (node.hasNode("publicationsystem:seosummary")
                && node.getNode("publicationsystem:seosummary").getProperty("hippostd:content") != null) {
                node.setProperty("common:seosummarytext", node.getNode("publicationsystem:seosummary").getProperty("hippostd:content").getString());
            }
        } else {
            if (node.hasNode("website:seosummary") && node.getNode("website:seosummary").getProperty("hippostd:content") != null) {
                node.setProperty("website:seosummarytext", node.getNode("website:seosummary").getProperty("hippostd:content").getString());
            }
        }
    }
}
