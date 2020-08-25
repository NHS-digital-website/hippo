package uk.nhs.digital.common.contentsearch;

import org.hippoecm.repository.HippoStdNodeType;
import org.hippoecm.repository.api.HippoNode;
import org.hippoecm.repository.util.JcrUtils;
import org.hippoecm.repository.util.NodeIterable;
import org.onehippo.cms7.services.eventbus.HippoEventListenerRegistry;
import org.onehippo.cms7.services.eventbus.Subscribe;
import org.onehippo.repository.events.HippoWorkflowEvent;
import org.onehippo.repository.modules.DaemonModule;

import java.util.Calendar;
import java.util.Locale;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class ContentSearchListener implements DaemonModule {

    private Session session;

    @Subscribe
    public void handleEvent(HippoWorkflowEvent event) {
        if (event.success() && event.get("methodName").equals("publish")) {
            postPublish(event);
        }
    }

    private void postPublish(HippoWorkflowEvent event) {
        try {
            final HippoNode handle = (HippoNode) session.getNodeByIdentifier(event.subjectId());
            final Node node = getPublishedVariant(handle);
            if (node != null) {
                setTabProperty(node);
                if (node.getProperty("publicationsystem:NominalDate").getValue() != null) {
                    setPublicationDateProperties(node);
                }
            }
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    private void setTabProperty(Node node) throws RepositoryException {
        Node parent = node.getParent();
        if (parent.hasProperty("searchTab")) {
            node.setProperty("searchTab", parent.getProperty("searchTab").getValue().toString());
            session.save();
        } else {
            setTabProperty(parent);
        }
    }

    private void setPublicationDateProperties(Node node) throws RepositoryException {
        Calendar date = node.getProperty("publicationsystem:NominalDate").getValue().getDate();
        node.setProperty("publicationsystem:month", date.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        node.setProperty("publicationsystem:year", date.get(Calendar.YEAR));
        session.save();
    }

    private static Node getPublishedVariant(Node handle) throws RepositoryException {
        for (Node variant : new NodeIterable(handle.getNodes(handle.getName()))) {
            final String state = JcrUtils.getStringProperty(variant, HippoStdNodeType.HIPPOSTD_STATE, null);
            if (HippoStdNodeType.PUBLISHED.equals(state)) {
                return variant;
            }
        }
        return null;
    }

    @Override
    public void initialize(Session session) {
        this.session = session;
        HippoEventListenerRegistry.get().register(this);
    }

    @Override
    public void shutdown() {
        HippoEventListenerRegistry.get().unregister(this);
    }
}
