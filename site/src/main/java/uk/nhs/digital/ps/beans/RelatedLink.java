package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "publicationsystem:relatedlink")
@Node(jcrType = "publicationsystem:relatedlink")
public class RelatedLink extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "publicationsystem:linkText")
    public String getLinkText() {
        return getProperty("publicationsystem:linkText", getLinkUrl());
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:linkUrl")
    public String getLinkUrl() {
        return getProperty("publicationsystem:linkUrl");
    }

    public String getSectionType() {
        return "relatedLink";
    }
}
