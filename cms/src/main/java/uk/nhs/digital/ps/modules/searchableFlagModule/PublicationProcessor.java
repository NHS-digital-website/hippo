package uk.nhs.digital.ps.modules.searchableFlagModule;

import org.hippoecm.repository.util.JcrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;

public class PublicationProcessor extends AbstractProcessor {

    private static final Logger log = LoggerFactory.getLogger(PublicationProcessor.class);

    private static final String TYPE_PUBLICATION = "publicationsystem:publication";

    @Override
    public void processNode(Node node, List<String> validStatuses) throws RepositoryException {
        if (!hasValidDocumentType(node, TYPE_PUBLICATION)) {
            return;
        }

        streamDocumentVariants(node)
            .filter((document) -> hasValidStateAndType(document, validStatuses, TYPE_PUBLICATION))
            .forEach(this::updatePublicationDocument);

        // now the tricky bit - update all datasets documents belonging to this publication,
        // only if publication name is "content"
        if ("content".equals(node.getName())) {
            boolean searchable = isPubliclyAccessible(node.getNode(node.getName()));
            String query = "SELECT * FROM [publicationsystem:dataset] WHERE ISDESCENDANTNODE (["
                + node.getParent().getPath() + "])";

            QueryResult res = node.getSession()
                .getWorkspace()
                .getQueryManager()
                .createQuery(query, Query.JCR_SQL2)
                .execute();

            for (NodeIterator i = res.getNodes(); i.hasNext(); ) {
                Node doc = i.nextNode();
                if (hasValidStateAndType(doc, validStatuses, "publicationsystem:dataset")) {
                    setSearchableFlag(doc, searchable);
                }
            }
        }
    }

    private void updatePublicationDocument(Node document) {
        try {
            setSearchableFlag(document, isPubliclyAccessible(document));
        } catch (RepositoryException ex) {
            log.error("RepositoryException during read operation", ex);
        }
    }

    private boolean isPubliclyAccessible(Node document) {
        try {
            return document.getProperty("publicationsystem:PubliclyAccessible").getBoolean();
        } catch (RepositoryException ex) {
            log.error("RepositoryException during read operation", ex);
            return false;
        }
    }

    protected void setSearchableFlag(Node document, boolean searchable) throws RepositoryException {
        JcrUtils.ensureIsCheckedOut(document);
        document.setProperty(SEARCHABLE_FLAG, searchable);
    }
}
