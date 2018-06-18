package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:externallink")
@Node(jcrType = "website:externallink")
public class Externallink extends HippoCompound {

    // used to differentiate between different types of content blocks
    public String getLinkType() {
        return "external";
    }

    @HippoEssentialsGenerated(internalName = "website:link")
    public String getLink() {
        return getProperty("website:link");
    }

    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getProperty("website:title");
    }

    @HippoEssentialsGenerated(internalName = "website:shortsummary")
    public String getShortsummary() {
        return getProperty("website:shortsummary");
    }
}
