package uk.nhs.digital.ps.workflow.searchableFlag;

import static java.text.MessageFormat.format;

import org.hippoecm.repository.HippoStdNodeType;
import org.hippoecm.repository.util.WorkflowUtils;
import org.onehippo.repository.documentworkflow.DocumentVariant;
import org.onehippo.repository.documentworkflow.task.AbstractDocumentTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;

public class SearchableFlagTask extends AbstractDocumentTask {

    private static final Logger log = LoggerFactory.getLogger(SearchableFlagTask.class);

    private static final String SEARCHABLE_FLAG = "common:searchable";
    private static final String TYPE_PUBLICATION = "publicationsystem:publication";
    private static final String TYPE_DATASET = "publicationsystem:dataset";
    private static final String DOCUMENTS_ROOT_FOLDER = "/content/documents";

    private DocumentVariant variant;
    private boolean depublishing;
    private Session session;

    @Override
    protected Object doExecute() throws RepositoryException {
        session = getWorkflowContext().getInternalWorkflowSession();

        processPublication();
        processDataset();

        return null;
    }

    private void processPublication() throws RepositoryException {
        Node publication = variant.getCheckedOutNode(session);
        if (!publication.isNodeType(TYPE_PUBLICATION)) {
            return;
        }

        // update all dataset documents belonging to this publication,
        // only if publication name is "content"
        if ("content".equals(publication.getName())) {
            Node folder = WorkflowUtils.getContainingFolder(variant, session).getNode(session);

            String query = format("SELECT * FROM [{0}] WHERE ISDESCENDANTNODE ([{1}]) AND [{2}] = ''{3}''",
                TYPE_DATASET,
                folder.getPath(),
                HippoStdNodeType.HIPPOSTD_STATE,
                variant.getState());

            NodeIterator nodes = session
                .getWorkspace()
                .getQueryManager()
                .createQuery(query, Query.JCR_SQL2)
                .execute()
                .getNodes();

            boolean searchable = !depublishing && publication.getProperty("publicationsystem:PubliclyAccessible").getBoolean();

            while (nodes.hasNext()) {
                nodes.nextNode().setProperty(SEARCHABLE_FLAG, searchable);
            }
        }
    }

    private void processDataset() throws RepositoryException {
        Node dataset = variant.getCheckedOutNode(session);

        // If we are not publishing, there is no need to change the searchable flag
        if (depublishing || !dataset.isNodeType(TYPE_DATASET)) {
            return;
        }

        Node parentPublication = findPublicationInFolder(dataset.getParent());

        if (parentPublication == null) {
            // no parent publication found
            log.error("Parent publication not found");
            log.error("failed for node" + dataset.getPath());
            return;
        }

        // get common:searchable value
        boolean searchable = parentPublication
            .getProperty("publicationsystem:PubliclyAccessible")
            .getBoolean();

        // apply it to dataset document(s)
        dataset.setProperty(SEARCHABLE_FLAG, searchable);
    }

    private Node findPublicationInFolder(Node folder) throws RepositoryException {
        if (DOCUMENTS_ROOT_FOLDER.equals(folder.getPath())) {
            return null;
        }

        NodeIterator nodes = folder.getNodes();
        while (nodes.hasNext()) {
            Node node = nodes.nextNode();
            if (isContentHandle(node)) {
                NodeIterator variants = node.getNodes(node.getName());
                while (variants.hasNext()) {
                    Node variantNode = variants.nextNode();
                    if (matchesPublicationVariant(variantNode)) {
                        return variantNode;
                    }
                }
            }
        }

        return findPublicationInFolder(folder.getParent());
    }

    private boolean matchesPublicationVariant(Node variantNode) throws RepositoryException {
        return TYPE_PUBLICATION.equals(variantNode.getPrimaryNodeType().getName())
            && variant.getState().equals(variantNode.getProperty(HippoStdNodeType.HIPPOSTD_STATE).getString());
    }

    private boolean isContentHandle(Node node) throws RepositoryException {
        return node.isNodeType("hippo:handle")
            && "content".equals(node.getName());
    }

    public void setVariant(DocumentVariant variant) {
        this.variant = variant;
    }

    public void setDepublishing(boolean depublishing) {
        this.depublishing = depublishing;
    }
}
