package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "publicationsystem:cihublink")
@Node(jcrType = "publicationsystem:cihublink")
public class CiHubLink extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "publicationsystem:title")
    public String getTitle() {
        return getProperty("publicationsystem:title");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:summary")
    public String getSummary() {
        return getProperty("publicationsystem:summary");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:pagelink")
    public HippoBean getPageLink() {
        return getLinkedBean("publicationsystem:pagelink", HippoBean.class);
    }
}
