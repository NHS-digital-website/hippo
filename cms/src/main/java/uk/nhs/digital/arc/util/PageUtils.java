package uk.nhs.digital.arc.util;

import com.google.common.collect.Sets;
import org.hippoecm.repository.api.HippoNodeType;
import org.onehippo.forge.content.exim.core.DocumentManager;
import org.onehippo.forge.content.exim.core.impl.WorkflowDocumentManagerImpl;
import org.onehippo.forge.content.exim.core.util.ContentPathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * Utility class to locate and manage removal of pages that are no longer used in a report.
 * This is likely to happen when a new version of a report is created and thus overwrites the old version
 */
public class PageUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(PageUtils.class);
    private final Session session;
    private final String nodePath;

    private DocumentManager documentManager;

    public PageUtils(Session session, String nodePath) {
        this.session = session;
        this.nodePath = nodePath;
    }

    public void removeOrphanedPages(List<String> existingPages, List<String> createdPages) {
        Set<String> removed = Sets.difference(new HashSet<>(existingPages), new HashSet<>(createdPages));

        DocumentManager workflowManager = getDocumentManager();

        removed.stream().forEach(pageName -> {
            LOGGER.debug("Following orphaned page found in ARC will be removed from the folder: {}", pageName);
            workflowManager.deleteDocument(pageName);
        });
    }

    /**
     * Find all pages currently attributed to this report so that we know what we have before we start
     *
     * @return the {@link List} of content node paths
     */
    public List<String> getExistingPages() {
        List<String> createdPages = new ArrayList<>();

        Node folderNode = null;
        try {
            folderNode = session.getNode(getReportRootFolder());
            NodeIterator pages = folderNode.getNodes();

            while (pages.hasNext()) {
                Node pageNode = pages.nextNode();
                if (HippoNodeType.NT_HANDLE.equals(pageNode.getPrimaryNodeType().getName())) {
                    createdPages.add(pageNode.getPath());
                }
            }
        } catch (RepositoryException e) {
            LOGGER.error("Error trying to read information about existing pages in the report", e);
        }
        return createdPages;
    }

    public String getReportRootFolder() {
        String[] paths = ContentPathUtils.splitToFolderPathAndName(nodePath);
        return paths[0];
    }

    public void setDocumentManager(DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    private DocumentManager getDocumentManager() {
        if (documentManager == null) {
            documentManager = new WorkflowDocumentManagerImpl(session);
        }

        return documentManager;
    }
}
