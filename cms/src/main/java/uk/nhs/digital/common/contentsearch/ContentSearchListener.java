package uk.nhs.digital.common.contentsearch;

import static org.hippoecm.repository.HippoStdNodeType.NT_RELAXED;

import org.hippoecm.repository.api.HippoNode;
import org.onehippo.cms7.services.eventbus.HippoEventListenerRegistry;
import org.onehippo.cms7.services.eventbus.Subscribe;
import org.onehippo.repository.events.HippoWorkflowEvent;
import org.onehippo.repository.modules.DaemonModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Locale;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class ContentSearchListener implements DaemonModule {

    private Session session;

    private static Logger LOGGER = LoggerFactory.getLogger(ContentSearchListener.class);

    @Subscribe
    public void handleEvent(HippoWorkflowEvent event) {
        if (event.success() && event.get("methodName").equals("publish")) {
            postPublish(event);
        }
    }

    private void postPublish(HippoWorkflowEvent event) {
        try {
            final HippoNode handle = (HippoNode) session.getNodeByIdentifier(event.subjectId());
            if (handle.hasNodes()) {
                final NodeIterator nodeIterator = handle.getNodes();
                while (nodeIterator.hasNext()) {
                    Node node = nodeIterator.nextNode();
                    if (node.getPath().contains("/content/documents/corporate-website")) {
                        setTabProperty(node, node);

                        if (node.hasProperty("hippotaxonomy:keys") && node.getProperty("hippotaxonomy:keys").getValues() != null && searchableDocType(node)) {
                            setTaxonomyProperties(node);
                        }

                        if (node.hasProperty("publicationsystem:NominalDate") && node.getProperty("publicationsystem:NominalDate").getValue() != null
                            && node.getProperty("publicationsystem:NominalDate").getValue().getDate() != null) {
                            setPublicationDateProperties(node);
                        }
                        session.save();
                    }
                }
            }
        } catch (
            RepositoryException e) {
            LOGGER.warn("An error occured while handling the post publish event", e);
        }
    }

    private void setTaxonomyProperties(Node nodeToUpdate) throws RepositoryException {
        if (nodeToUpdate.getPrimaryNodeType().getName().split(":")[0].equals("website")) {
            nodeToUpdate.setProperty("website:taxonomyClassificationField", nodeToUpdate.getProperty("hippotaxonomy:keys").getValues());
        } else if (nodeToUpdate.getPrimaryNodeType().getName().split(":")[0].equals("publicationsystem")
            || nodeToUpdate.getPrimaryNodeType().getName().split(":")[0].equals("nationalindicatorlibrary")) {
            nodeToUpdate.setProperty("common:taxonomyClassificationField", nodeToUpdate.getProperty("hippotaxonomy:keys").getValues());
        }
    }

    private boolean searchableDocType(Node node) throws RepositoryException {
        final String nodeName = node.getPrimaryNodeType().getName();

        return nodeName.equals("publicationsystem:publication") || nodeName.equals("publicationsystem:legacypublication")
            || nodeName.equals("publicationsystem:dataset") || nodeName.equals("nationalindicatorlibrary:indicator")
            || nodeName.equals("website:apimaster") || nodeName.equals("website:blog")
            || nodeName.equals("website:bloghub") || nodeName.equals("website:cyberalert")
            || nodeName.equals("website:event") || nodeName.equals("website:general")
            || nodeName.equals("website:hub") || nodeName.equals("website:news")
            || nodeName.equals("website:person") || nodeName.equals("website:publishedwork")
            || nodeName.equals("website:publishedworkchapter") || nodeName.equals("website:service");
    }

    private void setTabProperty(Node node, Node nodeToUpdate) throws RepositoryException {
        Node parent = node.getParent();
        if (parent.hasProperty("searchTab") && parent.getProperty("searchTab").getValue() != null
            && parent.getProperty("searchTab").getValue().getString() != null) {
            if (node.canAddMixin(NT_RELAXED)) {
                node.addMixin(NT_RELAXED);
            }

            if (nodeToUpdate.getPrimaryNodeType().getName().split(":")[0].equals("website")) {
                nodeToUpdate.setProperty("website:searchTab", parent.getProperty("searchTab").getValue().getString());
            } else if (nodeToUpdate.getPrimaryNodeType().getName().split(":")[0].equals("publicationsystem")
                || nodeToUpdate.getPrimaryNodeType().getName().split(":")[0].equals("nationalindicatorlibrary")) {
                nodeToUpdate.setProperty("common:searchTab", parent.getProperty("searchTab").getValue().getString());
            }

        } else {
            if (!node.getPath().equals("/content/documents/corporate-website")) {
                setTabProperty(parent, nodeToUpdate);
            }
        }
    }

    private void setPublicationDateProperties(Node node) throws RepositoryException {
        Calendar date = node.getProperty("publicationsystem:NominalDate").getValue().getDate();

        if (node.canAddMixin(NT_RELAXED)) {
            node.addMixin(NT_RELAXED);
        }
        node.setProperty("publicationsystem:month", date.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        node.setProperty("publicationsystem:year", Integer.toString(date.get(Calendar.YEAR)));
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
