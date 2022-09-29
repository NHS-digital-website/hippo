package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:feedhub")
@Node(jcrType = "website:feedhub")
public class FeedHub extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "website:hubType")
    public String getHubType() {
        return getSingleProperty("website:hubType");
    }

    @HippoEssentialsGenerated(internalName = "website:feedType")
    public String getFeedType() {
        return getSingleProperty("website:feedType");
    }

    @HippoEssentialsGenerated(internalName = "website:subject")
    public List<HippoBean> getSubject() {
        return getLinkedBeans("website:subject", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:ctabutton")
    public CtaButton getCtabutton() {
        return getBean("website:ctabutton", CtaButton.class);
    }
}
