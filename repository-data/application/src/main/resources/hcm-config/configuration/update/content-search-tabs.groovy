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
            setTabProperty(node)
            if (node.hasProperty("publicationsystem:NominalDate") && node.getProperty("publicationsystem:NominalDate").getValue() != null
                && node.getProperty("publicationsystem:NominalDate").getValue().getDate() != null) {
                setPublicationDateProperties(node);
            }
            session.save();
            return true
        }

        log.debug "Updating node ${node.path}"
        return false
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }

    void setTabProperty(Node node) throws RepositoryException {
        Node parent = node.getParent();
        if (parent.hasProperty("searchTab") && parent.getProperty("searchTab").getValue() != null
            && parent.getProperty("searchTab").getValue().getString() != null) {
            if (node.canAddMixin(NT_RELAXED)) {
                node.addMixin(NT_RELAXED);
            }
            node.setProperty("publicationsystem:searchTab", parent.getProperty("searchTab").getValue().getString());
        } else {
            if (!node.getPath().equals("/content/documents/corporate-website")) {
                setTabProperty(parent);
            }
        }
    }

    static void setPublicationDateProperties(Node node) throws RepositoryException {
        Calendar date = node.getProperty("publicationsystem:NominalDate").getValue().getDate();

        if (node.canAddMixin(NT_RELAXED)) {
            node.addMixin(NT_RELAXED);
        }
        node.setProperty("publicationsystem:month", date.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        node.setProperty("publicationsystem:year", date.get(Calendar.YEAR));
    }

}
