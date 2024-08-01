package uk.nhs.digital.common.components.catalogue;

import org.hippoecm.hst.content.beans.standard.HippoFolderBean;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

public class FacetObject {

    private HippoFolderBean folderBean;
    private boolean isLeaf;
    private Long resultCount;
    private static final String RESULTSET_NODE_NAME = "hippo:resultset";
    private static final String SHOWAPI_PROPERTY_NAME = "website:showApiResult";

    public FacetObject(HippoFolderBean folderBean, boolean isLeaf, Long resultCount) {
        this.folderBean = folderBean;
        this.isLeaf = isLeaf;
        this.resultCount = resultCount;
    }

    public Long getResultCount() {
        return resultCount;
    }

    public void setResultCount(Long resultCount) {
        this.resultCount = resultCount;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public HippoFolderBean getFolderBean() {
        return folderBean;
    }

    public void setFolderBean(HippoFolderBean folderBean) {
        this.folderBean = folderBean;
    }

    /**
     * Calculates the new result count excluding documents that are filtered out
     * using the checkbox field.
     * We get the resultset from the facet node and traverse the documents checking
     * for the checkmark.
     * On the facet nodes, the facet node with the same name as the parent facet
     * does not have a result set. This will cause the result count to be wrong.
     * If we find that this is the node we are traversing, we take the resultset
     * from the parent to correct this.
     * @throws RepositoryException when there is an issue getting the node from the repo
     */
    public void calculateResultCountWithApiResultFilter() throws RepositoryException {
        boolean hasResultSetNode = folderBean.getNode().hasNode(RESULTSET_NODE_NAME);
        Node resultSetNode;
        if (hasResultSetNode) {
            resultSetNode = folderBean.getNode().getNode(RESULTSET_NODE_NAME);
            setFilteredCount(getResultCount(), resultSetNode);
        } else if (folderBean.getNode().getName().equals(folderBean.getNode().getParent().getParent().getName())
            && folderBean.getNode().getParent().hasNode(RESULTSET_NODE_NAME)) {
            resultSetNode = folderBean.getNode().getParent().getNode(RESULTSET_NODE_NAME);
            setFilteredCount(getResultCount(), resultSetNode);
        }
    }

    private void setFilteredCount(Long filteredCount, Node resultSetNode) throws RepositoryException {
        NodeIterator nodeIterator = resultSetNode.getNodes("content");
        while (nodeIterator.hasNext()) {
            Node contentNode = nodeIterator.nextNode();
            boolean hasShowApiProperty = contentNode.hasProperty(SHOWAPI_PROPERTY_NAME);
            if (hasShowApiProperty && !contentNode.getProperty(SHOWAPI_PROPERTY_NAME).getValue().getBoolean()) {
                filteredCount--;
            }
        }
        setResultCount(filteredCount);
    }

    public boolean isEmpty() {
        return getResultCount() <= 0;
    }
}
