package org.hippoecm.frontend.plugins.cms.admin.updater

import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.RepositoryException
import javax.jcr.Session

import static org.hippoecm.repository.HippoStdNodeType.NT_RELAXED

class UpdaterTemplate extends BaseNodeUpdateVisitor {

    Session session

    void initialize(Session session) {
        this.session = session
    }

    boolean doUpdate(Node node) {

        if (node != null) {
            setTabProperty(node, node)
            session.save()
            return true
        }

        return false
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }

    void setTabProperty(Node node, Node nodeToUpdate) throws RepositoryException {
        Node parent = node.getParent();
        if (parent.hasProperty("searchTab") && parent.getProperty("searchTab").getValue() != null
            && parent.getProperty("searchTab").getValue().getString() != null) {
            nodeToUpdate.setProperty("website:searchTab", parent.getProperty("searchTab").getValue().getString());
        } else {
            if (!node.getPath().equals("/content/documents/corporate-website")) {
                setTabProperty(parent, nodeToUpdate);
            }
        }
    }
}
