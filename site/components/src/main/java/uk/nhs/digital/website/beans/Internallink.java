package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:internallink")
@Node(jcrType = "website:internallink")
public class Internallink extends HippoCompound {

    // used to differentiate between different types of content blocks
    public String getLinkType() {
        return "internal";
    }

    @HippoEssentialsGenerated(internalName = "website:link")
    public HippoBean getLink() {
        return getLinkedBean("website:link", HippoBean.class);
    }

    public String getTitle() {
        return getLink().getDisplayName();
    }

    public boolean getIsPublished() {
        HippoBean link = getLink();
        try {
            javax.jcr.Node linkNode = link.getNode();
            if (linkNode.hasProperty("hippostd:state") && "published".equals(linkNode.getProperty("hippostd:state").getString())) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
