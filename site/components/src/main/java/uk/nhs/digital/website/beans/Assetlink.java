package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:assetlink")
@Node(jcrType = "website:assetlink")
public class Assetlink extends HippoCompound {

    // used to differentiate between different types of content blocks
    public String getLinkType() {
        return "asset";
    }

    @HippoEssentialsGenerated(internalName = "website:filename")
    public String getFilename() {
        return getProperty("website:filename");
    }

    // convienent method for ordering using the flat_block
    public String getTitle() {
        return getProperty("website:filename");
    }

    @HippoEssentialsGenerated(internalName = "website:link")
    public HippoBean getLink() {
        return getLinkedBean("website:link", HippoBean.class);
    }
}
