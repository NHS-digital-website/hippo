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
                    if (node.getPath().contains("/content/documents/corporate-website") && searchableDocTypes(node)) {
                        setTabProperty(node, node);

                        if (node.hasProperty("hippotaxonomy:keys") && node.getProperty("hippotaxonomy:keys").getValues() != null && searchableDocTypeTaxonomy(node)) {
                            setTaxonomyProperties(node);
                        }

                        if (publicationDateDocType(node) && node.hasProperty("publicationsystem:NominalDate")
                            && node.getProperty("publicationsystem:NominalDate").getValue() != null
                            && node.getProperty("publicationsystem:NominalDate").getValue().getDate() != null) {
                            setPublicationDateProperties(node);
                        }

                        if (publicationDocTypes(node) && node.hasNode("publicationsystem:seosummary")
                            && node.getNode("publicationsystem:seosummary").getProperty("hippostd:content") != null) {
                            node.setProperty("common:seosummarytext", node.getNode("publicationsystem:seosummary").getProperty("hippostd:content").getString());
                        }

                        if (websiteDocTypes(node) && node.hasNode("website:seosummary") && node.getNode("website:seosummary").getProperty("hippostd:content") != null) {
                            node.setProperty("website:seosummarytext", node.getNode("website:seosummary").getProperty("hippostd:content").getString());
                        }

                        session.save();
                    }
                }
            }
        } catch (RepositoryException e) {
            LOGGER.warn("An error occurred while handling the post publish event", e);
        }
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

    private void setTaxonomyProperties(Node nodeToUpdate) throws RepositoryException {
        if (nodeToUpdate.getPrimaryNodeType().getName().split(":")[0].equals("website")) {
            nodeToUpdate.setProperty("website:taxonomyClassificationField", nodeToUpdate.getProperty("hippotaxonomy:keys").getValues());
        } else if (nodeToUpdate.getPrimaryNodeType().getName().split(":")[0].equals("publicationsystem")
            || nodeToUpdate.getPrimaryNodeType().getName().split(":")[0].equals("nationalindicatorlibrary")) {
            nodeToUpdate.setProperty("common:taxonomyClassificationField", nodeToUpdate.getProperty("hippotaxonomy:keys").getValues());
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

    private boolean searchableDocTypes(Node node) throws RepositoryException {
        return websiteDocTypes(node) || publicationDocTypes(node);
    }

    private boolean websiteDocTypes(Node node) throws RepositoryException {
        final String nodeName = node.getPrimaryNodeType().getName();
        return websiteTaxonomyDocType(node)
            || nodeName.equals("website:visualhub") || nodeName.equals("website:gdprsummary")
            || nodeName.equals("website:gdprtransparency") || nodeName.equals("website:roadmap")
            || nodeName.equals("website:roadmapitem") || nodeName.equals("website:componentlist")
            || nodeName.equals("website:glossarylist") || nodeName.equals("website:apiendpoint")
            || nodeName.equals("website:businessunit") || nodeName.equals("website:orgstructure")
            || nodeName.equals("website:supplementaryinformation");
    }

    private boolean websiteTaxonomyDocType(Node node) throws RepositoryException {
        final String nodeName = node.getPrimaryNodeType().getName();

        return nodeName.equals("website:apimaster") || nodeName.equals("website:blog")
            || nodeName.equals("website:bloghub") || nodeName.equals("website:cyberalert")
            || nodeName.equals("website:event") || nodeName.equals("website:general")
            || nodeName.equals("website:hub") || nodeName.equals("website:news")
            || nodeName.equals("website:person") || nodeName.equals("website:publishedwork")
            || nodeName.equals("website:publishedworkchapter") || nodeName.equals("website:service");
    }

    private boolean publicationDocTypes(Node node) throws RepositoryException {
        final String nodeName = node.getPrimaryNodeType().getName();
        return publicationDateDocType(node) || nodeName.equals("publicationsystem:dataset")
            || nodeName.equals("nationalindicatorlibrary:indicator");
    }

    private boolean publicationDateDocType(Node node) throws RepositoryException {
        final String nodeName = node.getPrimaryNodeType().getName();
        return nodeName.equals("publicationsystem:publication") || nodeName.equals("publicationsystem:legacypublication");
    }

    private boolean searchableDocTypeTaxonomy(Node node) throws RepositoryException {
        return publicationDocTypes(node) || websiteTaxonomyDocType(node);
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
