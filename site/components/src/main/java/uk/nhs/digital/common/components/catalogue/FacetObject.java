package uk.nhs.digital.common.components.catalogue;

import org.hippoecm.hst.content.beans.standard.HippoFolderBean;

public class FacetObject {

    private HippoFolderBean folderBean;
    private boolean isLeaf;
    private Long resultCount;

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

    public boolean isEmpty() {
        return getResultCount() <= 0;
    }
}