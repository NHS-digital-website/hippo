package uk.nhs.digital.ps.modules.searchableFlagModule;

import java.util.List;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;

public class PublicationDepublishProcessor extends PublicationProcessor {

    private static final String TYPE_PUBLICATION = "publicationsystem:publication";

    @Override
    public void processNode(Node node, List<String> validStatuses) throws RepositoryException {
        if (!hasValidDocumentType(node, TYPE_PUBLICATION)) {
            return;
        }

        // now the tricky bit - update all datasets documents belonging to this publication,
        // only if publication name is "content"
        // This will ensure that "orphan" datasets are not searchable.
        if ("content".equals(node.getName())) {
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
                    setSearchableFlag(doc, false);
                }
            }
        }
    }
}
