package uk.nhs.digital.common.earlyaccess;

import org.apache.commons.lang.StringUtils;
import org.hippoecm.repository.api.HippoNode;
import org.onehippo.repository.events.HippoWorkflowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * Processes search results for content that uses the early access key field.
 */
public class ProcessSearch {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessSearch.class);
    private static final String SEARCHABLE_FLAG = "common:searchable";
    private static final String EARLY_ACCESS_KEY = "website:earlyaccesskey";
    private static final String CORPORATE_SITE_ROOT_PATH = "/content/documents/corporate-website";
    private static final String GENERAL_DOC_TYPE = "website:general";

    /**
     * Checks if pre-release key is active and if so, hides the content from the search.
     *
     * @param event The workflow event
     * @param session The current session
     */
    public void processPreReleaseContentSearch(HippoWorkflowEvent event, Session session) {
        try {
            final HippoNode handle = (HippoNode) session.getNodeByIdentifier(event.subjectId());
            if (handle.hasNodes()) {
                final NodeIterator nodeIterator = handle.getNodes();
                while (nodeIterator.hasNext()) {
                    Node node = nodeIterator.nextNode();
                    if (node.getPath().contains(CORPORATE_SITE_ROOT_PATH)
                        && node.getPrimaryNodeType().getName().equals(GENERAL_DOC_TYPE)) {
                        if (StringUtils.isNotBlank(node.getProperty(EARLY_ACCESS_KEY).getString())) {
                            LOGGER.debug("early access key is set, hiding document from search");
                            node.setProperty(SEARCHABLE_FLAG, false);
                        } else {
                            LOGGER.debug("early access key not set, making document searchable");
                            node.setProperty(SEARCHABLE_FLAG, true);
                        }
                    }
                }
            }
            session.save();
        } catch (RepositoryException ex) {
            LOGGER.warn("An error occurred while handling the post publish event ", ex);
        }
    }
}
