package uk.nhs.digital.ps.modules.searchableFlagModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

public class DatasetProcessor extends AbstractProcessor {

    private static final Logger log = LoggerFactory.getLogger(DatasetProcessor.class);

    private static final String RPS_ROOT_FOLDER = "/content/documents/corporate-website/publication-system";
    private static final String TYPE_DATASET = "publicationsystem:dataset";
    private static final String TYPE_PUBLICATION = "publicationsystem:publication";

    public void processNode(Node node, List<String> validStatuses) throws RepositoryException {
        if (!hasValidDocumentType(node, TYPE_DATASET)) {
            return;
        }

        // find parent publication
        Node parentPublication = findParentPublication(node);

        if (parentPublication == null) {
            // no parent publication found
            log.error("Parent publication not found");
            log.error("failed for node" + node.getPath());
            return;
        }

        // get common:searchable value
        boolean searchable = parentPublication
            .getNode(parentPublication.getName())
            .getProperty("publicationsystem:PubliclyAccessible").getBoolean();

        // apply it to dataset document(s)
        streamDocumentVariants(node)
            .filter((document) -> hasValidStateAndType(document, validStatuses, TYPE_DATASET))
            .forEach((document) -> setSearchableFlag(document, searchable));
    }

    private Node findParentPublication(Node node) {
        Node publication = null;

        try {
            Node folder = node.getParent();
            publication = findPublicationInFolder(folder);
        } catch (RepositoryException ex) {
            log.error("Repository exception during read operation", ex);
        }

        return publication;
    }

    private Node findPublicationInFolder(Node node) throws RepositoryException {
        for (NodeIterator i = node.getNodes(); i.hasNext(); ) {
            Node object = i.nextNode();
            if (isParentPublication(object)) {
                return object;
            }
        }

        if (RPS_ROOT_FOLDER.equals(node.getPath())) {
            return null;
        }

        return findPublicationInFolder(node.getParent());
    }

    private boolean isParentPublication(Node node) throws RepositoryException {
        return "content".equals(node.getName()) && TYPE_PUBLICATION.equals(node.getNode(node.getName()).getPrimaryNodeType().getName());
    }

    private void setSearchableFlag(Node document, boolean searchable) {
        try {
            document.setProperty(SEARCHABLE_FLAG, searchable);
        } catch (RepositoryException ex) {
            log.error("RepositoryException during setting common:searchable flag", ex);
        }
    }
}
