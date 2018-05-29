package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.standard.HippoBean;

public class IndexPageImpl implements IndexPage {
    private String title;
    private HippoBean linkedBean;

    public IndexPageImpl(String title, HippoBean linkedBean) {
        this.title = title;
        this.linkedBean = linkedBean;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public HippoBean getLinkedBean() {
        return linkedBean;
    }
}
