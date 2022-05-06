package uk.nhs.digital.common.listeners;

import org.hippoecm.repository.api.HippoNode;
import org.onehippo.cms7.services.eventbus.HippoEventListenerRegistry;
import org.onehippo.cms7.services.eventbus.Subscribe;
import org.onehippo.repository.events.HippoWorkflowEvent;
import org.onehippo.repository.modules.DaemonModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * Sets the severity value from the severitystatuschanges compound on the cyberalert document
 * Needed for faceted navigation
 */
public class CyberAlertListener implements DaemonModule {

    private Session session;

    private static Logger LOGGER = LoggerFactory.getLogger(CyberAlertListener.class);

    @Subscribe
    public void handleEvent(HippoWorkflowEvent event) {
        if (event.success() && event.get("methodName").equals("publish")) {
            addSeverityToNode(event);
        }
    }

    private void addSeverityToNode(HippoWorkflowEvent event) {

        try {
            final HippoNode handle = (HippoNode) session.getNodeByIdentifier(event.subjectId());
            if (handle.hasNodes()) {
                final NodeIterator nodeIterator = handle.getNodes();
                while (nodeIterator.hasNext()) {
                    Node node = nodeIterator.nextNode();
                    if (node.getPath().contains("/content/documents/corporate-website/cyber-alerts") && node.getPrimaryNodeType().getName().equals("website:cyberalert")
                        && node.hasNode("website:severitystatuschanges") && node.getNode("website:severitystatuschanges").getProperty("website:severity") != null) {

                        if (node.getNodes("website:severitystatuschanges").getSize() > 1) {
                            final NodeIterator severityStatusIterator = node.getNodes("website:severitystatuschanges");
                            Calendar newestDate = null;
                            String newestSeverity = null;
                            int i = 0;
                            while (severityStatusIterator.hasNext()) {
                                final Node severityStatusNode = severityStatusIterator.nextNode();
                                Calendar nodeDate = severityStatusNode.getProperty("website:date").getDate();

                                if (i == 0) {
                                    newestDate = nodeDate;
                                }

                                if (nodeDate.equals(newestDate) || nodeDate.after(newestDate)) {
                                    newestSeverity = severityStatusNode.getProperty("website:severity").getString();
                                }
                                i++;
                            }
                            node.setProperty("website:severity", newestSeverity);
                        } else {
                            node.setProperty("website:severity", node.getNode("website:severitystatuschanges").getProperty("website:severity").getString());
                        }
                    }
                }
            }
            session.save();
        } catch (RepositoryException ex) {
            LOGGER.warn("An error occurred while handling the post publish event ", ex);
        }
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
