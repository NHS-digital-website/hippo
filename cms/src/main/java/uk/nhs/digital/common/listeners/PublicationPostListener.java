package uk.nhs.digital.common.listeners;

import org.hippoecm.repository.api.HippoNode;
import org.onehippo.cms7.services.eventbus.Subscribe;
import org.onehippo.repository.events.HippoWorkflowEvent;
import org.onehippo.repository.modules.DaemonModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import javax.jcr.query.Query;

public class PublicationPostListener implements DaemonModule {
    public static final String PUBLICATION_TIER = "publicationsystem:publicationTier";
    public static final String PUBLICATION_SERIES = "publicationsystem:series";
    public static final String WEBSITE_PUBLICATION_SYSTEM = "/content/documents/corporate-website/publication-system";
    public static final String JCR_ROOT = "/jcr:root";
    private Session session;
    private static Logger LOGGER = LoggerFactory.getLogger(PublicationPostListener.class);


    @Subscribe
    public void handleEvent(HippoWorkflowEvent event) {
        if (event.success() && event.get("methodName").equals("publish")) {
            addTierLevelToNode(event);
        }
    }

    private void addTierLevelToNode(HippoWorkflowEvent event) {

        try {
            final HippoNode handle = (HippoNode) session.getNodeByIdentifier(event.subjectId());
            if (handle.hasNodes()) {
                final NodeIterator nodeIterator = handle.getNodes();
                while (nodeIterator.hasNext()) {
                    Node node = nodeIterator.nextNode();
                    if (node.getPath().contains(WEBSITE_PUBLICATION_SYSTEM)
                        && node.getPrimaryNodeType().getName().equals("publicationsystem:publication")) {
                        //Get the ier from the series from the parent node
                        String pubTier = session
                            .getWorkspace()
                            .getQueryManager()
                            .createQuery(JCR_ROOT
                                + node.getParent().getParent().getParent().getPath()
                                + "//*[(@jcr:primaryType='publicationsystem:series')]", Query.XPATH)
                            .execute()
                            .getNodes()
                            .nextNode()
                            .getProperty(PUBLICATION_TIER)
                            .getValue()
                            .getString()
                            .trim();
                        node.setProperty("publicationsystem:publicationtier", pubTier);
                    } else if (node.getPath().contains(WEBSITE_PUBLICATION_SYSTEM)
                        && node.getPrimaryNodeType().getName().equals(PUBLICATION_SERIES)) {
                        String pubTier = node.getProperty(PUBLICATION_TIER).getValue().getString();
                        NodeIterator nodeItr = session
                            .getWorkspace()
                            .getQueryManager()
                            .createQuery(JCR_ROOT
                                + node.getParent().getParent().getPath()
                                + "//*[(@jcr:primaryType='publicationsystem:publication')]", Query.XPATH)
                            .execute()
                            .getNodes();
                        while (nodeItr.hasNext()) {
                            Node pubNode = nodeItr.nextNode();
                            pubNode.setProperty("publicationsystem:publicationtier", pubTier.trim());
                        }
                    }
                }
            }
            //session.save();
        } catch (RepositoryException ex) {
            LOGGER.warn("An error occurred while handling the post publish event ", ex);
        }
    }

    @Override
    public void initialize(Session session) {
        this.session = session;
        //HippoEventListenerRegistry.get().register(this);
    }

    @Override
    public void shutdown() {
        //HippoEventListenerRegistry.get().unregister(this);
    }
}
