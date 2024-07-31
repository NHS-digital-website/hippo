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

    public void calculateResultCountWithApiResultFilter() throws RepositoryException {
        Long filteredCount = getResultCount();

        boolean hasResultSetNode = folderBean.getNode().hasNode(RESULTSET_NODE_NAME);
        if (hasResultSetNode) {
            Node resultSetNode = folderBean.getNode().getNode(RESULTSET_NODE_NAME);
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
    }

    public boolean isEmpty() {
        return getResultCount() == 0;
    }
}
